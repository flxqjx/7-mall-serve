package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MsCashOutEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Mapper
public interface MsCashOutDao extends BaseMapper<MsCashOutEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MsCashOutEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MsCashOutEntity> page(IPage<MsCashOutEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     * 后台提现明细
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MsCashOutEntity> pageBack(IPage<MsCashOutEntity> page, @Param("params") Map<String, Object> params);
    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MsCashOutEntity> pageByUid(IPage<MsCashOutEntity> page, @Param("params") Map<String, Object> params);
}
