package com.xyhc.cms.vo.wechatMsg;


import com.xyhc.cms.entity.CommodityOrderCommodityEntity;
import com.xyhc.cms.entity.CommodityOrderEntity;
import lombok.Data;

import java.util.List;


@Data
public class CommodityOrderSaveDto {
    //id
    private CommodityOrderEntity commodityOrder;
    private List<CommodityOrderCommodityEntity> commodityOrderCommodity;
}
