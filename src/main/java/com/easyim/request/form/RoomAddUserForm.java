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
public class RoomAddUserForm extends SocketHideProtocolMessage {
    private String rid = ""; // 群ID
    @NotBlank(message = "群主用户帐号不能为空")
    private String owner = ""; // 群主auid
    @Size(min = 1, message = "邀请成员不能为空")
    private List<String> members = new ArrayList<>(); // 群成员, 无需再加owner自己的账号
    private Integer needAgree = 0; // 拉群时是否需要确认.
    private String inviteText = ""; // 邀请文字
    private String ex = ""; // 扩展字段
}
