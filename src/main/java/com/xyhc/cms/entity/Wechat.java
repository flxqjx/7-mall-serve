package com.xyhc.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@TableName("wechat")
public class Wechat {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String openid;

    private String parentId;

    private int isMember;

    private String mobile;

    private String wechatOpenid;

    private String nickName;
    private String avatarUrl;
    private String unionid;
    private String ttOpenId;
    private int integralMoney;

    /**
     * 购物金
     */
    private double shoppingMoney;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private int editNumBirthday;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date modifyTime;

    private long ranNo;
    private String qrCode;
    private String shopQrCode;
    private String vipNo;
    private String appId;
    private int isAdmin;
    private double balance;
    //待提现余额
    private double balanceWithdrawn;

    /**
     * 身份证正面
     */
    private String cardNoFrontUrl;
    /**
     * 身份证反面
     */
    private String cardNoRearUrl;
    /**
     * 身份证号
     */
    private String cardNumber;
    /**
     * 身份证姓名
     */
    private String cardName;
//    //已退款金额
//    private double refundTotal;
//    //已到账金额
//    private double collectedBalances;
//    //已提现金额
//    private double cashOutMoney;
    /**
     * 备注
     */
    private String remark;
    //团队人数
    @TableField(exist = false)
    private int parentSize;

    //成交单量
    @TableField(exist = false)
    private int orderSize;

    //余额
    @TableField(exist = false)
    private double balanceOutstanding;

    //已到账
    @TableField(exist = false)
    private double paymentReceived;

    //成交金额
    @TableField(exist = false)
    private double totalOrderNum;

    //总业绩
    @TableField(exist = false)
    private double totalPerforman;

    //总业绩
    @TableField(exist = false)
    private double teamTotalPerforman;

    //总收益
    @TableField(exist = false)
    private double totalRevenue;

    @TableField(exist = false)
    private double collectedBalances;

    @TableField(exist = false)
    private double cashOutMoney;

    @TableField(exist = false)
    private double refundTotal;

    @TableField(exist = false)
    private double upgradeMoeny;

    @TableField(exist = false)
    private double isUpgrade;

    @TableField(exist = false)
    private String levelId;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String name;

//    @TableField(exist = false)
//    private String cardNoRearUrl;
//
//    @TableField(exist = false)
//    private String cardNoFrontUrl;

    @TableField(exist = false)
    private String memberLevel;


    /**
     * 我的团队人的信息
     */
    @TableField(exist = false)
    private List<Wechat> teamList;

    /**
     * 省
     */
    @TableField(exist = false)
    private String provinceId;
    /**
     * 市
     */
    @TableField(exist = false)
    private String cityId;
    /**
     * 区
     */
    @TableField(exist = false)
    private String areaId;

    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String joinName;
    @TableField(exist = false)
    private String joinPhone;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyVipTime;
    @TableField(exist = false)
    private String levelType;
    @TableField(exist = false)
    private String parentLevelType;
    @TableField(exist = false)
    private List<Wechat> children;
    @TableField(exist = false)

    /**
     * 业绩合计
     */
    private double sumXfBalance;
    /**
     * 信息是否完整
     */
    private int informactionComplete;

    /**
     * 余额合计
     */
    @TableField(exist = false)
    private double sumBalance;
    /**
     * 驳回金额
     */
    @TableField(exist = false)
    private double balanceOutstandingReject;
    /**
     * 上级名称
     */
    @TableField(exist = false)
    private String parentName;

}
