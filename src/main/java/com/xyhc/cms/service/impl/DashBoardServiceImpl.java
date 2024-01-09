package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.dao.DashBoardDao;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.entity.MemberLevelOrderEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.DashBoardService;
import com.xyhc.cms.utils.AuthorityUtils;
import com.xyhc.cms.utils.LocalTokenUtils;
import com.xyhc.cms.vo.infra.DashBoardVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品订单表Service实现类
 */
@Service("dashBoardServiceImpl")
public class DashBoardServiceImpl extends ServiceImpl<DashBoardDao, DashBoardVo> implements DashBoardService {

    @Resource
    DashBoardDao dashBoardDao;

    @Resource
    AuthorityUtils authorityUtils;

    @Resource
    LocalTokenUtils localTokenUtils;

    /**
     * 首页走势图统计
     *
     * @return Result
     */
    @Override
    public List<DashBoardVo> homeOrderSummaryTrend() {
        return dashBoardDao.homeOrderSummaryTrend();
    }

    /**
     * 首页饼状图统计
     *
     * @return Result
     */
    @Override
    public List<DashBoardVo> homeOrderSummaryPie() {
        return dashBoardDao.homeOrderSummaryPie();
    }

    /**
     * 全部订单
     *
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> allOrder(Map<String, Object> params) {
        return dashBoardDao.allOrder(params);
    }

    /**
     * 当月订单
     *
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> monthOrder() {
        return dashBoardDao.monthOrder();

    }


    /**
     * 今日订单
     *
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> todayOrder() {

        return dashBoardDao.todayOrder();

    }


    /**
     * 全部用户
     *
     * @return Result
     */
    @Override
    public List<Wechat> allUser() {
        return dashBoardDao.allUser();
    }

    /**
     * 当月用户
     *
     * @return Result
     */
    @Override
    public List<Wechat> monthUser() {
        return dashBoardDao.monthUser();
    }


    /**
     * 今日用户
     *
     * @return Result
     */
    @Override
    public List<Wechat> dayUser() {
        return dashBoardDao.dayUser();
    }


    /**
     * 全部会员
     *
     * @return Result
     */
    @Override
    public List<MemberLevelOrderEntity> allUserLevel() {
        return dashBoardDao.allUserLevel();
    }

    /**
     * 当月会员
     *
     * @return Result
     */
    @Override
    public List<MemberLevelOrderEntity> monthUserLevel() {
        return dashBoardDao.monthUserLevel();
    }

    /**
     * 当月会员
     *
     * @return Result
     */
    @Override
    public PageUtils monthUserLevelPage(Map<String, Object> params) {
        params.put("memberLevel", params.get("memberLevel"));
        IPage<Wechat> page = new Query<Wechat>().getPage(params, "T.pay_time", false);
        List<Wechat> records = dashBoardDao.monthUserLevelPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 今日会员
     *
     * @return Result
     */
    @Override
    public List<MemberLevelOrderEntity> dayUserLevel() {
        return dashBoardDao.dayUserLevel();
    }

    /**
     * 今日会员
     *
     * @return Result
     */
    @Override
    public PageUtils dayUserLevelPage(Map<String, Object> params) {
        IPage<Wechat> page = new Query<Wechat>().getPage(params, "id", true);
        List<Wechat> records = dashBoardDao.dayUserLevelPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 团队新增排行
     *
     * @return Result
     */
    @Override
    public List<Wechat> teamAdd() {
        return dashBoardDao.teamAdd();
    }


    /**
     * 团队业绩
     *
     * @return Result
     */
    @Override
    public List<Wechat> teamMoney() {
        return dashBoardDao.teamMoney();
    }

    /**
     * 团队新增排行
     *
     * @return Result
     */
    @Override
    public PageUtils teamAddPage(Map<String, Object> params) {

        IPage<Wechat> page = new Query<Wechat>().getPage(params, "id", true);
        List<Wechat> records = dashBoardDao.teamAddPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 团队新增明细排行
     *
     * @return Result
     */
    @Override
    public PageUtils teamAddDetailPage(Map<String, Object> params) {

        IPage<Wechat> page = new Query<Wechat>().getPage(params, "id", true);
        List<Wechat> records = dashBoardDao.teamAddDetailPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 团队业绩
     *
     * @return Result
     */
    @Override
    public PageUtils teamMoneyPage(Map<String, Object> params) {

        IPage<Wechat> page = new Query<Wechat>().getPage(params, "id", true);
        List<Wechat> records = dashBoardDao.teamMoneyPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 平台监控
     *
     * @return Result
     */
    @Override
    public void balanceConsole() {
        List<Wechat> consoleVos1 = dashBoardDao.balanceCollectConsole();
        if (consoleVos1.size() > 0) {
            authorityUtils.sendConsoleError("蕲艾世家提醒-可提现金额", String.join(",", consoleVos1.stream().map(s -> s.getId()).collect(Collectors.toList())));
        }

        List<Wechat> consoleVos2 = dashBoardDao.orderMoneyConsole();
        if (consoleVos2.size() > 0) {
            authorityUtils.sendConsoleError("蕲艾世家提醒-订单总金额", String.join(",", consoleVos2.stream().map(s -> s.getId()).collect(Collectors.toList())));
        }
        List<Wechat> consoleVos3 = dashBoardDao.pointsConsole();
        if (consoleVos3.size() > 0) {
            authorityUtils.sendConsoleError("蕲艾世家提醒-积分", String.join(",", consoleVos3.stream().map(s -> s.getId()).collect(Collectors.toList())));
        }

        List<Wechat> consoleVos4 = dashBoardDao.shoppingMoneyConsole();
        if (consoleVos4.size() > 0) {
            authorityUtils.sendConsoleError("蕲艾世家提醒-购物金", String.join(",", consoleVos4.stream().map(s -> s.getId()).collect(Collectors.toList())));
        }

        if (consoleVos1.size() == 0 && consoleVos2.size() == 0 && consoleVos3.size() == 0 && consoleVos4.size() == 0) {
            if (localTokenUtils.getLocalToken()) {
                authorityUtils.sendConsoleNormal("蕲艾世家提醒正常", "");
            }
        }
    }
}
