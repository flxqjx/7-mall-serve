package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityOrderCommodityEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.vo.common.CommoditySummaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品订单明细表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface CommodityOrderCommodityDao extends BaseMapper<CommodityOrderCommodityEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderCommodityEntity> all(@Param("params") Map<String, Object> params);

    List<CommodityOrderCommodityEntity> allTwo();

    List<CommodityOrderCommodityEntity> queryCommodityId(String commodityId);
    List<CommodityOrderCommodityEntity> queryOrderId(String orderId);
    List<CommoditySummaryVo> commodityorderSummary();


    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderCommodityEntity> page(IPage<CommodityOrderCommodityEntity> page, @Param("params") Map<String, Object> params);

    Double salesPrice (String orderId);
}
