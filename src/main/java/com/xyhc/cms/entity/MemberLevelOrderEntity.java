
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
 * @since 2023-03-22 18:48:06
 */
@Data
@TableName("member_level_order")
public class MemberLevelOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    /**
     * 名称
     */
    private String levelId;


    /**
     *
     */
    private String uid;

    /**
     * 邀请人
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
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    /**
     * 操作人
     */
    private String updateBy;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否支付
     */
    private Integer isPay;

    /**
     * 支付流水号
     */
    private String outTradeNo;

    /**
     * 交易流水号
     */
    private String transactionId;

    /**
     * 备注
     */
    private String orderRemark;

    /**
     * 金额
     */
    private double total;

    /**
     * 签名图片
     */
    private String signImgurl;

    /**
     * 合同ID
     */
    private String contractId;

    @TableField(exist = false)
    private String contract;

    @TableField(exist = false)
    private String remark;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String partyCName;

    @TableField(exist = false)
    private String levelType;
    /**
     * 会员名称
     */
    @TableField(exist = false)
    private String levelName;
    /**
     * 购买人手机号
     */
    @TableField(exist = false)
    private String mobile;
    /**
     * 上级
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 上级电话
     */
    @TableField(exist = false)
    private String parentMobile;
    /**
     * 上上级
     */
    @TableField(exist = false)
    private String parentMaxName;


}
