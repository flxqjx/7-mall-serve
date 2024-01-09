package com.xyhc.cms.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Wechat {
    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    private String openId;



    private String nickName;
    private String avatarUrl;
    private String unionid;

}
