package com.xyhc.cms.vo.system;

import lombok.Data;

@Data
public class OpenIdDto {

    private String openid;
    private String session_key;
    private String unionid;
    private String userType;
    private String userId;
    private String token;
    private int informactionComplete;
}
