package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.PointsmallOrderCommodityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品订单明细表Dao
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Mapper
public interface PointsmallOrderCommodityDao extends BaseMapper<PointsmallOrderCommodityEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderCommodityEntity> all(@Param("params") Map<String, Object> params);

     List<PointsmallOrderCommodityEntity> allTwo();

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallOrderCommodityEntity> page(IPage<PointsmallOrderCommodityEntity> page, @Param("params") Map<String, Object> params);
}
