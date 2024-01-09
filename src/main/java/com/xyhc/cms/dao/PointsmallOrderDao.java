package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.PointsmallOrderEntity;
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
public interface PointsmallOrderDao extends BaseMapper<PointsmallOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> allExport(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> page(IPage<PointsmallOrderEntity> page, @Param("params") Map<String, Object> params);

    /*
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> pageCommon(IPage<PointsmallOrderEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> pageEntire(IPage<PointsmallOrderEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderEntity> pageRefund(IPage<PointsmallOrderEntity> page, @Param("params") Map<String, Object> params);
    /**
     * 查询————我的订单待发货数量
     */
    List<PointsmallOrderEntity> orderDeliverNum(String uid);

    /**
     * 查询————我的订单待收货数量
     */
    List<PointsmallOrderEntity> orderReceiveNum(String uid);

}
