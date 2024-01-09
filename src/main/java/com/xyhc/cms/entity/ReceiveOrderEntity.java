
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
 * 实体
 *
 * @author apollo
 * @since 2023-09-28 19:30:42
 */
@Data
@TableName("receive_order")
public class ReceiveOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 收货人姓名
     */
    private String receiveName;
    /**
     * 收货人联系电话
     */
    private String receiveTell;
    /**
     * 订单状态
     * TO_DELIVER 待发货
     * TO_RECEIVE 待收货
     * RECEIVED 已收货
     * APPLY_REFUND 申请退货
     * REFUNDED 已退货
     * COMMENT 已评价
     */
    private String orderStatus;
    /**
     * 订单备注
     */
    private String expressRemark;
    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 收货人地址
     */
    private String receiveAddress;
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
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 省
     */
    private String provinceId;
    /**
     * 市
     */
    private String cityId;
    /**
     * 区
     */
    private String areaId;
    /**
     * 商品名称
     */
    private String receiveCommodityName;
    /**
     * 商品名称
     */
    private String receiveCommodityImgurl;
    /**
     * 领取用户
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 礼包编号
     */
    private String bagNo;
}
