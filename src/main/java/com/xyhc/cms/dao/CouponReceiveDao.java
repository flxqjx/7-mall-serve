package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.entity.CouponReceiveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品图片Dao
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
@Mapper
public interface CouponReceiveDao extends BaseMapper<CouponReceiveEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CouponReceiveEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CouponReceiveEntity> page(IPage<CouponReceiveEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 查询我的优惠券
     *
     * @param params 查询参数 isUse
     * @return List
     */
    List<CouponReceiveEntity> myCoupon(@Param("params") Map<String, Object> params);
}
