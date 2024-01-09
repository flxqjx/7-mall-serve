package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommoditySpecEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品规格Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface CommoditySpecDao extends BaseMapper<CommoditySpecEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommoditySpecEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommoditySpecEntity> allSpec(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommoditySpecEntity> page(IPage<CommoditySpecEntity> page, @Param("params") Map<String, Object> params);
}
