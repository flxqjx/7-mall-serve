package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Dao
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Mapper
public interface MsLotteryInfoDao extends BaseMapper<MsLotteryInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MsLotteryInfoEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MsLotteryInfoEntity> page(IPage<MsLotteryInfoEntity> page, @Param("params") Map<String, Object> params);
}
