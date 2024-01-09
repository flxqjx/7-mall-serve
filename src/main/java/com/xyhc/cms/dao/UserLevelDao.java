package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.UserLevelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商学院分类Dao
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
@Mapper
public interface UserLevelDao extends BaseMapper<UserLevelEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<UserLevelEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<UserLevelEntity> page(IPage<UserLevelEntity> page, @Param("params") Map<String, Object> params);
}
