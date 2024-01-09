package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.vo.jijia.MatchApplyRank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 赛事申请表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface MatchApplyDao extends BaseMapper<MatchApplyEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MatchApplyEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MatchApplyEntity> page(IPage<MatchApplyEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 查询数据(matchId/matchApplyId/uid)
     *
     * @Param params 查询数据
     * @Return List
     */
    List<MatchApplyEntity> matchApplyById(@Param("params") Map<String,Object>params);

    /**
     * 查询我的作品
     *
     * @Param params 查询数据
     * @Return List
     */
    List<MatchApplyEntity> matchApplyByUser(@Param("params") Map<String,Object>params);

    /**
     * 排名
     *
     * @Param params 查询数据
     * @Return List
     */
    List<MatchApplyEntity> matchApplyRank(@Param("params") Map<String,Object>params);

    /**
     * 排名
     *
     * @Param params 查询数据
     * @Return List
     */
    List<MatchApplyRank> matchApplyRankRowNum(@Param("matchId") String matchId);
}
