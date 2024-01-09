package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.ReceiveOrderDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.PromotionActivitysEntity;
import com.xyhc.cms.entity.ReceiveOrderEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.ReceiveOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.text.SimpleDateFormat;
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
import com.xyhc.cms.listener.ReceiveOrderListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-09-28 19:30:42
 */
@Service("receiveOrderService")
public class ReceiveOrderServiceImpl extends ServiceImpl<ReceiveOrderDao, ReceiveOrderEntity> implements ReceiveOrderService {

    @Resource
    ReceiveOrderDao receiveOrderDao;

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
    public List<ReceiveOrderEntity> all(Map<String, Object> params) {
        return receiveOrderDao.all(params);
    }

    /**
     * 查询我的领取记录
     *
     * @param params
     * @return Result
     */
    @Override
    public List<ReceiveOrderEntity> myOrder(Map<String, Object> params) {
        params.put("userId", authUtils.AuthUser().getUserId());
        List<ReceiveOrderEntity> list = receiveOrderDao.myOrder(params);
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        list.forEach(item -> {
            Wechat wechat = wechatDao.selectById(item.getUserId());
            if (wechat != null) {
                item.setUserName(wechat.getNickName());
                if (StringUtils.isNotEmpty(item.getProvinceId())) {
                    item.setProvinceId(addressList.get(item.getProvinceId()).toString());
                } else {
                    item.setProvinceId("无");
                }
                if (StringUtils.isNotEmpty(item.getCityId())) {
                    item.setCityId(addressList.get(item.getCityId()).toString());
                } else {
                    item.setCityId("无");
                }
                if (StringUtils.isNotEmpty(item.getAreaId())) {
                    item.setAreaId(addressList.get(item.getAreaId()).toString());
                } else {
                    item.setAreaId("无");
                }
            }
        });
        return list;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<ReceiveOrderEntity> page = new Query<ReceiveOrderEntity>().getPage(params, "create_time", false);
        List<ReceiveOrderEntity> records = receiveOrderDao.page(page, params);
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        records.forEach(item -> {
            Wechat wechat = wechatDao.selectById(item.getUserId());
            if (wechat != null) {
                item.setUserName(wechat.getNickName());
                if (StringUtils.isNotEmpty(item.getProvinceId())) {
                    item.setProvinceId(addressList.get(item.getProvinceId()).toString());
                } else {
                    item.setProvinceId("无");
                }
                if (StringUtils.isNotEmpty(item.getCityId())) {
                    item.setCityId(addressList.get(item.getCityId()).toString());
                } else {
                    item.setCityId("无");
                }
                if (StringUtils.isNotEmpty(item.getAreaId())) {
                    item.setAreaId(addressList.get(item.getAreaId()).toString());
                } else {
                    item.setAreaId("无");
                }
            }

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param receiveOrder
     * @return boolean
     */
    @Override
    public Result saveOrder(ReceiveOrderEntity receiveOrder) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", authUtils.AuthUser().getUserId());
            List<ReceiveOrderEntity> list = receiveOrderDao.myOrder(params);
            if (list.size() > 0) {
                return Result.error("您已领取，不能重复领取！");
            }
            if (StringUtils.isBlank(receiveOrder.getId())) {
                receiveOrder.setId(commonUtitls.createKey());
                receiveOrder.setCreateBy(authUtils.AuthUser().getUserId());
                receiveOrder.setCreateTime(new Date());
                receiveOrder.setUserId(authUtils.AuthUser().getUserId());
                receiveOrder.setOrderStatus("TO_DELIVER");
                super.save(receiveOrder);
            } else {
                receiveOrder.setUpdateBy(authUtils.AuthUser().getUserId());
                receiveOrder.setUpdateTime(new Date());
                this.updateById(receiveOrder);
            }
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 修改订单处理
     */
    @Override
    public boolean saveProcess(ReceiveOrderEntity receiveOrder) {
        try {
            if (StringUtils.isBlank(receiveOrder.getId())) {

            } else {
                receiveOrder.setUpdateBy(authUtils.AuthUser().getUserId());
                receiveOrder.setUpdateTime(new Date());
                receiveOrder.setOrderStatus("TO_RECEIVE");
                this.updateById(receiveOrder);
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
    public ReceiveOrderEntity detail(String id) {
        return receiveOrderDao.selectById(id);
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
            ReceiveOrderEntity removeEntity = receiveOrderDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            receiveOrderDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }

    /**
     * 查询我是否已领取
     */
    @Override
    public Result isWhetherCollect() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", authUtils.AuthUser().getUserId());
            List<ReceiveOrderEntity> list = receiveOrderDao.myOrder(params);
            Map<String, Object> repMap = new HashMap<>();
            if (list.size() > 0) {
                repMap.put("isWhetherCollect", 1);
            }else {
                repMap.put("isWhetherCollect", 0);
            }
            return Result.success(repMap);
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 确认收货
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean saveReceiving(String id) {
        try {
            ReceiveOrderEntity removeEntity = receiveOrderDao.selectById(id);
            removeEntity.setOrderStatus("RECEIVED");
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            receiveOrderDao.updateById(removeEntity);
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
                ReceiveOrderEntity removeEntity = receiveOrderDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                receiveOrderDao.updateById(removeEntity);
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
            ReceiveOrderListener listener = new ReceiveOrderListener();
            EasyExcel.read(file.getInputStream(), ReceiveOrderEntity.class, listener).sheet().doRead();
            //获取数据
            List<ReceiveOrderEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                receiveOrderDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
