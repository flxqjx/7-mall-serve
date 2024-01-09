package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.ReceiveInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收货地址表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface ReceiveInfoDao extends BaseMapper<ReceiveInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ReceiveInfoEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param
     * @return List
     */
    List<ReceiveInfoEntity> allCity();

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ReceiveInfoEntity> page(IPage<ReceiveInfoEntity> page, @Param("params") Map<String, Object> params);
}
