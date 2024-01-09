
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
import java.util.List;

/**
 * 商品评论实体
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Data
@TableName("commodity_comment")
public class CommodityCommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 用户ID
     */
    private String uid;

    /**
     * 评论内容
     */
    private String comments;
    /**
     * 评价类型
     */
    private String commentType;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 是否回复 0 待回复 1 已回复 -1 驳回
     */
    private Integer isReply;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 商品ID
     */
    private String commodityId;
    /**
     * 评价用户
     */
    private String commentsName;
    /**
     * 评价用户头像
     */
    private String commentsImg;

    //商品名称
    @TableField(exist = false)
    private String commodityName;


    //主图
    @TableField(exist = false)
    private String commodityMainImgurl;


    //规格图片
    @TableField(exist = false)
    private String specImgurl;

    /**
     * 订单ID
     */
    private String commodityOrderCommodityId;


    //用户名称
    @TableField(exist = false)
    private String nickName;

    //用户名称
    @TableField(exist = false)
    private String avatarUrl;

    //用户名称
    @TableField(exist = false)
    private String replyRemark;

    /**
     * 多个评价图片
     */
    @TableField(exist = false)
    private List<CommodityCommentImgEntity> imgList;

}
