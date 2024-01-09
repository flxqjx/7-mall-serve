package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.UserLevelOrderDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.UserLevelOrderService;
import com.xyhc.cms.service.UserLevelService;
import com.xyhc.cms.service.UserService;
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
import com.xyhc.cms.listener.UserLevelOrderListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
@Service("userLevelOrderService")
public class UserLevelOrderServiceImpl extends ServiceImpl<UserLevelOrderDao, UserLevelOrderEntity> implements UserLevelOrderService {

    @Resource
    UserLevelOrderDao  userLevelOrderDao;

    @Resource
    UserLevelService userLevelService;

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
    public List<UserLevelOrderEntity> all(Map<String, Object> params) {
        return userLevelOrderDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<UserLevelOrderEntity> page = new Query<UserLevelOrderEntity>().getPage(params, "id", true);
        List<UserLevelOrderEntity> records = userLevelOrderDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  userLevelOrder
     * @return boolean
     */
    @Override
    public boolean save(UserLevelOrderEntity userLevelOrder) {
        try {
            if (StringUtils.isBlank(userLevelOrder.getId())){
                userLevelOrder.setId(commonUtitls.createKey());
                userLevelOrder.setCreateBy(authUtils.AuthUser().getUserId());
                userLevelOrder.setCreateTime(new Date());
                super.save(userLevelOrder);
            }else{
                userLevelOrder.setUpdateBy(authUtils.AuthUser().getUserId());
                userLevelOrder.setUpdateTime(new Date());
                this.updateById(userLevelOrder);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存会员级别
     *
     * @param  userLevelOrder
     * @return boolean
     */
    @Override
    public boolean saveOrder(UserLevelOrderEntity userLevelOrder) {
        try {
            if (StringUtils.isBlank(userLevelOrder.getId())){
                userLevelOrder.setId(commonUtitls.createKey());
                userLevelOrder.setCreateBy(authUtils.AuthUser().getUserId());
                userLevelOrder.setUserId(authUtils.AuthUser().getUserId());
                //保存订单的时候把is_pay的状态直接改成1
                userLevelOrder.setIsPay(1);
                userLevelOrder.setCreateTime(new Date());
                super.save(userLevelOrder);

            }else{

            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 根据状态查询记录
     *
     * @param
     * @return Result
     */
    @Override
    public UserLevelEntity userLevel() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("is_pay", 1);
        queryWrapper.eq("user_id", authUtils.AuthUser().getUserId());
        List<UserLevelOrderEntity> records = userLevelOrderDao.selectList(queryWrapper);

        UserLevelEntity userLevel = userLevelService.detail(records.get(0).getLevelId());



        return userLevel;
    }
    /**
     * 详情
     *
     * @return
     */
    @Override
    public UserLevelOrderEntity detail(String id) {
        return userLevelOrderDao.selectById(id);
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
            UserLevelOrderEntity removeEntity = userLevelOrderDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            userLevelOrderDao.updateById(removeEntity);
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
                UserLevelOrderEntity removeEntity = userLevelOrderDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                userLevelOrderDao.updateById(removeEntity);
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
            UserLevelOrderListener listener = new UserLevelOrderListener();
            EasyExcel.read(file.getInputStream(), UserLevelOrderEntity.class, listener).sheet().doRead();
            //获取数据
            List<UserLevelOrderEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                userLevelOrderDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
