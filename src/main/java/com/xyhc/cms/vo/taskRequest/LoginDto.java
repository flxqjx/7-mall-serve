package com.xyhc.cms.vo.taskRequest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class LoginDto {
    private String account;
    private String password;

}
