
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
 * 实体
 *
 * @author apollo
 * @since 2023-02-28 18:10:04
 */
@Data
@TableName("ms_bank")
public class MsBankEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * uid
     */
        private String uid;
    /**
     * 银行编号
     */
        private String bankCode;
    /**
     * 银行账号
     */
        private String bankNo;
    /**
     * 开户行信息
     */
        private String deposit;
    /**
     * 持卡姓名
     */
        private String realName;
    /**
     * 手机号码
     */
        private String mobile;

    /**
     * 卡名称
     */
    private String bankName;

    /**
     * 班级介绍
     */
        private String remark;
    /**
     * 删除标志
     */
        private Integer isDelete;
    /**
     * 状态
     */
        private Integer status;
    /**
     * 展示排序
     */
        private Integer sort;
    /**
     * 创建人
     */
        private String createBy;
    /**
     * 创建时间/注册时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 最后更新人
     */
        private String updateBy;
    /**
     * 最后更新时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * 是否默认
     */
        private Integer isDefault;
}
