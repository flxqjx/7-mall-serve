package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.VideoDao;
import com.xyhc.cms.entity.Video;
import com.xyhc.cms.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    VideoDao videoDao;

    @Autowired
    AuthUtils authUtils;

    /**
     * 列表
     *
     * @param
     * @return
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<Video> page = new Query<Video>().getPage(params, "create_time", false);
        List<Video> records = videoDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 新增 保存
     *
     * @param
     * @return
     */
    @Override
    public Result save(Video video) {
        try {
            if (StringUtils.isBlank(video.getId())) {
                video.setId(createNo());
                video.setUid(authUtils.AuthUser().getUserId());
                video.setCreateBy(authUtils.AuthUser().getUserId());
                video.setCreateTime(new Date());
                videoDao.insert(video);
            }else{
                video.setUpdateBy(authUtils.AuthUser().getUserId());
                video.setUpdateTime(new Date());
                videoDao.updateById(video);
            }
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @Override
    public void remove(String id) {
        String str = "";
        try {
            Date date = new Date();
            Video video = videoDao.selectById(id);
            video.setIsDelete(1);
            video.setUpdateTime(date);
            video.setUpdateBy(authUtils.AuthUser().getUserId());
            videoDao.updateById(video);
            str = "删除成功";
        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * 详情
     *
     * @return
     */
    @Override
    public Video detail(String id) {
        return videoDao.selectById(id);
    }


    /**
     * 查询所有满足条件分类
     *
     * @return
     */
    @Override
    public Result all(Map<String, Object> params) {
        try {
            params.put("isRecommend", params.get("isRecommend"));
            List<Video> list = videoDao.page(params);
            return new Result(200,"成功",list);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    /**
     * 首页推荐
     *
     * @param
     * @return
     */
    @Override
    public void recommend(String id,int isRecommend) {
        String str = "";
        try {
            Date date = new Date();
            Video video = videoDao.selectById(id);
            video.setUpdateTime(date);
            video.setUpdateBy(authUtils.AuthUser().getUserId());
            videoDao.updateById(video);

            str = "成功";
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 生成单号
     *
     * @return
     */
    public String createNo() {
        try {
            int rannum = (int) (1 + Math.random() * (99 - 1 + 1));
            String ranNumStr = String.format("%02d", rannum);
            String strPre = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            return strPre + ranNumStr + String.format("%04d", (maxNo() + 1));
        } catch (Exception e) {
            return "";
        }
    }





    /**
     * 获取数据库中最大 编号
     *
     * @return
     */
    public long maxNo() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            List<Video> scheduleList = videoDao.selectList(new QueryWrapper<Video>()
                    .gt("create_time", minDate).lt("create_time", maxDate));
            return scheduleList.stream().count();
        } catch (Exception e) {
            return 1;
        }
    }

}
