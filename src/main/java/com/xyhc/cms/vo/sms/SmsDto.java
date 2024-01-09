package com.xyhc.cms.vo.sms;


import lombok.Data;


@Data
public class SmsDto {
    private String mobile;
    private String templateCode;
    private String signName;
    private String msgBody;
}
