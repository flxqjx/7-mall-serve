package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.MemberLevelOrderEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.vo.infra.DashBoardVo;

import java.util.List;
import java.util.Map;

/**
 * 商品订单表Service接口
 */
public interface DashBoardService extends IService<DashBoardVo> {

    /**
     * 首页走势图统计
     *
     * @return Result
     */
    List<DashBoardVo> homeOrderSummaryTrend();

    /**
     * 首页走势图统计
     *
     * @return Result
     */
    List<DashBoardVo> homeOrderSummaryPie();

    /**
     * 全部订单
     *
     * @return Result
     */
    public List<CommodityOrderEntity> allOrder(Map<String, Object> params);


    /**
     * 当月订单
     *
     * @return Result
     */
    public List<CommodityOrderEntity> monthOrder();


    /**
     * 今日订单
     *
     * @return Result
     */
    public List<CommodityOrderEntity> todayOrder();


    /**
     * 全部用户
     *
     * @return List<Wechat>
     */
    public List<Wechat> allUser();

    /**
     * 当月用户
     *
     * @return List<Wechat>
     */
    public List<Wechat> monthUser();

    /**
     * 今日用户
     *
     * @return List<Wechat>
     */
    public List<Wechat> dayUser();


    /**
     * 全部会员
     *
     * @return List<Wechat>
     */
    public List<MemberLevelOrderEntity> allUserLevel();

    /**
     * 当月会员
     *
     * @return List<Wechat>
     */
    public List<MemberLevelOrderEntity> monthUserLevel();

    /**
     * 今日会员
     *
     * @return List<Wechat>
     */
    public List<MemberLevelOrderEntity> dayUserLevel();

    /**
     * 团队新增排行
     *
     * @return List<Wechat>
     */
    public List<Wechat> teamAdd();

    /**
     * 团队业绩
     *
     * @return List<Wechat>
     */
    public List<Wechat> teamMoney();

    /**
     * 团队新增排行
     *
     * @return Result
     */
    public PageUtils teamAddPage(Map<String, Object> params);
    /**
     * 团队新增明细排行
     *
     * @return Result
     */
    public PageUtils teamAddDetailPage(Map<String, Object> params);


    /**
     * 团队业绩
     *
     * @return Result
     */
    public PageUtils teamMoneyPage(Map<String, Object> params);

    /**
     * 当月会员
     *
     * @return Result
     */
    public PageUtils monthUserLevelPage(Map<String, Object> params);

    /**
     * 今日会员
     *
     * @return Result
     */
    public PageUtils dayUserLevelPage(Map<String, Object> params);

    /**
     * 平台监控
     *
     * @return Result
     */
    public void balanceConsole() ;
}
