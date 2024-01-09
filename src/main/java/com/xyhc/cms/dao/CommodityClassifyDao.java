package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityClassifyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface CommodityClassifyDao extends BaseMapper<CommodityClassifyEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityClassifyEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 根据二级id查询三级
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityClassifyEntity> lastLevelAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityClassifyEntity> page(IPage<CommodityClassifyEntity> page, @Param("params") Map<String, Object> params);
}
