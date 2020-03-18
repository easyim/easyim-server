package com.easyim.response.dto;


import com.easyim.request.form.SocketHideProtocolMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class RoomDetailListDTO extends SocketHideProtocolMessage {
    private Integer gid = 0; // 群id
    private String name = ""; //群名
    private String owner = ""; // 群主,用户auid
    private String announce = ""; // 群公告
    private String introduce = ""; // 群简介
    private String inviteText = ""; // 邀请文字
    private String avatar = ""; // 群LOGO,头像
    private String confJoinmode = ""; // 群加入模式
    private Integer confBeinvite = 0; // 群员被邀请方式
    private Integer confInviteother = 0; // 群员邀请权限(OWNER:仅群主;ALL:任何人也可以)
    private Integer confUpdate = 0; // 群信息更新权限(OWNER:仅群主;ALL:任何人也可以)
    private Integer maxMember = 0; // 群最大成员数量
    private String status = ""; // 状态(NORMAL:正常;DEL: 已解散)
    private List<UserListDTO> members = new ArrayList<>();

}
