package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.SysAuthTokenDao;
import com.xyhc.cms.entity.SysAuthTokenEntity;
import com.xyhc.cms.service.SysAuthTokenService;
import com.xyhc.cms.utils.CommonUtitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统登录TOKENService实现类
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
@Service("sysAuthTokenService")
public class SysAuthTokenServiceImpl extends ServiceImpl<SysAuthTokenDao, SysAuthTokenEntity> implements SysAuthTokenService {

    @Resource
    SysAuthTokenDao sysAuthTokenDao;

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
    public List<SysAuthTokenEntity> all(Map<String, Object> params) {
        return sysAuthTokenDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<SysAuthTokenEntity> page = new Query<SysAuthTokenEntity>().getPage(params, "id", true);
        List<SysAuthTokenEntity> records = sysAuthTokenDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param sysAuthToken
     * @return boolean
     */
    @Override
    public boolean save(SysAuthTokenEntity sysAuthToken) {
        try {
            if (sysAuthToken.getId() == 0) {
                super.save(sysAuthToken);
            } else {
                sysAuthToken.setUpdateTime(new Date());
                this.updateById(sysAuthToken);
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
    public SysAuthTokenEntity detail(String id) {
        return sysAuthTokenDao.selectById(id);
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
            SysAuthTokenEntity removeEntity = sysAuthTokenDao.selectById(id);
            sysAuthTokenDao.updateById(removeEntity);
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
                SysAuthTokenEntity removeEntity = sysAuthTokenDao.selectById(id);
                sysAuthTokenDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


}
