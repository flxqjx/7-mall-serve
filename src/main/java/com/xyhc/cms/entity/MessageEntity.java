
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯主表实体
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
@Data
@TableName("message")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 标题
     */
        private String title;
    /**
     * 内容
     */
        private String remark;
    /**
     * 图片
     */
        private String imgurl;

    /**
     * 资讯分类
     */
    private String classifyId;

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
     * 更新时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 更新人
     */
        private String updateBy;
    /**
     * 删除标志
     */
        private Integer isDelete;

    @TableField(exist = false)
    private String classifyName;

}
