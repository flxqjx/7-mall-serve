package com.xyhc.cms.vo.user;

import lombok.Data;

@Data
public class SysRoleDto {


    private String id;
    private int isDelete;
    private String tips;
    private String pid;
    private String name;
    private int num;
    private int version;

    private String vendorId;
    private String vendorName;
}
