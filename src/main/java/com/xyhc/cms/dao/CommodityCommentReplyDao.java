package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityCommentReplyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评论回复Dao
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Mapper
public interface CommodityCommentReplyDao extends BaseMapper<CommodityCommentReplyEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentReplyEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentReplyEntity> page(IPage<CommodityCommentReplyEntity> page, @Param("params") Map<String, Object> params);
}
