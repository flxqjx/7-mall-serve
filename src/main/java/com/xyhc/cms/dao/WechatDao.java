package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.Wechat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WechatDao extends BaseMapper<Wechat> {

    List<Wechat> all(@Param("params") Map<String, Object> params);

    List<Wechat> page(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    List<Wechat> teamPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    List<Wechat> wechatPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    Double totalB(@Param("mapB") Map<String, Object> mapB);

    Double totalPu(@Param("mapPu") Map<String, Object> mapPu);

    Double totalMyB(@Param("mapMyB") Map<String, Object> mapMyB);

    Double totalMyBPu(@Param("mapMyBPu") Map<String, Object> mapMyBPu);

    Double totalOrderNum(@Param("orderNum") Map<String, Object> orderNum);

    Double xfLogTotal(@Param("xfLog") Map<String, Object> xfLog);

    Double xfLogCollectedBalances(@Param("params") Map<String, Object> params);

    Double xfLogCollectedTotal(@Param("paramsCollected") Map<String, Object> paramsCollected);

    Double xfLogCanWithdraw(@Param("paramsCanWithdraw") Map<String, Object> paramsCanWithdraw);

    Double xfLogRefund(@Param("paramsRefund") Map<String, Object> paramsRefund);

    Double orderTotal(@Param("orderMy") Map<String, Object> orderMy);

    List<Wechat> wechatTotalPerforman(@Param("uid") String uid);

    Double xfLogCollectedBalancesReject(@Param("params") Map<String, Object> params);

    /**
     * 取全部的C用户
     */
    List<Wechat> levelCList();

    /**
     * 取普通会员
     */
    List<Wechat> normalUser(@Param("uid") String uid);


    /**
     * 取普通会员订单
     */
    List<CommodityOrderEntity> normalUserOrder(@Param("uid") String uid);
    /**
     * 当天是否过生日
     */
    List<Wechat> birthday();

}
