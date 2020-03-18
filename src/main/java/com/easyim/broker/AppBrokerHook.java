package com.easyim.broker;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.protocol.request.RequestMessage;
import com.broker.hook.BrokerLinkableHook;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.easyim.broker.localsvc.TopicDispatcher;
import com.easyim.broker.localsvc.TopicSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *  broker 业务处理
 *
 * */
@Slf4j
@Component
public class AppBrokerHook extends BrokerLinkableHook {
    private TopicDispatcher appDispatcher;
    private TopicSender appSender;

    public AppBrokerHook(TopicDispatcher appDispatcher, TopicSender appSender) {
        this.appDispatcher = appDispatcher;
        this.appSender = appSender;
    }


    @Override
    public void startup(SocketIOServer socketIOServer, IStorage iStorage, IBrokerEventBus eventBus) {
        this.appSender.registerEventBus(eventBus);
        this.appSender.setSocketServer(socketIOServer);
    }

    public void onConnected(SocketIOClient client) {
    }

    public void onDisConnected(SocketIOClient client) {
    }

    public void onReceiveMessage(SocketIOClient client, RequestMessage message, AckRequest ackSender) {
        // 转发到业务层
        ackSender.sendAckData(this.appDispatcher.consumeMessage(message));
        if (this.next != null) {
            this.next.onReceiveMessage(client, message, ackSender);
        }
    }
}
