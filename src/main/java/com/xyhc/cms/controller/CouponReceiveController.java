package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.CouponReceiveEntity;
import com.xyhc.cms.entity.MemberLevelImgEntity;
import com.xyhc.cms.service.CouponReceiveService;
import com.xyhc.cms.service.MemberLevelImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品图片Controller
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
@RestController
@RequestMapping("/couponReceive")
public class CouponReceiveController {
    @Autowired
    private CouponReceiveService couponReceiveService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<CouponReceiveEntity> all(@RequestParam Map<String, Object> params) {
        return couponReceiveService.all(params);
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
            return couponReceiveService.page(params);
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
            return Result.success(couponReceiveService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param couponReceiveEntity
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody CouponReceiveEntity couponReceiveEntity) {
        try {
            couponReceiveService.save(couponReceiveEntity);
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
            couponReceiveService.remove(id);
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
            couponReceiveService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }

    /**
     * 查询我的优惠券
     *
     * @param params isUse
     * @return RestResponse
     */
    @RequestMapping("/myCoupon")
    public List<CouponReceiveEntity> myCoupon(@RequestParam Map<String, Object> params) {
        return couponReceiveService.myCoupon(params);
    }


}
