package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.entity.UserDaySignEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 签到记录表Dao
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
@Mapper
public interface UserDaySignDao extends BaseMapper<UserDaySignEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<UserDaySignEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<UserDaySignEntity> page(IPage<UserDaySignEntity> page, @Param("params") Map<String, Object> params);
}
