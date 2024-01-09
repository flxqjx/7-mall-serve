
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
 * 店铺收藏实体
 *
 * @author apollo
 * @since 2023-09-20 10:26:49
 */
@Data
@TableName("commodity_collect")
public class CommodityCollectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 店铺ID
     */
        private String commodityId;
    /**
     * 添加时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 删除时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 创建人
     */
        private String createBy;
    /**
     * 更改人
     */
        private String updateBy;
    /**
     * UID
     */
        private String uid;
    /**
     * 是否删除
     */
        private Integer isCollect;
    /**
     * 图片
     */
    @TableField(exist = false)
    private String commodityMainImgurl;
    /**
     * 名称
     */
    @TableField(exist = false)
    private String commodityName;
    /**
     * 价格
     */
    @TableField(exist = false)
    private BigDecimal price;
    /**
     * 优惠价格
     */
    @TableField(exist = false)
    private String originalPrice;

}
