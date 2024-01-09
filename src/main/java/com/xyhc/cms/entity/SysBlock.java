package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("sys_block")
public class SysBlock {
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     *最后更新人
     */
    private String updateBy;

    /**
     *创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
    /**
     *最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;


    private String block;
    private String title;
    private String blockType;
    private int isDelete;
    private String remark;
    private String urlTo;
    private String isType;
    private String typeId;

}
