
package com.xyhc.cms.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论实体
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Data
@TableName("pointsmall_comment")
public class PointsmallCommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "id",type = IdType.INPUT)
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
     * 是否回复 0 待回复 1 已回复
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
     * 订单id
     */
        private String commodityOrderCommodityId;
}
