
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 会员级别实体
 *
 * @author apollo
 * @since 2023-03-22 15:18:26
 */
@Data
@TableName("member_level")
public class MemberLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.INPUT)
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
     * 套餐类型
     */
    private String levelType;
    /**
     * 积分
     */
    private int integral;
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
     * 注册福利图
     */
    @TableField(exist = false)
    List<MemberLevelImgEntity> imgList;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

}
