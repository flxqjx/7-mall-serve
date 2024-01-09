package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.MatchApplyVoteDao;
import com.xyhc.cms.dao.MatchApplyDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.MatchApplyVoteEntity;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.entity.XfLogEntity;
import com.xyhc.cms.service.MatchApplyVoteService;
import com.xyhc.cms.service.MatchApplyService;
import com.xyhc.cms.service.XfLogService;
import com.xyhc.cms.utils.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.util.*;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import com.xyhc.cms.config.auth.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.utils.CommonUtitls;
import com.alibaba.excel.EasyExcel;
import com.xyhc.cms.listener.MatchApplyVoteListener;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 赛事作品投票Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("matchApplyVoteService")
public class MatchApplyVoteServiceImpl extends ServiceImpl<MatchApplyVoteDao, MatchApplyVoteEntity> implements MatchApplyVoteService {

    @Resource
    MatchApplyVoteDao matchApplyVoteDao;

    @Resource
    MatchApplyDao matchApplyDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    AuthorityUtils authorityUtils;

    @Resource
    WechatDao wechatDao;
    @Resource
    XfLogService xfLogService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchApplyVoteEntity> all(Map<String, Object> params) {
        return matchApplyVoteDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MatchApplyVoteEntity> page = new Query<MatchApplyVoteEntity>().getPage(params, "id", true);
//        List<MatchApplyVoteEntity> records = matchApplyVoteDao.page(page, params);
        List<MatchApplyVoteEntity> matchApplyVote = matchApplyVoteDao.page(page, params);
//        List<Wechat> wechatList = wechatDao.all();
//        matchApplyVote.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setNickName(wechat.get().getNickName());
//            } else {
//                item.setNickName("暂无");
//            }
//
//        });
        page.setRecords(matchApplyVote);
        return new PageUtils(matchApplyVote, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 根据作品ID查询投票
     *
     * @param matchApplyId
     * @return Result
     */
    @Override
    public List<MatchApplyVoteEntity> voteByApplyId(String matchApplyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("match_apply_id", matchApplyId);

        List<MatchApplyVoteEntity> vote = matchApplyVoteDao.selectList(queryWrapper);
//        List<Wechat> wechatList = wechatDao.all();
//        vote.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setNickName(wechat.get().getNickName());
//                item.setAvatarUrl(wechat.get().getAvatarUrl());
//            } else {
//                item.setNickName("未填写");
//            }
//        });
        return vote;
    }


    /**
     * 保存
     *
     * @param matchApplyVote
     * @return boolean
     */
    @Override
    public Result saveVote(@RequestBody MatchApplyVoteEntity matchApplyVote) {
        try {
            if (StringUtils.isBlank(matchApplyVote.getId())) {
                matchApplyVote.setId(commonUtitls.createKey());
                matchApplyVote.setCreateBy(authUtils.AuthUser().getUserId());
                matchApplyVote.setUid(authUtils.AuthUser().getUserId());
                matchApplyVote.setCreateTime(new Date());
                super.save(matchApplyVote);

                if ("MONEY".equals(matchApplyVote.getPayWay())) {
                    Wechat wechat = wechatDao.selectById(matchApplyVote.getUid());
                    matchApplyVote.setIsPay(1);
                    matchApplyVote.setPayTime(new Date());
                    double afterBalance = wechat.getBalance() - matchApplyVote.getAmount();
                    if (wechat != null) {
                        wechat.setBalance(afterBalance);
                        wechatDao.updateById(wechat);
                    }
                    MatchApplyEntity detailEntity = matchApplyDao.selectById(matchApplyVote.getMatchApplyId());
                    detailEntity.setVoteNum(detailEntity.getVoteNum() + (int) (matchApplyVote.getAmount() * 1));
                    matchApplyDao.updateById(detailEntity);
                }
            } else {
                matchApplyVote.setUpdateBy(authUtils.AuthUser().getUserId());
                matchApplyVote.setUpdateTime(new Date());
                this.updateById(matchApplyVote);
            }
            return Result.success(matchApplyVote.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 更新回调
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public Result updateVotePayStatus(String orderId, String orderNo, String tradeNo) {
        try {
            MatchApplyVoteEntity matchApplyVote = matchApplyVoteDao.selectById(orderId);
            if (matchApplyVote != null) {
                if (matchApplyVote.getIsPay() == 1) {
                    return Result.success();
                }
                matchApplyVote.setIsPay(1);
                matchApplyVote.setPayTime(new Date());
                matchApplyVote.setOutTradeNo(orderNo);
                matchApplyVote.setTransactionId(tradeNo);
                matchApplyVoteDao.updateById(matchApplyVote);

                MatchApplyEntity detailEntity = matchApplyDao.selectById(matchApplyVote.getMatchApplyId());
                detailEntity.setVoteNum(detailEntity.getVoteNum() + (int) (matchApplyVote.getAmount() * 10));
                matchApplyDao.updateById(detailEntity);
            }
            return Result.success(matchApplyVote.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MatchApplyVoteEntity detail(String id) {
        return matchApplyVoteDao.selectById(id);
    }

    /**
     * 删除
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean remove(String id) {
        try {
            MatchApplyVoteEntity removeEntity = matchApplyVoteDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchApplyVoteDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(String[] ids) {

        try {
            for (String id : ids) {
                MatchApplyVoteEntity removeEntity = matchApplyVoteDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                matchApplyVoteDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 导入
     *
     * @param file
     * @return boolean
     */
    @Override
    public Result upload(MultipartFile file) throws IOException {
        try {
            MatchApplyVoteListener listener = new MatchApplyVoteListener();
            EasyExcel.read(file.getInputStream(), MatchApplyVoteEntity.class, listener).sheet().doRead();
            //获取数据
            List<MatchApplyVoteEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                matchApplyVoteDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
