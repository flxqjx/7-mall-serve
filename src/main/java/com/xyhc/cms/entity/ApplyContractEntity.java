
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
 * 实体
 *
 * @author apollo
 * @since 2023-05-24 15:55:30
 */
@Data
@TableName("apply_contract")
public class ApplyContractEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 合同内容
     */
        private String remark;
    /**
     * 合同标题
     */
        private String title;
    /**
     * 会员套餐id
     */
    private String levelId;
    /**
     * 协议
     */
    private String agreement;
    /**
     * 创建时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    /**
     * 是否被删除
     */
        private Integer isDelete;
    /**
     * 修改时间
     */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    /**
     * UID
     */
        private String uid;
    /**
     * 创建人
     */
        private String createBy;
    /**
     * 修改人
     */
        private String updateBy;

    /**
     * 会员名称
     */
    @TableField(exist = false)
    private String levelName;

    @TableField(exist = false)
    private String levelType;

}
