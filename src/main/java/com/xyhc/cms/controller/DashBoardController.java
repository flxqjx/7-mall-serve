package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.DashBoardService;
import com.xyhc.cms.vo.infra.DashBoardVo;
import com.xyhc.cms.vo.infra.PieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.DoubleStream;


/**
 * 登录
 *
 * @Author apollo
 */
@RestController
public class DashBoardController {


    @Autowired
    AuthUtils authUtils;
    @Autowired
    DashBoardService dashBoardService;

    /**
     * 首页数据统计
     *
     * @param
     * @return
     */

    @GetMapping("/dashborad/home")
    public Result home(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> repMap = new HashMap<>();
            Map<String, Object> repMapDay = new HashMap<>();
            repMap.put("dayOrderTotal", dashBoardService.todayOrder().size());
            repMap.put("monthOrderTotal", dashBoardService.monthOrder().size());
            repMap.put("allOrderTotal", dashBoardService.allOrder(params).size());

            repMap.put("dayOrderMoney", String.format("%.2f", dashBoardService.todayOrder().stream().mapToDouble(s -> s.getTotal()).sum()));
            repMap.put("monthOrderMoney", String.format("%.2f", dashBoardService.monthOrder().stream().mapToDouble(s -> s.getTotal()).sum()));
            repMap.put("allOrderMoney", String.format("%.2f", dashBoardService.allOrder(params).stream().mapToDouble(s -> s.getTotal()).sum()));

            repMap.put("dayUser", dashBoardService.dayUser().size());
            repMap.put("monthUser", dashBoardService.monthUser().size());
            repMap.put("allUser", dashBoardService.allUser().size());


            repMap.put("dayUserLevel", dashBoardService.dayUserLevel().size());
            repMap.put("monthUserLevel", dashBoardService.monthUserLevel().size());
            repMap.put("allUserLevel", dashBoardService.allUserLevel().size());

            repMap.put("teamAdd", dashBoardService.teamAdd().size());
            repMap.put("teamMoney", String.format("%.2f", dashBoardService.teamMoney().stream().mapToDouble(s -> s.getSumXfBalance()).sum()));

            return Result.success(repMap);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 首页走势图
     *
     * @param
     * @return
     */

    @GetMapping("/dashborad/trendChart")
    public Result trendChart() {
        try {
            Map<String, Object> repMap = new HashMap<>();
            List<String> xList = new ArrayList<>();
            List<Object> yList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 取总单数
            List<DashBoardVo> homeOrderSummary = dashBoardService.homeOrderSummaryTrend();
            // 更新数据库字段
            Calendar calendar = Calendar.getInstance();
            Date nowDate = new Date();
            for (int i = 0; i <= 7; i++) {
                calendar.setTime(nowDate);
                calendar.add(Calendar.DATE, -7 + i);
                String dataFormat = sdf.format(calendar.getTime());
                xList.add(dataFormat);

                Optional<DashBoardVo> findDto = homeOrderSummary.stream().filter(s -> s.getFKey().equals(dataFormat)).findFirst();
                if (findDto.isPresent()) {
                    yList.add(findDto.get().getTCount());
                } else {
                    yList.add(0);
                }

            }
            repMap.put("x", xList);
            repMap.put("y", yList);
            return Result.success(repMap);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 首页饼状图
     *
     * @param
     * @return
     */

    @GetMapping("/dashborad/pieChart")
    public Result pieChart() {
        try {
            List<PieDto> repPieDto = new ArrayList<>();

            // 取总单数
            List<DashBoardVo> homeOrderSummary = dashBoardService.homeOrderSummaryPie();

            homeOrderSummary.forEach(summary -> {
                PieDto pieDto = new PieDto();
                pieDto.setName(summary.getFKey());
                pieDto.setValue(summary.getTCount());
                repPieDto.add(pieDto);
            });

            return Result.success(repPieDto);
        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * 团队新增排行
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/dashborad/teamAddPage")
    public PageUtils teamAddPage(@RequestParam Map<String, Object> params) {
        try {
            return dashBoardService.teamAddPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 团队新增明细排行
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/dashborad/teamAddDetailPage")
    public PageUtils teamAddDetailPage(@RequestParam Map<String, Object> params) {
        try {
            return dashBoardService.teamAddDetailPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 团队业绩
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/dashborad/teamMoneyPage")
    public PageUtils teamMoneyPage(@RequestParam Map<String, Object> params) {
        try {
            return dashBoardService.teamMoneyPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 今日会员 分页
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/dashborad/dayUserLevelPage")
    public PageUtils dayUserLevelPage(@RequestParam Map<String, Object> params) {
        try {
            return dashBoardService.dayUserLevelPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 当月会员 分页
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/dashborad/monthUserLevelPage")
    public PageUtils monthUserLevelPage(@RequestParam Map<String, Object> params) {
        try {
            return dashBoardService.monthUserLevelPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}

