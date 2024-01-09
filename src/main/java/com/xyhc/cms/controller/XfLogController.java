package com.xyhc.cms.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.XfLogEntity;
import com.xyhc.cms.service.XfLogService;
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
 * Controller
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@RestController
@RequestMapping("/xfLog")
public class XfLogController {
    @Autowired
    private XfLogService xfLogService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<XfLogEntity> all(@RequestParam Map<String, Object> params) {
        return xfLogService.all(params);
    }

    /**
     * 已到账
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/receivedLog")
    public List<XfLogEntity> receivedLog(@RequestParam Map<String, Object> params) {
        return xfLogService.receivedLog(params);
    }

    /**
     * 购物金明细
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/shoppingMoneyall")
    public List<XfLogEntity> shoppingMoneyall(@RequestParam Map<String, Object> params) {
        return xfLogService.shoppingMoneyall(params);
    }


    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/page")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/balanceDetail")
    public PageUtils balanceDetail(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.balanceDetail(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 订单收入订单列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/orderRevenue")
    public PageUtils orderRevenue(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.orderRevenue(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询已到账的
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/collectedBalances")
    public Result collectedBalances() {
        try {
            return Result.success(xfLogService.collectedBalances());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询
     * 查询充值记录
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/payPage")
    public PageUtils payPage(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.payPage(params);
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
            return Result.success(xfLogService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param xfLogEntity
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody XfLogEntity xfLogEntity) {
        try {
            xfLogService.save(xfLogEntity);
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
            xfLogService.remove(id);
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
            xfLogService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }

    /**
     * 保存提现金额
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveWithdrawMoney")
    public Result saveWithdrawMoney(@RequestBody XfLogEntity xfLogEntity) {
        try {
            return xfLogService.saveWithdrawMoney(xfLogEntity);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 根据id更新提现金额的审核状态
     *
     * @param
     */
    @RequestMapping("/saveAuditStatus")
    public Result saveAuditStatus(@RequestParam String id) {
        try {
            xfLogService.saveAuditStatus(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /*
     * 驳回
     * */
    @GetMapping("/reject")
    public Result reject(@RequestParam Map<String, Object> params) {
        try {
            xfLogService.reject(params);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 购物金使用明细
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/deducPage")
    public PageUtils deducPage(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.deducPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 团队业绩明细
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/teamYjPage")
    public PageUtils teamYjPage(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.teamYjPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 用户分佣明细
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/userDetailPage")
    public PageUtils userDetailPage(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.userDetailPage(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分页查询 根据订单ID查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/balanceDetailByOrderId")
    public PageUtils balanceDetailByOrderId(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.balanceDetailByOrderId(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 已到账明细
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/balanceWithdrawnLog")
    public PageUtils balanceWithdrawnLogByUserId(@RequestParam Map<String, Object> params) {
        try {
            return xfLogService.balanceWithdrawnLogByUserId(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
