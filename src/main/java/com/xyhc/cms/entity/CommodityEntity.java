
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
 * 商品主表实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("commodity")
public class CommodityEntity implements Serializable {
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
     * 推荐购物车
     */
    private Integer isRecommendCart;
    /**
     * 推荐收藏
     */
    private Integer isRecommendCollect;
    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 排序
     */
    private Integer commoditySort;
    /**
     * 图片
     */
    private String commodityMainImgurl;
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
     * 是否推荐
     */
    private String isRecommend;

    /**
     * 是否新人专享
     */
    private String isNew;

    /**
     * 是否限时秒杀
     */
    private String timeLimit;

    /**
     * 是否推荐详情
     */
    private String isDetailRecommend;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 运费
     */
    private Integer freight;

    /**
     * 是否上架
     */
    private String isOnShelf;

    /**
     * 是否收藏
     */
    private String isCollect;

    /**
     * 收藏人uid
     */
    private String collectUid;
    /**
     * 是否新品
     */
    private int isNewCommodity;


    /**
     * 分类
     */
//    @ColumnWidth(21)
//    @ExcelProperty(value = "分类")
    private String classifyId;

    @TableField(exist = false)
    private String comClassifyName;

    @TableField(exist = false)
    private int SalesVolume;

    @TableField(exist = false)
    private BigDecimal discountMembers;

    @TableField(exist = false)
    private String priceName;

    @TableField(exist = false)
    private BigDecimal salesPrice;

    /**
     * 多个商品图片
     */
    @TableField(exist = false)
    private List<CommodityImgEntity> imgList;

    /**
     * 商品是否收藏
     */
    @TableField(exist = false)
    private int isCommodityCollect;
    /**
     * 商品分类
     */
    @TableField(exist = false)
    private String classifyName;


    /**
     * 销量
     */
    private Integer salesCount;
}
