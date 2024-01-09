package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import com.xyhc.cms.service.MsLotteryInfoService;
import com.xyhc.cms.service.MsLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@RestController
@RequestMapping("/msLotteryInfo")
public class MsLotteryInfoController {
    @Autowired
    private MsLotteryInfoService mslotteryInfoService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MsLotteryInfoEntity> all(@RequestParam Map<String, Object> params) {
        return mslotteryInfoService.all(params);
    }


    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/page")
    public PageUtils page(@RequestParam Map<String, Object> params) {
        try {
            return mslotteryInfoService.page(params);
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
            return Result.success(mslotteryInfoService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param mslotteryInfo
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MsLotteryInfoEntity mslotteryInfo) {
        try {
            mslotteryInfoService.save(mslotteryInfo);
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
        mslotteryInfoService.remove(id);
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
        mslotteryInfoService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }


    /**
     * 随机返回抽奖奖品
     *
     * @param
     */
    @GetMapping("/randomLotteryInfo")
    public Result randomLotteryInfo(@RequestParam Map<String, Object> params) {
        try {
            return mslotteryInfoService.randomLotteryInfo(params);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
