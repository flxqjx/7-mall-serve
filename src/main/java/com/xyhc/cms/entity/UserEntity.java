
package com.xyhc.cms.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户主表实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 账号
     */
        private String account;
    /**
     * 姓名
     */
        private String userName;
    /**
     * 手机
     */
        private String mobile;
    /**
     * 密码
     */
        private String passward;
    /**
     * 上级ID
     */
        private String parentId;
    /**
     * 操作时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 操作人
     */
        private String createBy;
    /**
     * 操作时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 操作人
     */
        private String updateBy;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 状态0 正常 -1 lock
     */
        private Integer status;
    /**
     * 余额
     */
        private double balance;
    /**
     * 邀请码
     */
        private String vipNo;
}
