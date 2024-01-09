package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.entity.CommodityClassifyEntity;
import com.xyhc.cms.entity.CommodityEntity;
import com.xyhc.cms.entity.CommodityOrderCommodityEntity;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.service.CommodityOrderService;
import com.xyhc.cms.vo.wechatMsg.CommodityImgDto;
import com.xyhc.cms.vo.wechatMsg.CommodityOrderSaveDto;
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
@RequestMapping("/commodityOrder")
public class CommodityOrderController {
    @Autowired
    private CommodityOrderService commodityOrderService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<CommodityOrderEntity> all(@RequestParam Map<String, Object> params) {
        return commodityOrderService.all(params);
    }

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/allByUid")
    public List<CommodityOrderEntity> allByUid(@RequestParam Map<String, Object> params) {
        return commodityOrderService.allByUid(params);
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
            return commodityOrderService.page(params);
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
            return commodityOrderService.pageCommon(params);
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
            return commodityOrderService.pageEntire(params);
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
            return commodityOrderService.myOrderNum();
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
            return Result.success(commodityOrderService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询该用户是否购买过新人专享商品
     *
     * @param 主键
     * @return RestResponse
     */
    @RequestMapping("/newOrderdetail")
    public Result newOrderdetail() {
        try {
            return Result.success(commodityOrderService.newOrderdetail());
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
            return Result.success(commodityOrderService.orderDetail(id));
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
    public Result saveReceiving(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.saveReceiving(commodityOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 退货申请
     */
    @RequestMapping("/saveRefund")
    public Result saveRefund(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.saveRefund(commodityOrderEntity);
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
            return commodityOrderService.pageRefund(params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 修改订单地址
     */
    @RequestMapping("/editAddress")
    public Result editAddress(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.editAddress(commodityOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }



    /**
     * 修改，添加订单
     */
    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody CommodityOrderSaveDto commodityOrderSave) {
        try {
            return commodityOrderService.saveOrder(commodityOrderSave);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public Result save(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.save(commodityOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改订单处理
     */
    @RequestMapping("/saveProcess")
    public Result saveProcess(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.saveProcess(commodityOrderEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改支付状态
     */
    @RequestMapping("/updatePayStatus")
    public Result updatePayStatus(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.updatePayStatus(commodityOrderEntity);
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
            commodityOrderService.remove(id);
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
            commodityOrderService.audit(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 驳回退货申请
     */
    @RequestMapping("/reject")
    public Result reject(@RequestBody CommodityOrderEntity commodityOrderEntity) {
        try {
            commodityOrderService.reject(commodityOrderEntity);
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
            commodityOrderService.removeBatch(ids);
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
          return commodityOrderService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<CommodityOrderEntity> commodityOrderEntity = new ArrayList();

        List<String> columns = new ArrayList<>();
        columns.add("nickName");
        columns.add("id");
        columns.add("parentName");
        columns.add("price");
        columns.add("commodityTotal");
        columns.add("discount");
        columns.add("total");
        columns.add("levelType");
        columns.add("isPayStatus");
        columns.add("payType");
        columns.add("orderStatus");
        columns.add("transactionId");
        columns.add("outTradeNo");
        columns.add("receiveName");
        columns.add("provinceId");
        columns.add("cityId");
        columns.add("areaId");
        columns.add("receiveAddress");
        columns.add("receiveTell");
        columns.add("remark");
        columns.add("payTime");

        if (params != null && params.size() > 0) {
            commodityOrderEntity = commodityOrderService.allExport(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), CommodityOrderEntity.class)
                    .sheet("sheet1")
                    .includeColumnFiledNames(columns)
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(commodityOrderEntity);
//            EasyExcel.write(response.getOutputStream(), CommodityOrderEntity.class)
//                    .sheet("sheet1")
//                    .registerWriteHandler(defaultStyles())
//                    .doWrite(commodityOrderEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), CommodityOrderEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(commodityOrderEntity);
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
