package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.ApplyContractEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-05-24 15:55:30
 */
@Mapper
public interface ApplyContractDao extends BaseMapper<ApplyContractEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ApplyContractEntity> all(@Param("params") Map<String, Object> params);

    ApplyContractEntity queryLevelId(String levelId);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ApplyContractEntity> page(IPage<ApplyContractEntity> page, @Param("params") Map<String, Object> params);
}
