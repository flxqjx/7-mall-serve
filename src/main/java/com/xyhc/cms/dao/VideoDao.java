package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoDao extends BaseMapper<Video> {

    List<Video> page(IPage<Video> page, @Param("params") Map<String, Object> params);

    List<Video> page(@Param("params") Map<String, Object> params);

}
