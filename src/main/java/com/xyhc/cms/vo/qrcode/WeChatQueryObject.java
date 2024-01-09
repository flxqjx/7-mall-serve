package com.xyhc.cms.vo.qrcode;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信请求对象
 * https://developers.weixin.qq.com/miniprogram/
 * dev/api-backend/open-api/qr-code/wxacode.get.html
 */
@Data
public class WeChatQueryObject implements Serializable {
    private String page;
    private String scene;
    private Integer width;
    private String fileName;
    private boolean auto_color;
    //    private Object line_color;
    private boolean is_hyaline;
    private String applicationCode;
}

