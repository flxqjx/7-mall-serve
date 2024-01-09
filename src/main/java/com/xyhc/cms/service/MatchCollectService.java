package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.entity.MatchCollectEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 赛事收藏表Service接口
 *
 * @author apollo
 * @since 2023-03-10 12:04:37
 */
public interface MatchCollectService extends IService<MatchCollectEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchCollectEntity>
     */
    public List<MatchCollectEntity> all(Map<String, Object> params);


    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils page(Map<String, Object> params);

    /**
     * 查询数据(matchId/matchApplyId/uid)
     *
     * @param  params
     * @return List<MatchCollectEntity>
     */
    public List<MatchCollectEntity> matchCollectByMatch(Map<String,Object> params);

//    /**
//     * 查询我的收藏
//     *
//     * @param  params
//     * @return List<MatchCollectEntity>
//     */
//    public List<MatchCollectEntity> matchCollectByMatch(Map<String,Object> params);

    /**
     * 保存
     *
     * @param  matchCollect matchCollect
     * @return boolean
     */
    public Result saveMatchCollect(MatchCollectEntity matchCollect);

    /**
    * 详情
    *
    * @return
    */
    public MatchCollectEntity detail(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    public boolean removeBatch(String[] ids);

    /**
      * 导入
      *
      * @param file
      * @return boolean
      */
    public Result upload(MultipartFile file) throws IOException;

    /**
     * 查询是否收藏此赛事
     *
     * @param matchId
     * @return boolean
     */
    public MatchCollectEntity isCollectByMatchId(String matchId) ;
}
