package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.config.auth.AuthInfo;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.entity.SysBlock;
import com.xyhc.cms.service.BlockService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 登录
 *
 * @Author apollo
 */
@RestController
public class BlockController {


    @Autowired
    BlockService blockService;

    @Autowired
    AuthUtils authUtils;


    /**
     * 列表
     *
     * @param
     * @return
     */

    @GetMapping("/block/search")
    public PageUtils search(@RequestParam Map<String, Object> params) {
        try {
            AuthUtils auto = new AuthUtils();
            AuthInfo authInfo = auto.AuthUser();
            params.put("vendorId", authInfo.getVendorId());
            PageUtils proList = blockService.page(params);
            return proList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/block/page")
    public PageUtils page(@RequestParam Map<String, Object> params) {
        try {
            return blockService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 单条信息
     *
     * @param
     * @return
     */
    @GetMapping("/block/detail")
    public Result detail(@RequestParam String blockId) {
        try {
            SysBlock order = blockService.detail(blockId);
            return Result.success(order);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 单条信息
     *
     * @param
     * @return
     */
    @GetMapping("/block/detailByBlock")
    public Result detailByBlock(@RequestParam String block) {
        try {
            SysBlock order = blockService.detailByBlock(block);
            return Result.success(order);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改，添加
     *
     * @param
     * @return
     */
    @PostMapping("/block/save")
    public Result saveBlock(@RequestBody SysBlock order) {
        try {
            String rep = blockService.save(order);
            if (StringUtils.isNotEmpty(rep)) {
                return Result.error(rep);
            } else {
                return Result.success();
            }


        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 删除
     *
     * @param
     * @return
     */
    @PostMapping("/block/remove")
    public Result remove(@RequestParam String id) {
        try {
            blockService.remove(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 轮播
     *
     * @param
     * @return
     */
    @GetMapping("/sysBlock/indexBanner")
    public Result indexBanner() {
        try {
            return Result.success(blockService.indexBanner());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 快捷入口
     *
     * @param
     * @return
     */
    @GetMapping("/sysBlock/typeBanner")
    public Result typeBanner() {
        try {
            return Result.success(blockService.typeBanner());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


}

