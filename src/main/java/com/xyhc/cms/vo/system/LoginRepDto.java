package com.xyhc.cms.vo.system;

import lombok.Data;

@Data
public class LoginRepDto {

    private String userId;
    private String token;
    private String unionid;
    private String openid;
}
