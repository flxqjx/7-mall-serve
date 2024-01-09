
package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信认证TOKEN实体
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
@Data
@TableName("sys_access_token")
public class SysAccessTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    /**
     * 
     */
        private String openid;
    /**
     * 
     */
        private String token;
    /**
     * 
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 
     */
        private Long expireTime;
}
