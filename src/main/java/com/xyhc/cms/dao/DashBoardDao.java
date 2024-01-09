package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.vo.infra.DashBoardVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品订单表Dao
 */
@Mapper
public interface DashBoardDao extends BaseMapper<DashBoardVo> {

    /**
     * 首页走势图统计
     *
     * @return List
     */
    List<DashBoardVo> homeOrderSummaryTrend();

    /**
     * 首页饼状图统计
     *
     * @return List
     */
    List<DashBoardVo> homeOrderSummaryPie();

    /**
     * 全部订单
     *
     * @return List
     */
    List<CommodityOrderEntity> allOrder(@Param("params") Map<String, Object> params);

    /**
     * 当月订单
     *
     * @return List
     */
    List<CommodityOrderEntity> monthOrder();

    /**
     * 今日订单
     *
     * @return List
     */
    List<CommodityOrderEntity> todayOrder();

    /**
     * 全部用户
     *
     * @return List
     */
    List<Wechat> allUser();

    /**
     * 月用户
     *
     * @return List
     */
    List<Wechat> monthUser();

    /**
     * 今日用户
     *
     * @return List
     */
    List<Wechat> dayUser();


    /**
     * 全部会员
     *
     * @return List
     */
    List<MemberLevelOrderEntity> allUserLevel();

    /**
     * 当月会员
     *
     * @return List
     */
    List<MemberLevelOrderEntity> monthUserLevel();

    /**
     * 今日会员
     *
     * @return List
     */
    List<MemberLevelOrderEntity> dayUserLevel();
    /**
     * 今日会员
     *
     * @return List
     */
    List<Wechat> dayUserLevelPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    /**
     * 当月会员
     *
     * @return List
     */
    List<Wechat> monthUserLevelPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    /**
     * 团队新增排行
     *
     * @return List
     */
    List<Wechat> teamAdd();

    /**
     * 团队业绩
     *
     * @return List
     */
    List<Wechat> teamMoney();

    /**
     * 团队新增排行
     *
     * @return List
     */
    List<Wechat> teamAddPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);


    /**
     * 团队新增明细排行
     *
     * @return List
     */
    List<Wechat> teamAddDetailPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    /**
     * 团队业绩
     *
     * @return List
     */
    List<Wechat> teamMoneyPage(IPage<Wechat> page, @Param("params") Map<String, Object> params);

    /**
     * 可提现监控
     *
     * @return List
     */
    List<Wechat> balanceCollectConsole();


    /**
     * 订单总金额监控
     *
     * @return List
     */
    List<Wechat> orderMoneyConsole();

    /**
     * 积分监控
     *
     * @return List
     */
    List<Wechat> pointsConsole();

    /**
     * 购物金监控
     *
     * @return List
     */
    List<Wechat> shoppingMoneyConsole();
}
