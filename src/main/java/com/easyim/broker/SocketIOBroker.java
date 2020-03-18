package com.easyim.broker;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.protocol.ProtocolMessage;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.base.storage.BrokerStorageEntry;
import com.broker.base.utils.ObjectUtils;
import com.broker.hook.BrokerLinkableHook;
import com.broker.hook.support.ClientAuthCollectorHook;
import com.broker.hook.support.ClientConnectionCollectorHook;
import com.broker.hook.support.ClientSingleHook;
import com.broker.utils.events.EventFactory;
import com.broker.utils.strorage.StorageFactory;
import com.corundumstudio.socketio.SocketIOServer;
import com.easyim.broker.storage.OfflineMessageCollector;
import com.easyim.broker.localsvc.TopicDispatcher;
import com.easyim.broker.localsvc.TopicSender;
import com.easyim.service.app.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 13:52
 * Description: easyimbroker
 **/

@Component
public class SocketIOBroker {
    @Autowired
    private UserService userService;
    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    TopicDispatcher topicDispatcher;
    @Autowired
    OfflineMessageCollector scheduleStorageSync;
    @Autowired
    TopicSender topicSender;
    @Value("${broker.machineId}")
    private String machineId;

    @PostConstruct
    private void autoStartup() {
        startSocketIOBroker(socketIOServer, topicDispatcher, scheduleStorageSync);
    }

    @PreDestroy
    private void autoStop() {
        socketIOServer.stop(); // 在销毁 Bean之前关闭,避免重启项目服务端口占用问题
        EventFactory.getInstance().destroy(); // 销毁事件组件
    }

    public void destroy() {
        this.autoStop();
    }

    private void startSocketIOBroker(SocketIOServer socketIOServer, TopicDispatcher topicDispatcher, OfflineMessageCollector scheduleStorageSync){
        // broker 组件列表, 可根据需要自行添加组件
        LinkedList<BrokerLinkableHook> hooks = new LinkedList<BrokerLinkableHook>(){{
            add(new ClientConnectionCollectorHook(machineId)); // client 连接收集器
            add(new ClientAuthCollectorHook(machineId)); // client 登录授权, client登录用户收集器
            add(new AppBrokerHook(topicDispatcher, topicSender));// 业务处理
            add(new ClientSingleHook(machineId)); // Client转发组件(分发到实际连接client 的机器)
        }};
        LinkedList<BrokerLinkableHook> hooksLinkable = this.createBrokerLinkableHooks(hooks);
        // 配置broker
        BrokerConfig brokerConfig = new BrokerConfig();
        brokerConfig.setMqPoint(machineId);
        brokerConfig.initSystemProperty();
        // 设置用户的认证信息
        IStorage storage = initAuthUserInfo();
        IBrokerEventBus eventBus = EventFactory.getInstance();
        // 配置中间件
        configBroker(socketIOServer, hooksLinkable, storage);
        // 配置数据同步器
        scheduleStorageSync.startUp(eventBus, storage);
        // 启动socket.io server
        socketIOServer.start();
    }
    // 获取所有用户认证信息(auid => token)
    private IStorage  initAuthUserInfo(){
        IStorage storage = StorageFactory.getInstance();
        Map<String, String> userAuidToToken = new HashMap<>();
        userService.list().forEach(u->{
            userAuidToToken.put(u.getAuid(), u.getToken());
        });
        storage.setKeyValue(BrokerStorageEntry.CLIENT_USER_TOKEN, userAuidToToken);

        return storage;
    }

    // 事件通知
    private IBrokerEventBus createBrokerEventBus(){
        return EventFactory.getInstance();
    }

    // 配置中间件
    public void configBroker(SocketIOServer socketIOServer, LinkedList<BrokerLinkableHook> hooks, IStorage storage){
        IBrokerEventBus eventBus = createBrokerEventBus();
        // 组件初始化
        hooks.forEach(h->{
            h.startup(socketIOServer, storage, eventBus);
        });
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            hooks.forEach(e->{
                e.onConnected(client);
            });
        });
        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            hooks.forEach(e->{
                e.onDisConnected(client);
            });
        });
        // 所有的topic
        List<String> topics = Arrays.asList(
                "topic.error",
                "topic.connection",
                "topic.user"
        );
        topics.forEach(e->{
            socketIOServer.addEventListener(e, Object.class, (client, msgBody, ackSender) -> {
                ProtocolMessage message = ObjectUtils.copy(msgBody, ProtocolMessage.class);
                if(message != null){
                    if(hooks.size() > 0){
                        // 使用责任链模式, 将onReceiveMessage 依次向后传递
                        hooks.get(0).onReceiveMessage(
                                client,
                                new RequestMessage<>()
                                        .setTopic(e)
                                        .setMethod(message.getMethod())
                                        .setRequestId(message.getRequestId())
                                        .setBody(msgBody)
                                        .setProtocolMessage(message)
                                ,ackSender
                        );
                    }
                }
            });
        });
    }


    // 将组件一个接一个连接起来
    public LinkedList<BrokerLinkableHook> createBrokerLinkableHooks(LinkedList<BrokerLinkableHook> hooks){
        for(int i = 0; i < hooks.size(); i++){
            if( i == hooks.size()-1 ){
                hooks.get(i).setNext(null); // 没有后续组件
            }
            else {
                hooks.get(i).setNext(hooks.get(i+1));
            }
        }
        return hooks;
    }


}
