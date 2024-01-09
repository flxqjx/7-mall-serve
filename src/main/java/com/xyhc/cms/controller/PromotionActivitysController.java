package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.PromotionActivitysEntity;
import com.xyhc.cms.service.MsLotteryService;
import com.xyhc.cms.service.PromotionActivitysService;
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
@RequestMapping("/promotionActivitys")
public class PromotionActivitysController {
    @Autowired
    private PromotionActivitysService promotionActivitysService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<PromotionActivitysEntity> all(@RequestParam Map<String, Object> params) {
        return promotionActivitysService.all(params);
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
            return promotionActivitysService.page(params);
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
            return Result.success(promotionActivitysService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param promotionActivitys
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody PromotionActivitysEntity promotionActivitys) {
        try {
            promotionActivitysService.save(promotionActivitys);
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
        promotionActivitysService.remove(id);
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
        promotionActivitysService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 开启
     *
     * @param
     */
    @RequestMapping("/open")
    public Result open(@RequestParam String id) {
        return promotionActivitysService.open(id);
    }


    /**
     * 关闭
     *
     * @param
     */
    @RequestMapping("/cancelOpen")
    public Result cancelOpen(@RequestParam String id) {
        try {
            promotionActivitysService.cancelOpen(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

}
