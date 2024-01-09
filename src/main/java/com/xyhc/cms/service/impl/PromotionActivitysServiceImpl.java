package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MsLotteryDao;
import com.xyhc.cms.dao.PromotionActivitysDao;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.PromotionActivitysEntity;
import com.xyhc.cms.service.MsLotteryService;
import com.xyhc.cms.service.PromotionActivitysService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("promotionActivitysService")
public class PromotionActivitysServiceImpl extends ServiceImpl<PromotionActivitysDao, PromotionActivitysEntity> implements PromotionActivitysService {

    @Resource
    PromotionActivitysDao promotionActivitysDao;

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
    public List<PromotionActivitysEntity> all(Map<String, Object> params) {
        return promotionActivitysDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PromotionActivitysEntity> page = new Query<PromotionActivitysEntity>().getPage(params, "create_time", false);
        List<PromotionActivitysEntity> records = promotionActivitysDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  promotionActivitys
     * @return boolean
     */
    @Override
    public boolean save(PromotionActivitysEntity promotionActivitys) {
        try {
            if (StringUtils.isBlank(promotionActivitys.getId())){
                promotionActivitys.setId(commonUtitls.createKey());
                promotionActivitys.setCreateBy(authUtils.AuthUser().getUserId());
                promotionActivitys.setCreateTime(new Date());
                super.save(promotionActivitys);
            }else{
                promotionActivitys.setUpdateBy(authUtils.AuthUser().getUserId());
                promotionActivitys.setUpdateTime(new Date());
                this.updateById(promotionActivitys);
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
    public PromotionActivitysEntity detail(String id) {
        return promotionActivitysDao.selectById(id);
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
            PromotionActivitysEntity removeEntity = promotionActivitysDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            promotionActivitysDao.updateById(removeEntity);
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
                PromotionActivitysEntity removeEntity = promotionActivitysDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                promotionActivitysDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 开启
     *
     * @return
     */
    @Override
    public Result open(String id) {
        try {
            //查询是否有促销活动开启
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("is_open", 1);
            List<PromotionActivitysEntity> promotionActivitysList = promotionActivitysDao.selectList(queryWrapper);
            if(promotionActivitysList.size()>0){
                return Result.error("请先把其他促销活动关闭哦");
            }

            Date date = new Date();
            UpdateWrapper<PromotionActivitysEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("is_open", 1);
            wrapper.set("update_time", new Date());
            promotionActivitysDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
        return Result.success();
    }

    /**
     * 关闭
     *
     * @return
     */
    @Override
    public void cancelOpen(String id) {
        try {
            Date date = new Date();
            UpdateWrapper<PromotionActivitysEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("is_open", 0);
            wrapper.set("update_time", new Date());
            promotionActivitysDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
