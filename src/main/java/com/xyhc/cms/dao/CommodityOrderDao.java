package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.XfLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品订单表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
@Mapper
public interface CommodityOrderDao extends BaseMapper<CommodityOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> allByUid(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> allExport(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> page(IPage<CommodityOrderEntity> page, @Param("params") Map<String, Object> params);

    /*
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> pageCommon(IPage<CommodityOrderEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> pageEntire(IPage<CommodityOrderEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityOrderEntity> pageRefund(IPage<CommodityOrderEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 确认收货已经过了一天
     */
    List<CommodityOrderEntity> oneDayBeforeOrder();

    /**
     * 7日后到账(待确认收货自动变已确认收货)-自动处理
     */
    List<CommodityOrderEntity> sevenDayBeforeOrder();

    /**
     * 7日后下单反积分到账(待确认收货自动变已确认收货)-自动处理
     */
    List<CommodityOrderEntity> sevenDayPointsOrder();

    /**
     * 查询————我的订单待付款数量
     */
    List<CommodityOrderEntity> orderNoIsPay(String uid);

    /**
     * 查询————我的订单待发货数量
     */
    List<CommodityOrderEntity> orderToDeliverNum(String uid);

    /**
     * 查询————我的订单待收货数量
     */
    List<CommodityOrderEntity> orderToReceiveNum(String uid);


}
