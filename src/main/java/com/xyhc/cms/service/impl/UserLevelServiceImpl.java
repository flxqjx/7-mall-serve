package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.UserLevelDao;
import com.xyhc.cms.entity.UserLevelEntity;
import com.xyhc.cms.service.UserLevelService;
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
import com.xyhc.cms.listener.UserLevelListener;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
/**
 * 商学院分类Service实现类
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
@Service("userLevelService")
public class UserLevelServiceImpl extends ServiceImpl<UserLevelDao, UserLevelEntity> implements UserLevelService {

    @Resource
    UserLevelDao  userLevelDao;

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
    public List<UserLevelEntity> all(Map<String, Object> params) {
        List<UserLevelEntity> userLevelAll= userLevelDao.all(params);
        List<UserLevelEntity> userLevel = userLevelAll.stream().filter(s-> !s.getId().equals("43be38e6-27c4-4c49-ac75-fa47a90e7a01") ).collect(Collectors.toList());

        return userLevel;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<UserLevelEntity> page = new Query<UserLevelEntity>().getPage(params, "id", true);
        List<UserLevelEntity> records = userLevelDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  userLevel
     * @return boolean
     */
    @Override
    public boolean save(UserLevelEntity userLevel) {
        try {
            if (StringUtils.isBlank(userLevel.getId())){
                userLevel.setId(commonUtitls.createKey());
                userLevel.setCreateBy(authUtils.AuthUser().getUserId());
                userLevel.setCreateTime(new Date());
                super.save(userLevel);
            }else{
                userLevel.setUpdateBy(authUtils.AuthUser().getUserId());
                userLevel.setUpdateTime(new Date());
                this.updateById(userLevel);
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
    public UserLevelEntity detail(String id) {
        return userLevelDao.selectById(id);
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
            UserLevelEntity removeEntity = userLevelDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            userLevelDao.updateById(removeEntity);
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
                UserLevelEntity removeEntity = userLevelDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                userLevelDao.updateById(removeEntity);
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
            UserLevelListener listener = new UserLevelListener();
            EasyExcel.read(file.getInputStream(), UserLevelEntity.class, listener).sheet().doRead();
            //获取数据
            List<UserLevelEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                userLevelDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
