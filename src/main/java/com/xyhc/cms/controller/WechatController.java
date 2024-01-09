package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.entity.UserEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户主表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@RestController
@RequestMapping("/wechat")
public class WechatController {
    @Autowired
    private WechatService wechatService;

    @Autowired
    AuthUtils authUtils;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<Wechat> all(@RequestParam Map<String, Object> params) {
        return wechatService.all(params);
    }

    /**
     * 更新
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/updatePid")
    public Result updatePid(@RequestBody Wechat wechat) {
        try {
            wechatService.updatePid(wechat);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
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
            return Result.success(wechatService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询用户团队
     *
     * @return RestResponse
     */
    @GetMapping("/teamPage")
    public PageUtils teamPage(@RequestParam Map<String, Object> params) {
        try {
            return wechatService.teamPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询我的团队
     *
     * @return RestResponse
     */
    @RequestMapping("/team")
    public Result team() {
        try {
            return Result.success(wechatService.team());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询用户是否A套餐会员
     */
    @RequestMapping("/userInfoVip")
    public Result userInfoVip() {
        try {
            return Result.success(wechatService.userInfoVip());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据主键查询详情
     *
     * @param ranNo 主键
     * @return RestResponse
     */
    @RequestMapping("/userDetail")
    public Result userDetail(@RequestParam int ranNo) {
        try {
            return Result.success(wechatService.userDetail(ranNo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
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
            return wechatService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 保存头像和昵称
     *
     * @param wechat
     * @return RestResponse
     */
    @RequestMapping("/saveMessage")
    public Result saveMessage(@RequestBody Wechat wechat) {
        try {
            return wechatService.saveMessage(wechat);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 完善个人信息
     *
     * @param wechat
     * @return RestResponse
     */
    @RequestMapping("/saveInfo")
    public Result saveInfo(@RequestBody Wechat wechat) {
        try {
            return wechatService.saveInfo(wechat);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 备注
     *
     * @param id
     * @return RestResponse
     */
    @RequestMapping("/saveUserRemark")
    public Result saveUserRemark(@RequestParam String id, String remark) {
        try {
            wechatService.saveUserRemark(id, remark);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 查询用户业绩
     *
     * @param
     * @return RestResponse
     */
    @GetMapping("/userPerformanceByUid")
    public PageUtils userPerformanceByUid(@RequestParam Map<String, Object> params) {
        try {
            params.put("uid", authUtils.AuthUser().getUserId());
            return wechatService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询用户团队业绩
     *
     * @param
     * @return RestResponse
     */
    @GetMapping("/teamUserPerformanceByUid")
    public PageUtils teamUserPerformanceByUid(@RequestParam Map<String, Object> params) {
        try {
            params.put("id", authUtils.AuthUser().getUserId());
            return wechatService.teamPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 个人中心取会员团队信息
     */
    @RequestMapping("/userInfoTeam")
    public Result userInfoTeam() {
        try {
            return wechatService.userInfoTeam();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}
