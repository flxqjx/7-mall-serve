package com.xyhc.cms.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysUserDto {

    private String account;
    private int isDelete;
    private String name;
    private String phone;
    private String id;//userid
    private String vendorId;
    private String vendorName;
    private String roleId;//角色id、
}
