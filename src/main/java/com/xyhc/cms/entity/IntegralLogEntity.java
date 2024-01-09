
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
 * 实体
 *
 * @author apollo
 * @since 2023-08-17 15:26:30
 */
@Data
@TableName("integral_log")
public class IntegralLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 变更人
     */
    private String userId;
    /**
     * 是否到账 0否 1是 2已退款
     */
    private int xfAccount;
    /**
     * 已到账积分
     */
    private Integer collectedIntegral;
    /**
     * 变更金额
     */
    private int xfBalance;
    /**
     * 变更前金额
     */
    private int beforeBalane;
    /**
     * 变更后金额
     */
    private int afterBalance;
    /**
     * 消费类型
     * 一级分销收入
     * INCOME_LEVEL1
     * 二级分销收入
     * INCOME_LEVEL2
     * 任务收入
     * INCOME_TASK
     * 支付
     * PAY
     * 提现
     * CASH_OUT
     * 提现我的已到账收益
     * CASH_OUT_COLLECTED
     * 提现驳回
     * CASH_OUT_REJECT
     * 普通分销收入
     * COMMON_SHARING
     * 会员分销收入
     * VIP_SHARING
     * 会员级差分销收入
     * VIP_JICHA_SHARING
     * B会员升级A会员一次性反钱
     * UPGRADE
     * 加盟分享收入
     * JOIN_MONEY
     */
    private String xfClassify;
    /**
     * 消费备注
     */
    private String xfRemark;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
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
    @TableField(exist = false)
    private String xfType;
    /**
     * 未处理 10 已处理 20
     */
    private Integer status;
    /**
     * 分销收入_订单id
     */
    private String orderId;
}
