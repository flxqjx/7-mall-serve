package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MsLotteryDao;
import com.xyhc.cms.dao.MsLotteryInfoDao;
import com.xyhc.cms.dao.MsLotteryUserDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.IntegralLogService;
import com.xyhc.cms.service.MsLotteryService;
import com.xyhc.cms.service.MsLotteryUserService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 赛事分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("msLotteryUserService")
public class MsLotteryUserServiceImpl extends ServiceImpl<MsLotteryUserDao, MsLotteryUserEntity> implements MsLotteryUserService {

    @Resource
    MsLotteryUserDao msLotteryUserDao;
    @Resource
    MsLotteryInfoDao msLotteryInfoDao;
    @Resource
    MsLotteryDao msLotteryDao;
    @Resource
    IntegralLogService integralLogService;
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
    public List<MsLotteryUserEntity> all(Map<String, Object> params) {
        return msLotteryUserDao.all(params);
    }

    /**
     * 查询全部用户中奖记录
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MsLotteryUserEntity> userAll(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<MsLotteryUserEntity> lotteryUserList = msLotteryUserDao.userAll(params);

        return lotteryUserList;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MsLotteryUserEntity> page = new Query<MsLotteryUserEntity>().getPage(params, "T.create_time", false);
        List<MsLotteryUserEntity> records = msLotteryUserDao.page(page, params);
        records.forEach(item -> {
            MsLotteryInfoEntity msLotteryInfo = msLotteryInfoDao.selectById(item.getLotteryInfoId());
            Wechat wechat = wechatDao.selectById(item.getUid());
            if(msLotteryInfo!=null && wechat !=null){
                if(msLotteryInfo.getType().equals("POINTS")){
                    item.setReceiveName(wechat.getNickName());
                    item.setReceiveTell(wechat.getMobile());
                }
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param msLotteryUser
     * @return boolean
     */
    @Override
    public Result saveOrder(MsLotteryUserEntity msLotteryUser) {
        try {
            Map<String, Object> map = new HashMap<>();
            MsLotteryEntity msLottery = new MsLotteryEntity();
            if(msLotteryUser.getLotteryInfoId() !=null){
                MsLotteryInfoEntity msLotteryInfo = msLotteryInfoDao.selectById(msLotteryUser.getLotteryInfoId());
                if(msLotteryInfo !=null){
                    msLottery = msLotteryDao.selectById(msLotteryInfo.getLotteryId());
                    if(msLottery !=null){
                        //开始时间结束时间
                        msLotteryUser.setStartTime(msLottery.getStartTime());
                        msLotteryUser.setEndTime(msLottery.getEndTime());
                    }
                }
            }
            if (StringUtils.isBlank(msLotteryUser.getId())) {

                msLotteryUser.setId(commonUtitls.createKey());
                msLotteryUser.setCreateBy(authUtils.AuthUser().getUserId());
                msLotteryUser.setUid(authUtils.AuthUser().getUserId());
                msLotteryUser.setCreateTime(new Date());

                if(msLotteryUser.getLotteryInfoId() !=null){
                    MsLotteryInfoEntity msLotteryInfoEntity = msLotteryInfoDao.selectById(msLotteryUser.getLotteryInfoId());
                    map.put("lotteryInfoId",msLotteryUser.getLotteryInfoId());
                    map.put("lotteryUserId",msLotteryUser.getId());
                    if(msLotteryInfoEntity !=null && msLotteryInfoEntity.getType().equals("POINTS")){
                        msLotteryUser.setIsReceive(1);
                        this.updateLotteryPoints(map);
                    }else {
                        msLotteryUser.setIsReceive(0);
                    }
                }
                super.save(msLotteryUser);
            } else {
                msLotteryUser.setUpdateBy(authUtils.AuthUser().getUserId());
                msLotteryUser.setUpdateTime(new Date());
                this.updateById(msLotteryUser);
            }
        } catch (Exception ex) {
            throw ex;
        }
//        this.updateLotteryOrderStatus(msLotteryUser.getId(),"1","2");
        return Result.success(msLotteryUser.getId());
    }

    /**
     * 更新新人专享商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public Result updateLotteryOrderStatus(String orderId, String orderNo, String tradeNo) {
        try {
            MsLotteryUserEntity msLotteryUserEntity = msLotteryUserDao.selectById(orderId);
            if (msLotteryUserEntity != null) {
                if (msLotteryUserEntity.getIsPay() == 1) {
                    return Result.success();
                }
                msLotteryUserEntity.setIsPay(1);
                msLotteryUserEntity.setPayTime(new Date());
                msLotteryUserEntity.setOutTradeNo(orderNo);
                msLotteryUserEntity.setTransactionId(tradeNo);
                msLotteryUserEntity.setIsReceive(1);
                msLotteryUserDao.updateById(msLotteryUserEntity);
            }
            return Result.success(msLotteryUserEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MsLotteryUserEntity detail(String id) {
        return msLotteryUserDao.selectById(id);
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
            MsLotteryUserEntity removeEntity = msLotteryUserDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            msLotteryUserDao.updateById(removeEntity);
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
                MsLotteryUserEntity removeEntity = msLotteryUserDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                msLotteryUserDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 奖品发货
     *
     * @return
     */
    @Override
    public void sendLottery(Map<String, Object> params) {
        try {
            Date date = new Date();
            UpdateWrapper<MsLotteryUserEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", params.get("id"));
            wrapper.set("express_company", params.get("expressCompany"));
            wrapper.set("express_no", params.get("expressNo"));
            wrapper.set("is_receive", 2);
            wrapper.set("update_time", new Date());
            msLotteryUserDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 更新抽奖积分
     *
     * @return
     */
    @Override
    public void updateLotteryPoints(Map<String, Object> params) {
        try {
            Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
            if (wechat != null) {
                //中奖积分
                MsLotteryInfoEntity lotteryInfo = msLotteryInfoDao.selectById(params.get("lotteryInfoId").toString());
                int points = wechat.getIntegralMoney() + lotteryInfo.getNum();
                UpdateWrapper<MsLotteryUserEntity> wrapperUser = new UpdateWrapper<>();
                wrapperUser.eq("id", params.get("lotteryUserId"));
                wrapperUser.set("is_receive", 1);
                wrapperUser.set("update_time", new Date());
                msLotteryUserDao.update(null, wrapperUser);
                //更新用户积分
                UpdateWrapper<Wechat> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", wechat.getId());
                wrapper.set("integral_money", points);
                wechatDao.update(null, wrapper);
                //更新log
                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance(lotteryInfo.getNum());
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance(points);
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(params.get("lotteryUserId").toString());
                xfLog.setXfClassify("LOTTERY_INTEGRAL");
                xfLog.setXfRemark("抽奖奖励积分");
                integralLogService.save(xfLog);


            }
        } catch (Exception ex) {
            throw ex;
        }
    }

}
