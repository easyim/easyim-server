package com.easyim.service.app;

import com.annotation.MethodFor;
import com.annotation.TopicFor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.broker.base.protocol.response.Resp;
import com.broker.base.utils.ObjectUtils;
import com.commom.DBConst;
import com.commom.Topic;
import com.easyim.entity.Room;
import com.easyim.entity.RoomUser;
import com.easyim.entity.User;
import com.easyim.mapper.RoomMapper;
import com.easyim.request.form.*;
import com.easyim.response.dto.RoomDetailListDTO;
import com.easyim.response.dto.RoomListDTO;
import com.easyim.response.dto.UserListDTO;
import com.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/2/21 13:21
 * Description: easyim
 **/


@Slf4j
@TopicFor(value = Topic.APP_ROOM.name)
@Service
public class RoomService extends ServiceImpl<RoomMapper, Room>  {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomUserService roomUserService;

    @Transactional
    @MethodFor(value = Topic.APP_ROOM.METHOD.ADD, consumer = RoomAddForm.class)
    public Resp<String> add(@Valid RoomAddForm roomAddForm){
        log.info(" add: " + ObjectUtils.json(roomAddForm));
        String appKey = "";
        List<String> members = roomAddForm.getMembers();
        members.add(roomAddForm.getOwner()); // 添加群主
        // 检测 members, owner 的有效性
        List<User> users = userService.lambdaQuery().in(User::getAuid, members).list();
        List<String> memberNotExist = new ArrayList<>();
        members.forEach(v->{
            List<String> auids = users.stream().map(vv->vv.getAuid()).collect(Collectors.toList());
            if(auids.contains(v)){
                memberNotExist.add(v);
            }
        });
        if(!memberNotExist.isEmpty()){
            return Resp.failed("以下的群员不存在:" + ObjectUtils.json(memberNotExist));
        }
        // 添加群
        Room room = ObjectUtils.copy(roomAddForm, Room.class).setUid(UUIDUtils.gen()).setAppKey(appKey);
        this.save(room);
        // 添加群员
        addRoomUserToDB(appKey, room.getUid(), members);

        return Resp.ok("添加成功");
    }
    @MethodFor(value = Topic.APP_ROOM.METHOD.UPDATE, consumer = RoomAddForm.class)
    public Resp<String> updateRoom(RoomUpdateForm updateForm) {
        this.lambdaUpdate()
                .set(ObjectUtils.strNotEmpty(updateForm.getName()), Room::getName, updateForm.getName())
                .set(ObjectUtils.strNotEmpty(updateForm.getOwner()), Room::getOwner, updateForm.getAvatar())
                .set(ObjectUtils.strNotEmpty(updateForm.getAnnounce()), Room::getAnnounce, updateForm.getAnnounce())
                .set(ObjectUtils.strNotEmpty(updateForm.getIntroduce()), Room::getIntroduce, updateForm.getIntroduce())
                .set(ObjectUtils.strNotEmpty(updateForm.getAvatar()), Room::getAvatar, updateForm.getAvatar())
                .set(ObjectUtils.strNotEmpty(updateForm.getConfJoinmode()), Room::getConfJoinmode, updateForm.getConfJoinmode())
                .set(ObjectUtils.strNotEmpty(updateForm.getConfBeinvite()), Room::getConfJoinmode, updateForm.getConfBeinvite())
                .set(ObjectUtils.strNotEmpty(updateForm.getConfInviteother()), Room::getConfInviteother, updateForm.getConfInviteother())
                .set(ObjectUtils.strNotEmpty(updateForm.getConfUpdate()), Room::getConfUpdate, updateForm.getConfUpdate())
                .set(Room::getMaxMember, updateForm.getMaxMember())
                // where
                .eq(Room::getUid, updateForm.getGid());

        return Resp.ok("更新成功");
    }
    @MethodFor(value = Topic.APP_ROOM.METHOD.LIST, consumer = RoomAddForm.class)
    public Resp<List<RoomListDTO>> listRoom(List<String> gids) {
        List<Room> users = this.lambdaQuery().in(Room::getId, gids).list();
        List<RoomListDTO> dtos = users.stream().map(v->ObjectUtils.copy(v, RoomListDTO.class).setGid(v.getId())).collect(Collectors.toList());
        return Resp.ok(dtos);
    }

