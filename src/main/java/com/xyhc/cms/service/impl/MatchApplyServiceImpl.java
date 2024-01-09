package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.MatchApplyDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.CommodityClassifyEntity;
import com.xyhc.cms.entity.MatchApplyEntity;
import com.xyhc.cms.entity.MatchInfoEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.MatchApplyService;
import com.xyhc.cms.service.MatchInfoService;
import com.xyhc.cms.utils.AuthorityUtils;
import com.xyhc.cms.vo.jijia.MatchApplyRank;
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
import com.xyhc.cms.listener.MatchApplyListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 赛事申请表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("matchApplyService")
public class MatchApplyServiceImpl extends ServiceImpl<MatchApplyDao, MatchApplyEntity> implements MatchApplyService {

    @Resource
    MatchApplyDao matchApplyDao;

    @Resource
    WechatDao wechatDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;
    @Autowired
    AuthorityUtils authorityUtils;

    @Resource
    MatchInfoService matchInfoService;

    @Resource
    MatchApplyService matchApplyService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchApplyEntity> all(Map<String, Object> params) {
        return matchApplyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MatchApplyEntity> page = new Query<MatchApplyEntity>().getPage(params, "id", true);
        List<MatchApplyEntity> records = matchApplyDao.page(page, params);
//        List<MatchApplyEntity> matchApply = matchApplyDao.matchApplyById(params);
//        List<Wechat> wechatList = wechatDao.all();
//        records.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setUid(wechat.get().getNickName());
//            } else {
//                item.setUid("暂无");
//            }
//
//        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param matchApply
     * @return boolean
     */
    @Override
    public boolean save(MatchApplyEntity matchApply) {
        try {
            if (StringUtils.isBlank(matchApply.getId())) {
                matchApply.setId(commonUtitls.createKey());
                matchApply.setCreateBy(authUtils.AuthUser().getUserId());
                matchApply.setUid(authUtils.AuthUser().getUserId());
                matchApply.setCreateTime(new Date());
                super.save(matchApply);
            } else {
                matchApply.setUpdateBy(authUtils.AuthUser().getUserId());
                matchApply.setUpdateTime(new Date());
                super.updateById(matchApply);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 查询数据(matchId/matchApplyId/uid)
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchApplyEntity> matchApplyById(Map<String, Object> params) {
        List<MatchApplyEntity> matchApply = matchApplyDao.matchApplyById(params);
//        List<Wechat> wechatList = wechatDao.all();
//        matchApply.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setNickName(wechat.get().getNickName());
//                item.setAvatarUrl(wechat.get().getAvatarUrl());
//            } else {
//                item.setNickName("暂无");
//            }
//
//            MatchInfoEntity matchInfoEntity = matchInfoService.detail(item.getMatchId());
//            if (matchInfoEntity != null) {
//                item.setMatchName(matchInfoEntity.getName());
//                item.setBeginDate(matchInfoEntity.getBeginDate());
//                item.setEndDate(matchInfoEntity.getEndDate());
//            }
//
//
//        });
        return matchApply;

    }

    /**
     * 查询我的作品
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchApplyEntity> matchApplyByUser(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<MatchApplyEntity> matchApply = matchApplyDao.matchApplyByUser(params);
//        List<Wechat> wechatList = wechatDao.all();
//        matchApply.forEach(item -> {
//
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
////            item.setUid(wechat.get().getNickName());
//            if (wechat.isPresent()) {
//                item.setUid(wechat.get().getNickName());
//            } else {
//                item.setUid("暂无");
//            }
//
//            List<MatchApplyRank> matchApplyRanks = matchApplyDao.matchApplyRankRowNum(item.getMatchId());
////            item.setUid(wechat.get().getNickName());
//            Optional<MatchApplyRank> matchApplyRank = matchApplyRanks.stream().filter(s -> s.getUid() == authUtils.AuthUser().getUserId()).findFirst();
//            if (matchApplyRank.isPresent()) {
//                item.setRankNum(matchApplyRank.get().getRowNum());
//                item.setUid(authUtils.AuthUser().getUserId());
//            } else {
//                item.setRankNum(matchApplyRanks.size());
//            }
//
//            MatchInfoEntity matchInfoEntity = matchInfoService.detail(item.getMatchId());
////            item.setUid(wechat.get().getNickName());
//            if (matchInfoEntity != null) {
//                item.setMatchName(matchInfoEntity.getName());
//                item.setBeginDate(matchInfoEntity.getBeginDate());
//                item.setEndDate(matchInfoEntity.getEndDate());
//            }
//
//
//        });
        return matchApply;

    }

    /**
     * 排名
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchApplyEntity> matchApplyRank(Map<String, Object> params) {
        List<MatchApplyEntity> matchApply = matchApplyDao.matchApplyRank(params);
//        List<Wechat> wechatList = wechatDao.all();
//        matchApply.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setNickName(wechat.get().getNickName());
//                item.setAvatarUrl(wechat.get().getAvatarUrl());
//            } else {
//                item.setNickName("暂无");
//            }
//
//            MatchInfoEntity matchInfoEntity = matchInfoService.detail(item.getMatchId());
//            if (matchInfoEntity != null) {
//                item.setMatchName(matchInfoEntity.getName());
//                item.setBeginDate(matchInfoEntity.getBeginDate());
//                item.setEndDate(matchInfoEntity.getEndDate());
//            }
//
//
//        });
        return matchApply;

    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MatchApplyEntity detail(String id) {
        return matchApplyDao.selectById(id);
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
            MatchApplyEntity removeEntity = matchApplyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchApplyDao.updateById(removeEntity);
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
                MatchApplyEntity removeEntity = matchApplyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                matchApplyDao.updateById(removeEntity);
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
            MatchApplyListener listener = new MatchApplyListener();
            EasyExcel.read(file.getInputStream(), MatchApplyEntity.class, listener).sheet().doRead();
            //获取数据
            List<MatchApplyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                matchApplyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     *
     *
     * @param
     * @return boolean
     */

}
