
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
 * 评论回复实体
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Data
@TableName("pointsmall_comment_reply")
public class PointsmallCommentReplyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 回复的ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 留言的ID
     */
        private String commentId;
    /**
     * 回复内容
     */
        private String replyRemark;
    /**
     * 回复时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 是否删除
     */
        private Integer isDelete;
    /**
     * 修改时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
}