    public Resp<String> addRoomUser(RoomAddUserForm addUserForm) {
        String sdkId = "";
        addRoomUserToDB(sdkId, addUserForm.getRid(), addUserForm.getMembers());
        return Resp.ok("成功.");
    }

    private void addRoomUserToDB(String sdkId, String roomId, List<String> members){
        // 添加群员
        List<RoomUser> roomUsers = members.stream()
                .map(v->{
                    return new RoomUser().setUid(UUIDUtils.gen()).setRid(roomId).setAuid(v).setAppKey(sdkId).setStatus("NORMAL");
                }).collect(Collectors.toList());
        roomUserService.saveBatch(roomUsers);
    }

    public Resp<String> delRoomUser(RoomDelUserForm delUserForm) {
        delRoomUserFromDB(delUserForm.getMembers());
        return Resp.ok("成功.");
    }
    private void delRoomUserFromDB(List<String> members){
        List<RoomUser> roomUsers = roomUserService.lambdaQuery().in(RoomUser::getAuid, members).list();
        List<Integer> ids = roomUsers.stream().map(v->v.getId()).collect(Collectors.toList());
        roomUserService.removeByIds(ids);
    }

    public Resp<String> delRoom(RoomDelForm delForm) {
        int cnt = this.lambdaQuery()
                .eq(Room::getUid, delForm.getRid())
                .eq(Room::getOwner, delForm.getOwner())
                .count();
        if(cnt == 0){
            return Resp.failed(String.format("owner:%s 不是群:%d 的群主", delForm.getOwner(), delForm.getRid()));
        }
        // 标记删除
        this.lambdaUpdate()
                .set(Room::getStatus, DBConst.EntryStatus.DEL.name())
                .eq(Room::getUid, delForm.getRid())
                .update();
        // 删组员
        List<RoomUser> roomUsers = roomUserService.lambdaQuery().in(RoomUser::getRid, delForm.getRid()).list();
        List<Integer> ids = roomUsers.stream().map(v->v.getId()).collect(Collectors.toList());
        roomUserService.removeByIds(ids);

        return Resp.ok("成功.");
    }


    @MethodFor(value = Topic.APP_ROOM.METHOD.LIST, consumer = RoomAddForm.class)
    public Resp<List<RoomDetailListDTO>> listRoomDetail(List<String> gids) {
        List<Room> rooms = this.lambdaQuery().in(Room::getId, gids).list();
        List<RoomUser> roomUsers = roomUserService.lambdaQuery()
                .in(RoomUser::getRid, rooms.stream().map(v->v.getId()))
                .list();
        List<User> users = userService.lambdaQuery()
                .in(User::getAuid, roomUsers.stream().map(v->v.getAuid()))
                .list();

        List<UserListDTO> userListDTOS = users.stream().map(v->ObjectUtils.copy(v, UserListDTO.class)).collect(Collectors.toList());
        Map<String, UserListDTO> userMapByAuid = new HashMap<>();
        userListDTOS.forEach(v->{
            userMapByAuid.put(v.getAuid(), v);
        });
        List<RoomDetailListDTO> dtos = new ArrayList<>();
        rooms.forEach(v->{
            RoomDetailListDTO dto = ObjectUtils.copy(v, RoomDetailListDTO.class);
            dto.setMembers(
                    roomUsers.stream()
                    .map(vv->userMapByAuid.getOrDefault(vv.getAuid(), new UserListDTO()))
                    .collect(Collectors.toList())
            );
            dtos.add(dto);
        });

        return Resp.ok(dtos);
    }
}
