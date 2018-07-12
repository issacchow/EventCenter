package com.isc.eventCenter.impl.activemq;

import com.isc.eventCenter.Event;
import com.isc.eventCenter.EventDispatchMode;
import com.isc.eventCenter.IEventCenter;
import com.isc.eventCenter.IEventListener;
import com.isc.eventCenter.annotation.Listener;
import com.isc.eventCenter.message.MessageProcessorFactory;
import com.isc.eventCenter.message.receiver.IMessageReceiver;
import com.isc.eventCenter.message.sender.IMessageSender;
import com.isc.eventCenter.util.EventUtil;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.jms.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ActiveMQ事件中心
 * 使用持久化订阅 + EventListener控制消息消费模式实现
 * Created by IssacChow on 17/6/6.
 */
public class ActiveMQEventCenter implements
        IEventCenter {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<IEventListener> listenerList = new LinkedList<>();
    //事件订阅关联的主题及队列列表
    private List<Topic> subcrbieEventTopicList = new LinkedList<>();
    private List<Queue> subcrbieEventQueueList = new LinkedList<>();

    //发布事件关联的生产者列表
    private ConcurrentHashMap<String, MessageProducer> topicProducerList = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, MessageProducer> queueProducerList = new ConcurrentHashMap<>();




    private IMessageSender messageSender = MessageProcessorFactory.getMessageSender();


    private boolean onRunning = false;
    private ActiveMQConnection connection = null;
    private Session session = null;
    //默认连接重发策略
    private RedeliveryPolicy redeliveryPolicy = null;
    //默认预取策略
    private ActiveMQPrefetchPolicy prefetchPolicy = null;


    private String id = "undefined";
    private String username = ActiveMQConnectionFactory.DEFAULT_USER;
    private String password = ActiveMQConnectionFactory.DEFAULT_PASSWORD;
    private String brokerUrl = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public RedeliveryPolicy getRedeliveryPolicy() {
        return redeliveryPolicy;
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
//        if(this.connection!=null && redeliveryPolicy !=null){
//            this.connection.setRedeliveryPolicy(redeliveryPolicy);
//        }
    }

    public ActiveMQPrefetchPolicy getPrefetchPolicy() {
        return prefetchPolicy;
    }

    public void setPrefetchPolicy(ActiveMQPrefetchPolicy prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
//        if(this.connection!=null && prefetchPolicy !=null){
//            this.connection.setPrefetchPolicy(prefetchPolicy);
//        }
    }

    /************
     * 实例方法
     ************/

    public ActiveMQEventCenter() {
        this(null, null, null, null, false);
    }

    public ActiveMQEventCenter(String id, String brokerurl, String username, String password) {
        this(id, brokerurl, username, password, false);
    }

    /**
     * 构造一个实例
     *
     * @param id
     * @param brokerurl
     * @param username
     * @param password
     */
    public ActiveMQEventCenter(String id, String brokerurl, String username, String password, boolean autoConnect) {

        this.redeliveryPolicy = new RedeliveryPolicy();
        this.prefetchPolicy = new ActiveMQPrefetchPolicy();

        if (id != null || StringUtils.isEmpty(id) == false)
            this.setId(id);

        if (brokerurl != null || StringUtils.isEmpty(brokerurl) == false)
            this.setBrokerUrl(brokerurl);

        if (username != null || StringUtils.isEmpty(username) == false)
            this.setUsername(username);

        if (password != null || StringUtils.isEmpty(password) == false)
            this.setPassword(password);

        if (autoConnect) {
            this.connect();
            this.reloadAllListener();
        }

    }


    @Override
    public void publishEvent(Event event) throws Exception {
        logger.info("public Event:{}", event.getClass().getName());
        if (onRunning == false) {
            throw new Exception("ActiveMQCenter is not connected,please invoke connect()");
        }

        EventDispatchMode dispatchMode = EventUtil.getEventDispatchMode((Class<Event>) event.getClass());
        String eventName = EventUtil.getEventName((Class<Event>) event.getClass());

        MessageProducer producer = null;

        switch (dispatchMode){
            case Broadcast:{
                producer = topicProducerList.get(eventName);
                if (producer == null) {
                    Topic topic = session.createTopic(eventName);
                    producer = session.createProducer(topic);
                    topicProducerList.put(eventName, producer);
                    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                }
                break;
            }
            case Once:{
                producer = queueProducerList.get(eventName);
                if (producer == null) {
                    Queue queue = session.createQueue(eventName);
                    producer = session.createProducer(queue);
                    queueProducerList.put(eventName, producer);
                    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                }
                break;
            }
        }


        boolean success = messageSender.send(event,session,producer);
        if(success) {
            logger.info("public Event finish success:{}", event.getClass().getName());
        }else{
            logger.error("public Event finish fail:{}", event.getClass().getName());
        }

    }



    @Override
    public void registerEventListener(IEventListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void reloadAllListener() {
        if (onRunning == false) {
            logger.error("ActiveEventCenter[{}] is not connected", this.getId());
            return;
        }


        try {

            //持久化事件订阅

            for (IEventListener eventListener : listenerList) {

                Class<Event> eventClass = EventUtil.getEventClass(eventListener);
                String eventName = EventUtil.getEventName(eventClass);
                String listenerName = eventListener.getName();
                EventDispatchMode dispatchModeEnum = EventUtil.getEventDispatchMode(eventClass);

                //注册广播式事件监听器
                if (dispatchModeEnum == EventDispatchMode.Broadcast) {
                    registerBroadcastEventListener(eventName, listenerName, eventListener,eventClass);
                    continue;
                }

                //注册一次性事件监听器
                if (dispatchModeEnum == EventDispatchMode.Once) {
                    registerOnceEventListener(eventName, listenerName, eventListener,eventClass);
                    continue;
                }


            }


            logger.info("IEventCenter [{}] reload all listener success!", this.getId());


        }
        catch (JMSException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //@Override
//    public void startListen() {
//
//        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
//
//
//        long startTime=System.currentTimeMillis();
//        logger.info("start listening...");
//        service.schedule(new Runnable() {
//                             @Override
//                             public void run() {
//                                 listen();
//                                 logger.info("started!");
//                             }
//                         },
//                1000, TimeUnit.MILLISECONDS
//        );
//
//    }


    @Override
    public void connect() {


        if (onRunning)
            return;

        logger.info("ActiveMQ IEventCenter {} start to connect...", this.getId());

        if (initSession() == false) {
            logger.error("ActiveMQ IEventCenter {} connect fail", this.getId());
            return;
        }

        logger.info("ActiveMQ IEventCenter {} is connected", this.getId());


        onRunning = true;

        logger.info("IEventCenter [{}] connected!", this.getId());

    }

    @Override
    public void disconnect() {
        try {
            session.close();
            connection.close();
            onRunning = false;
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    private boolean initSession() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                this.getUsername(), this.getPassword(), this.getBrokerUrl()
        );


        try {
            connection = (ActiveMQConnection) connectionFactory.createConnection();
            if (redeliveryPolicy != null) {
                //设置默认重发策略
                connection.setRedeliveryPolicy(redeliveryPolicy);
            }
            if (prefetchPolicy != null) {
                //设置默认预取策略
                connection.setPrefetchPolicy(prefetchPolicy);
            }

            //耐久性订阅者,必须对connection设定ClientId且此ID全局不能重复,否则将会抛出
            //javax.jms.JMSException:
            //You cannot create a durable subscriber without specifying a unique clientID on a Connection.
            //UUID uuid = UUID.randomUUID();
            //connection.setClientID(getEventId() + "-" + uuid);
            connection.setClientID(getId());
            connection.start();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);//第二个参数,消费控制由EventListener处理
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }


    }








//    @Override
//    public void onApplicationEvent(ContextStoppedEvent event) {
//        this.disconnect();
//    }


    /**
     * 注册一次性事件监听器
     *
     * @param eventName
     * @param listenerName
     * @param eventListener
     */
    private void registerOnceEventListener(String eventName, String listenerName, IEventListener eventListener,Class<Event> eventClass) throws JMSException {

        //配置独占模式
        Listener listenerConfig = eventListener.getClass().getAnnotation(Listener.class);
        StringBuilder option = new StringBuilder();
        if(listenerConfig!=null && listenerConfig.isExclusive()){
            option.append("consumer.exclusive=true");
        }
        if(option.length()>0) option.insert(0,"?");


        Queue queue = session.createQueue(eventName + option.toString());
        subcrbieEventQueueList.add(queue);


        MessageConsumer consumer = session.createConsumer(queue);


        InnerMessageListener messageListener = new InnerMessageListener();
        messageListener.setEventCenter(this);
        messageListener.setSession(session);
        messageListener.setDestination(queue);
        messageListener.setEventListener(eventListener);
        consumer.setMessageListener(messageListener);
    }


    /**
     * 注册广播式事件监听器
     *
     * @param eventName
     * @param listenerName
     * @param eventListener
     * @throws JMSException
     */
    private void registerBroadcastEventListener(String eventName, String listenerName, IEventListener eventListener,Class<Event> eventClass) throws JMSException {
        Topic topic = session.createTopic(eventName);
        subcrbieEventTopicList.add(topic);
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, listenerName);
        InnerMessageListener messageListener = new InnerMessageListener();
        messageListener.setEventCenter(this);
        messageListener.setSession(session);
        messageListener.setDestination(topic);
        messageListener.setEventListener(eventListener);
        subscriber.setMessageListener(messageListener);
    }


    /**
     * 消息监听器
     **/
    private class InnerMessageListener implements MessageListener {

        private Destination destination;
        private Session session;
        private IEventListener eventListener;
        private ActiveMQEventCenter eventCenter;
        private IMessageReceiver  receiver = MessageProcessorFactory.getMessageReceiver();

        public Destination getDestination() {
            return destination;
        }

        public void setDestination(Destination destination) {
            this.destination = destination;
        }

        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        public ActiveMQEventCenter getEventCenter() {
            return eventCenter;
        }

        public void setEventCenter(ActiveMQEventCenter eventCenter) {
            this.eventCenter = eventCenter;
        }

        public IEventListener getEventListener() {
            return eventListener;
        }

        public void setEventListener(IEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void onMessage(Message message) {

            Event event = null;
            try {
                event = receiver.receive(message,this.getEventListener());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("receive event exception:",e);
                return;
            }

            logger.info("receive message - event name:{}", EventUtil.getEventName((Class<Event>) event.getClass()));


            String logId = UUID.randomUUID().toString();
            logger.info("execute event(trace id:{}) - begin", logId);
            try {
                boolean acknowledge = eventListener.onEvent(this.eventCenter, event);
                if (acknowledge) {
                    message.acknowledge();
                    logger.info("execute event(trace id:{}) success! acknowledge:{} - end", logId, acknowledge);
                } else {
                    logger.warn("execute event(trace id:{}) fail! acknowledge:{} - end", logId, acknowledge);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("execute event(trace id:{}) fail ! reason:{}", logId, e.getMessage());
            }

        }
    }


}

