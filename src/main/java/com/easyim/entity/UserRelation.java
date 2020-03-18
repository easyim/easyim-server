package com.easyim.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 用户关系表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRelation implements Serializable {

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
     * 关系用户auid
     */
    @TableField("target_auid")
    private Integer targetAuid;

    /**
     * 关系(FRIEND:好友)
     */
    @TableField("type")
    private String type;

    /**
     * 备注名
     */
    @TableField("alias")
    private String alias;

    /**
     * 状态(NORMAL:正常;BLACKLIST:拉黑;FORBID:禁言;)
     */
    @TableField("status")
    private String status;

}
