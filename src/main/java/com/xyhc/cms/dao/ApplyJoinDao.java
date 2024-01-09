package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.ApplyJoinEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-05-24 13:40:41
 */
@Mapper
public interface ApplyJoinDao extends BaseMapper<ApplyJoinEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ApplyJoinEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param  查询参数
     * @return List
     */
    List<ApplyJoinEntity> allJoin();

    /**
     * 查询所有列表
     *
     */
    ApplyJoinEntity queryUid(String shopId);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ApplyJoinEntity> page(IPage<ApplyJoinEntity> page, @Param("params") Map<String, Object> params);
}
