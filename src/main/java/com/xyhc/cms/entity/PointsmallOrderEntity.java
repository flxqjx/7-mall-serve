
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
@TableName("pointsmall_order")
public class PointsmallOrderEntity implements Serializable {
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
//    /**
//     * 商品ID
//     */
//        private String commodityId;
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
    /**
     * 收货人姓名
     */
    private String receiveName;
    /**
     * 收货人地址
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "收货地址")
    private String receiveAddress;
    /**
     * 收货人联系电话
     */
    private String receiveTell;
    /**
     * 收货地址
     */
    private String receiveId;
    /**
     * 合计
     */
    private BigDecimal total;


    /**
     * 商品合计
     */
    private BigDecimal commodityTotal;
    /**
     * 商品id
     */
    private String pointsmallId;


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
    /**
     * 订单类型
     */
    private String orderType;
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
     * 备注
     */
    private String remark;

    /**
     * 运费
     */
    private String freight;

    /**
     * 是否是从新人专享下的单
     */
    private String isNewOrder;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单退货状态
     */
    private String approveStatus;

    /**
     * 订单备注
     */
    private String expressRemark;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 多个商品
     */
    @TableField(exist = false)
    private List<PointsmallOrderCommodityEntity> orderList;

    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private int isDeleteCommodity;

    /**
     * 省
     */
    @TableField(exist = false)
    private String provinceId;
    /**
     * 市
     */
    @TableField(exist = false)
    private String cityId;
    /**
     * 区
     */
    @TableField(exist = false)
    private String areaId;
    @TableField(exist = false)
    private String pointsmallImg;
    @TableField(exist = false)
    private String pointsmallName;
    @TableField(exist = false)
    private BigDecimal pointsmallPrice;

}
