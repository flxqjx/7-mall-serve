package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.*;
import com.xyhc.cms.utils.CommonUtitls;
import com.xyhc.cms.vo.commodity.OrderTjVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Service("xfLogService")
public class XfLogServiceImpl extends ServiceImpl<XfLogDao, XfLogEntity> implements XfLogService {

    @Resource
    XfLogDao xfLogDao;

    @Resource
    CommodityDao commodityDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Resource
    UserService userService;

    @Resource
    WechatService wechatService;

    @Resource
    WechatDao wechatDao;

    @Resource
    CommodityOrderCommodityDao commodityOrderCommodityDao;
    @Resource
    IntegralLogDao integralLogDao;

    @Resource
    CommodityOrderDao commodityOrderDao;

    @Resource
    XfLogService xfLogService;

    @Resource
    CommoditySpecService commoditySpecService;

    @Resource
    CommodityService commodityService;

    @Resource
    UserDao userDao;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<XfLogEntity> all(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<XfLogEntity> records = xfLogDao.all(params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {
            UserEntity user = userService.detail(xfLog.getUserId());
            if (user != null) {
                xfLog.setUserName(user.getAccount());
                xfLog.setMobile(user.getMobile());
            }
            if (xfLog.getXfClassify().equals("CASH_OUT")) {
                xfLog.setType('-');
            } else {
                xfLog.setType('+');
            }
        });
        return records;
    }

    /**
     * 已到账
     *
     * @param params
     * @return Result
     */
    @Override
    public List<XfLogEntity> receivedLog(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<XfLogEntity> records = xfLogDao.receivedLog(params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {
            UserEntity user = userService.detail(xfLog.getUserId());
            if (user != null) {
                xfLog.setUserName(user.getAccount());
                xfLog.setMobile(user.getMobile());
            }
            if (xfLog.getXfClassify().equals("CASH_OUT") || xfLog.getXfClassify().equals("CASH_OUT")) {
                xfLog.setXfType("-");
            } else {
                xfLog.setXfType("+");
            }
        });
        return records;
    }

