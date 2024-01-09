package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.CommodityCommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品评论Dao
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Mapper
public interface CommodityCommentDao extends BaseMapper<CommodityCommentEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 查询评价
     */
    List<CommodityCommentEntity> commentsIsReply(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<CommodityCommentEntity> page(IPage<CommodityCommentEntity> page, @Param("params") Map<String, Object> params);
}
