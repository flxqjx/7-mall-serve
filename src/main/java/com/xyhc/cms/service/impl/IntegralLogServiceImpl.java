package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.IntegralLogDao;
import com.xyhc.cms.entity.IntegralLogEntity;
import com.xyhc.cms.service.IntegralLogService;
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
import com.xyhc.cms.listener.IntegralLogListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-08-17 15:26:30
 */
@Service("integralLogService")
public class IntegralLogServiceImpl extends ServiceImpl<IntegralLogDao, IntegralLogEntity> implements IntegralLogService {

    @Resource
    IntegralLogDao  integralLogDao;

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
    public List<IntegralLogEntity> all(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<IntegralLogEntity> records = integralLogDao.all(params);
        records.forEach(items->{
            if (items.getXfClassify().equals("POINTS_PAY") ) {
                items.setXfType("-");
            }
            else if (items.getXfClassify().equals("POINTPRODUCT_SALE")) {
                items.setXfType("-");
            }
            else if (items.getXfClassify().equals("POINTS_COUPON")) {
                items.setXfType("-");
            }
            else {
                items.setXfType("+");
            }
        });
        return records;
    }

    /**
     * 后台查询用户全部数据
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pcAllByUid(Map<String, Object> params) {
        params.put("uid", params.get("uid"));
        IPage<IntegralLogEntity> page = new Query<IntegralLogEntity>().getPage(params, "id", true);
        List<IntegralLogEntity> records = integralLogDao.page(page, params);
        records.forEach(items->{
            if (items.getXfClassify().equals("POINTS_PAY") ) {
                items.setXfType("-");
            }
            else if (items.getXfClassify().equals("POINTPRODUCT_SALE")) {
                items.setXfType("-");
            }
            else if (items.getXfClassify().equals("POINTS_COUPON")) {
                items.setXfType("-");
            }
            else {
                items.setXfType("+");
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<IntegralLogEntity> page = new Query<IntegralLogEntity>().getPage(params, "id", true);
        List<IntegralLogEntity> records = integralLogDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  integralLog
     * @return boolean
     */
    @Override
    public boolean save(IntegralLogEntity integralLog) {
        try {
            if (StringUtils.isBlank(integralLog.getId())){
                integralLog.setId(commonUtitls.createKey());
                integralLog.setCreateBy(authUtils.AuthUser().getUserId());
                integralLog.setCreateTime(new Date());
                super.save(integralLog);
            }else{
                integralLog.setUpdateBy(authUtils.AuthUser().getUserId());
                integralLog.setUpdateTime(new Date());
                this.updateById(integralLog);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }
    /**
     * 保存
     *
     * @param  integralLog
     * @return boolean
     */
    @Override
    public boolean saveInternal(IntegralLogEntity integralLog) {
        try {
            if (StringUtils.isBlank(integralLog.getId())){
                integralLog.setId(commonUtitls.createKey());
                integralLog.setCreateTime(new Date());
                super.save(integralLog);
            }else{
                integralLog.setUpdateTime(new Date());
                this.updateById(integralLog);
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
    public IntegralLogEntity detail(String id) {
        return integralLogDao.selectById(id);
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
            IntegralLogEntity removeEntity = integralLogDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            integralLogDao.updateById(removeEntity);
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
                IntegralLogEntity removeEntity = integralLogDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                integralLogDao.updateById(removeEntity);
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
            IntegralLogListener listener = new IntegralLogListener();
            EasyExcel.read(file.getInputStream(), IntegralLogEntity.class, listener).sheet().doRead();
            //获取数据
            List<IntegralLogEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                integralLogDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