    /**
     * 购物金明细
     *
     * @param params
     * @return Result
     */
    @Override
    public List<XfLogEntity> shoppingMoneyall(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        List<XfLogEntity> records = xfLogDao.shoppingMoneyall(params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {
            UserEntity user = userService.detail(xfLog.getUserId());
            if (user != null) {
                xfLog.setUserName(user.getAccount());
                xfLog.setMobile(user.getMobile());
            }
            if (xfLog.getXfClassify().equals("DISCOUNT")) {
                xfLog.setXfType("-");
            } else if (xfLog.getXfClassify().equals("MEMBER_AWARD")) {
                xfLog.setXfType("+");
            }else if (xfLog.getXfClassify().equals("DISCOUNT_REFUND")) {
                xfLog.setXfType("+");
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
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "create_time", false);
        List<XfLogEntity> records = xfLogDao.page(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {
            UserEntity user = userService.detail(xfLog.getUserId());
            if (user != null) {
                xfLog.setUserName(user.getAccount());
                xfLog.setMobile(user.getMobile());
            }
//            if(xfLog.getXfClassify().equals("CASH_OUT")){
//                xfLog.setType('-');
//            }else {
//                xfLog.setType('+');
//            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils balanceDetail(Map<String, Object> params) {
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "create_time", false);
        List<XfLogEntity> records = xfLogDao.balanceDetail(page, params);
        //再查提现这个人的姓名和电话
        List<Wechat> wechatList = wechatDao.all(params);
        records.forEach(xfLog -> {
            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(xfLog.getCreateBy())).findFirst();
            if (wechat.isPresent()) {
                xfLog.setNickName(wechat.get().getNickName());
                xfLog.setAvatarUrl(wechat.get().getAvatarUrl());
            } else {
                xfLog.setNickName("暂无");
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 订单收入订单列表
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils orderRevenue(Map<String, Object> params) {
        params.put("userId", authUtils.AuthUser().getUserId());
        params.put("orderStatus", params.get("orderStatus"));
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "create_time", false);
        List<XfLogEntity> records = xfLogDao.orderRevenue(page, params);
        //查询订单详情
        records.forEach(xfLog -> {
            QueryWrapper<CommodityOrderEntity> queryOrder = new QueryWrapper<>();
            queryOrder.eq("id", xfLog.getOrderId());
            CommodityOrderEntity commodityOrder = commodityOrderDao.selectOne(queryOrder);
            xfLog.setOrderForm(commodityOrder);
            if (commodityOrder != null) {
                //查询一条订单里面的多个商品 （一对多）
                QueryWrapper<CommodityOrderCommodityEntity> query = new QueryWrapper<>();
                query.eq("order_id", commodityOrder.getId());
                List<CommodityOrderCommodityEntity> orderList = commodityOrderCommodityDao.selectList(query);
                commodityOrder.setOrderList(orderList);
                //获取规格图片和规格名称
                orderList.forEach(orderItem -> {
                    CommoditySpecEntity commoditySpecEntity = commoditySpecService.detail(orderItem.getSpecId());
                    if (commoditySpecEntity != null) {
                        orderItem.setSpecName(commoditySpecEntity.getSpecName());
                        orderItem.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
                    }
                    if (orderItem.getCommodityId() != null && !orderItem.getCommodityId().equals("")) {
                        CommodityEntity commodityEntity = commodityDao.commodityDetail(orderItem.getCommodityId());
                        if (commodityEntity != null) {
                            orderItem.setCommodityName(commodityEntity.getCommodityName());
                            orderItem.setCommodityImgurl(commodityEntity.getCommodityMainImgurl());
                        }
                    }
                });
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询
     * 查询充值记录
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils payPage(Map<String, Object> params) {
        params.put("status", params.get("status"));
        params.put("userId", authUtils.AuthUser().getUserId());
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "create_time", false);
        List<XfLogEntity> records = xfLogDao.payPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public XfLogEntity detail(String id) {
        return xfLogDao.selectById(id);
    }

    /**
     * 查询已到账的
     *
     * @return
     */
    @Override
    public XfLogEntity collectedBalances() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_pay", 1);
        List<CommodityOrderEntity> orderList = commodityOrderDao.selectList(queryWrapper);
        if (orderList.size() > 0) {
            orderList.forEach(items -> {
                if (!("REFUNDED".equals(items.getOrderStatus()))) {
                    if (items.getPayTime() != null) {
                        if (new Date().getTime() - items.getPayTime().getTime() > 10 * 24 * 3600 * 1000) {
                            QueryWrapper<XfLogEntity> queryLog = new QueryWrapper<>();
                            queryLog.eq("order_id", items.getId());
                            queryLog.eq("xf_account", 0);
                            XfLogEntity xfLog = xfLogDao.selectOne(queryLog);
                            if (xfLog != null) {
                                UpdateWrapper<XfLogEntity> xfLogMoney = new UpdateWrapper<>();
                                xfLogMoney.eq("id", xfLog.getId());
                                xfLogMoney.set("collected_balances", xfLog.getXfBalance()); //已到账余额
                                xfLogMoney.set("xf_account", 1);
                                xfLogService.update(null, xfLogMoney);
                            }
                        }
                    }

                } else if ("REFUNDED".equals(items.getOrderStatus())) {
                    QueryWrapper<XfLogEntity> queryLog = new QueryWrapper<>();
                    queryLog.eq("order_id", items.getId());
                    queryLog.eq("xf_account", 0);
                    XfLogEntity xfLog = xfLogDao.selectOne(queryLog);
                    if (xfLog != null) {
                        UpdateWrapper<XfLogEntity> xfLogMoney = new UpdateWrapper<>();
                        xfLogMoney.eq("id", xfLog.getId());
                        xfLogMoney.set("refund_total", xfLog.getXfBalance()); //已到账余额
                        xfLogMoney.set("xf_account", 2);
                        xfLogService.update(null, xfLogMoney);
                    }
                }
            });
        }

//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("user_id", authUtils.AuthUser().getUserId());
//        queryWrapper.eq("xf_account", 0);
//        List<XfLogEntity> xfLogList = xfLogDao.selectList(queryWrapper);
//        XfLogEntity xfLogEntity = new XfLogEntity();
//        xfLogList.forEach(item -> {
//            QueryWrapper<CommodityOrderEntity> queryOrder = new QueryWrapper<>();
//            queryOrder.eq("id", item.getOrderId());
//            CommodityOrderEntity commodityOrder = commodityOrderDao.selectOne(queryOrder);
//            if (commodityOrder != null) {
//                if (!("APPLY_REFUND".equals(commodityOrder.getOrderStatus()))) {
//                    if (new Date().getTime() - commodityOrder.getPayTime().getTime() > 15 * 24 * 3600 * 1000) {
//                        QueryWrapper<XfLogEntity> xfLog = new QueryWrapper<>();
//                        xfLog.eq("order_id", commodityOrder.getId());
//                        XfLogEntity xfLogOrder = xfLogDao.selectOne(xfLog);
//
//                        UpdateWrapper<XfLogEntity> xfLogMoney = new UpdateWrapper<>();
//                        xfLogMoney.eq("id", xfLogOrder.getId());
//                        xfLogMoney.set("collected_balances", xfLogOrder.getXfBalance());
//                        xfLogMoney.set("xf_account", 1);
//                        xfLogService.update(null, xfLogMoney);
//                    }
//                } else if ("APPLY_REFUND".equals(commodityOrder.getOrderStatus())) {
//                    QueryWrapper<XfLogEntity> xfLog = new QueryWrapper<>();
//                    xfLog.eq("order_id", commodityOrder.getId());
//                    XfLogEntity xfLogOrder = xfLogDao.selectOne(xfLog);
//                    xfLogEntity.setRefundTotal(xfLogEntity.getRefundTotal() + xfLogOrder.getXfBalance());
//                }
//            }
//        });
        return null;
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
            XfLogEntity xfLogEntity = xfLogDao.selectById(id);
            xfLogEntity.setIsDelete(1);
            xfLogEntity.setUpdateTime(new Date());
            xfLogEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            xfLogDao.updateById(xfLogEntity);
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
                XfLogEntity xfLogEntity = xfLogDao.selectById(id);
                xfLogEntity.setIsDelete(1);
                xfLogEntity.setUpdateTime(new Date());
                xfLogEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                xfLogDao.updateById(xfLogEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存充值金额
     *
     * @param
     * @return boolean
     */
    @Override
    public Result saveWithdrawMoney(@RequestBody XfLogEntity xfLogEntity) {
        try {
            XfLogEntity xfLog = new XfLogEntity();
            //查询当前人
//                Wechat wechat = wechatService.getById(authUtils.AuthUser().getUserId());
            Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
            if (wechat != null) {
                //金钱转换成能量石（一块钱等于x个能量石）
                double xfBalance = xfLogEntity.getXfBalance() * xfLogEntity.getEnergy();
                //查变更前的金额
                double beforeBalane = wechat.getBalance();
                //查变更后的金额
                double afterBalance = wechat.getBalance() + xfBalance;
                //用户总金额
                xfLog.setXfBalance(xfBalance);
                xfLog.setBeforeBalane(beforeBalane);
                xfLog.setAfterBalance(afterBalance);
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setStatus("10");
                xfLog.setXfRemark("用户充值能量石" + xfBalance);
                xfLogService.save(xfLog);
//                    //更新用户提现之后的总金额
//                    if("20".equals(xfLog.getStatus())){
//                        Wechat wechatTotalBalance = wechatDao.selectById(wechat.getId());
//                        wechatTotalBalance.setBalance(afterBalance);
//                        wechatDao.updateById(wechatTotalBalance);
//                    }
            }
            return Result.success(xfLog.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存
     *
     * @param xfLogEntity
     * @return boolean
     */
    @Override
    public boolean save(XfLogEntity xfLogEntity) {
        try {
            if (StringUtils.isBlank(xfLogEntity.getId())) {
                xfLogEntity.setId(commonUtitls.createKey());
//                xfLogEntity.setCreateBy(authUtils.AuthUser().getUserId());
                xfLogEntity.setCreateTime(new Date());
                super.save(xfLogEntity);
            } else {
                xfLogEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                xfLogEntity.setUpdateTime(new Date());
                xfLogEntity.setStatus("20");
                this.updateById(xfLogEntity);
                XfLogEntity xfLog = xfLogService.detail(xfLogEntity.getId());
//                XfLogEntity xfLog = xfLogDao.selectById(xfLogEntity.getId());
                if (xfLog != null) {
                    if (xfLog.getStatus().equals("20")) {
                        //更新用户提现之后的总金额
                        Wechat wechat = wechatDao.selectById(xfLogEntity.getUserId());
                        //查变更后的金额
                        double afterBalance = wechat.getBalance() + xfLogEntity.getXfBalance();
                        if (wechat != null) {
                            Wechat wechatTotalBalance = wechatDao.selectById(wechat.getId());
                            wechatTotalBalance.setBalance(afterBalance);
                            wechatDao.updateById(wechatTotalBalance);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 更新支付
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public boolean updatePay(String orderId) {
        try {
            XfLogEntity xfLog = xfLogService.detail(orderId);
            xfLog.setStatus("20");
            this.updateById(xfLog);

            if (xfLog != null) {
                if (xfLog.getStatus().equals("20")) {
                    //更新用户提现之后的总金额
                    Wechat wechat = wechatDao.selectById(xfLog.getUserId());
                    //查变更后的金额
                    double afterBalance = wechat.getBalance() + xfLog.getXfBalance();
                    if (wechat != null) {
                        Wechat wechatTotalBalance = wechatDao.selectById(wechat.getId());
                        wechatTotalBalance.setBalance(afterBalance);
                        wechatDao.updateById(wechatTotalBalance);
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 根据id更新提现金额的审核状态
     *
     * @return
     */
    @Override
    public void saveAuditStatus(String id) {
        try {
            Date date = new Date();
            UpdateWrapper<XfLogEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("is_pass", "1");
            xfLogDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 驳回提现
     *
     * @param params
     * @return boolean
     */
    @Override
    public void reject(Map<String, Object> params) {
        try {
            Date date = new Date();
            UpdateWrapper<XfLogEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", params.get("id"));
            wrapper.set("is_pass", "-1");
            wrapper.set("reject_remark", params.get("rejectRemark"));
            xfLogDao.update(null, wrapper);

            XfLogEntity msCashOutEntity = detail(params.get("id").toString());
            UserEntity user = userService.getById(msCashOutEntity.getUserId());

            double xfBalance = msCashOutEntity.getXfBalance();
            //查变更前的金额
            double beforeBalane = user.getBalance();
            //查变更后的金额
            double afterBalance = user.getBalance() + xfBalance;


            //更新用户提现之后的总金额
            UpdateWrapper<UserEntity> wrapperTotalBalance = new UpdateWrapper<>();
            wrapperTotalBalance.eq("id", user.getId());
            wrapperTotalBalance.set("balance", afterBalance);
            userDao.update(null, wrapperTotalBalance);


            //更新当前人余额日志
            XfLogEntity xfLog = new XfLogEntity();
            xfLog.setCollectedBalances(xfBalance);
            xfLog.setXfBalance(xfBalance);
            xfLog.setBeforeBalane(user.getBalance());
            xfLog.setAfterBalance(afterBalance);
            xfLog.setCreateBy(user.getId());
            xfLog.setUserId(user.getId());
            xfLog.setXfClassify("CASH_OUT_REJECT");
            xfLog.setXfRemark("余额提现驳回");
            xfLogService.save(xfLog);


        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 处理未到账
     *
     * @param
     */
    @Override
    public void notCompleteBalance() {
        // 处理未到账
        List<XfLogEntity> notCompleteBalanceList = xfLogDao.notCompleteBalance();
        notCompleteBalanceList.forEach(item -> {
            item.setXfAccount(1);
            item.setCollectedBalances(item.getXfBalance());
            xfLogDao.updateById(item);
        });
//        // 处理积分未到账
//        List<IntegralLogEntity> notCompleteIntegralList = integralLogDao.notCompleteIntegral();
//        notCompleteIntegralList.forEach(item -> {
//            item.setXfAccount(1);
//            item.setCollectedIntegral(item.getXfBalance());
//            integralLogDao.updateById(item);
//
//            Wechat wechat = wechatDao.selectById(item.getUserId());
//
//            //用户购买B会员送积分
//            UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//            wrapperIntegralMoney.eq("id", wechat.getId());
//            wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + item.getXfBalance());
//            wechatDao.update(null, wrapperIntegralMoney);
//
//        });
    }


    /**
     * 购物金使用明细
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils deducPage(Map<String, Object> params) {
        IPage<OrderTjVo> page = new Query<OrderTjVo>().getPage(params, "T.create_time", false);
        List<OrderTjVo> records = xfLogDao.deducPage(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 团队业绩明细
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils teamYjPage(Map<String, Object> params) {
        IPage<OrderTjVo> page = new Query<OrderTjVo>().getPage(params, "O.create_time", false);
        List<OrderTjVo> records = xfLogDao.teamYjPage(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 用户分佣明细
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils userDetailPage(Map<String, Object> params) {
        IPage<OrderTjVo> page = new Query<OrderTjVo>().getPage(params, "T.create_time", false);
        List<OrderTjVo> records = xfLogDao.userDetailPage(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询 根据订单ID查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils balanceDetailByOrderId(Map<String, Object> params) {
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "T.create_time", false);
        List<XfLogEntity> records = xfLogDao.balanceDetailByOrderId(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 已到账明细
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils balanceWithdrawnLogByUserId(Map<String, Object> params) {
        IPage<XfLogEntity> page = new Query<XfLogEntity>().getPage(params, "create_time", false);
        List<XfLogEntity> records = xfLogDao.balanceWithdrawnLogByUserId(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(xfLog -> {
            UserEntity user = userService.detail(xfLog.getUserId());
            if (user != null) {
                xfLog.setUserName(user.getAccount());
                xfLog.setMobile(user.getMobile());
            }
//            if(xfLog.getXfClassify().equals("CASH_OUT")){
//                xfLog.setType('-');
//            }else {
//                xfLog.setType('+');
//            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


}
