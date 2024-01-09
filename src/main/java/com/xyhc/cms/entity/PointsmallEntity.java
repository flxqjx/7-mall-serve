
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
import java.util.List;

/**
 * 商品主表实体
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
@Data
@TableName("pointsmall")
public class PointsmallEntity implements Serializable {
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
     * 商品名称
     */
        private String commodityName;
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
     * 分类一级
     */
        private String classifyId;
    /**
     * 是否推荐
     */
        private Integer isRecommend;
    /**
     * 0.未上架，1.已上架
     */
        private Integer isOnShelf;
    /**
     * 是否详情推荐
     */
        private Integer isDetailRecommend;
    /**
     * 是否收藏
     */
        private Integer isCollect;
    /**
     * 是否新人专享 0否 1是
     */
        private Integer isNew;
    /**
     * 用户id
     */
        private String collectUid;
    /**
     * 库存
     */
        private Integer inventory;
    /**
     * 运费
     */
        private double freight;
    /**
     * POINTS 积分商城 MALL 普通商城
     */
        private String commodityType;
    /**
     * 是否限时秒杀 0否 1是
     */
        private Integer timeLimit;

    /**
     * 多个商品图片
     */
    @TableField(exist = false)
    private List<PointsmallImgEntity> imgList;

    /**
     * 商品是否收藏
     */
    @TableField(exist = false)
    private int isCommodityCollect;
}
