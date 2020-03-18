package com.easyim.service.app;

import com.annotation.MethodFor;
import com.annotation.TopicFor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broker.base.event.ClusterDispatcherEvent;
import com.broker.base.protocol.response.Resp;
import com.broker.base.utils.ObjectUtils;
import com.commom.DBConst;
import com.commom.Topic;
import com.easyim.broker.localsvc.TopicSender;
import com.easyim.entity.*;
import com.easyim.mapper.MessageMapper;
import com.easyim.request.form.MessageSendForm;
import com.easyim.request.form.UsersAddForm;
import com.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:21
 * Description: easyim
 **/


@Slf4j
@TopicFor(value = Topic.APP_MESSAGE.name)
@Service
public class MessageService extends ServiceImpl<MessageMapper, Message>  {
    @Autowired
    TopicSender topicSender;
    @Autowired
    MessageStableOfflineService messageStableOfflineService;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    RoomUserService roomUserService;


    @Transactional
    @MethodFor(value = Topic.APP_MESSAGE.METHOD.SEND, consumer = UsersAddForm.class)
    public Resp<String> send(@Valid MessageSendForm sendForm){
        log.info(" add: " + ObjectUtils.json(sendForm));
        if(!Arrays.asList(
                DBConst.MessageWay.P2P.name(),
                DBConst.MessageWay.P2R.name(),
                DBConst.MessageWay.P2HR.name()
                ).contains(sendForm.getWay())){
            return Resp.failed("消息way必须是P2P,P2R,P2HR 其中之一");
        }
        int fromUserCnt = userService.lambdaQuery().eq(User::getAuid, sendForm.getFormUser()).count();
        if(fromUserCnt == 0){
            return Resp.failed(String.format("发送用户:%s不存在.", sendForm.getFormUser()));
        }

        Message msg = ObjectUtils.copy(sendForm, Message.class)
                .setUid(UUIDUtils.gen())
                .setAppKey(sendForm.getUserJwt().getAppKey())
                .setStatus(DBConst.EntryStatus.NORMAL.name());
        this.save(msg);

        // 创建Client转发消息，交给Client转发组件来路由
        ClusterDispatcherEvent dispatcherMsg = new ClusterDispatcherEvent();
        if(msg.getWay().equals(DBConst.MessageWay.P2P.name())){
            int cnt = userService.lambdaQuery().eq(User::getAuid, sendForm.getToTarget()).count();
            if(cnt == 0){
                return Resp.failed(String.format("目标用户:%s不存在.", sendForm.getToTarget()));
            }

            MessageStableOffline messageStableOffline = ObjectUtils.copy(sendForm, MessageStableOffline.class)
                    .setUid(UUIDUtils.gen())
                    .setMid(msg.getUid())
                    .setFromUser(sendForm.getFormUser())
                    .setToUser(sendForm.getToTarget())
                    .setWay(msg.getWay())
                    .setAppKey(sendForm.getUserJwt().getAppKey())
                    .setStatus(DBConst.MessageStableOfflineStatus.OFFLINE.name())
                    .setCreateTime(new Date());
            messageStableOfflineService.save(messageStableOffline);

        }
        else if(msg.getWay().equals(DBConst.MessageWay.P2R.name())){
            Room room = roomService.lambdaQuery().eq(Room::getUid, sendForm.getToTarget()).one();
            if(room == null){
                return Resp.failed("IM群不存在:rid=" + sendForm.getToTarget());
            }
            // 查找群的所有成员
            List<RoomUser> roomUsers = roomUserService.lambdaQuery().eq(RoomUser::getRid, room.getUid()).list();
            List<MessageStableOffline> messageStableOfflines = roomUsers.stream().map(roomUser -> {
                return ObjectUtils.copy(sendForm, MessageStableOffline.class)
                        .setUid(UUIDUtils.gen())
                        .setMid(msg.getUid())
                        .setRid(room.getUid())
                        .setFromUser(sendForm.getFormUser())
                        .setToUser(roomUser.getAuid()) // 用户的auid
                        .setWay(msg.getWay())
                        .setAppKey(sendForm.getUserJwt().getAppKey())
                        .setStatus(DBConst.MessageStableOfflineStatus.OFFLINE.name())
                        .setCreateTime(new Date());
            }).collect(Collectors.toList());

            messageStableOfflineService.saveBatch(messageStableOfflines);

            dispatcherMsg.setRoomMembers(roomUsers.stream().map(v->v.getAuid()).collect(Collectors.toList()));
        }



        dispatcherMsg.setMid(msg.getUid());
        dispatcherMsg.setWay(msg.getWay());
        dispatcherMsg.setFrom(sendForm.getFormUser());
        dispatcherMsg.setTo(sendForm.getToTarget());
        dispatcherMsg.setMsgBody(sendForm.getBody());
        dispatcherMsg.setAppKey(sendForm.getUserJwt().getAppKey());

        topicSender.getSender().send(dispatcherMsg);

        return Resp.ok("发送成功");
    }

}
