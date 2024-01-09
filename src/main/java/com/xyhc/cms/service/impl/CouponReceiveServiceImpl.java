package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.CommodityImgDao;
import com.xyhc.cms.dao.CouponReceiveDao;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.entity.CouponReceiveEntity;
import com.xyhc.cms.listener.CommodityImgListener;
import com.xyhc.cms.service.CommodityImgService;
import com.xyhc.cms.service.CouponReceiveService;
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
 * 商品图片Service实现类
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
@Service("couponReceiveService")
public class CouponReceiveServiceImpl extends ServiceImpl<CouponReceiveDao, CouponReceiveEntity> implements CouponReceiveService {

    @Resource
    CouponReceiveDao couponReceiveDao;

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
    public List<CouponReceiveEntity> all(Map<String, Object> params) {
        return couponReceiveDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CouponReceiveEntity> page = new Query<CouponReceiveEntity>().getPage(params, "id", true);
        List<CouponReceiveEntity> records = couponReceiveDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param couponReceive
     * @return boolean
     */
    @Override
    public boolean save(CouponReceiveEntity couponReceive) {
        try {
            if (StringUtils.isBlank(couponReceive.getId())) {
                couponReceive.setId(commonUtitls.createKey());
                couponReceive.setCreateTime(new Date());
                couponReceive.setUid(authUtils.AuthUser().getUserId());
                couponReceive.setCreateBy(authUtils.AuthUser().getUserId());
                super.save(couponReceive);
            } else {
                couponReceive.setUpdateTime(new Date());
                couponReceive.setIsDelete(0);
                this.updateById(couponReceive);
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
    public CouponReceiveEntity detail(String id) {
        return couponReceiveDao.selectById(id);
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
            CouponReceiveEntity removeEntity = couponReceiveDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            couponReceiveDao.updateById(removeEntity);
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
                CouponReceiveEntity removeEntity = couponReceiveDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                couponReceiveDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 查询我的优惠券
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CouponReceiveEntity> myCoupon(Map<String, Object> params) {

        params.put("userId", authUtils.AuthUser().getUserId());
        return couponReceiveDao.myCoupon(params);
    }

}
