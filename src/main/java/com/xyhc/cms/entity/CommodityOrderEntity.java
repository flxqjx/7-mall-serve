
package com.xyhc.cms.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
import java.util.List;

/**
 * 商品订单表实体
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
@Data
@TableName("commodity_order")
public class CommodityOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "订单号")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人
     */
    private String uid;


    /**
     * 分享人id
     */
    private String userId;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 微信昵称
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "微信昵称")
    @TableField(exist = false)
    private String nickName;
    /**
     * 上级
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "所属人")
    @TableField(exist = false)
    private String parentName;
    /**
     * 销售价
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "订单销售价")
    private double price;
    /**
     * 商品合计
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "会员总价")
    private double commodityTotal;
    /**
     * 抵扣金额
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "购物金抵扣")
    private double discount;
    /**
     * 合计
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "合计")
    private double total;
    /**
     * 会员身份
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "会员身份")
    @TableField(exist = false)
    private String levelType;

    /**
     * 是否支付
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "是否支付")
    @TableField(exist = false)
    private String isPayStatus;
    /**
     * 是否支付
     */
    private Integer isPay;

    /**
     * 下单人上级ID
     */
    private String parentId;

    /**
     * 付款方式
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "付款方式")
    private String payType;
    /**
     * 订单状态
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "订单状态")
    private String orderStatus;
    /**
     * 交易流水号
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "交易流水号")
    private String transactionId;
    /**
     * 支付流水号
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "支付流水号")
    private String outTradeNo;
    /**
     * 收货人姓名
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "收货人姓名")
    private String receiveName;
    /**
     * 省
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "省")
    @TableField(exist = false)
    private String provinceId;
    /**
     * 市
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "市")
    @TableField(exist = false)
    private String cityId;

    /**
     * 区
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "区")
    @TableField(exist = false)
    private String areaId;
    /**
     * 收货人地址
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "收货人地址")
    private String receiveAddress;
    /**
     * 收货人联系电话
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "收货人电话")
    private String receiveTell;
    /**
     * 备注
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "订单备注")
    private String remark;
    /**
     * 支付时间
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;





    /**
     * 收货地址
     */

    private String receiveId;


//    /**
//     * 优惠之后的商品金额
//     */
//    private BigDecimal saleTotal;

    /**
     * 退货原因
     */
    private String refundReason;
    /**
     * 退货地址
     */
    private String refundAddress;
    /**
     * 退货电话
     */
    private String refundMobile;
//    /**
//     * 订单类型
//     */
//    private String orderType;
    /**
     * 退货姓名
     */
    private String refundName;
    /**
     * 退货快递单号
     */
    private String refundExpressNo;

    /**
     * 驳回退货备注
     */
    private String approveRemark;

//    /**
//     * 平台是否收货
//     */
//        private Integer isPlatReceive;


    /**
     * 运费
     */
    private double freight;

    /**
     * 是否是从新人专享下的单
     */
    private String isNewOrder;



    /**
     * 订单退货状态
     */
    private String approveStatus;

    /**
     * 订单备注
     */
    private String expressRemark;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 是否返积分
     */
    private Integer isReturnPoints;

    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 促销活动ID
     */
    private String promotionActivitysId;
    /**
     * 会员ID
     */
    private String levelId;
    /**
     * 优惠券ID
     */
    private String couponReceiveId;
    /**
     * 多个商品
     */
    @TableField(exist = false)
    private List<CommodityOrderCommodityEntity> orderList;



    @TableField(exist = false)
    private String avatarUrl;



    @TableField(exist = false)
    private int isDeleteCommodity;







    /**
     * 上上级
     */
    @TableField(exist = false)
    private String parentMaxName;



}
