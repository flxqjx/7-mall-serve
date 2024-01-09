package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MsBankDao;
import com.xyhc.cms.entity.MsBankEntity;
import com.xyhc.cms.listener.MsBankListener;
import com.xyhc.cms.service.MsBankService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-02-28 18:10:04
 */
@Service("msBankService")
public class MsBankServiceImpl extends ServiceImpl<MsBankDao, MsBankEntity> implements MsBankService {

    @Resource
    MsBankDao msBankDao;

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
    public List<MsBankEntity> all(Map<String, Object> params) {
        return msBankDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils bankList(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        IPage<MsBankEntity> page = new Query<MsBankEntity>().getPage(params, "create_time", false);
        List<MsBankEntity> records = msBankDao.bankList(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  msBank
     * @return boolean
     */
    @Override
    public boolean saveBank(MsBankEntity msBank) {
        try {
            if (StringUtils.isBlank(msBank.getId())){
                msBank.setId(commonUtitls.createKey());
                msBank.setCreateBy(authUtils.AuthUser().getUserId());
                msBank.setUid(authUtils.AuthUser().getUserId());
                msBank.setCreateTime(new Date());
                super.save(msBank);
            }else{
                msBank.setUpdateBy(authUtils.AuthUser().getUserId());
                msBank.setUpdateTime(new Date());
                this.updateById(msBank);
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
    public MsBankEntity detail(String id) {
        return msBankDao.selectById(id);
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
            MsBankEntity removeEntity = msBankDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            msBankDao.updateById(removeEntity);
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
                MsBankEntity removeEntity = msBankDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                msBankDao.updateById(removeEntity);
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
            MsBankListener listener = new MsBankListener();
            EasyExcel.read(file.getInputStream(), MsBankEntity.class, listener).sheet().doRead();
            //获取数据
            List<MsBankEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                msBankDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
