package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.ApplyJoinDao;
import com.xyhc.cms.dao.MemberLevelDao;
import com.xyhc.cms.dao.MemberLevelOrderDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.ApplyJoinService;
import com.xyhc.cms.service.MemberLevelService;
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
import com.xyhc.cms.listener.ApplyJoinListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-05-24 13:40:41
 */
@Service("applyJoinService")
public class ApplyJoinServiceImpl extends ServiceImpl<ApplyJoinDao, ApplyJoinEntity> implements ApplyJoinService {

    @Resource
    ApplyJoinDao applyJoinDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    MemberLevelDao memberLevelDao;

    @Resource
    MemberLevelOrderDao memberLevelOrderDao;

    @Resource
    MemberLevelService memberLevelService;

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
    public List<ApplyJoinEntity> all(Map<String, Object> params) {
        return applyJoinDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<ApplyJoinEntity> page = new Query<ApplyJoinEntity>().getPage(params, "create_time", false);
        List<ApplyJoinEntity> records = applyJoinDao.page(page, params);
        records.forEach(item -> {
            QueryWrapper<MemberLevelEntity> query = new QueryWrapper<>();
            query.eq("id", item.getLevelId());
            query.eq("is_delete", 0);
            MemberLevelEntity memberLevelEntity = memberLevelDao.selectOne(query);
            if (memberLevelEntity != null) {
                item.setLevelName(memberLevelEntity.getName());
            }
            QueryWrapper<Wechat> wechatQuery = new QueryWrapper<>();
            wechatQuery.eq("id", item.getUid());
            Wechat wechat = wechatDao.selectOne(wechatQuery);
            if (wechat != null) {
                item.setNickName(wechat.getNickName());
                item.setAvatarUrl(wechat.getAvatarUrl());
            }
            //查支付状态
            QueryWrapper<MemberLevelOrderEntity> queryOrder = new QueryWrapper<>();
            queryOrder.eq("level_id", item.getLevelId());
            queryOrder.eq("uid", item.getUid());
            queryOrder.orderByDesc("pay_time");
            queryOrder.eq("is_pay", 1);
            List<MemberLevelOrderEntity> order = memberLevelOrderDao.selectList(queryOrder);
            if (order.size() > 0) {
                item.setIsPay(order.get(0).getIsPay());
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param applyJoin
     * @return boolean
     */
    @Override
    public boolean save(ApplyJoinEntity applyJoin) {
        try {
            if (StringUtils.isBlank(applyJoin.getId())) {
                applyJoin.setId(commonUtitls.createKey());
                applyJoin.setUid(authUtils.AuthUser().getUserId());
                applyJoin.setCreateBy(authUtils.AuthUser().getUserId());
                applyJoin.setCreateTime(new Date());
                applyJoin.setApplyState("TO_APPROE");
                super.save(applyJoin);
            } else {
                applyJoin.setUpdateBy(authUtils.AuthUser().getUserId());
                applyJoin.setUpdateTime(new Date());
                this.updateById(applyJoin);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 厨师申请审核通过
     */
    @Override
    public void audit(String id) {
        try {
            UpdateWrapper<ApplyJoinEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("update_by", authUtils.AuthUser().getUserId());
            wrapper.set("update_time", new Date());
            wrapper.set("apply_state", "APPROVE");
            applyJoinDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 驳回厨师申请
     */
    @Override
    public boolean reject(ApplyJoinEntity applyJoinEntity) {
        try {
            if (StringUtils.isBlank(applyJoinEntity.getId())) {

            } else {
                applyJoinEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                applyJoinEntity.setUpdateTime(new Date());
                applyJoinEntity.setApplyState("REJECT");
                this.updateById(applyJoinEntity);
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
    public ApplyJoinEntity detail(String id) {
        return applyJoinDao.selectById(id);
    }

    /**
     * 查询用户是否申请加盟
     */
    @Override
    public ApplyJoinEntity whetherApplyJoin() {
        UpdateWrapper<ApplyJoinEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq("uid", authUtils.AuthUser().getUserId());
        ApplyJoinEntity applyJoinEntity = applyJoinDao.selectOne(wrapper);
        if (applyJoinEntity != null) {
            if (applyJoinEntity.getApplyState().equals("TO_APPROE")) {
                applyJoinEntity.setState("TO_APPROE");
            } else if (applyJoinEntity.getApplyState().equals("APPROVE")) {
                applyJoinEntity.setState("APPROVE");
                UpdateWrapper<MemberLevelOrderEntity> wrapperLevel = new UpdateWrapper<>();
                wrapperLevel.eq("uid", authUtils.AuthUser().getUserId());
                wrapperLevel.eq("is_delete", 0);
                wrapperLevel.orderByDesc("pay_time");
                wrapperLevel.eq("is_pay", 1);
                List<MemberLevelOrderEntity> memberLevelOrderEntity = memberLevelOrderDao.selectList(wrapperLevel);
                if (memberLevelOrderEntity.size() > 0) {
                    applyJoinEntity.setState("已购买");
                    MemberLevelEntity memberLevelEntity = memberLevelService.detail(memberLevelOrderEntity.get(0).getLevelId());
                    if (memberLevelEntity != null) {
                        if (memberLevelEntity.getLevelType().equals("A")) {
                            applyJoinEntity.setShare("分享");
                            applyJoinEntity.setLevelId(memberLevelEntity.getId());
                        } else {
                            applyJoinEntity.setShare("");
                        }
                    }
                } else {
                    applyJoinEntity.setState("APPROVE");
                }
            } else if (applyJoinEntity.getApplyState().equals("REJECT")) {
                applyJoinEntity.setState("REJECT");
            }
        } else {
            applyJoinEntity.setState("0");
        }
        return applyJoinEntity;
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
            ApplyJoinEntity removeEntity = applyJoinDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            applyJoinDao.updateById(removeEntity);
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
                ApplyJoinEntity removeEntity = applyJoinDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                applyJoinDao.updateById(removeEntity);
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
            ApplyJoinListener listener = new ApplyJoinListener();
            EasyExcel.read(file.getInputStream(), ApplyJoinEntity.class, listener).sheet().doRead();
            //获取数据
            List<ApplyJoinEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                applyJoinDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
