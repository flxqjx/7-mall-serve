package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.*;
import com.xyhc.cms.vo.wechatMsg.PointsmallOrderSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
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
import com.xyhc.cms.listener.PointsmallOrderListener;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商品订单表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
@Service("pointsmallOrderService")
public class PointsmallOrderServiceImpl extends ServiceImpl<PointsmallOrderDao, PointsmallOrderEntity> implements PointsmallOrderService {

    @Resource
    PointsmallOrderDao pointsmallOrderDao;
    @Resource
    PointsmallDao pointsmallDao;

    @Resource
    PointsmallSpecDao pointsmallSpecDao;

    @Resource
    PointsmallOrderCommodityDao pointsmallOrderCommodityDao;

    @Resource
    CommodityShoppingCartDao commodityShoppingCartDao;

    @Resource
    ReceiveInfoDao receiveInfoDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    MemberLevelOrderDao memberLevelOrderDao;

    @Resource
    MemberLevelDao memberLevelDao;

    @Resource
    BlockDao blockDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Resource
    PointsmallOrderCommodityService pointsmallOrderCommodityService;

   /* @Resource
    CommoditySpecService commoditySpecService;*/
    @Resource
    PointsmallSpecService pointsmallSpecService;

    @Resource
    XfLogService xfLogService;

    @Resource
    WechatService wechatService;

    @Resource
    PointsmallService pointsmallService;

