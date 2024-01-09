
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

/**
 * 商品规格实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("commodity_spec")
public class CommoditySpecEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     * 扣除积分
     */
    private Integer points;
    /**
     * 商品ID
     */
    private String commodityId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 图片
     */
    private String specImgurl;
    /**
     * 内容
     */
    private String remark;
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    /**
     * 销售价
     */
    private BigDecimal price;
    /**
     * 成本
     */
    private BigDecimal cost;
    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String commodityName;

    /**
     * 规格销售价
     */
    @TableField(exist = false)
    private BigDecimal salesPrice;
}