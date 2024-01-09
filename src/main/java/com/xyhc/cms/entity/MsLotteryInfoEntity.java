
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
 * 赛事分类表实体
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Data
@TableName("ms_lottery_info")
public class MsLotteryInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    /**
     * 类型
     */
        private String type;
    /**
     * 名称
     */
    private String title;

    /**
     * 图片
     */
    private String imgurl;
    /**
     * 数量
     */
    private int num;
    /**
     * 抽奖ID
     */
    private String lotteryId;
    /**
     * 运费
     */
    private double freight;
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
     * 中奖率
     */
    private double lotteryRate;

    /**
     * 是否在活动内
     */
    @TableField(exist = false)
    private int isActivity;
}
