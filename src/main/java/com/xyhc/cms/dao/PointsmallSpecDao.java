package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.PointsmallSpecEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品规格Dao
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Mapper
public interface PointsmallSpecDao extends BaseMapper<PointsmallSpecEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PointsmallSpecEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<PointsmallSpecEntity> page(IPage<PointsmallSpecEntity> page, @Param("params") Map<String, Object> params);
}
