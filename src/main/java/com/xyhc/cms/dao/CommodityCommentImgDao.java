package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityCommentImgEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评论图片Dao
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Mapper
public interface CommodityCommentImgDao extends BaseMapper<CommodityCommentImgEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentImgEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentImgEntity> page(IPage<CommodityCommentImgEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentImgEntity> imgPage(IPage<CommodityCommentImgEntity> page, @Param("params") Map<String, Object> params);

}
