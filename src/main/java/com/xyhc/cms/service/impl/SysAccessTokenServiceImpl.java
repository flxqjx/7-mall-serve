package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.SysAccessTokenDao;
import com.xyhc.cms.entity.SysAccessTokenEntity;
import com.xyhc.cms.service.SysAccessTokenService;
import com.xyhc.cms.utils.CommonUtitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 微信认证TOKENService实现类
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
@Service("sysAccessTokenService")
public class SysAccessTokenServiceImpl extends ServiceImpl<SysAccessTokenDao, SysAccessTokenEntity> implements SysAccessTokenService {

    @Resource
    SysAccessTokenDao sysAccessTokenDao;

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
    public List<SysAccessTokenEntity> all(Map<String, Object> params) {
        return sysAccessTokenDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<SysAccessTokenEntity> page = new Query<SysAccessTokenEntity>().getPage(params, "id", true);
        List<SysAccessTokenEntity> records = sysAccessTokenDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param sysAccessToken
     * @return boolean
     */
    @Override
    public boolean save(SysAccessTokenEntity sysAccessToken) {
        try {
            if (sysAccessToken.getId() == 0) {
                super.save(sysAccessToken);
            } else {
                sysAccessToken.setUpdateTime(new Date());
                this.updateById(sysAccessToken);
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
    public SysAccessTokenEntity detail(String id) {
        return sysAccessTokenDao.selectById(id);
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
            SysAccessTokenEntity removeEntity = sysAccessTokenDao.selectById(id);
            sysAccessTokenDao.updateById(removeEntity);
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
                SysAccessTokenEntity removeEntity = sysAccessTokenDao.selectById(id);
                sysAccessTokenDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


}
