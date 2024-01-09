package com.xyhc.cms.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MemberLevelEntity;
import com.xyhc.cms.service.MemberLevelService;
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
 * 会员级别Controller
 *
 * @author apollo
 * @since 2023-03-22 15:18:26
 */
@RestController
@RequestMapping("/memberLevel")
public class MemberLevelController {
    @Autowired
    private MemberLevelService memberLevelService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MemberLevelEntity> all(@RequestParam Map<String, Object> params) {
        return memberLevelService.all(params);
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
            return memberLevelService.page(params);
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
            return Result.success(memberLevelService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param memberLevel
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MemberLevelEntity memberLevel) {
        try {
            memberLevelService.save(memberLevel);
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
            memberLevelService.remove(id);
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
            memberLevelService.removeBatch(ids);
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
          return memberLevelService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<MemberLevelEntity> memberLevelEntity = new ArrayList();

        if (params != null && params.size() > 0) {
            memberLevelEntity = memberLevelService.all(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), MemberLevelEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(memberLevelEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), MemberLevelEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(memberLevelEntity);
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
