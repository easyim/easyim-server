package com.easyim.request.form;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class RoomAddForm extends SocketHideProtocolMessage {
    @NotBlank(message = "群名不能为空")
    private String name = ""; // 群名
    @NotBlank(message = "群主用户帐号不能为空")
    private String owner = ""; // 群主auid
    @Size(min = 0)
    private List<String> members = new ArrayList<>(); // 群成员, 无需再加owner自己的账号
    private String announce = ""; // 群公告

    private String introduce = ""; // 群简介
    private String inviteText = ""; // 邀请文字
    private String avatar = ""; // 群LOGO,头像
    private String confJoinmode = ""; // 群加入模式
    private String confBeinvite = ""; // 群员被邀请方式
    private String confInviteother = ""; // 群员邀请权限(OWNER:仅群主;ALL:任何人也可以)
    private String confUpdate = ""; // 群信息更新权限(OWNER:仅群主;ALL:任何人也可以)
    private Integer maxMember = 100; // 群最大成员数量
}
