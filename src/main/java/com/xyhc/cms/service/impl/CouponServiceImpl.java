package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityService;
import com.xyhc.cms.service.CouponService;
import com.xyhc.cms.service.CouponUsersService;
import com.xyhc.cms.service.impl.CouponUsersServiceImpl;
import com.xyhc.cms.utils.DictUtils;
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
import com.xyhc.cms.listener.CouponListener;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

/**
 * 平台优惠券Service实现类
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponDao, CouponEntity> implements CouponService {

    @Resource
    CouponDao couponDao;

    @Resource
    CommodityDao commodityDao;

    @Resource
    CouponUsersDao couponUsersDao;

    @Resource
    CommodityOrderDao commodityOrderDao;

    @Resource
    WechatDao wechatDao;
    @Resource
    CouponReceiveDao couponReceiveDao;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    DictUtils dictUtils;

    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    CommodityService commodityService;

    @Autowired
    CouponUsersService CouponUsers;


    @Autowired
    CouponUsersServiceImpl CouponUsersimpl;


    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CouponEntity> all(Map<String, Object> params) {
        List<CouponEntity> records = couponDao.all(params);
        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");
        Map<String, Object> couponTimeTypeList = dictUtils.dictList("couponTimeType");
        Map<String, Object> couponClassList = dictUtils.dictList("couponClass");
        List<Wechat> wechatList = wechatDao.all(params);
        records.forEach(item -> {
            if (item.getCouponType() != null) {
                if (couponTypeList.get(item.getCouponType()) != null) {
                    item.setCouponType(couponTypeList.get(item.getCouponType()).toString());
                }
            }
            if (item.getCouponTimeType() != null) {
                if (couponTimeTypeList.get(item.getCouponTimeType()) != null) {
                    item.setCouponTimeType(couponTimeTypeList.get(item.getCouponTimeType()).toString());
                }
            }
            if (item.getCouponClass() != null) {
                if (couponClassList.get(item.getCouponClass()) != null) {
                    item.setCouponClass(couponClassList.get(item.getCouponClass()).toString());
                }
            }
            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
            if (wechat.isPresent()) {
                item.setUid(wechat.get().getNickName());
            } else {
                item.setUid("暂无");
            }
            CommodityEntity commodityEntity = commodityDao.commodityDetail(item.getCommodityId());
            if (commodityEntity != null) {
                item.setCommodityId(commodityEntity.getCommodityName());
            }
        });
        return records;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        Map<String, Object> maps = new HashMap<>();
        IPage<CouponEntity> page = new Query<CouponEntity>().getPage(params, "id", true);
        List<CouponEntity> records = couponDao.page(page, params);
        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");
        Map<String, Object> couponTimeTypeList = dictUtils.dictList("couponTimeType");
        Map<String, Object> couponClassList = dictUtils.dictList("couponClass");
        List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.allByUid(maps);
        List<Wechat> wechatList = wechatDao.all(params);
        List<CouponReceiveEntity> couponReceiveList = couponReceiveDao.all(params);
        records.forEach(item -> {
            //查询当前用户是否下过单
            List<CommodityOrderEntity> orderList = commodityOrderList.stream().filter(a -> a.getIsDelete() == 0).collect(Collectors.toList());
            if (orderList.size() > 0) {
                if (item.getCouponType().equals("NEW")) {
                    item.setIsNewPeople(0);//新人
                } else {
                    item.setIsNewPeople(1);
                }
            } else {
                item.setIsNewPeople(1);
            }

            //查询当前用户是否使用优惠券
            List<CouponReceiveEntity> receiveUseList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getIsUse() == 1).collect(Collectors.toList());
            if (receiveUseList.size() > 0) {
                item.setIsUse(1);//已使用
            } else {
                item.setIsUse(0);
            }

            //查询当前用户是否领取优惠券
            List<CouponReceiveEntity> receiveList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getUid().equals(authUtils.AuthUser().getUserId())).collect(Collectors.toList());
            if (receiveList.size() > 0) {
                item.setIsReceive(1);//已领取
            } else {
                item.setIsReceive(0);
            }

            if (item.getCouponType() != null) {
                if (couponTypeList.get(item.getCouponType()) != null) {
                    item.setCouponType(couponTypeList.get(item.getCouponType()).toString());
                }
            }
            if (item.getCouponTimeType() != null) {
                if (couponTimeTypeList.get(item.getCouponTimeType()) != null) {
                    item.setCouponTimeType(couponTimeTypeList.get(item.getCouponTimeType()).toString());
                }
            }
            if (item.getCouponClass() != null) {
                if (couponClassList.get(item.getCouponClass()) != null) {
                    item.setCouponClass(couponClassList.get(item.getCouponClass()).toString());
                }
            }

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 小程序分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils appPage(Map<String, Object> params) {
        Map<String, Object> maps = new HashMap<>();
        IPage<CouponEntity> page = new Query<CouponEntity>().getPage(params, "id", true);
        List<CouponEntity> records = couponDao.appPage(page, params);
        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");
        Map<String, Object> couponTimeTypeList = dictUtils.dictList("couponTimeType");
        Map<String, Object> couponClassList = dictUtils.dictList("couponClass");
        List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.allByUid(maps);
        List<Wechat> wechatList = wechatDao.all(params);
        List<CouponReceiveEntity> couponReceiveList = couponReceiveDao.all(params);
        records.forEach(item -> {
            //查询当前用户是否下过单
            List<CommodityOrderEntity> orderList = commodityOrderList.stream().filter(a -> a.getIsDelete() == 0).collect(Collectors.toList());
            if (orderList.size() > 0) {
                if (item.getCouponType().equals("NEW")) {
                    item.setIsNewPeople(0);//新人
                } else {
                    item.setIsNewPeople(1);
                }
            } else {
                item.setIsNewPeople(1);
            }

            //查询当前用户是否使用优惠券
            List<CouponReceiveEntity> receiveUseList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getIsUse() == 1).collect(Collectors.toList());
            if (receiveUseList.size() > 0) {
                item.setIsUse(1);//已使用
            } else {
                item.setIsUse(0);
            }

            //查询当前用户是否领取优惠券
            List<CouponReceiveEntity> receiveList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getUid().equals(authUtils.AuthUser().getUserId())).collect(Collectors.toList());
            if (receiveList.size() > 0) {
                item.setIsReceive(1);//已领取
            } else {
                item.setIsReceive(0);
            }

            if (item.getCouponType() != null) {
                if (couponTypeList.get(item.getCouponType()) != null) {
                    item.setCouponType(couponTypeList.get(item.getCouponType()).toString());
                }
            }
            if (item.getCouponTimeType() != null) {
                if (couponTimeTypeList.get(item.getCouponTimeType()) != null) {
                    item.setCouponTimeType(couponTimeTypeList.get(item.getCouponTimeType()).toString());
                }
            }
            if (item.getCouponClass() != null) {
                if (couponClassList.get(item.getCouponClass()) != null) {
                    item.setCouponClass(couponClassList.get(item.getCouponClass()).toString());
                }
            }

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 小程序分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils appPageByPoints(Map<String, Object> params) {
        Map<String, Object> maps = new HashMap<>();
        IPage<CouponEntity> page = new Query<CouponEntity>().getPage(params, "id", true);
        List<CouponEntity> records = couponDao.appPageByPoints(page, params);
        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");
        Map<String, Object> couponTimeTypeList = dictUtils.dictList("couponTimeType");
        Map<String, Object> couponClassList = dictUtils.dictList("couponClass");
        List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.allByUid(maps);
        List<Wechat> wechatList = wechatDao.all(params);
        List<CouponReceiveEntity> couponReceiveList = couponReceiveDao.all(params);
        records.forEach(item -> {
            //查询当前用户是否下过单
            List<CommodityOrderEntity> orderList = commodityOrderList.stream().filter(a -> a.getIsDelete() == 0).collect(Collectors.toList());
            if (orderList.size() > 0) {
                if (item.getCouponType().equals("NEW")) {
                    item.setIsNewPeople(0);//新人
                } else {
                    item.setIsNewPeople(1);
                }
            } else {
                item.setIsNewPeople(1);
            }

            //查询当前用户是否使用优惠券
            List<CouponReceiveEntity> receiveUseList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getIsUse() == 1).collect(Collectors.toList());
            if (receiveUseList.size() > 0) {
                item.setIsUse(1);//已使用
            } else {
                item.setIsUse(0);
            }

            //查询当前用户是否领取优惠券
            List<CouponReceiveEntity> receiveList = couponReceiveList.stream().filter(a -> a.getIsDelete() == 0 && a.getCouponId().equals(item.getId()) && a.getUid().equals(authUtils.AuthUser().getUserId())).collect(Collectors.toList());
            if (receiveList.size() > 0) {
                item.setIsReceive(1);//已领取
            } else {
                item.setIsReceive(0);
            }

            if (item.getCouponType() != null) {
                if (couponTypeList.get(item.getCouponType()) != null) {
                    item.setCouponType(couponTypeList.get(item.getCouponType()).toString());
                }
            }
            if (item.getCouponTimeType() != null) {
                if (couponTimeTypeList.get(item.getCouponTimeType()) != null) {
                    item.setCouponTimeType(couponTimeTypeList.get(item.getCouponTimeType()).toString());
                }
            }
            if (item.getCouponClass() != null) {
                if (couponClassList.get(item.getCouponClass()) != null) {
                    item.setCouponClass(couponClassList.get(item.getCouponClass()).toString());
                }
            }

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param coupon
     * @return boolean
     */
    @Override
    public boolean save(CouponEntity coupon) {
        try {
            if (StringUtils.isBlank(coupon.getId())) {
                coupon.setId(commonUtitls.createKey());
                coupon.setIntegralcodeuser(commonUtitls.createKey());
                coupon.setCreateBy(authUtils.AuthUser().getUserId());
                coupon.setCreateTime(new Date());
                super.save(coupon);
            } else {
                coupon.setUpdateBy(authUtils.AuthUser().getUserId());
                coupon.setUpdateTime(new Date());
                this.updateById(coupon);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 详情
     *
     * @return3334444
     */
    @Override
    public CouponEntity detail(String id) {

        CouponEntity list = (CouponEntity) couponDao.selectById(id);
        //list.setIntegralCode(commonUtitls.createKey());

        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");

        QueryWrapper level = new QueryWrapper<>();
        level.eq("coupon_id", id);
        level.eq("is_delete", 0);
        level.eq("receiveuid", authUtils.AuthUser().getUserId());//领取优惠券的用户ID有可能为空，也就是没领取

        CouponUsersEntity couponuser = couponUsersDao.selectOne(level);

        if (couponuser != null) {
            //sales(memberLevel.getId(),orderResult);
            list.setCouponUsersMainId(couponuser.getId());
            list.setReceiveuid(couponuser.getReceiveUid().toString());
            list.setSource(couponuser.getSource().toString());
            list.setIsUse(couponuser.getIsUse());
            list.setIntegralcodeuser(couponuser.getIntegralCodeUser());

            if (list.getCouponType() != null) {
                if (couponTypeList.get(list.getCouponType()) != null) {
                    list.setCouponType(couponTypeList.get(list.getCouponType()).toString());
                }
            }
        } else {
            list.setCouponUsersMainId("");
            list.setReceiveuid("");
            list.setSource("");
            list.setIsUse(0);
            //list.setIntegralcodeuser("请领券后可生成券码");
            list.setIntegralcodeuser(commonUtitls.createKey());
        }

        return list;
        //return couponDao.selectById(id);
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
            CouponEntity removeEntity = couponDao.selectById(id);
            removeEntity.setIsDelete(1);
//            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            couponDao.updateById(removeEntity);
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
                CouponEntity removeEntity = couponDao.selectById(id);
                removeEntity.setIsDelete(1);
//                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                couponDao.updateById(removeEntity);
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
            CouponListener listener = new CouponListener();
            EasyExcel.read(file.getInputStream(), CouponEntity.class, listener).sheet().doRead();
            //获取数据
            List<CouponEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                couponDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
