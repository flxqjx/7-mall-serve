
package com.xyhc.cms.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author apollo
 * @since 2023-05-24 13:40:41
 */
@Data
@TableName("apply_join")
public class ApplyJoinEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 收货人姓名
     */
        private String name;
    /**
     * 收货人联系电话
     */
        private String phone;
    /**
     * 创建时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 是否被删除
     */
        private Integer isDelete;
    /**
     * 修改时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 会员id
     */
        private String levelId;
    /**
     * 申请状态
     */
    private String applyState;

    /**
     * 驳回备注
     */
    private String applyRemark;

    /**
     * 身份证正面
     */
        private String cardNoFrontUrl;
    /**
     * 身份证反面
     */
        private String cardNoRearUrl;
    /**
     * UID
     */
        private String uid;
    /**
     * 创建人
     */
        private String createBy;
    /**
     * 修改人
     */
        private String updateBy;

    /**
     * 会员名称
     */
    @TableField(exist = false)
    private String levelName;

    /**
     * 微信昵称
     */
    @TableField(exist = false)
    private String nickName;

    /**
     * 微信头像
     */
    @TableField(exist = false)
    private String avatarUrl;

    /**
     * 微信头像
     */
    @TableField(exist = false)
    private String state;

    @TableField(exist = false)
    private String share;

    @TableField(exist = false)
    private Integer isPay;
}
