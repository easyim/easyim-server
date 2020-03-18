package com.easyim.broker.storage;

import com.broker.base.IBrokerEventBus;
import com.broker.base.IStorage;
import com.broker.base.event.ClusterDispatcherEvent;
import com.broker.base.storage.BrokerStorageEntry;
import com.broker.base.utils.ObjectUtils;
import com.commom.DBConst;
import com.corundumstudio.socketio.SocketIOClient;
import com.easyim.broker.localsvc.TopicSender;
import com.easyim.entity.MessageStableOffline;
import com.easyim.service.app.MessageStableOfflineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 *  1. 离线/重发消息收集器
 *  2. 群组收集器
 *
 * */

@Slf4j
@Component
public class OfflineMessageCollector {
    @Autowired
    private TopicSender topicSender;
    @Autowired
    private MessageStableOfflineService messageStableOfflineService;
    @Value("${broker.machineId}")
    private String machineId;
    private IBrokerEventBus eventBus;
    private IStorage storage;

    @Scheduled(fixedRate = 60*1000) //  定时打印 broker 共享存储中的内容/连接的socket 客户端
    public void dumpBrokerStorageEntry() {
        Collection<SocketIOClient> clients = topicSender.getSocketIOServer().getAllClients();
        List<Pair<String, Map<String, String>>> storageValues = new ArrayList<Pair<String, Map<String, String>>>(){{
           add(new Pair<>(BrokerStorageEntry.MACHINE_CLIENTS, topicSender.getStorage().getAllKeyValues(BrokerStorageEntry.MACHINE_CLIENTS)));
           add(new Pair<>(BrokerStorageEntry.CLIENT_TO_LOGINUSER, topicSender.getStorage().getAllKeyValues(BrokerStorageEntry.CLIENT_TO_LOGINUSER)));
           add(new Pair<>(BrokerStorageEntry.CLIENT_USER_TOKEN, topicSender.getStorage().getAllKeyValues(BrokerStorageEntry.CLIENT_USER_TOKEN)));
        }};
        log.info(" =========== print broker storage start =========== ");
        storageValues.forEach(v->{
            log.info(" storage entry:{} ==> {} ", v.getObject1(), ObjectUtils.json(v.getObject2()));
        });
        log.info(" ----------------- connected client --------------------- ");
        clients.forEach(v->{
            log.info(" clientId:{} ==> user: {} ", v.getSessionId().toString(), topicSender.getStorage().getValue(BrokerStorageEntry.CLIENT_TO_LOGINUSER, v.getSessionId().toString()));
        });
        log.info(" =========== print broker storage end =========== ");
    }

    // 从 DB 更新离线消息/重发消息/发送失败的消息 到 cache
    @Scheduled(fixedRate = 5000) // 5000 毫秒执行一次
    public void protocolMessageReSender() {
        Collection<SocketIOClient> clients = topicSender.getSocketIOServer().getAllClients();
        List<String> clientIds = clients.stream().map(v->v.getSessionId().toString()).collect(Collectors.toList());
        Map<String, String> clientUserMap = storage.getAllKeyValues(BrokerStorageEntry.CLIENT_TO_LOGINUSER);
        List<String> loginUserAuid = new ArrayList<>();
        clientUserMap.forEach((k,v)->{
            if(clientIds.contains(k)){
                loginUserAuid.add(v);
            }
        });
        if(loginUserAuid.isEmpty()){
            return;
        }
        // 用已登录的用户查离线消息
        List<MessageStableOffline> offlineMessages = messageStableOfflineService.lambdaQuery()
                .in(MessageStableOffline::getToUser, loginUserAuid)
                .ne(MessageStableOffline::getStatus, DBConst.MessageStableOfflineStatus.SENT.name())
                .gt(MessageStableOffline::getCreateTime, new Date().getTime() + 1*60*1000) // 超过1分钟的离线消息
                .list();

        // 创建Client转发消息，交给Client转发组件来路由
        offlineMessages.forEach(msg->{
            ClusterDispatcherEvent dispatcherMsg = new ClusterDispatcherEvent();
            dispatcherMsg.setMid(msg.getMid());
            dispatcherMsg.setWay(msg.getWay());
            dispatcherMsg.setFrom(msg.getFromUser());
            dispatcherMsg.setTo(msg.getToUser());
            dispatcherMsg.setMsgBody(msg.getBody());
            dispatcherMsg.setAppKey(msg.getAppKey());
            dispatcherMsg.setOffline(1); // 这是离线消息

            topicSender.getSender().send(dispatcherMsg);
        });

    }

    public void startUp(IBrokerEventBus eventBus, IStorage storage) {
        this.eventBus = eventBus;
        this.storage = storage;
        // 监听事件
        this.eventBus.register(this);
    }
}
