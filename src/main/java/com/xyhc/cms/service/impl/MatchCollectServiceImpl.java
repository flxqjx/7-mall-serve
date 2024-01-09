package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.MatchCollectDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.listener.CommodityClassifyListener;
import com.xyhc.cms.service.MatchCollectService;
import com.xyhc.cms.service.MatchInfoService;
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
import com.xyhc.cms.listener.MatchCollectListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 赛事收藏表Service实现类
 *
 * @author apollo
 * @since 2023-03-10 12:04:37
 */
@Service("matchCollectService")
public class MatchCollectServiceImpl extends ServiceImpl<MatchCollectDao, MatchCollectEntity> implements MatchCollectService {

    @Resource
    MatchCollectDao  matchCollectDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    MatchInfoService matchInfoService;

    @Autowired
    AuthorityUtils authorityUtils;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;
    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchCollectEntity> all(Map<String, Object> params) {
        return matchCollectDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MatchCollectEntity> page = new Query<MatchCollectEntity>().getPage(params, "id", true);
        List<MatchCollectEntity> records = matchCollectDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 查询数据(matchId/matchApplyId/uid)
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchCollectEntity> matchCollectByMatch(Map<String, Object> params) {
        params.put("uid",authUtils.AuthUser().getUserId());
        List<MatchCollectEntity> matchCollect = matchCollectDao.matchCollectByMatch(params);
//        List<Wechat> wechatList = wechatDao.all();
//        matchCollect.forEach(item -> {
//            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
//            if (wechat.isPresent()) {
//                item.setUid(wechat.get().getNickName());
//            }
//
//            MatchInfoEntity matchInfoEntity = matchInfoService.detail(item.getMatchId());
//            if (matchInfoEntity != null) {
//                item.setMatchName(matchInfoEntity.getName());
//                item.setMatchAddress(matchInfoEntity.getMatchAddress());
//                item.setBeginDate(matchInfoEntity.getBeginDate());
//                item.setEndDate(matchInfoEntity.getEndDate());
//                item.setImgurl(matchInfoEntity.getImgurl());
//
//            }
//
//
//        });
        return matchCollect;

    }

    /**
     * 保存
     *
     * @param  matchCollect
     * @return boolean
     */
    @Override
    public Result saveMatchCollect(MatchCollectEntity matchCollect) {


        try {

            if (StringUtils.isBlank(matchCollect.getId())){
                matchCollect.setId(commonUtitls.createKey());
                matchCollect.setCreateBy(authUtils.AuthUser().getUserId());
                matchCollect.setUid(authUtils.AuthUser().getUserId());
                matchCollect.setCreateTime(new Date());
                super.save(matchCollect);
            }else{
                matchCollect.setUpdateBy(authUtils.AuthUser().getUserId());
                matchCollect.setUpdateTime(new Date());
                this.updateById(matchCollect);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }




    }

//    /**
//     * 检查是否已收藏
//     *
//     * @param matchId
//     * @return boolean
//     */
//    @Override
//    public boolean checkMatchCollect(String matchId) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("match_id", matchId);
//        queryWrapper.eq("uid", authUtils.AuthUser().getUserId());
//        List<String> statusList = new ArrayList<>();
//        statusList.add("SAVED");
//        queryWrapper.in("task_status", statusList);
//        List<MatchCollectEntity> applyList = MatchCollectDao.selectList(queryWrapper);
//        if (applyList.size() > 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MatchCollectEntity detail(String id) {
        return matchCollectDao.selectById(id);
    }


    /**
     * 查询是否收藏此赛事
     *
     * @return
     */
    @Override
    public MatchCollectEntity isCollectByMatchId(String matchId) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("match_id", matchId);
        queryWrapper.eq("uid", authUtils.AuthUser().getUserId());
        queryWrapper.eq("is_delete", 0);
        return matchCollectDao.selectOne(queryWrapper);
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
            MatchCollectEntity removeEntity = matchCollectDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchCollectDao.updateById(removeEntity);
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
                MatchCollectEntity removeEntity = matchCollectDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                matchCollectDao.updateById(removeEntity);
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
            MatchCollectListener listener = new MatchCollectListener();
            EasyExcel.read(file.getInputStream(), MatchCollectEntity.class, listener).sheet().doRead();
            //获取数据
            List<MatchCollectEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                matchCollectDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
