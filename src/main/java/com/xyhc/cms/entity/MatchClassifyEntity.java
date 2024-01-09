
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
 * 赛事分类表实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("match_classify")
public class MatchClassifyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 分类名称
     */
        private String classifyName;
    /**
     * 分类图片
     */
        private String imgurl;
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
     * 是否推荐
     */
        private Integer isRecommend;
    /**
     * 标签
     */
        private String label;
}
