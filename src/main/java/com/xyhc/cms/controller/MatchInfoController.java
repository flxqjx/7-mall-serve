package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.entity.MatchInfoEntity;
import com.xyhc.cms.service.MatchInfoService;
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
 * 赛事主表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:42:15
 */
@RestController
@RequestMapping("/matchInfo")
public class MatchInfoController {
    @Autowired
    private MatchInfoService matchInfoService;
    @Autowired
    AuthUtils authUtils;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MatchInfoEntity> all(@RequestParam Map<String, Object> params) {
        return matchInfoService.all(params);
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
            return matchInfoService.page(params);
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
            return Result.success(matchInfoService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param matchInfo
     * @return RestResponse
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MatchInfoEntity matchInfo) {
        try {
            matchInfo.setCreateBy(authUtils.AuthUser().getUserId());
            matchInfo.setUpdateBy(authUtils.AuthUser().getUserId());
            matchInfoService.save(matchInfo);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 根据主键推荐赛事
     *
     * @param id
     * @return RestResponse
     */
    @RequestMapping("/recommend")
    public Result recommend(@RequestParam String id){
        try{
            matchInfoService.recommend(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 根据主键取消推荐赛事
     *
     * @param id
     * @return RestResponse
     */
    @RequestMapping("/cancelRecommend")
    public Result cancelRecommend(@RequestParam String id){
        try{
            matchInfoService.cancelRecommend(id);
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
            matchInfoService.remove(id);
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
            matchInfoService.removeBatch(ids);
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
          return matchInfoService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<MatchInfoEntity> matchInfoEntity = new ArrayList();

        if (params != null && params.size() > 0) {
            matchInfoEntity = matchInfoService.all(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), MatchInfoEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(matchInfoEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), MatchInfoEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(matchInfoEntity);
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
