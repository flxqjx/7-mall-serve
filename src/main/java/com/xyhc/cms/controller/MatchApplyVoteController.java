package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.entity.MatchApplyVoteEntity;
import com.xyhc.cms.entity.XfLogEntity;
import com.xyhc.cms.service.MatchApplyVoteService;
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
 * 赛事作品投票Controller
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@RestController
@RequestMapping("/matchApplyVote")
public class MatchApplyVoteController {
    @Autowired
    private MatchApplyVoteService matchApplyVoteService;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<MatchApplyVoteEntity> all(@RequestParam Map<String, Object> params) {
        return matchApplyVoteService.all(params);
    }

    /**
     * 根据作品ID查询投票
     *
     * @param matchApplyId
     * @return RestResponse
     */
    @RequestMapping("/voteByApplyId")
    public List<MatchApplyVoteEntity> voteByApplyId(@RequestParam String matchApplyId) {
        return matchApplyVoteService.voteByApplyId(matchApplyId);
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
            return matchApplyVoteService.page(params);
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
            return Result.success(matchApplyVoteService.detail(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 保存
     *
     * @param matchApplyVote
     * @return RestResponse
     */
    @RequestMapping("/saveVote")
    public Result saveVote(@RequestBody MatchApplyVoteEntity matchApplyVote) {
        try {
            return matchApplyVoteService.saveVote(matchApplyVote);
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
            matchApplyVoteService.remove(id);
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
            matchApplyVoteService.removeBatch(ids);
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
        return matchApplyVoteService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<MatchApplyVoteEntity> matchApplyVoteEntity = new ArrayList();

        if (params != null && params.size() > 0) {
            matchApplyVoteEntity = matchApplyVoteService.all(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), MatchApplyVoteEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(matchApplyVoteEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), MatchApplyVoteEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(matchApplyVoteEntity);
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
