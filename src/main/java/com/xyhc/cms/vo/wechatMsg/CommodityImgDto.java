package com.xyhc.cms.vo.wechatMsg;


import com.xyhc.cms.entity.CommodityEntity;
import com.xyhc.cms.entity.CommodityImgEntity;
import lombok.Data;

import java.util.List;


@Data
public class CommodityImgDto {
    //id
    private CommodityEntity commodityForm;
    private List<CommodityImgEntity> commodityImgurlList;
}
