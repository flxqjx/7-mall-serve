package com.xyhc.cms.service;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.Video;

import java.util.Map;

public interface VideoService {
    PageUtils page(Map<String,Object> params);

    Result save(Video video);

    public void remove(String id);

    Video detail(String id);

    public Result all(Map<String, Object> params);

    public void recommend(String id,int isRecommend);
}
