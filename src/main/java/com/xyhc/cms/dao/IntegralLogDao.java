package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.IntegralLogEntity;
import com.xyhc.cms.entity.XfLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-08-17 15:26:30
 */
@Mapper
public interface IntegralLogDao extends BaseMapper<IntegralLogEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<IntegralLogEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<IntegralLogEntity> page(IPage<IntegralLogEntity> page, @Param("params") Map<String, Object> params);
    /**
     * 取未到账
     */
    List<IntegralLogEntity> notCompleteIntegral();

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<IntegralLogEntity> pcAllByUid(IPage<IntegralLogEntity> page, @Param("params") Map<String, Object> params);
}
