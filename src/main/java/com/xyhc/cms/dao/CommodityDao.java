package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品主表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface CommodityDao extends BaseMapper<CommodityEntity> {


    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询推荐
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityEntity> allRecommend(@Param("params") Map<String, Object> params);

    /**
     * 查询推荐
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityEntity> allTimeLimit(@Param("params") Map<String, Object> params);

    /**
     * 查询推荐详情
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityEntity> allIsDetailRecommend(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityEntity> page(IPage<CommodityEntity> page, @Param("params") Map<String, Object> params);

    CommodityEntity commodityDetail(String CommodityId);
}
