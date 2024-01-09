
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

/**
 * 赛事申请表实体
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Data
@TableName("pointsmall_shopping_cart")
public class PointsmallShoppingCartEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 商品ID
     */
        private String commodityId;
    /**
     * 规格ID
     */
        private String spceId;
    /**
     * 单价
     */
        private BigDecimal price;
    /**
     * 数量
     */
        private Integer num;
    /**
     * 合计
     */
        private BigDecimal total;
    /**
     * 申请人UID
     */
        private String uid;
    /**
     * 创建人
     */
        private String createBy;
    /**
     * 创建时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 更新人
     */
        private String updateBy;
    /**
     * 更新时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 尺寸ID
     */
        private String sizeId;

    @TableField(exist = false)
    private String commodityName;

    @TableField(exist = false)
    private BigDecimal originalPrice;

    @TableField(exist = false)
    private BigDecimal specPrice;

    @TableField(exist = false)
    private int freight;
}
