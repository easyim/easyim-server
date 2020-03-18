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
 * 系统消息/自定义消息表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MessageNotify implements Serializable {

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
     * 消息ID
     */
    @TableField("mid")
    private String mid;

    /**
     * 消息发送方式(P2P:点对点;P2G:点对多)
     */
    @TableField("way")
    private String way;

    /**
     * 来源用户auid
     */
    @TableField("from")
    private String from;

    /**
     * 目标用户auid
     */
    @TableField("to")
    private String to;

    /**
     * 消息体,json格式
     */
    @TableField("body")
    private String body;

    /**
     * 推送文案
     */
    @TableField("pushcontent")
    private String pushcontent;

    /**
     * 是否离线消息(1:是;0:否)
     */
    @TableField("offline")
    private Integer offline;

    /**
     * 离线消失超时时间(仅offline为1时有效)
     */
    @TableField("offline_expire_date")
    private Date offlineExpireDate;

    /**
     * 消息状态(NORMAL:正常;DEL: 已删除;CANCEL:已撤回)
     */
    @TableField("status")
    private String status;

    /**
     * 消息的附加内容(应用在这里添加自定义内容)
     */
    @TableField("ex")
    private String ex;

}
