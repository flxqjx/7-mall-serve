package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.ApplyContractDao;
import com.xyhc.cms.dao.MemberLevelDao;
import com.xyhc.cms.dao.MemberLevelOrderDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.ApplyContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import com.xyhc.cms.config.auth.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.utils.CommonUtitls;
import com.alibaba.excel.EasyExcel;
import com.xyhc.cms.listener.ApplyContractListener;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-05-24 15:55:30
 */
@Service("applyContractService")
public class ApplyContractServiceImpl extends ServiceImpl<ApplyContractDao, ApplyContractEntity> implements ApplyContractService {

    @Resource
    ApplyContractDao applyContractDao;
    @Resource
    MemberLevelOrderDao memberLevelOrderDao;

    @Resource
    MemberLevelDao memberLevelDao;

    @Resource
    WechatDao wechatDao;

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
    public List<ApplyContractEntity> all(Map<String, Object> params) {
        return applyContractDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<ApplyContractEntity> page = new Query<ApplyContractEntity>().getPage(params, "id", true);
        List<ApplyContractEntity> records = applyContractDao.page(page, params);
        records.forEach(item -> {
            QueryWrapper<MemberLevelEntity> query = new QueryWrapper<>();
            query.eq("id", item.getLevelId());
            query.eq("is_delete", 0);
            MemberLevelEntity memberLevelEntity = memberLevelDao.selectOne(query);
            if (memberLevelEntity != null) {
                item.setLevelName(memberLevelEntity.getName());
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param applyContract
     * @return boolean
     */
    @Override
    public Result saveContract(@RequestBody ApplyContractEntity applyContract) {
        try {
            if (StringUtils.isBlank(applyContract.getId())) {
                QueryWrapper<ApplyContractEntity> query = new QueryWrapper<>();
                query.eq("level_id", applyContract.getLevelId());
                query.eq("agreement", applyContract.getAgreement());
                query.eq("is_delete", 0);
                ApplyContractEntity applyContractEntity = applyContractDao.selectOne(query);
                if (applyContractEntity == null) {
                    applyContract.setId(commonUtitls.createKey());
                    applyContract.setCreateBy(authUtils.AuthUser().getUserId());
                    applyContract.setUid(authUtils.AuthUser().getUserId());
                    applyContract.setCreateTime(new Date());
                    super.save(applyContract);
                } else {
                    return Result.error("您选择的套餐协议类型已存在！");
                }
            } else {
                applyContract.setUpdateBy(authUtils.AuthUser().getUserId());
                applyContract.setUpdateTime(new Date());
                this.updateById(applyContract);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public ApplyContractEntity detail(String id) {
        return applyContractDao.selectById(id);
    }


    /**
     * 详情
     *
     * @return
     */
    @Override
    public ApplyContractEntity detailContract(String levelId, String isUpgrade) {
        ApplyContractEntity applyContractEntity = new ApplyContractEntity();
        QueryWrapper<ApplyContractEntity> query = new QueryWrapper<>();
        if (isUpgrade.equals("1")) {
            query.eq("level_id", levelId);
            query.eq("agreement", "TWO_DEAL");
            query.eq("is_delete", 0);
            applyContractEntity = applyContractDao.selectOne(query);
        } else {
            query.eq("level_id", levelId);
            query.eq("agreement", "TWO_DEAL");
            query.eq("is_delete", 0);
            applyContractEntity = applyContractDao.selectOne(query);
        }

//        //查询这个用户有没有上级
//        QueryWrapper<Wechat> wechatQuery = new QueryWrapper<>();
//        wechatQuery.eq("id", authUtils.AuthUser().getUserId());
//        Wechat wechat = wechatDao.selectOne(wechatQuery);
//        if(wechat!=null){
//            QueryWrapper<Wechat> pidQuery = new QueryWrapper<>();
//            pidQuery.eq("id", wechat.getParentId());
//            List<Wechat> wechatPid = wechatDao.selectList(pidQuery);
//            if(wechatPid.size()!=0){
//                if(isUpgrade.equals("1")){
//                    //如果有就取三方协议
//                    QueryWrapper<ApplyContractEntity> query = new QueryWrapper<>();
//                    query.eq("level_id", levelId);
//                    query.eq("agreement", "TWO_DEAL");
//                    query.eq("is_delete", 0);
//                    applyContractEntity = applyContractDao.selectOne(query);
//                }else {
//                    //如果有就取三方协议
//                    QueryWrapper<ApplyContractEntity> query = new QueryWrapper<>();
//                    query.eq("level_id", levelId);
//                    query.eq("agreement", "THREE_DEAL");
//                    query.eq("is_delete", 0);
//                    applyContractEntity = applyContractDao.selectOne(query);
//                }
//            }else {
//                //没有就取二方协议
//                QueryWrapper<ApplyContractEntity> query = new QueryWrapper<>();
//                query.eq("level_id", levelId);
//                query.eq("agreement", "TWO_DEAL");
//                query.eq("is_delete", 0);
//                applyContractEntity = applyContractDao.selectOne(query);
//            }
//        }
        return applyContractEntity;
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
            ApplyContractEntity removeEntity = applyContractDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            applyContractDao.updateById(removeEntity);
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
                ApplyContractEntity removeEntity = applyContractDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                applyContractDao.updateById(removeEntity);
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
            ApplyContractListener listener = new ApplyContractListener();
            EasyExcel.read(file.getInputStream(), ApplyContractEntity.class, listener).sheet().doRead();
            //获取数据
            List<ApplyContractEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                applyContractDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 查询当前用户的合同
     *
     * @return
     */
    @Override
    public ApplyContractEntity detailByUserId() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", authUtils.AuthUser().getUserId());
        queryWrapper.eq("is_pay", 1);
        queryWrapper.orderByDesc("pay_time");
        queryWrapper.eq("is_delete", 0);
        List<MemberLevelOrderEntity> memberLevelOrderEntity = memberLevelOrderDao.selectList(queryWrapper);
        if (memberLevelOrderEntity.size()>0) {
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("level_id", memberLevelOrderEntity.get(0).getLevelId());
            queryWrapper.eq("is_delete", 0);
            ApplyContractEntity applyContractEntity = applyContractDao.selectOne(queryWrapper);
            if(applyContractEntity!=null){
                MemberLevelEntity memberLevel = memberLevelDao.selectById(applyContractEntity.getLevelId());
                if(memberLevel!=null){
                    applyContractEntity.setLevelType(memberLevel.getLevelType());
                }
            }
            return applyContractEntity;
        } else {
            return null;
        }

    }
}
