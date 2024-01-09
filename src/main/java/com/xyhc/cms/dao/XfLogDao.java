package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.XfLogEntity;
import com.xyhc.cms.vo.commodity.OrderTjVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Mapper
public interface XfLogDao extends BaseMapper<XfLogEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> receivedLog(@Param("params") Map<String, Object> params);

    /**
     * 购物金明细
     *
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> shoppingMoneyall(@Param("params") Map<String, Object> params);

    /*
     * 订单收入订单列表
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> orderRevenue(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> page(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> balanceDetail(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     * 查询充值记录
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> payPage(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    Double xfLogRefundTotal(@Param("refund") Map<String, Object> refund);

    /**
     * 取未到账
     */
    List<XfLogEntity> notCompleteBalance();

    /**
     * 购物金使用明细
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<OrderTjVo> deducPage(IPage<OrderTjVo> page, @Param("params") Map<String, Object> params);

    /**
     * 团队业绩明细
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<OrderTjVo> teamYjPage(IPage<OrderTjVo> page, @Param("params") Map<String, Object> params);


    /**
     * 用户分佣明细
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<OrderTjVo> userDetailPage(IPage<OrderTjVo> page, @Param("params") Map<String, Object> params);


    /**
     * 自定义分页查询 根据订单ID查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> balanceDetailByOrderId(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 待处理到账
     */
    List<XfLogEntity> toReward();

    /**
     * 全部已到账
     */
    List<XfLogEntity> saveToReceivingMallTransfer();

    /**
     * 自定义分页查询 已到账明细
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<XfLogEntity> balanceWithdrawnLogByUserId(IPage<XfLogEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 全部已到账
     */
    List<XfLogEntity> getFirst(@Param("uid") String uid);
}
