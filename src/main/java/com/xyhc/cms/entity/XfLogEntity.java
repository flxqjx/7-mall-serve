
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Data
@TableName("xf_log")
public class XfLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
//    /**
//     * uid
//     */
//        private String uid;

    /**
     * 变更人
     */
    private String userId;

    /**
     * 申请金额
     */
    private double xfBalance;

    /**
     * 变更前金额
     */
    private double beforeBalane;
    /**
     * 变更后金额
     */
    private double afterBalance;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 消费类型
     */
    private String xfClassify;

    /**
     * 是否到账
     */
    private int xfAccount;
    /**
     * 已退款金额
     */
    private double refundTotal;

    /**
     * 消费备注
     */
    private String xfRemark;

    /**
     * 处理状态
     */
    private String status;

    /**
     * 删除标志
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间/注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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


    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private String bankName;

    //能量石
    @TableField(exist = false)
    private double energy;


    @TableField(exist = false)
    private double type;

    @TableField(exist = false)
    private String xfType;

    private double collectedBalances;

    @TableField(exist = false)
    private double balanceWithdrawn;


    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatarUrl;

    /**
     * 多个商品
     */
    @TableField(exist = false)
    private List<CommodityOrderCommodityEntity> orderList;
    /**
     * 订单
     */
    @TableField(exist = false)
    private CommodityOrderEntity orderForm;

}
