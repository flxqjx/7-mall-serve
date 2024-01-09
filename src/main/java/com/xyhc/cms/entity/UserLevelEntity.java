
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
 * 商学院分类实体
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
@Data
@TableName("user_level")
public class UserLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 名称
     */
        private String name;
    /**
     * 图片
     */
        private String imgurl;
    /**
     * 金额
     */
        private BigDecimal price;
    /**
     * 描述
     */
        private String remark;
    /**
     * 操作时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 操作人
     */
        private String createBy;
    /**
     * 操作时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 操作人
     */
        private String updateBy;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 直推多少人自动升级
     */
        private Integer directNum;

    /**
     * 一级佣金比例
     */
    @TableField(value = "commission_rate_1")
    private double commissionRate1;

    /**
     * 二级佣金比例
     */
    @TableField(value = "commission_rate_2")
    private double commissionRate2;
}
