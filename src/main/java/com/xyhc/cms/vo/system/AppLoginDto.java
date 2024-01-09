package com.xyhc.cms.vo.system;

import lombok.Data;

@Data
public class AppLoginDto {

    private String avatarUrl;
    private String nickName;
    private String openid;
    private String unionid;
    private int informactionComplete;
}
