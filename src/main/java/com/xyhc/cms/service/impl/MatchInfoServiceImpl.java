package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.MatchInfoDao;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.entity.MatchInfoEntity;
import com.xyhc.cms.service.MatchClassifyService;
import com.xyhc.cms.service.MatchInfoService;
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
import com.xyhc.cms.listener.MatchInfoListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 赛事主表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:42:15
 */
@Service("matchInfoService")
public class MatchInfoServiceImpl extends ServiceImpl<MatchInfoDao, MatchInfoEntity> implements MatchInfoService {

    @Resource
    MatchInfoDao matchInfoDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    MatchClassifyService matchClassifyService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MatchInfoEntity> all(Map<String, Object> params) {
        return matchInfoDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MatchInfoEntity> page = new Query<MatchInfoEntity>().getPage(params, "id", true);
        List<MatchInfoEntity> records = matchInfoDao.page(page, params);

        // 取全部分类
        Map<String, Object> map = new HashMap<>();
        List<MatchClassifyEntity> classifyEntityList = matchClassifyService.all(map);
        records.forEach(item -> {
            Optional<MatchClassifyEntity> classifyEntity = classifyEntityList.stream().filter(s -> s.getId().equals(item.getClassifyId())).findFirst();
            if (classifyEntity.isPresent()) {
                item.setClassifyId(classifyEntity.get().getClassifyName());
            }
        });

        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param matchInfo
     * @return boolean
     */
    @Override
    public boolean save(MatchInfoEntity matchInfo) {
        try {
            if (StringUtils.isBlank(matchInfo.getId())) {
                matchInfo.setId(commonUtitls.createKey());
                matchInfo.setCreateBy(authUtils.AuthUser().getUserId());
                matchInfo.setCreateTime(new Date());
                super.save(matchInfo);
            } else {
                matchInfo.setUpdateBy(authUtils.AuthUser().getUserId());
                matchInfo.setUpdateTime(new Date());
                this.updateById(matchInfo);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MatchInfoEntity detail(String id) {
        return matchInfoDao.selectById(id);
    }

    /**
     * 推荐赛事
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean recommend(String id) {
        try {
            MatchInfoEntity recommendEntity = matchInfoDao.selectById(id);
            recommendEntity.setIsRecommend(1);
            recommendEntity.setUpdateTime(new Date());
            recommendEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchInfoDao.updateById(recommendEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 不推荐赛事
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean cancelRecommend(String id) {
        try {
            MatchInfoEntity recommendEntity = matchInfoDao.selectById(id);
            recommendEntity.setIsRecommend(0);
            recommendEntity.setUpdateTime(new Date());
            recommendEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchInfoDao.updateById(recommendEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
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
            MatchInfoEntity removeEntity = matchInfoDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchInfoDao.updateById(removeEntity);
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
                MatchInfoEntity removeEntity = matchInfoDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                matchInfoDao.updateById(removeEntity);
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
            MatchInfoListener listener = new MatchInfoListener();
            EasyExcel.read(file.getInputStream(), MatchInfoEntity.class, listener).sheet().doRead();
            //获取数据
            List<MatchInfoEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                matchInfoDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 自动刷新状态
     *
     * @return boolean
     */
    @Override
    public boolean updateMatchStatus() {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("is_delete", 0);
            List<MatchInfoEntity> matchInfoEntities = matchInfoDao.selectList(queryWrapper);
            matchInfoEntities.forEach(match -> {
                if (match.getBeginDate().getTime() > new Date().getTime()) {
                    match.setMatchStatus("TODO");
                }
                if (match.getBeginDate().getTime() < new Date().getTime() && new Date().getTime() < match.getEndDate().getTime()) {
                    match.setMatchStatus("DOING");
                }
                if (match.getEndDate().getTime() < new Date().getTime()) {
                    match.setMatchStatus("DONE");
                }
            });

            this.updateBatchById(matchInfoEntities);

        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }
}
