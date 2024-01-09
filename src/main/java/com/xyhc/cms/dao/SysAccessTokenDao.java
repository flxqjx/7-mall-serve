package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.SysAccessTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 微信认证TOKENDao
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
@Mapper
public interface SysAccessTokenDao extends BaseMapper<SysAccessTokenEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<SysAccessTokenEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<SysAccessTokenEntity> page(IPage<SysAccessTokenEntity> page, @Param("params") Map<String, Object> params);
}
