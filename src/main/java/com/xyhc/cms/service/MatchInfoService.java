package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.MatchInfoEntity;
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
 * 赛事主表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:42:15
 */
public interface MatchInfoService extends IService<MatchInfoEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchInfoEntity>
     */
    public List<MatchInfoEntity> all(Map<String, Object> params);


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils page(Map<String, Object> params);

    /**
     * 保存
     *
     * @param matchInfo matchInfo
     * @return boolean
     */
    public boolean save(MatchInfoEntity matchInfo);

    /**
     * 详情
     *
     * @return
     */
    public MatchInfoEntity detail(String id);

    /**
     * 推荐赛事
     *
     * @param id
     * @return boolean
     */
    public boolean recommend(String id);

    /**
     * 不推荐赛事
     *
     * @param id
     * @return Result
     */
    public boolean cancelRecommend(String id);

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
     * 自动刷新状态
     *
     * @return boolean
     */
    public boolean updateMatchStatus();
}
