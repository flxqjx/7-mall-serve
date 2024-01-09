package com.xyhc.cms.vo.common;

import lombok.Data;

@Data
public class WxJsapiSignatureVo {
    private String appId;
    private String nonceStr;
    private long timestamp;
    private String url;
    private String signature;
    private String jsapiTicket;
}