    @Resource
    PointsmallOrderService pointsmallOrderService;
    @Resource
    IntegralLogService integralLogService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<PointsmallOrderEntity> all(Map<String, Object> params) {
        return pointsmallOrderDao.all(params);
    }

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<PointsmallOrderEntity> allExport(Map<String, Object> params) {
        return pointsmallOrderDao.allExport(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {

        params.put("uid", authUtils.AuthUser().getUserId());
        params.put("orderStatus", params.get("orderStatus"));
//        params.put("orderDetail", params.get("isPay"));
        IPage<PointsmallOrderEntity> page = new Query<PointsmallOrderEntity>().getPage(params, "create_time", false);

        List<PointsmallOrderEntity> records = pointsmallOrderDao.page(page, params);
        List<PointsmallOrderCommodityEntity> commodity = pointsmallOrderCommodityDao.allTwo();

        records.forEach(item -> {
            List<PointsmallOrderCommodityEntity> orderList = commodity.stream().filter(s -> s.getOrderId().equals(item.getId())).collect(Collectors.toList());
            item.setOrderList(orderList);
            PointsmallEntity pointsmallEntity = pointsmallDao.selectById(item.getPointsmallId());
            if (pointsmallEntity != null) {
                item.setPointsmallImg(pointsmallEntity.getCommodityMainImgurl());
                item.setPointsmallName(pointsmallEntity.getCommodityName());
                item.setPointsmallPrice(pointsmallEntity.getPrice());
            }
            //查询一条订单里面的多个商品 （一对多）
//            QueryWrapper<PointsmallOrderCommodityEntity> query = new QueryWrapper<>();
//            query.eq("order_id", item.getId());
//            List<PointsmallOrderCommodityEntity> orderList = pointsmallOrderCommodityDao.selectList(query);
//            item.setOrderList(orderList);
            //获取规格图片和规格名称
            orderList.forEach(orderItem -> {

                // psSpecService=new PointsmallSpecService();
                //PointsmallSpecEntity commoditySpecEntity = PointsmallSpecService.detail(orderItem.getSpecId());
                //PointsmallSpecEntity commoditySpecEntity = PointsmallSpecService.detailbyid(orderItem.getSpecId());
                //PointsmallSpecEntity commoditySpecEntity = psSpecService.detail(orderItem.getSpecId());

                PointsmallSpecEntity commoditySpecEntity = pointsmallSpecDao.selectById(orderItem.getSpecId());

                if (commoditySpecEntity != null) {
                    orderItem.setSpecName(commoditySpecEntity.getSpecName());
                    orderItem.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
                }
                if (orderItem.getCommodityId() != null && !orderItem.getCommodityId().equals("")) {
                    PointsmallEntity commodityEntity = pointsmallService.detailOrder(orderItem.getCommodityId());
                    if (commodityEntity != null) {
                        orderItem.setCommodityName(commodityEntity.getCommodityName());
                        orderItem.setCommodityImgurl(commodityEntity.getCommodityMainImgurl());
                        item.setIsDeleteCommodity(commodityEntity.getIsDelete());
                    }
                }
            });
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 查询我分享的普通用户下的单
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pageCommon(Map<String, Object> params) {
        params.put("userId", authUtils.AuthUser().getUserId());
        params.put("orderStatus", params.get("orderStatus"));
        params.put("orderDetail", params.get("isPay"));
        IPage<PointsmallOrderEntity> page = new Query<PointsmallOrderEntity>().getPage(params, "create_time", false);
        List<PointsmallOrderEntity> records = pointsmallOrderDao.pageCommon(page, params);
        List<Wechat> wechatList = wechatDao.all(params);
        records.forEach(item -> {
            Optional<Wechat> wechat = wechatList.stream().filter(s -> s.getId().equals(item.getUid())).findFirst();
            if (wechat.isPresent()) {
                item.setNickName(wechat.get().getNickName());
            } else {
                item.setNickName("暂无");
            }
//            item.setNickName(wechatDao.selectById(item.getUid()).getNickName());
            //查询一条订单里面的多个商品 （一对多）
            QueryWrapper<PointsmallOrderCommodityEntity> query = new QueryWrapper<>();
            query.eq("order_id", item.getId());
            List<PointsmallOrderCommodityEntity> orderList = pointsmallOrderCommodityDao.selectList(query);
            item.setOrderList(orderList);
            //获取规格图片和规格名称
            orderList.forEach(orderItem -> {
                //PointsmallSpecEntity commoditySpecEntity = pointsmallSpecService.detail(orderItem.getSpecId());
                PointsmallSpecEntity commoditySpecEntity = pointsmallSpecService.detailbyid(orderItem.getSpecId());
                if (commoditySpecEntity != null) {
                    orderItem.setSpecName(commoditySpecEntity.getSpecName());
                    orderItem.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
                }
                if (orderItem.getCommodityId() != null && !orderItem.getCommodityId().equals("")) {
                    PointsmallEntity commodityEntity = pointsmallService.detail(orderItem.getCommodityId());
                    if (commodityEntity != null) {
                        orderItem.setCommodityName(commodityEntity.getCommodityName());
                        orderItem.setCommodityImgurl(commodityEntity.getCommodityMainImgurl());
                    }
                }
            });
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询全部订单
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pageEntire(Map<String, Object> params) {
        IPage<PointsmallOrderEntity> page = new Query<PointsmallOrderEntity>().getPage(params, "create_time", false);
        List<PointsmallOrderEntity> records = pointsmallOrderDao.page(page, params);
        List<ReceiveInfoEntity> siteList = receiveInfoDao.allCity();
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        records.forEach(item->{
            Optional<ReceiveInfoEntity> findSite = siteList.stream().filter(s -> s.getId().equals(item.getReceiveId())).findFirst();
            if(findSite.isPresent()){
                if (StringUtils.isNotEmpty(findSite.get().getProvinceId())) {
                    item.setProvinceId(addressList.get(findSite.get().getProvinceId()).toString());
                } else {
                    item.setProvinceId("无");
                }
                if (StringUtils.isNotEmpty(findSite.get().getCityId())) {
                    item.setCityId(addressList.get(findSite.get().getCityId()).toString());
                } else {
                    item.setCityId("无");
                }
                if (StringUtils.isNotEmpty(findSite.get().getAreaId())) {
                    item.setAreaId(addressList.get(findSite.get().getAreaId()).toString());
                } else {
                    item.setAreaId("无");
                }
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询全部订单
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pageRefund(Map<String, Object> params) {
        IPage<PointsmallOrderEntity> page = new Query<PointsmallOrderEntity>().getPage(params, "create_time", false);
        List<PointsmallOrderEntity> records = pointsmallOrderDao.pageRefund(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param
     */
    @Override
    public Result saveOrder(@RequestBody PointsmallOrderSaveDto pointsmallOrderSave) {
        try {
            //查询当前人
            Wechat wechat = wechatService.detail(authUtils.AuthUser().getUserId());
            PointsmallOrderEntity pointsmallOrderEntity = pointsmallOrderSave.getPointsmallOrder();
            if (pointsmallOrderEntity.getTotal().doubleValue()> wechat.getIntegralMoney()) {
                return Result.error("积分不足");
            }
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {
                pointsmallOrderEntity.setId(createNo());
                pointsmallOrderEntity.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUid(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setOrderStatus("TO_DELIVER");
                pointsmallOrderEntity.setCreateTime(new Date());
                super.save(pointsmallOrderEntity);
                List<PointsmallOrderCommodityEntity> itemList = pointsmallOrderSave.getPointsmallOrderCommodity();
                if (itemList.size() > 0) {
                    itemList.forEach(item -> {
                        item.setOrderId(pointsmallOrderEntity.getId());
                        item.setSpecTotal(item.getNum().multiply(item.getSpecPrice()));
                        pointsmallOrderCommodityService.save(item);
                    });
                }
            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUid(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setPayTime(new Date());
                this.updateById(pointsmallOrderEntity);
            }
            //如果从购物车下单的，下完单之后把购物车里的这条商品删除
            UpdateWrapper<PointsmallOrderCommodityEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("order_id", pointsmallOrderEntity.getId());
            List<PointsmallOrderCommodityEntity> pointsmallOrderList = pointsmallOrderCommodityDao.selectList(wrapper);
            if (pointsmallOrderList.size() > 0) {
                pointsmallOrderList.forEach(item -> {
                    UpdateWrapper<CommodityShoppingCartEntity> wrapperCart = new UpdateWrapper<>();
                    wrapperCart.eq("uid", authUtils.AuthUser().getUserId());
                    wrapperCart.eq("spce_id", item.getSpecId());
                    commodityShoppingCartDao.delete(wrapperCart);
                });
            }

            //减少wechat表该下单用户的积分-integral_money字段



            if (wechat != null) {

//                if (pointsmallOrderEntity.getTotal().doubleValue()<= wechat.getIntegralMoney()) {
                    //查变更前的金额
                    double beforeBalane = wechat.getIntegralMoney();
                    //查变更后的金额
                    double afterIntegralMoney = wechat.getIntegralMoney() - pointsmallOrderEntity.getTotal().doubleValue();//@

                    //更新用户购物之后的总金额
                    UpdateWrapper<Wechat> wrapperTotalIntegralMoney = new UpdateWrapper<>();
                    wrapperTotalIntegralMoney.eq("id", wechat.getId());
                    wrapperTotalIntegralMoney.set("integral_money", afterIntegralMoney);//@
                    wechatDao.update(null, wrapperTotalIntegralMoney);

                    //写进积分流水记录表

                    /*IntegralLogEntity integralLog = new IntegralLogEntity();
                    integralLog.setXfBalance(shareMoney);
                    integralLog.setBeforeBalane(wechat.getBalance());
                    integralLog.setAfterBalance(wechat.getBalance() + shareMoney);
                    integralLog.setCreateBy(orderResult.getUid());
                    integralLog.setUserId(orderResult.getUserId());
                    integralLog.setOrderId(orderId);*/

                    IntegralLogEntity integralLog = new IntegralLogEntity();
                    //integralLog.setXfBalance(pointsmallOrderEntity.getTotal().doubleValue());
                    integralLog.setXfBalance(pointsmallOrderEntity.getTotal().intValue());

                    integralLog.setBeforeBalane(wechat.getIntegralMoney());
                    //integralLog.setBeforeBalane(beforeBalane.intValue());

                    //integralLog.setAfterBalance(wechat.getIntegralMoney() + pointsmallOrderEntity.getTotal().doubleValue());
                    integralLog.setAfterBalance(wechat.getIntegralMoney() - pointsmallOrderEntity.getTotal().intValue());
                   // integralLog.setAfterBalance(afterIntegralMoney.intValue());

                    integralLog.setCreateBy(authUtils.AuthUser().getUserId());
                    integralLog.setUserId(authUtils.AuthUser().getUserId());
                    integralLog.setOrderId(pointsmallOrderEntity.getId());


                    integralLog.setXfClassify("POINTPRODUCT_SALE");
                    integralLog.setXfRemark("购买积分商品扣除积分");
                    integralLog.setXfAccount(1);
                    integralLogService.save(integralLog);


//                } else {
//                    return Result.error("notenough");//积分不足
//                }
            }

            return Result.success(pointsmallOrderEntity.getId());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新新人专享商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public Result updateCommodityNewOrderStatus(String orderId, String orderNo, String tradeNo) {
        try {
            PointsmallOrderEntity pointsmallOrderEntity = pointsmallOrderDao.selectById(orderId);
            if (pointsmallOrderEntity != null) {
                pointsmallOrderEntity.setIsPay(1);
                pointsmallOrderEntity.setPayTime(new Date());
                pointsmallOrderEntity.setOutTradeNo(orderNo);
                pointsmallOrderEntity.setTransactionId(tradeNo);
                pointsmallOrderEntity.setOrderStatus("TO_DELIVER");
                pointsmallOrderDao.updateById(pointsmallOrderEntity);
            }
            return Result.success(pointsmallOrderEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 更新商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public Result updatePointsmallOrderStatus(String orderId, String orderNo, String tradeNo) {
        try {
            PointsmallOrderEntity pointsmallOrderEntity = pointsmallOrderDao.selectById(orderId);
            if (pointsmallOrderEntity != null) {
                pointsmallOrderEntity.setIsPay(1);
                pointsmallOrderEntity.setPayTime(new Date());
                pointsmallOrderEntity.setOutTradeNo(orderNo);
                pointsmallOrderEntity.setTransactionId(tradeNo);
                pointsmallOrderEntity.setOrderStatus("TO_DELIVER");
                pointsmallOrderDao.updateById(pointsmallOrderEntity);

                //作为普通用户分享提成————不做绑定的上下级，单次分享有效，,作为普通用户，A分享给B，B买后，A有提成
                //1: 先查询这个订单是否支付（只有支付才会给邀请人返钱）
                QueryWrapper orderUser = new QueryWrapper<>();
                orderUser.eq("id", orderId);
                orderUser.eq("is_pay", 1);
                PointsmallOrderEntity orderResult = pointsmallOrderDao.selectOne(orderUser);
                if (orderResult != null) {
                    //2：查到了再判断这条订单是否有邀请人，getUserId不等于null就说明有邀请人
                    if (StringUtils.isNotEmpty(orderResult.getUserId())) {
                        //3：有邀请人再查这个邀请人是否是会员
                        QueryWrapper query = new QueryWrapper<>();
                        query.eq("uid", orderResult.getUserId());
                        query.eq("is_delete", 0);
                        query.orderByDesc("pay_time");
                        query.eq("is_pay", 1);
                        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
                        if (memberLevelOrder.size()>0) {
                            QueryWrapper level = new QueryWrapper<>();
                            level.eq("id", memberLevelOrder.get(0).getLevelId());
                            MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                            if (memberLevel != null) {
                                sales(memberLevel.getId(),orderResult);
                            }
                        } else {
                            //邀请人不是会员-单次分享有效
                            QueryWrapper queryWrapper = new QueryWrapper();
                            queryWrapper.eq("block", "BLOCK_0015");
                            SysBlock boss = blockDao.selectOne(queryWrapper);

                            Wechat wechat = wechatService.detail(orderResult.getUserId());
                            if (boss != null) {
//                              BigDecimal ratio = new BigDecimal(boss.getRemark());
                                double shareMoney = (orderResult.getCommodityTotal().doubleValue() * Double.parseDouble(boss.getRemark())) / 100;

                                UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                                wrapperTotalBalance.eq("id", wechat.getId());
                                wrapperTotalBalance.set("balance", shareMoney + wechat.getBalance());
                                wechatDao.update(null, wrapperTotalBalance);

                                XfLogEntity xfLog = new XfLogEntity();
                                xfLog.setXfBalance(shareMoney);
                                xfLog.setBeforeBalane(wechat.getBalance());
                                xfLog.setAfterBalance(wechat.getBalance() + shareMoney);
                                xfLog.setCreateBy(orderResult.getUid());
                                xfLog.setUserId(orderResult.getUserId());
                                xfLog.setOrderId(orderId);
                                xfLog.setXfClassify("COMMON_SHARING");
                                xfLog.setXfRemark("普通分销收入");
                                xfLog.setXfAccount(0);
                                xfLogService.save(xfLog);
                            }
                        }
                    }else {

                    }
                }

            }
            return Result.success(pointsmallOrderEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    public void sales(String levelId ,PointsmallOrderEntity orderResult){
        //4：查到了说明邀请人已经购买会员，下一步再查邀请人是A套餐会员还是B套餐会员
        QueryWrapper level = new QueryWrapper<>();
        level.eq("id", levelId);
        MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
        if (memberLevel != null) {
            //判断是A就代表是A套餐会员
            if (memberLevel.getLevelType().equals("A")) {
                //查询A邀请的购买人是否是会员
                QueryWrapper queryUid = new QueryWrapper<>();
                queryUid.eq("uid", orderResult.getUid());
                queryUid.orderByDesc("pay_time");
                queryUid.eq("is_pay", 1);
                List<MemberLevelOrderEntity> memberLevelUidOrder = memberLevelOrderDao.selectList(queryUid);
                if (memberLevelUidOrder.size()>0) {
                    //A套餐会员邀请的是B套餐会员
                    QueryWrapper levelB = new QueryWrapper<>();
                    levelB.eq("id", memberLevelUidOrder.get(0).getLevelId());
                    MemberLevelEntity memberLevelB = memberLevelDao.selectOne(levelB);
                    if (memberLevelB != null) {
                        if(memberLevelB.getLevelType().equals("B")){
                            //判断A是不是B的直属上级，只有A是B的上级才能给A返钱，不是就不能反
                            QueryWrapper superior = new QueryWrapper<>();
                            superior.eq("id", orderResult.getUid());
                            Wechat memberSuperior = wechatDao.selectOne(superior);
                            if(memberSuperior.getParentId().equals(orderResult.getUserId())){
                                //A的折扣价
                                QueryWrapper queryWrapperA = new QueryWrapper();
                                queryWrapperA.eq("block", "BLOCK_0016");
                                SysBlock bossA = blockDao.selectOne(queryWrapperA);
                                //B的折扣价
                                QueryWrapper queryWrapperB = new QueryWrapper();
                                queryWrapperB.eq("block", "BLOCK_0017");
                                SysBlock bossB = blockDao.selectOne(queryWrapperB);
                                Wechat wechat = wechatService.detail(orderResult.getUserId());
                                if (bossA != null) {
                                    if(bossB!=null){
                                        //查到了就计算 A套餐会员分享B套餐会员购买
                                        //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                                        double shareMoney = orderResult.getCommodityTotal().doubleValue() * ((Double.valueOf(bossB.getRemark()) - Double.valueOf(bossA.getRemark())) / 100);
                                        UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                                        wrapperTotalBalance.eq("id", wechat.getId());
                                        wrapperTotalBalance.set("balance", shareMoney + wechat.getBalance());
                                        wechatDao.update(null, wrapperTotalBalance);

                                        UpdateWrapper<PointsmallOrderEntity> order = new UpdateWrapper<>();
                                        order.eq("id", orderResult.getId());
                                        order.set("user_id", "");
                                        pointsmallOrderDao.update(null, order);

                                        XfLogEntity xfLog = new XfLogEntity();
                                        xfLog.setXfBalance(shareMoney);
                                        xfLog.setBeforeBalane(wechat.getBalance());
                                        xfLog.setAfterBalance(wechat.getBalance() + shareMoney);
                                        xfLog.setCreateBy(orderResult.getUid());
                                        xfLog.setUserId(orderResult.getUserId());
                                        xfLog.setOrderId(orderResult.getId());
                                        xfLog.setXfClassify("VIP_JICHA_SHARING");
                                        xfLog.setXfRemark("会员级差分销收入");
                                        xfLog.setXfAccount(0);
                                        xfLogService.save(xfLog);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //A套餐会员分享普通用户购买，单次有效
                    //查询A套餐会员的分销比例
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("block", "BLOCK_0016");
                    SysBlock boss = blockDao.selectOne(queryWrapper);
                    Wechat wechat = wechatService.detail(orderResult.getUserId());
                    if (boss != null) {
                        //查到了就计算 A套餐会员分享普通用户购买，单次有效
                        //计算公式: A赚差价=商品原价*40%（100%-60%）
                        double shareMoney = orderResult.getCommodityTotal().doubleValue() * ((Double.valueOf(100) - Double.valueOf(boss.getRemark())) / 100);
                        UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                        wrapperTotalBalance.eq("id", wechat.getId());
                        wrapperTotalBalance.set("balance", shareMoney + wechat.getBalance());
                        wechatDao.update(null, wrapperTotalBalance);

                        XfLogEntity xfLog = new XfLogEntity();
                        xfLog.setXfBalance(shareMoney);
                        xfLog.setBeforeBalane(wechat.getBalance());
                        xfLog.setAfterBalance(wechat.getBalance() + shareMoney);
                        xfLog.setCreateBy(orderResult.getUid());
                        xfLog.setUserId(orderResult.getUserId());
                        xfLog.setOrderId(orderResult.getId());
                        xfLog.setXfClassify("VIP_SHARING");
                        xfLog.setXfRemark("会员分销收入");
                        xfLog.setXfAccount(0);
                        xfLogService.save(xfLog);
                    }
                }
                //判断是B就代办B套餐会员
            } else if (memberLevel.getLevelType().equals("B")) {
                //查询B邀请的购买人是否是会员——B套餐只能要求普通会员
//                QueryWrapper queryUid = new QueryWrapper<>();
//                queryUid.eq("uid", orderResult.getUid());
//                queryUid.eq("is_pay", 1);
//                MemberLevelOrderEntity memberLevelUidOrder = memberLevelOrderDao.selectOne(queryUid);
//                if (memberLevelUidOrder != null) {
//
//                }else {

                //B套餐会员邀请的普通用户
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("block", "BLOCK_0017");
                SysBlock boss = blockDao.selectOne(queryWrapper);
                Wechat wechat = wechatService.detail(orderResult.getUserId());
                if (boss != null) {
                    //查到了就计算
                    //计算公式: A赚差价=商品原价*40%（100%-60%）
                    double shareMoney = orderResult.getCommodityTotal().doubleValue() * ((Double.valueOf(100) - Double.valueOf(boss.getRemark())) / 100);
                    UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                    wrapperTotalBalance.eq("id", wechat.getId());
                    wrapperTotalBalance.set("balance", shareMoney + wechat.getBalance());
                    wechatDao.update(null, wrapperTotalBalance);

                    XfLogEntity xfLog = new XfLogEntity();
                    xfLog.setXfBalance(shareMoney);
                    xfLog.setBeforeBalane(wechat.getBalance());
                    xfLog.setAfterBalance(wechat.getBalance() + shareMoney);
                    xfLog.setCreateBy(orderResult.getUid());
                    xfLog.setUserId(orderResult.getUserId());
                    xfLog.setOrderId(orderResult.getId());
                    xfLog.setXfClassify("VIP_SHARING");
                    xfLog.setXfRemark("会员分销收入");
                    xfLog.setXfAccount(0);
                    xfLogService.save(xfLog);
                }

                //查分享人B会员是否有上级
                QueryWrapper userId = new QueryWrapper<>();
                userId.eq("id", orderResult.getUserId());
                Wechat higherUp = wechatDao.selectOne(userId);
                if(!higherUp.getParentId().equals(0)){
                    //查分享人B的上级
                    QueryWrapper wrapperPid = new QueryWrapper<>();
                    wrapperPid.eq("id", higherUp.getParentId());
                    Wechat wrapperPids = wechatDao.selectOne(wrapperPid);
                    //B邀请普通用户给A返钱
                    //A的折扣价
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0016");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    //B的折扣价
                    QueryWrapper queryWrapperB = new QueryWrapper();
                    queryWrapperB.eq("block", "BLOCK_0017");
                    SysBlock bossB = blockDao.selectOne(queryWrapperB);
                    if (bossA != null) {
                        if(bossB!=null){
                            //B会员邀请普通用户下单，给B按比例把钱 ，给A反级差钱
                            //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                            double shareMoney = orderResult.getCommodityTotal().doubleValue() * ((Double.valueOf(bossB.getRemark()) - Double.valueOf(bossA.getRemark())) / 100);
                            UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                            wrapperTotalBalance.eq("id", wrapperPids.getId());
                            wrapperTotalBalance.set("balance", shareMoney + wrapperPids.getBalance());
                            wechatDao.update(null, wrapperTotalBalance);

                            XfLogEntity xfLog = new XfLogEntity();
                            xfLog.setXfBalance(shareMoney);
                            xfLog.setBeforeBalane(wrapperPids.getBalance());
                            xfLog.setAfterBalance(wrapperPids.getBalance() + shareMoney);
                            xfLog.setCreateBy(orderResult.getUid());
                            xfLog.setUserId(wrapperPids.getId());
                            xfLog.setOrderId(orderResult.getId());
                            xfLog.setXfClassify("VIP_JICHA_SHARING");
                            xfLog.setXfRemark("会员级差分销收入");
                            xfLog.setXfAccount(0);
                            xfLogService.save(xfLog);
                        }
                    }
                }
//                                    }
            }
        }
    }

    /**
     * 更改状态
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean updatePayStatus(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            UpdateWrapper<PointsmallOrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", pointsmallOrderEntity.getId());
            wrapper.set("is_pay", 1);
            wrapper.set("order_status", "TO_DELIVER");
            pointsmallOrderDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 确认收货
     *
     * @param pointsmallOrderEntity
     * @return boolean
     */
    @Override
    public boolean saveReceiving(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setOrderStatus("RECEIVED");
                this.updateById(pointsmallOrderEntity);

                PointsmallOrderEntity pointsmallOrder = pointsmallOrderDao.selectById(pointsmallOrderEntity.getId());

                Wechat wechat = wechatDao.selectById(pointsmallOrder.getUid());

                //会员日购买产品，积分翻倍
                QueryWrapper query = new QueryWrapper<>();
                query.eq("uid", pointsmallOrder.getUid());
                query.eq("is_delete", 0);
                query.orderByDesc("pay_time");
                query.eq("is_pay", 1);
                List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
                if(memberLevelOrder!=null){
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(memberLevelOrder.get(0).getPayTime());
                    calendar.add(Calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
                    Date date = calendar.getTime();
                    //当天日期等于会员日————积分翻倍
                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    if(formatter.format(date).equals(formatter.format(pointsmallOrder.getPayTime()))){
                        // 将 BigDecimal 转换为 Integer
                        Integer integerMember = pointsmallOrder.getTotal().intValue();
                        Integer integerTotal= integerMember*2;

                        UpdateWrapper<Wechat> wrapperIntegerMember = new UpdateWrapper<>();
                        wrapperIntegerMember.eq("id", wechat.getId());
                        wrapperIntegerMember.set("integral_money", wechat.getIntegralMoney() + integerTotal);
                        wechatDao.update(null, wrapperIntegerMember);

                        IntegralLogEntity xfLogMember = new IntegralLogEntity();
                        xfLogMember.setXfBalance(integerTotal);
                        xfLogMember.setBeforeBalane(wechat.getIntegralMoney());
                        xfLogMember.setAfterBalance(wechat.getIntegralMoney() + integerTotal);
                        xfLogMember.setCreateBy(wechat.getId());
                        xfLogMember.setUserId(wechat.getId());
                        xfLogMember.setOrderId(pointsmallOrderEntity.getId());
                        xfLogMember.setXfClassify("INTEGRAL");
                        xfLogMember.setXfRemark("下单积分收入");
                        integralLogService.save(xfLogMember);

                    }else {
                        //购买产品送积分
                        //2: 购买产品送积分：购买送积分，1元=1积分

                        // 将 BigDecimal 转换为 Integer
                        Integer integer = pointsmallOrder.getTotal().intValue();

                        UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
                        wrapperIntegralMoney.eq("id", wechat.getId());
                        wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
                        wechatDao.update(null, wrapperIntegralMoney);

                        IntegralLogEntity xfLog = new IntegralLogEntity();
                        xfLog.setXfBalance(integer);
                        xfLog.setBeforeBalane(wechat.getIntegralMoney());
                        xfLog.setAfterBalance(wechat.getIntegralMoney() + integer);
                        xfLog.setCreateBy(wechat.getId());
                        xfLog.setUserId(wechat.getId());
                        xfLog.setOrderId(pointsmallOrderEntity.getId());
                        xfLog.setXfClassify("INTEGRAL");
                        xfLog.setXfRemark("下单积分收入");
                        integralLogService.save(xfLog);
                    }
                }else {
                    //购买产品送积分
                    //2: 购买产品送积分：购买送积分，1元=1积分

                    // 将 BigDecimal 转换为 Integer
                    Integer integer = pointsmallOrder.getTotal().intValue();

                    UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
                    wrapperIntegralMoney.eq("id", wechat.getId());
                    wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
                    wechatDao.update(null, wrapperIntegralMoney);

                    IntegralLogEntity xfLog = new IntegralLogEntity();
                    xfLog.setXfBalance(integer);
                    xfLog.setBeforeBalane(wechat.getIntegralMoney());
                    xfLog.setAfterBalance(wechat.getIntegralMoney() + integer);
                    xfLog.setCreateBy(wechat.getId());
                    xfLog.setUserId(wechat.getId());
                    xfLog.setOrderId(pointsmallOrderEntity.getId());
                    xfLog.setXfClassify("INTEGRAL");
                    xfLog.setXfRemark("下单积分收入");
                    integralLogService.save(xfLog);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 退货申请
     *
     * @param pointsmallOrderEntity
     * @return boolean
     */
    @Override
    public boolean saveRefund(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setApproveStatus("TO_APPROVE");
                pointsmallOrderEntity.setOrderStatus("APPLY_REFUND");
                this.updateById(pointsmallOrderEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 修改订单地址
     */
    @Override
    public boolean editAddress(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                this.updateById(pointsmallOrderEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 保存
     */
    @Override
    public boolean save(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setOrderStatus("TO_DELIVER");
                this.updateById(pointsmallOrderEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 修改订单处理
     */
    @Override
    public boolean saveProcess(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setOrderStatus("TO_RECEIVE");
                this.updateById(pointsmallOrderEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 生成单号
     *
     * @return
     */
    public String createNo() {
        try {
            int rannum = (int) (1 + Math.random() * (99 - 1 + 1));
            String ranNumStr = String.format("%02d", rannum);
            String strPre = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            return strPre + ranNumStr + String.format("%04d", (maxNo() + 1));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取数据库中最大编号
     *
     * @return
     */
    public long maxNo() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            List<PointsmallOrderEntity> scheduleList = pointsmallOrderDao.selectList(new QueryWrapper<PointsmallOrderEntity>()
                    .gt("create_time", minDate).lt("create_time", maxDate));
            return scheduleList.stream().count();
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public PointsmallOrderEntity detail(String id) {
        return pointsmallOrderDao.selectById(id);
    }

    /**
     * 查询该用户是否购买过新人专享商品
     *
     * @return
     */
    @Override
    public Result newOrderdetail() {
        try {
            QueryWrapper<PointsmallOrderEntity> query = new QueryWrapper<>();
            query.eq("uid", authUtils.AuthUser().getUserId());
            query.eq("is_pay", 1);
            query.eq("is_new_order", 1);
            PointsmallOrderEntity pointsmallOrderEntity = pointsmallOrderDao.selectOne(query);
            if (pointsmallOrderEntity != null) {
                return Result.error("您已购买过新人专享商品啦！");
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 退货审核通过
     */
    @Override
    public void audit(String id) {
        try {
            UpdateWrapper<PointsmallOrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("approve_status", "PASS");
            wrapper.set("order_status", "REFUNDED");
            pointsmallOrderDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * 驳回退货申请
     */
    @Override
    public boolean reject(PointsmallOrderEntity pointsmallOrderEntity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderEntity.getId())) {

            } else {
                pointsmallOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderEntity.setUpdateTime(new Date());
                pointsmallOrderEntity.setApproveStatus("REJECT");
                this.updateById(pointsmallOrderEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 根据orderid查询订单详情
     *
     * @return
     */
    @Override
    public PointsmallOrderEntity orderDetail(String id) {
        PointsmallOrderEntity pointsmallOrderEntity = pointsmallOrderDao.selectById(id);
        //取详情多张图片
        UpdateWrapper<PointsmallOrderCommodityEntity> wrapperOrder = new UpdateWrapper<>();
        //wrapperOrder.eq("order_id", pointsmallOrderEntity.getId());
        wrapperOrder.eq("id", pointsmallOrderEntity.getId());
        wrapperOrder.eq("is_delete", 0);
        List<PointsmallOrderCommodityEntity> orderList = pointsmallOrderCommodityDao.selectList(wrapperOrder);
        orderList.forEach(item -> {
            //CommoditySpecEntity commoditySpecEntity = commoditySpecService.detail(item.getSpecId());
            //PointsmallSpecEntity commoditySpecEntity = PointsmallSpecService.detail(item.getSpecId());
            PointsmallSpecEntity commoditySpecEntity = pointsmallSpecDao.selectById(item.getSpecId());

            if (commoditySpecEntity != null) {
                item.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
            }
        });
        pointsmallOrderEntity.setOrderList(orderList);
        return pointsmallOrderEntity;
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
            PointsmallOrderEntity removeEntity = pointsmallOrderDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallOrderDao.updateById(removeEntity);
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
                PointsmallOrderEntity removeEntity = pointsmallOrderDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderDao.updateById(removeEntity);
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
            PointsmallOrderListener listener = new PointsmallOrderListener();
            EasyExcel.read(file.getInputStream(), PointsmallOrderEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallOrderEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallOrderDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 查询————我的订单状态各数量
     */
    @Override
    public Result myOrderNum() {
        Map<String, Object> repMap = new HashMap<>();
        String uid = authUtils.AuthUser().getUserId();
        //待发货的数量
        repMap.put("deliverNum", pointsmallOrderDao.orderDeliverNum(uid).size());
        //待收货的数量
        repMap.put("receiveNum", pointsmallOrderDao.orderReceiveNum(uid).size());
        return Result.success(repMap);
    }

}
