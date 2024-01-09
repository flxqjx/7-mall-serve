package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MemberLevelOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-03-22 18:48:06
 */
@Mapper
public interface MemberLevelOrderDao extends BaseMapper<MemberLevelOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberLevelOrderEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberLevelOrderEntity> allList();


    /**
     * 查询我的全部数据
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberLevelOrderEntity> myContractAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MemberLevelOrderEntity> page(IPage<MemberLevelOrderEntity> page, @Param("params") Map<String, Object> params);
}
