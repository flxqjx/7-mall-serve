
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
 * 商品订单明细表实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("pointsmall_order_commodity")
public class PointsmallOrderCommodityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
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
     * 规格ID
     */
        private String specId;
    /**
     * 商品ID
     */
        private String commodityId;
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 商品名称
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "商品名称")
    private String commodityName;
    /**
     * 商品合计
     */
        private BigDecimal specTotal;
    /**
     * 单价
     */
    private BigDecimal specPrice;
    /**
     * 尺寸名称
     */
    private String sizeName;
    /**
     * 数量
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "数量")
    private BigDecimal num;

    /**
     * 规格昵称
     */
    @ColumnWidth(21)  //列宽度
    @ExcelProperty(value = "规格昵称")
    private String specName;

    /**
     * 评价状态
     */
    private String commentStatus;

    //规格图片
    @TableField(exist = false)
    private String specImgurl;

    //商品图片
    @TableField(exist = false)
    private String commodityImgurl;

    @TableField(exist = false)
    private int SalesVolume;

    /**
     * 多个商品图片
     */
    @TableField(exist = false)
    private List<CommodityImgEntity> imgList;


}
