package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CouponEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 平台优惠券Dao
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CouponEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CouponEntity> page(IPage<CouponEntity> page, @Param("params") Map<String, Object> params);


    /**
     * 小程序自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CouponEntity> appPage(IPage<CouponEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 小程序分页查询积分兑换券
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CouponEntity> appPageByPoints(IPage<CouponEntity> page, @Param("params") Map<String, Object> params);
}
