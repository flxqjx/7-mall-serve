package com.xyhc.cms.vo.wechatMsg;


import lombok.Data;


@Data
public class TemplateDto {
    private String toUid;
    private String template_id;
    private String url;
    private Object data;
}
