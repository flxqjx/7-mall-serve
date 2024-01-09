package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.entity.PointsmallClassifyEntity;
import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.entity.PointsmallOrderCommodityEntity;
import com.xyhc.cms.entity.PointsmallOrderEntity;
import com.xyhc.cms.service.PointsmallOrderService;
//import com.xyhc.cms.vo.wechatMsg.PointsmallImgDto;
import com.xyhc.cms.vo.wechatMsg.PointsmallOrderSaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
/**
 * 商品订单表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
@RestController
@RequestMapping("/pointsmallOrder")
public class PointsmallOrderController {


    @Autowired
    private PointsmallOrderService pointsmallOrderService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<PointsmallOrderEntity> all(@RequestParam Map<String, Object> params) {
        return pointsmallOrderService.all(params);
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
            return pointsmallOrderService.page(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询我分享的普通用户下的单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/pageCommon")
    public PageUtils pageCommon(@RequestParam Map<String, Object> params) {
        try {
            return pointsmallOrderService.pageCommon(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 分页查询全部订单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/pageEntire")
    public PageUtils pageEntire(@RequestParam Map<String, Object> params) {
        try {
            return pointsmallOrderService.pageEntire(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * 查询————我的订单状态各数量
     *
     */
    @GetMapping("/myOrderNum")
    public Result myOrderNum() {
        try{
            return pointsmallOrderService.myOrderNum();
        }catch (Exception e){
            return Result.error(e.getMessage());
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
            return Result.success(pointsmallOrderService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询该用户是否购买过新人专享商品
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/newOrderdetail")
    public Result newOrderdetail() {
        try {
            return Result.success(pointsmallOrderService.newOrderdetail());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据orderid查询订单详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/orderDetail")
    public Result orderDetail(@RequestParam String id) {
        try {
            return Result.success(pointsmallOrderService.orderDetail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 确认收货
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveReceiving")
    public Result saveReceiving(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.saveReceiving(pointsmallOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 退货申请
     */
    @RequestMapping("/saveRefund")
    public Result saveRefund(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.saveRefund(pointsmallOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 分页查询退货订单
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/pageRefund")
    public PageUtils pageRefund(@RequestParam Map<String, Object> params) {
        try {
            return pointsmallOrderService.pageRefund(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 修改订单地址
     */
    @RequestMapping("/editAddress")
    public Result editAddress(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.editAddress(pointsmallOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }



    /**
     * 修改，添加订单
     */
    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody PointsmallOrderSaveDto pointsmallOrderSave) {
        try {
            return pointsmallOrderService.saveOrder(pointsmallOrderSave);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.save(pointsmallOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改订单处理
     */
    @RequestMapping("/saveProcess")
    public Result saveProcess(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.saveProcess(pointsmallOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改支付状态
     */
    @RequestMapping("/updatePayStatus")
    public Result updatePayStatus(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.updatePayStatus(pointsmallOrderEntity);
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
            pointsmallOrderService.remove(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 退货审核通过
     */
    @RequestMapping("/audit")
    public Result audit(@RequestParam String id) {
        try {
            pointsmallOrderService.audit(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 驳回退货申请
     */
    @RequestMapping("/reject")
    public Result reject(@RequestBody PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            pointsmallOrderService.reject(pointsmallOrderEntity);
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
            pointsmallOrderService.removeBatch(ids);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }

    }


    /**
     * 导入
     *
     * @param
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("Files") MultipartFile file) throws IOException {
        return pointsmallOrderService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<PointsmallOrderEntity> pointsmallOrderEntity = new ArrayList();

        List<String> columns = new ArrayList<>();
        columns.add("id");
        columns.add("commodityName");
        columns.add("specName");
        columns.add("num");
        columns.add("receiveAddress");
//        columns.add("provinceId");
//        columns.add("cityId");
//        columns.add("areaId");

        if (params != null && params.size() > 0) {
            pointsmallOrderEntity = pointsmallOrderService.allExport(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), PointsmallOrderEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(pointsmallOrderEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), PointsmallOrderEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(pointsmallOrderEntity);
        }

    }

    /**
     * 初始化导出样式
     */
    public static HorizontalCellStyleStrategy defaultStyles() {
        //表头样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//        //表头前景设置淡蓝色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
        //内容样式策略策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置垂直居中为居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置左右对齐为靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置单元格上下左右边框为细边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }


}
