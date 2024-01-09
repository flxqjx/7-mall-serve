package com.xyhc.cms.vo.wechatMsg;


import com.xyhc.cms.entity.CommodityCommentEntity;
import com.xyhc.cms.entity.CommodityCommentImgEntity;
import lombok.Data;

import java.util.List;


@Data
public class CommodityCommentsDto {
    //id
    private CommodityCommentEntity commentsForm;
    private List<CommodityCommentImgEntity> imgList;
}
