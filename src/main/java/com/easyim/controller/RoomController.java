package com.easyim.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.broker.base.protocol.response.Resp;
import com.commom.Topic;
import com.easyim.response.dto.RoomDetailListDTO;
import com.easyim.response.dto.RoomListDTO;
import com.easyim.request.form.*;
import com.easyim.service.app.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 群相关业务
 */
@RestController
@RequestMapping(Topic.APP_ROOM.base_uri)
public class RoomController extends ApiController {
    @Autowired
    private RoomService roomService;

    /**
     * APP添加IM群
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.ADD)
    public Resp<String> addRoom(@RequestBody @Valid RoomAddForm addForm) {
        return roomService.add(addForm);
    }

    /**
     * APP更新IM群
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.UPDATE)
    public Resp<String> updateRoom(@RequestBody @Valid RoomUpdateForm updateForm) {
        return roomService.updateRoom(updateForm);
    }


    /**
     * APP获取IM群
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.LIST)
    public Resp<List<RoomListDTO>> listRooms(@RequestBody @Valid List<String> auids) {
        return roomService.listRoom(auids);
    }

    /**
     * APP获取IM群/IM群成员
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.LIST_MEMBER)
    public Resp<List<RoomDetailListDTO>> listRoomDetail(@RequestBody @Valid List<String> auids) {
        return roomService.listRoomDetail(auids);
    }


    /**
     * APP IM群添加成员
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.ADD_USER)
    public Resp<String> groupAddUser(@RequestBody @Valid RoomAddUserForm addUserForm) {
        return roomService.addRoomUser(addUserForm);
    }

    /**
     * APP IM群移除成员
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.DEL_USER)
    public Resp<String> groupDelUser(@RequestBody @Valid RoomDelUserForm delUserForm) {
        return roomService.delRoomUser(delUserForm);
    }

    /**
     * APP IM 删除群/解散群
     */
    @PostMapping("/" + Topic.APP_ROOM.METHOD.DEL)
    public Resp<String> groupDel(@RequestBody @Valid RoomDelForm delForm) {
        return roomService.delRoom(delForm);
    }
}
