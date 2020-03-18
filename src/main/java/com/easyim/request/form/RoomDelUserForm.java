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
public class RoomDelUserForm extends SocketHideProtocolMessage {
    private Integer gid = 0; // 群ID
    @NotBlank(message = "群主用户帐号不能为空")
    private String owner = ""; // 群主auid
    @Size(min = 1, message = "移除的群成员不能为空")
    private List<String> members = new ArrayList<>(); // 要移除的群成员
    private String ex = ""; // 扩展字段
}
