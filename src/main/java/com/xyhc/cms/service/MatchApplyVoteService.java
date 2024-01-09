package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.MatchApplyVoteEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.entity.XfLogEntity;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 赛事作品投票Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface MatchApplyVoteService extends IService<MatchApplyVoteEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchApplyVoteEntity>
     */
    public List<MatchApplyVoteEntity> all(Map<String, Object> params);


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
     * @param matchApplyVote matchApplyVote
     * @return
     */
    public Result saveVote(MatchApplyVoteEntity matchApplyVote);

    /**
     * 详情
     *
     * @return
     */
    public MatchApplyVoteEntity detail(String id);

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
     * 根据作品ID查询投票
     *
     * @param matchApplyId
     * @return Result
     */
    public List<MatchApplyVoteEntity> voteByApplyId(String matchApplyId);

    /**
     * 更新回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updateVotePayStatus(String orderId, String orderNo, String tradeNo);
}
