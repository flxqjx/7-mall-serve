package com.xyhc.cms.vo.wechatMsg;


import com.xyhc.cms.entity.PointsmallOrderCommodityEntity;
import com.xyhc.cms.entity.PointsmallOrderEntity;
import lombok.Data;

import java.util.List;


@Data
public class PointsmallOrderSaveDto {
    //id
    private PointsmallOrderEntity pointsmallOrder;
    private List<PointsmallOrderCommodityEntity> pointsmallOrderCommodity;
}
