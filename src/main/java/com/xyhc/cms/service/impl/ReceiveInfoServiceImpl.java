package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.ReceiveInfoDao;
import com.xyhc.cms.entity.ReceiveInfoEntity;
import com.xyhc.cms.service.ReceiveInfoService;
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
import com.xyhc.cms.listener.ReceiveInfoListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 收货地址表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("receiveInfoService")
public class ReceiveInfoServiceImpl extends ServiceImpl<ReceiveInfoDao, ReceiveInfoEntity> implements ReceiveInfoService {

    @Resource
    ReceiveInfoDao  receiveInfoDao;

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
    public List<ReceiveInfoEntity> all(Map<String, Object> params) {
        return receiveInfoDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("userId", authUtils.AuthUser().getUserId());
        IPage<ReceiveInfoEntity> page = new Query<ReceiveInfoEntity>().getPage(params, "id", true);
        List<ReceiveInfoEntity> records = receiveInfoDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  receiveInfo
     * @return boolean
     */
    @Override
    public boolean save(ReceiveInfoEntity receiveInfo) {
        try {
            UpdateWrapper<ReceiveInfoEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id",authUtils.AuthUser().getUserId());
            wrapper.set("is_default",0);
            receiveInfoDao.update(null,wrapper);

            if (StringUtils.isBlank(receiveInfo.getId())){
                receiveInfo.setId(commonUtitls.createKey());
                receiveInfo.setUserId(authUtils.AuthUser().getUserId());
                receiveInfo.setCreateTime(new Date());
                super.save(receiveInfo);
            }else{
                receiveInfo.setUpdateTime(new Date());
                this.updateById(receiveInfo);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存默认
     *
     * @param  receiveInfo
     * @return boolean
     */
    @Override
    public boolean saveDefault(ReceiveInfoEntity receiveInfo) {
        try {
            UpdateWrapper<ReceiveInfoEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id",receiveInfo.getUserId());
            wrapper.set("is_default",0);
            receiveInfoDao.update(null,wrapper);
            UpdateWrapper<ReceiveInfoEntity> wrapperDefault = new UpdateWrapper<>();
            wrapper.eq("user_id",receiveInfo.getUserId());
            wrapperDefault.eq("id",receiveInfo.getId());
            wrapperDefault.set("is_default",1);
            receiveInfoDao.update(null,wrapperDefault);
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
    public ReceiveInfoEntity detail(String id) {
        return receiveInfoDao.selectById(id);
    }

    /**
     * 获取我的默认地址
     *
     * @return
     */
    @Override
    public ReceiveInfoEntity defaultAddress() {
        QueryWrapper<ReceiveInfoEntity> query = new QueryWrapper<>();
        query.eq("user_id",authUtils.AuthUser().getUserId());
        query.eq("is_default",1);
        return receiveInfoDao.selectOne(query);
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
            ReceiveInfoEntity removeEntity = receiveInfoDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            receiveInfoDao.updateById(removeEntity);
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
                ReceiveInfoEntity removeEntity = receiveInfoDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                receiveInfoDao.updateById(removeEntity);
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
            ReceiveInfoListener listener = new ReceiveInfoListener();
            EasyExcel.read(file.getInputStream(), ReceiveInfoEntity.class, listener).sheet().doRead();
            //获取数据
            List<ReceiveInfoEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                receiveInfoDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
