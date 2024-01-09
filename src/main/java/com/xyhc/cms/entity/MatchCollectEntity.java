
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

/**
 * 赛事收藏表实体
 *
 * @author apollo
 * @since 2023-03-10 12:04:37
 */
@Data
@TableName("match_collect")
public class MatchCollectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 比赛ID
     */
        private String matchId;
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

    @TableField(exist = false)
    private String matchName;

    @TableField(exist = false)
    private String matchAddress;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String imgurl;
}
