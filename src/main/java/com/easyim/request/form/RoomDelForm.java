package com.easyim.request.form;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class RoomDelForm extends SocketHideProtocolMessage {
    private String rid = ""; // 群ID
    @NotBlank(message = "群主用户帐号不能为空")
    private String owner = ""; // 群主auid
}
