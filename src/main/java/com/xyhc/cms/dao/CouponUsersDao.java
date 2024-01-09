package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CouponUsersEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 优惠券领取Dao
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Mapper
public interface CouponUsersDao extends BaseMapper<CouponUsersEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CouponUsersEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CouponUsersEntity> page(IPage<CouponUsersEntity> page, @Param("params") Map<String, Object> params);
}
