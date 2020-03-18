package com.easyim.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 不丢失的发送消息/离线消息管理表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MessageStableOffline implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内部ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * UUID格式的ID
     */
    @TableField("uid")
    private String uid;

    /**
     * SDK APP KEY
     */
    @TableField("app_key")
    private String appKey;

    /**
     * 消息UID
     */
    @TableField("mid")
    private String mid;

    /**
     * 群ID
     */
    @TableField("rid")
    private String rid;

    /**
     * 消息发送方式(P2P:点对点;P2G:点对多)
     */
    @TableField("way")
    private String way;

    /**
     * 消息来源用户auid
     */
    @TableField("from_user")
    private String fromUser;

    /**
     * 消息目标用户auid
     */
    @TableField("to_user")
    private String toUser;

    /**
     * 待发送的消息正文(冗余)
     */
    @TableField("body")
    private String body;

    /**
     * 消息的发送状态(WAIT:待发送, OFFLINE:离线中, SEND:发送完成)
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
