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
 * 组用户关系表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoomUser implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 群ID
     */
    @TableField("rid")
    private String rid;

    /**
     * 用户auid
     */
    @TableField("auid")
    private String auid;

    /**
     * 群员状态(NORMAL:正常;DEL: 已移除)
     */
    @TableField("status")
    private String status;

}
