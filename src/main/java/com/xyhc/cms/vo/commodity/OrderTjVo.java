package com.xyhc.cms.vo.commodity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderTjVo implements Serializable {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单商品销售价
     */
    private double salesPrice;
    /**
     * 会员价
     */
    private double vipPrice;
    /**
     * 抵扣购物金
     */
    private double deducBuyPrice;
    /**
     * 实际支付金额
     */
    private double payMoney;

    /**
     * 所得积分
     */
    private double points;

    /**
     * 一级分销金额
     */
    private double returnLevel1Money;

    /**
     * 二级分销金额
     */
    private double returnLevel2Money;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date payTime;

    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 微信头像
     */
    private String avatarUrl;
    /**
     * 会员名称
     */
    private String levelName;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 备注
     */
    private String xfRemark;
    /**
     * 类别
     */
    private String xfClassify;

}


