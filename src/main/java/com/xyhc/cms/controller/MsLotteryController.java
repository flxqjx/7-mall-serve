package com.xyhc.cms.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.service.MatchClassifyService;
import com.xyhc.cms.service.MsLotteryService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@RestController
@RequestMapping("/msLottery")
public class MsLotteryController {
    @Autowired
    private MsLotteryService mslotteryService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MsLotteryEntity> all(@RequestParam Map<String, Object> params) {
        return mslotteryService.all(params);
    }

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
//    @RequestMapping("/randomLotteryNum")
//    public List<MsLotteryEntity> randomLotteryNum(@RequestParam Map<String, Object> params) {
//        return mslotteryService.randomLotteryNum(params);
//    }

    /**
     * 查询是否有抽奖次数
     */
    @RequestMapping("/randomLotteryNum")
    public Result randomLotteryNum(@RequestParam Map<String, Object> params) {
        try {
            return mslotteryService.randomLotteryNum(params);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
//

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/page")
    public PageUtils page(@RequestParam Map<String, Object> params) {
        try {
            return mslotteryService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/detail")
    public Result detail(@RequestParam String id) {
        try {
            return Result.success(mslotteryService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param mslottery
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MsLotteryEntity mslottery) {
        try {
            mslotteryService.save(mslottery);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 根据主键删除
     *
     * @param id
     * @return RestResponse
     */
    @RequestMapping("/remove")
    public Result remove(@RequestParam String id) {
    try {
        mslotteryService.remove(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return RestResponse
     */
    @RequestMapping("/removeBatch")
    public Result removeBatch(@RequestBody String[] ids) {
    try {
        mslotteryService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }

    /**
     * 最近的一条抽奖活动
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/lastLottery")
    public Result lastLottery() {
        try {
            return Result.success(mslotteryService.lastLottery());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
