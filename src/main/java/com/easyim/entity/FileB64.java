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
 * base64格式文件表
 * </p>
 *
 * @author sun
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileB64 implements Serializable {

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
     * base64格式的文件内容
     */
    @TableField("content")
    private String content;

    /**
     * 上传文件类型
     */
    @TableField("type")
    private String type;

    /**
     * 超时时间
     */
    @TableField("exire_date")
    private Date exireDate;

    /**
     * 文件状态(NORMAL:正常;DEL: 已删除)
     */
    @TableField("status")
    private String status;

}
