package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import com.xyhc.cms.entity.MsLotteryUserEntity;
import com.xyhc.cms.service.MsLotteryInfoService;
import com.xyhc.cms.service.MsLotteryUserService;
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
@RequestMapping("/msLotteryUser")
public class MsLotteryUserController {
    @Autowired
    private MsLotteryUserService mslotteryUserService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MsLotteryUserEntity> all(@RequestParam Map<String, Object> params) {
        return mslotteryUserService.all(params);
    }

    /**
     * 查询全部数据用户中奖记录
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/userAll")
    public List<MsLotteryUserEntity> userAll(@RequestParam Map<String, Object> params) {
        return mslotteryUserService.userAll(params);
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
            return mslotteryUserService.page(params);
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
            return Result.success(mslotteryUserService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param mslotteryUser
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MsLotteryUserEntity mslotteryUser) {
        try {
            return mslotteryUserService.saveOrder(mslotteryUser);
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
        mslotteryUserService.remove(id);
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
        mslotteryUserService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }


    /**
     * 奖品发货
     *
     * @param
     */
    @RequestMapping("/sendLottery")
    public Result sendLottery(@RequestParam Map<String, Object> params) {
        try {
            mslotteryUserService.sendLottery(params);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
    /**
     * 更新抽奖积分
     *
     * @param
     */
    @RequestMapping("/updateLotteryPoints")
    public Result updateLotteryPoints(@RequestParam Map<String, Object> params) {
        try {
            mslotteryUserService.updateLotteryPoints(params);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
}
