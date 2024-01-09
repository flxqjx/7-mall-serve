package com.xyhc.cms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyhc.cms.dao.CommodityClassifyDao;
import com.xyhc.cms.entity.CommodityEntity;
import com.xyhc.cms.service.CommodityClassifyService;
import com.xyhc.cms.service.CommodityService;
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
 * 商品主表Controller
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    @Autowired
    CommodityClassifyDao commodityClassifyDao;

    /**
     * 查询全部数据
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/all")
    public List<CommodityEntity> all(@RequestParam Map<String, Object> params) {
        return commodityService.all(params);
    }


    /**
     * 查询推荐
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/allIsDetailRecommend")
    public List<CommodityEntity> allIsDetailRecommend(@RequestParam Map<String, Object> params) {
        return commodityService.allIsDetailRecommend(params);
    }

    /**
     * 查询推荐详情
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/allRecommend")
    public List<CommodityEntity> allRecommend(@RequestParam Map<String, Object> params) {
        return commodityService.allRecommend(params);
    }

    /**
     * 查询限时秒杀商品
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/allTimeLimit")
    public List<CommodityEntity> allTimeLimit(@RequestParam Map<String, Object> params) {
        return commodityService.allTimeLimit(params);
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
            return commodityService.page(params);
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
    @GetMapping("/pageIsNew")
    public PageUtils pageIsNew(@RequestParam Map<String, Object> params) {
        try {
            return commodityService.pageIsNew(params);
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
    public Result detail(@RequestParam String id,String uid) {
        try {
            return Result.success(commodityService.detail(id,uid));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/detailNew")
    public Result detailNew(@RequestParam String id) {
        try {
            return Result.success(commodityService.detailNew(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/detailOne")
    public Result detailOne(@RequestParam String id) {
        try {
            return Result.success(commodityService.detailOne(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody CommodityImgDto commoditySave) {
        try {
            return commodityService.save(commoditySave);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存推荐
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveRecommend")
    public Result saveRecommend(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.saveRecommend(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 设置为新人专享
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveIsNew")
    public Result saveIsNew(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.saveIsNew(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 设置为限时秒杀
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveTimeLimit")
    public Result saveTimeLimit(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.saveTimeLimit(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 保存推荐详情
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveIsDetailRecommend")
    public Result saveIsDetailRecommend(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.saveIsDetailRecommend(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存是否上架下架
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveIsBuy")
    public Result saveIsBuy(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.saveIsBuy(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 设置为新品
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/isNewCommodity")
    public Result isNewCommodity(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.isNewCommodity(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 推荐购物车页面
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/isRecommendCart")
    public Result isRecommendCart(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.isRecommendCart(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 推荐收藏页面
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/isRecommendCollect")
    public Result isRecommendCollect(@RequestBody CommodityEntity commodity) {
        try {
            commodityService.isRecommendCollect(commodity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
    /**
     * 收藏
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveIsCollect")
    public Result saveIsCollect(@RequestBody CommodityEntity commodityEntity) {
        try {
            commodityService.saveIsCollect(commodityEntity);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 取消收藏
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/saveCloseIsCollect")
    public Result saveCloseIsCollect(@RequestBody CommodityEntity commodityEntity) {
        try {
            commodityService.saveCloseIsCollect(commodityEntity);
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
            commodityService.remove(id);
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
            commodityService.removeBatch(ids);
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
          return commodityService.upload(file);
    }

    /**
     * 导出
     *
     * @param
     */
    @ResponseBody
    @RequestMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) throws IOException {
        List<CommodityEntity> commodityEntity = new ArrayList();

        if (params != null && params.size() > 0) {
            commodityEntity = commodityService.all(params);
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");

            EasyExcel.write(response.getOutputStream(), CommodityEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())
                    .doWrite(commodityEntity);
        } else {
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("数据模板下载", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), CommodityEntity.class)
                    .sheet("sheet1")
                    .registerWriteHandler(defaultStyles())      // 初始化所有内容样式
                    .doWrite(commodityEntity);
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
