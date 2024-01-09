package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.Video;
import com.xyhc.cms.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class VideoController {
    @Autowired
    VideoService videoService;

    /**
     * 列表
     *
     * @param
     */
    @RequestMapping("/video/page")
    @CrossOrigin
    public PageUtils page(@RequestParam Map<String,Object> params) {
        try {
            return videoService.page(params);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 删除
     *
     * @param
     */
    @PostMapping("/video/remove")
    public Result remove(@RequestParam String id) {
        try {
            videoService.remove(id);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 新增，修改
     */
    @PostMapping("/video/save")
    @CrossOrigin
    public Result save(@RequestBody Video video){
        return  videoService.save(video);
    }

    /**
     * 详情
     */
    @GetMapping("/video/detail")
    @CrossOrigin
    public Result detail(@RequestParam String id){
        try {
            return Result.success(videoService.detail(id));
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    /**
     * 满足条件的全部分类
     */
    @GetMapping("/video/all")
    public Result all(@RequestParam Map<String, Object> params) {
        try {
            return videoService.all(params);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 推荐
     *
     * @param
     */
    @GetMapping("/video/recommend")
    public Result recommend(@RequestParam String id, int isRecommend) {
        try {
            videoService.recommend(id,isRecommend);
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }
}

