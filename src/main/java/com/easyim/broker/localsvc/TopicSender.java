package com.easyim.broker.localsvc;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.event.ClientSendEvent;
import com.broker.base.event.MQSubscribe;
import com.broker.base.protocol.ProtocolMessage;
import com.broker.utils.strorage.StorageFactory;
import com.commom.DBConst;
import com.commom.Topic;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import com.easyim.entity.MessageStableOffline;
import com.easyim.service.app.MessageStableOfflineService;
import com.google.common.eventbus.Subscribe;
import com.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:18
 * Description: 向client 发送消息
 **/

@Slf4j
@Component
public class TopicSender {
    @Autowired
    private MessageStableOfflineService messageStableOfflineService;
    private IBrokerEventBus eventBus = null;
    private SocketIOServer socketIOServer = null;
    private IStorage storage = null;
    private Map<String, Consumer<String>> clientAckCallback = new HashMap<>();

    public void registerEventBus(IBrokerEventBus eventBus){
        this.eventBus = eventBus;
        this.eventBus.register(this);
        // 获取 broker 的存储实例
        storage = StorageFactory.getInstance();
    }

    public void setSocketServer(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;

        // 设置客户端监听回调到 'topic.clientack'
        this.socketIOServer.addEventListener("topic.clientack", String.class, (client, ackId, ackSender) -> {
            Consumer<String> ackCallback = clientAckCallback.get(ackId);
            if(ackCallback != null){
                ackCallback.accept(ackId);
                clientAckCallback.remove(ackId);
            }
        });
    }

    public IBrokerEventBus getSender() {
        return eventBus;
    }
    public SocketIOServer getSocketIOServer() {
        return socketIOServer;
    }
    public IStorage getStorage() {
        return storage;
    }

    // 发消息到连接的客户端
    @MQSubscribe
    @Subscribe
    public void onReceiveClientSendMessage(ClientSendEvent message){
        if("".equals(message.getTo())){
            return;
        }
        SocketIOClient client = findClientWithId(message.getClientId());
        if(client == null){
            return;
        }
        String appKey = message.getAppKey();
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setRequestId(new Date().getTime()+"");
        String topic = Topic.APP_MESSAGE.name;
        protocolMessage.setMethod(Topic.APP_MESSAGE.METHOD.SEND_MESSAGE_TO_CLIENT);
        clientSendEventWithAck(client, topic, protocolMessage, (ackId)->{
            log.info("clientSendEventWithAck  ok, ackId:" + ackId);
            messageStableOfflineService.lambdaUpdate()
                    .set(MessageStableOffline::getStatus, DBConst.MessageStableOfflineStatus.SENT.name())
                    // where
                    .eq(MessageStableOffline::getAppKey, appKey)
                    .eq(MessageStableOffline::getMid, message.getMid())
                    // update
                    .update();
        });
    }

    @Nullable
    private SocketIOClient findClientWithId(String clientId){
        return this.socketIOServer.getAllClients().stream().filter(v->v.getSessionId().toString().equals(clientId)).findFirst().orElse(null);
    }

    private void clientSendEventWithAck(SocketIOClient client, String topic, ProtocolMessage protocolMessage, Consumer<String> consumer){
        String ackKey = "ack-" + UUIDUtils.gen();
        clientAckCallback.put(ackKey, consumer);
        client.sendEvent(topic, protocolMessage, new VoidAckCallback(){
            protected void onSuccess() {
            }
        }, ackKey);
    }

}
