
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
 * @since 2023-02-14 13:58:59
 */
@Data
@TableName("user_level_order")
public class UserLevelOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * LevelID
     */
        private String levelId;
    /**
     * 变更人
     */
        private String userId;
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
     * 是否付费
     */
        private Integer isPay;
    /**
     * 支付时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date payTime;
    /**
     * 支付流水号
     */
        private String outTradeNo;
    /**
     * 交易流水号
     */
        private String transactionId;

//
    @TableField(exist = false)
    private String levelName;
}
