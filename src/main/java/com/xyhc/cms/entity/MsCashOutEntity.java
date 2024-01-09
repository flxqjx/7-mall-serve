
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Data
@TableName("ms_cash_out")
public class MsCashOutEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * uid
     */
    private String uid;
    /**
     * 原始申请金额
     */
    private double originalMoney;
    /**
     * 申请金额
     */
    private double applyMoney;
    /**
     * 变更前金额
     */
    private double beforeBalane;
    /**
     * 变更后金额
     */
    private double afterBalance;
    /**
     * 提现账号
     */
    private String bankId;

    /**
     * 提现分类
     */
    private String cashOutType;
    /**
     * 发票
     */
    private String invoice;

    /**
     * 手续费
     */
    private double serviceCharge;

    /**
     * 支付宝账号
     */
    private String apayAccount;

    /**
     * 姓名
     */
    private String apayName;

    /**
     * 变更人
     */
    private String userId;

    /**
     * 说明
     */
    private String remark;
    /**
     * 删除标志
     */
    private Integer isDelete;

    /**
     * 是否审核通过 待审核 0 审核通过 1 驳回 -1
     */
    private String isPass;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间/注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    /**
     * 最后更新人
     */
    private String updateBy;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 驳回备注
     */
    private String rejectRemark;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String bankName;

    @TableField(exist = false)
    private String bankCode;

    @TableField(exist = false)
    private String bankNo;

    @TableField(exist = false)
    private String deposit;

    @TableField(exist = false)
    private String realName;

    @TableField(exist = false)
    private String mobileBank;


    /**
     * 身份证正面
     */
    @TableField(exist = false)
    private String cardNoFrontUrl;
    /**
     * 身份证反面
     */
    @TableField(exist = false)
    private String cardNoRearUrl;

    @TableField(exist = false)
    private String nickName;
    /**
     * 实际金额
     */
    @TableField(exist = false)
    private double realityCashMoney;


    private String paymentNo;
    private String returnCode;
    private String returnMsg;
    private String errCodeDes;
    private String outBatchNo;
    private String partnerTradeNo;
    private String resultCode;
    private String outDetailNo;

    
    /**
     * 商户平台状态回写完成
     */
    private int isUpdateDone;
    /**
     * 商户平台转账状态
     */
    private String merchantPayStatus;
    /**
     * 商户平台转账备注
     */
    private String merchantPayRemark;
    /**
     * 是否新用户
     */
    @TableField(exist = false)
    private String isNewUsers;
}
