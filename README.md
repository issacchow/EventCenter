# EventCenter 事件中心

用于发布/订阅分布式事件,用于事件领域模型,业务系统只需要将注意力关注在事件上，比如一个用户注册、一件商品发布/修改。

当前只有ActiveMQ实现IEventCenter。

## 事件类型
### 广播式事件(持久化事件)
一个事件可以由多个监听器处理,每个监听器处理负责不同的逻辑。

场景: 一个事件需要做多个扩展逻辑.

举例: 用户注册事件发布以后，可以有两个监听器，分别负责用户统计、欢迎邮件。



广播式事件的监听器只有注册后，才会收到注册前的所有未被消费的历史事件。(单次式事件注册时就可以读取历史事件)

### 单次式事件(持久化事件)
一个事件只能被 **一个监听器** 或者 **多个监听同一事件并且逻辑相同的监听器组** 处理。 单次式事件监听器只要注册就可以收到所有历史事件。

场景：需要固定顺序逻辑; 需要处理历史事件的场景。


## 使用

创建EventCenter
```
public IEventCenter buildEventCenter(String id){
    ActiveMQEventCenterFactory factory = new ActiveMQEventCenterFactory();
    ActiveMQEventCenter center = (ActiveMQEventCenter) factory.build("classpath:activeMQEventCenter.properties");
    center.setId(id);
    return center;
}
```

定义一次性事件:
```
@EventDispatchConfig(mode = EventDispatchMode.Once)
private class OnceEvent extends Event{
    private String eventData;

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
```

定义广播式事件:
```
@EventDispatchConfig(mode = EventDispatchMode.Broadcast)
public class BroadcastEvent extends Event{
    private String eventData;

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
```

定义事件监听器:
```
public OnceEventListener implements IEventListener<OnceEvent> {
    private String name;
    public OnceEventListener(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean onExecuteEvent(IEventCenter eventCenter, Event event) {

        System.out.println();
        System.out.print(String.format("%s -- consume a event,id:%s",this.name,event.getId()));
        return true;
    }
}
```


事件发布:
```
public void publishOnceEvent() throws Exception {
    IEventCenter eventCenter = buildEventCenter("onceEventPublisher");
    OnceEvent onceEvent = new OnceEvent();
    eventCenter.connect();

    int id = 0;
    while(id<100) {
        id++;
        onceEvent.setId(String.valueOf(id));
        eventCenter.publishEvent(onceEvent);
    }
}
```

事件监听:
```
public void onceEventListener() throws InterruptedException {
    IEventCenter eventCenter = buildEventCenter("onceEventListener1");
    eventCenter.connect();

    OnceEventListener listener = new OnceEventListener("listener1");
    OnceEventListener listener2 = new OnceEventListener("listener2");
    eventCenter.registerEventListener(listener);
    eventCenter.registerEventListener(listener2);
    eventCenter.reloadAllListener();

    while(true){
        Thread.sleep(1000);
    }
}
```
