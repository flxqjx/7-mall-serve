
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
 * 优惠券领取实体
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Data
@TableName("coupon_users")
public class CouponUsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户领取优惠卷ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 用户ID
     */
        private String receiveUid;
    /**
     * 优惠卷ID
     */
        private String couponId;

    /**
     * 优惠卷名称
     */
    private String title;

    /**
     * 优惠券类型
     */
    private String couponType;


    /**
     * 赚送方式
     */

    private String giveType;

    /**
     * 积分兑换券券码
     */
    private String integralCodeUser;
    /**
     * 积分兑换券券码
     */
    private Integer isGive;
    /**
     * 积分兑换使用的积分
     */
    private String giveUserId;
    /**
     * 赠送的用户ID
     */
    private int integral;


    /**
     * 赠送的用户名称
     */

    private String giveUser;
    /**
     * 积分兑换券券码
     */

    private String integralCouponCode;

    /**
     * 赠送给他人时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date giveDateTime;


    /**
     * 创建时间(领取时间)
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;
    /**
     * 是否使用(0.未使用，1.已使用)
     */
        private Integer isUse;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 更改时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 积分商城id
     */
        private String pointshopId;
    /**
     * 小程序选择，RECEIVE 领取   发放是后台指定 ASSIGN，积分领取
     */
        private String source;
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
     * wechat表 nickname
     */
    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String isGiveState;

}
