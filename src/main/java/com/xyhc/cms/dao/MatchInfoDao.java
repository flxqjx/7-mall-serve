package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MatchInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 赛事主表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:42:15
 */
@Mapper
public interface MatchInfoDao extends BaseMapper<MatchInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MatchInfoEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MatchInfoEntity> page(IPage<MatchInfoEntity> page, @Param("params") Map<String, Object> params);
}
