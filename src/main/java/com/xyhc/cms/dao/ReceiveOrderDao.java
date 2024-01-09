package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.ReceiveOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-09-28 19:30:42
 */
@Mapper
public interface ReceiveOrderDao extends BaseMapper<ReceiveOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ReceiveOrderEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询我的领取记录
     *
     * @param params 查询参数
     * @return List
     */
    List<ReceiveOrderEntity> myOrder(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ReceiveOrderEntity> page(IPage<ReceiveOrderEntity> page, @Param("params") Map<String, Object> params);
}
