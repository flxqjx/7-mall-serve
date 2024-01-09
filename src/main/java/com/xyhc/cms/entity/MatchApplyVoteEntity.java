
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 赛事作品投票实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("match_apply_vote")
public class MatchApplyVoteEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 比赛ID
     */
    private String matchId;
    /**
     * 作品ID
     */
    private String matchApplyId;
    /**
     * 支付类型
     */
    private String payWay;
    /**
     * 支付金额
     */
    private double amount;

    /**
     * 申请人UID
     */
    private String uid;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatarUrl;


    /**
     * 是否支付
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
}
