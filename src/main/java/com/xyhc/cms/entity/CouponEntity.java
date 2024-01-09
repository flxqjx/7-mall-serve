
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
 * 平台优惠券实体
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Data
@TableName("coupon")
public class CouponEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠卷ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 优惠卷类型
     */
    private String title;
    /**
     * 优惠卷类型
     */
    private String couponType;
    /**
     * 生效模式
     */
    private String couponTimeType;
    /**
     * 消费满金额
     */
    private BigDecimal couponSalesMoney;
    /**
     * 消费满递减金额
     */
    private BigDecimal couponReduceMoney;
    /**
     * 时间点生效持续天数
     */
    private Integer couponDays;
    /**
     * 优惠卷金额
     */
    private double couponPrice;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 发放数量
     */
    private Integer couponNum;
    /**
     * 针对类型  PLAT.针对全平台，SHOP.针对店铺
     */
    private String couponClass;
    /**
     * 针对店铺的ID
     */
    private String userId;
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
     * 创建人
     */
    private String createBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 指定商品ID
     */
    private String commodityId;
    /**
     * 指定用户ID
     */
    private String uid;
    /**
     * 详细说明
     */
    private String content;
    /**
     * 券码
     */
    private String integralCodetemp;

    /**
     * couponusers表中领取优惠券的用户ID
     */
    @TableField(exist = false)
    private String receiveuid;



    /**
     * couponusers表中积分兑换券券码
     */
    @TableField(exist = false)
    private String integralcodeuser;



    /**
     * couponusers表中来源
     */
    @TableField(exist = false)
    private String source;

    /**
     * couponusers表中是否使用过此券
     */
    @TableField(exist = false)
    private Integer IsUse;


    /**
     * couponusers表中ID主键的值
     */
    @TableField(exist = false)
    private  String  CouponUsersMainId;

    /**
     * couponusers表中是否已赠送给别人
     */
    @TableField(exist = false)
    private  int isGive;

    @TableField(exist = false)
    private  String CouponTypeName;

    /**
     * 是否是新人
     */
    @TableField(exist = false)
    private int isNewPeople;
    /**
     * 是否已领取优惠券
     */
    @TableField(exist = false)
    private int isReceive;
}
