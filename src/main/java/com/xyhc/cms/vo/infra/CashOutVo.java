package com.xyhc.cms.vo.infra;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信请求对象
 * https://developers.weixin.qq.com/miniprogram/
 * dev/api-backend/open-api/qr-code/wxacode.get.html
 */
@Data
public class CashOutVo implements Serializable {
    private double amount;
    private String openId;
    private String applicationCode;
}


