package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.entity.CouponUsersEntity;
import com.xyhc.cms.service.CouponUsersService;
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
 * 优惠券领取Controller
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@RestController
@RequestMapping("/couponUsers")
public class CouponUsersController {
    @Autowired
    private CouponUsersService couponUsersService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<CouponUsersEntity> all(@RequestParam Map<String, Object> params) {
        return couponUsersService.all(params);
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
            return couponUsersService.page(params);
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
            return Result.success(couponUsersService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param couponUsers
     * @return RestResponse
     */
    @RequestMapping("/saveUsers")
    public Result saveUsers(@RequestBody CouponUsersEntity couponUsers) {
        try {
            return couponUsersService.saveUsers(couponUsers);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
    /**
     * 通过复制办法得到券码后，进行兑换
     *
     * @param  couponUsers
     * @return boolean
     */
    @RequestMapping("/exchangecode")
    //public boolean exchangecode(@RequestBody CouponUsersEntity couponUsers,String integralcode) {
    //public boolean exchangecode(CouponUsersEntity couponUsers,String integralcode) {

    //public boolean exchangecode(@RequestParam String integralcode,@RequestBody CouponUsersEntity couponUsers) {
    public Result exchangecode(@RequestBody CouponUsersEntity couponUsers) {
        try {
            return couponUsersService.exchangecode(couponUsers);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
/////////////////////////////////////////////////////////////////////

    /**
     * 通过复制券码准备赠送给别人，此时先向COUPONUSERS表中插入一条记录
     *
     * @param  couponUsers
     * @return boolean
     */
    @RequestMapping("/copycode")

    public Result copycode(@RequestBody CouponUsersEntity couponUsers) {
        try {
            return couponUsersService.copycode(couponUsers);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 通过分享链接办法得到优惠券
     *
     * @param  couponUsers
     * @return boolean
     */
    @RequestMapping("/datelshare")
    public Result datelshare(@RequestBody CouponUsersEntity couponUsers) {
        try {

            //return "5";
            //return Result.success();
            return couponUsersService.datelshare(couponUsers);
        } catch (Exception ex) {
            //return '5';
            //throw ex;
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
            couponUsersService.remove(id);
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
            couponUsersService.removeBatch(ids);
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
          return couponUsersService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<CouponUsersEntity> couponUsersEntity = new ArrayList();

        if (params != null && params.size() > 0) {
            couponUsersEntity = couponUsersService.all(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), CouponUsersEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(couponUsersEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), CouponUsersEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(couponUsersEntity);
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
