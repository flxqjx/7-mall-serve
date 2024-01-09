package com.xyhc.cms.vo.wechatMsg;


import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.entity.PointsmallImgEntity;
import lombok.Data;

import java.util.List;


@Data
public class PointsmallImgDto {
    //id
    private PointsmallEntity PointsmallForm;
    private List<PointsmallImgEntity> PointsmallImgurlList;
}
