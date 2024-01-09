package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.entity.MatchCollectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 赛事收藏表Dao
 *
 * @author apollo
 * @since 2023-03-10 12:04:37
 */
@Mapper
public interface MatchCollectDao extends BaseMapper<MatchCollectEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MatchCollectEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MatchCollectEntity> page(IPage<MatchCollectEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 查询数据(matchId/matchApplyId/uid)
     *
     * @Param params 查询数据
     * @Return List
     */
    List<MatchCollectEntity> matchCollectByMatch(@Param("params") Map<String,Object>params);

//    /**
//     * 查询我的收藏
//     *
//     * @Param params 查询数据
//     * @Return List
//     */
//    List<MatchApplyEntity> matchCollectByMatch(@Param("params") Map<String,Object>params);
}
