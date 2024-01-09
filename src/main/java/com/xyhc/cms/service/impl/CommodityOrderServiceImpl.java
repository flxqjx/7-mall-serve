package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.*;
import com.xyhc.cms.vo.wechatMsg.CommodityOrderSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.xyhc.cms.listener.CommodityOrderListener;

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
@Service("commodityOrderService")
public class CommodityOrderServiceImpl extends ServiceImpl<CommodityOrderDao, CommodityOrderEntity> implements CommodityOrderService {

    @Resource
    CommodityOrderDao commodityOrderDao;
    @Resource
    CommodityDao commodityDao;
    @Resource
    XfLogDao xfLogDao;

    @Resource
    CommodityOrderCommodityDao commodityOrderCommodityDao;

    @Resource
    CommodityShoppingCartDao commodityShoppingCartDao;

    @Resource
    ReceiveInfoDao receiveInfoDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    PromotionActivitysDao promotionActivitysDao;

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
    CommodityOrderCommodityService commodityOrderCommodityService;

    @Resource
    CommoditySpecService commoditySpecService;
    @Resource
    MemberLevelOrderService memberLevelOrderService;

    @Resource
    XfLogService xfLogService;

    @Resource
    WechatService wechatService;

    @Resource
    CommodityService commodityService;

    @Resource
    CommodityOrderService commodityOrderService;
    @Resource
    IntegralLogService integralLogService;
    @Resource
    CouponReceiveDao couponReceiveDao;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> all(Map<String, Object> params) {
        return commodityOrderDao.all(params);
    }


    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> allByUid(Map<String, Object> params) {
        params.put("uid", params.get("uid"));
        return commodityOrderDao.allByUid(params);
    }

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityOrderEntity> allExport(Map<String, Object> params) {
        params.put("levelType", params.get("levelType"));
        List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.allExport(params);
        List<ReceiveInfoEntity> siteList = receiveInfoDao.allCity();
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        commodityOrderList.forEach(item -> {

            if (item.getIsPay() == 0) {
                item.setIsPayStatus("未支付");
            } else if (item.getIsPay() == 1) {
                item.setIsPayStatus("已支付");
            }
            if (item.getPayType() != null) {
                if (item.getPayType().equals("integral")) {
                    item.setPayType("积分");
                } else if (item.getPayType().equals("weChat")) {
                    item.setPayType("微信");
                }
            } else {
                item.setPayType("");
            }

            if (item.getOrderStatus() != null) {
                if (item.getOrderStatus().equals("TO_DELIVER")) {
                    item.setOrderStatus("待发货");
                } else if (item.getOrderStatus().equals("TO_RECEIVE")) {
                    item.setOrderStatus("待收货");
                } else if (item.getOrderStatus().equals("RECEIVED")) {
                    item.setOrderStatus("已收货");
                } else if (item.getOrderStatus().equals("APPLY_REFUND")) {
                    item.setOrderStatus("申请退货");
                } else if (item.getOrderStatus().equals("REFUNDED")) {
                    item.setOrderStatus("已退货");
                } else if (item.getOrderStatus().equals("COMMENT")) {
                    item.setOrderStatus("已评价");
                }
            }
            Optional<ReceiveInfoEntity> findSite = siteList.stream().filter(s -> s.getId().equals(item.getReceiveId())).findFirst();
            if (findSite.isPresent()) {
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
            Wechat wechat = wechatDao.selectById(item.getUid());
            if (wechat != null) {

                if (!"0".equals(wechat.getParentId())) {
                    Wechat pid = wechatDao.selectById(wechat.getParentId());
                    if (pid != null) {
                        item.setParentName(pid.getNickName());
                    }
                    if (!"0".equals(pid.getParentId())) {
                        Wechat pidMax = wechatDao.selectById(pid.getParentId());
                        if (pidMax != null) {
                            item.setParentMaxName(pidMax.getNickName());
                        }
                    }
                }
            }
        });
        return commodityOrderList;
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
        params.put("orderDetail", params.get("isPay"));
        IPage<CommodityOrderEntity> page = new Query<CommodityOrderEntity>().getPage(params, "create_time", false);
        List<CommodityOrderEntity> records = commodityOrderDao.page(page, params);
        List<CommodityOrderCommodityEntity> commodity = commodityOrderCommodityDao.allTwo();
        records.forEach(item -> {
            List<CommodityOrderCommodityEntity> orderList = commodity.stream().filter(s -> s.getOrderId().equals(item.getId())).collect(Collectors.toList());
            item.setOrderList(orderList);
            //查询一条订单里面的多个商品 （一对多）
//            QueryWrapper<CommodityOrderCommodityEntity> query = new QueryWrapper<>();
//            query.eq("order_id", item.getId());
//            List<CommodityOrderCommodityEntity> orderList = commodityOrderCommodityDao.selectList(query);
//            item.setOrderList(orderList);
            //获取规格图片和规格名称
            orderList.forEach(orderItem -> {
                CommoditySpecEntity commoditySpecEntity = commoditySpecService.detail(orderItem.getSpecId());
                if (commoditySpecEntity != null) {
                    orderItem.setSpecName(commoditySpecEntity.getSpecName());
                    orderItem.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
                }
                if (orderItem.getCommodityId() != null && !orderItem.getCommodityId().equals("")) {
                    CommodityEntity commodityEntity = commodityService.detailOrder(orderItem.getCommodityId());
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
        IPage<CommodityOrderEntity> page = new Query<CommodityOrderEntity>().getPage(params, "create_time", false);
        List<CommodityOrderEntity> records = commodityOrderDao.pageCommon(page, params);
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
            QueryWrapper<CommodityOrderCommodityEntity> query = new QueryWrapper<>();
            query.eq("order_id", item.getId());
            List<CommodityOrderCommodityEntity> orderList = commodityOrderCommodityDao.selectList(query);
            item.setOrderList(orderList);
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
        params.put("levelType", params.get("levelType"));
        IPage<CommodityOrderEntity> page = new Query<CommodityOrderEntity>().getPage(params, "T.create_time", false);
        List<CommodityOrderEntity> records = commodityOrderDao.page(page, params);
        List<ReceiveInfoEntity> siteList = receiveInfoDao.allCity();
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        records.forEach(item -> {
            Optional<ReceiveInfoEntity> findSite = siteList.stream().filter(s -> s.getId().equals(item.getReceiveId())).findFirst();
            if (findSite.isPresent()) {
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
            Wechat wechat = wechatDao.selectById(item.getUid());
            if (wechat != null) {
                //下单人有上级
//                if (!"0".equals(item.getParentId()) && item.getParentId() != null && item.getParentId() != "") {
//                    Wechat orderParent = wechatDao.selectById(item.getParentId());
//                    if (orderParent != null) {
//                        item.setParentName(orderParent.getNickName());
//                    }
//                } else {
//                    item.setParentName("");
//                }
                if (!"0".equals(wechat.getParentId())) {
                    Wechat pid = wechatDao.selectById(wechat.getParentId());
                    if (pid != null) {
                        item.setParentName(pid.getNickName());
                    }
                    if (!"0".equals(pid.getParentId())) {
                        Wechat pidMax = wechatDao.selectById(pid.getParentId());
                        if (pidMax != null) {
                            item.setParentMaxName(pidMax.getNickName());
                        }
                    }
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
        IPage<CommodityOrderEntity> page = new Query<CommodityOrderEntity>().getPage(params, "create_time", false);
        List<CommodityOrderEntity> records = commodityOrderDao.pageRefund(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param
     */
    @Override
    public Result saveOrder(@RequestBody CommodityOrderSaveDto commodityOrderSave) {
        try {


            if ("integral".equals(commodityOrderSave.getCommodityOrder().getPayType())) {
                // 取全部积分
                Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
                if (wechat != null) {
                    if (commodityOrderSave.getCommodityOrder().getTotal() > wechat.getIntegralMoney()) {
                        return Result.error("积分不足");
                    }
                }
            } else {
                // 订单单信息校验
                String validateMessage = validateOrder(commodityOrderSave);
                if (StringUtils.isNotEmpty(validateMessage)) {
                    return Result.error(validateMessage);
                }
            }


            CommodityOrderEntity commodityOrderEntity = commodityOrderSave.getCommodityOrder();
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {
                //查询当前用户上级ID
                Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
                if (wechat != null) {
                    //有上级
                    if (!wechat.getParentId().equals("0")) {
                        commodityOrderEntity.setParentId(wechat.getParentId());
                    } else {
                        commodityOrderEntity.setParentId("0");
                    }
                }
                //查询当前用户会员级别
                QueryWrapper query = new QueryWrapper<>();
                query.eq("uid", authUtils.AuthUser().getUserId());
                query.eq("is_delete", 0);
                query.eq("is_pay", 1);
                MemberLevelOrderEntity memberLevelOrder = memberLevelOrderDao.selectOne(query);
                if (memberLevelOrder != null) {
                    commodityOrderEntity.setLevelId(memberLevelOrder.getLevelId());
                } else {
                    commodityOrderEntity.setLevelId("");
                }

                commodityOrderEntity.setId(createNo());
                commodityOrderEntity.setCreateBy(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUid(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setCreateTime(new Date());
                if (commodityOrderEntity.getPoints().equals(0)) {
                    commodityOrderEntity.setPoints(0);
                    commodityOrderEntity.setIsReturnPoints(2);
                }
                super.save(commodityOrderEntity);
                List<CommodityOrderCommodityEntity> itemList = commodityOrderSave.getCommodityOrderCommodity();
                if (itemList.size() > 0) {
                    itemList.forEach(item -> {
                        item.setOrderId(commodityOrderEntity.getId());
                        item.setSpecTotal(item.getNum() * item.getSpecPrice());
                        item.setPrice(item.getNum() * item.getPrice());
                        commodityOrderCommodityService.save(item);
                    });
                }
            } else {
                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUid(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUpdateTime(new Date());
                commodityOrderEntity.setPayTime(new Date());
                this.updateById(commodityOrderEntity);
            }
            //如果从购物车下单的，下完单之后把购物车里的这条商品删除
            UpdateWrapper<CommodityOrderCommodityEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("order_id", commodityOrderEntity.getId());
            List<CommodityOrderCommodityEntity> commodityOrderList = commodityOrderCommodityDao.selectList(wrapper);
            if (commodityOrderList.size() > 0) {
                commodityOrderList.forEach(item -> {
                    UpdateWrapper<CommodityShoppingCartEntity> wrapperCart = new UpdateWrapper<>();
                    wrapperCart.eq("uid", authUtils.AuthUser().getUserId());
                    wrapperCart.eq("spce_id", item.getSpecId());
                    wrapperCart.set("is_delete", 1);
                    commodityShoppingCartDao.update(null, wrapperCart);
                });
            }

            // 处理积分
            if ("integral".equals(commodityOrderSave.getCommodityOrder().getPayType())) {
                // 取全部积分
                Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
                if (wechat != null) {
                    // 处理积分
                    updateIntegralByCommodityOrder(commodityOrderSave.getCommodityOrder());
                    // 修改订单状态
                    CommodityOrderEntity commodityOrderEntityUpdate = commodityOrderDao.selectById(commodityOrderEntity.getId());
                    if (commodityOrderEntityUpdate != null) {
                        commodityOrderEntityUpdate.setIsPay(1);
                        commodityOrderEntityUpdate.setPayTime(new Date());
                        commodityOrderEntityUpdate.setOutTradeNo("");
                        commodityOrderEntityUpdate.setTransactionId("");
                        commodityOrderEntityUpdate.setOrderStatus("TO_DELIVER");
                        commodityOrderEntityUpdate.setPoints(0);
                        commodityOrderEntityUpdate.setIsReturnPoints(2);
                        commodityOrderDao.updateById(commodityOrderEntityUpdate);

                    }
                }
            }
            // 处理优惠券

//            CouponReceiveEntity couponReceiveEntity = couponReceiveDao.selectById(commodityOrderSave.getCommodityOrder().getCouponReceiveId());
//            if (couponReceiveEntity != null) {
//                couponReceiveEntity.setIsUse(1);
//                couponReceiveEntity.setUpdateTime(new Date());
//                couponReceiveDao.updateById(couponReceiveEntity);
//            }

            return Result.success(commodityOrderEntity.getId());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 订单信息校验
     *
     * @param
     */
    public String validateOrder(CommodityOrderSaveDto commodityOrderSave) {

        //查下单人属于什么会员
        QueryWrapper query = new QueryWrapper<>();
        query.eq("uid", authUtils.AuthUser().getUserId());
        query.eq("is_delete", 0);
        query.orderByDesc("pay_time");
        query.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
        String levelType = "";
        if (memberLevelOrder.size() > 0) {
            //会员类型
            MemberLevelEntity memberLevel = memberLevelDao.selectById(memberLevelOrder.get(0).getLevelId());
            if (memberLevel != null) {
                levelType = memberLevel.getLevelType();
            }
        } else {
            levelType = "普通会员";
        }
        //查询不同会员的折扣比例
        // block
        String proportion = "0";
        QueryWrapper queryWrapperBlock = new QueryWrapper();
        List<SysBlock> blockList = blockDao.selectList(queryWrapperBlock);
        if ("A".equals(levelType)) {
            Optional<SysBlock> blockA = blockList.stream().filter(s -> "BLOCK_0016".equals(s.getBlock())).findFirst();
            proportion = blockA.get().getRemark();
        } else if ("B".equals(levelType)) {
            Optional<SysBlock> blockB = blockList.stream().filter(s -> "BLOCK_0017".equals(s.getBlock())).findFirst();
            proportion = blockB.get().getRemark();
        } else if ("C".equals(levelType)) {
            Optional<SysBlock> blockC = blockList.stream().filter(s -> "BLOCK_0030".equals(s.getBlock())).findFirst();
            proportion = blockC.get().getRemark();
        } else {
            proportion = "0";
        }


        List<CommodityOrderCommodityEntity> commodityOrderCommodityEntity = commodityOrderSave.getCommodityOrderCommodity();

        String finalProportion = proportion;
        String finalLevelType = levelType;
        String finalProportion1 = proportion;

        final double[] vipPriceTotal = {0};

        commodityOrderCommodityEntity.forEach(item -> {
            if (finalProportion.equals("0")) {
                double vipPrice = item.getPrice() * (item.getNum());
                vipPriceTotal[0] = vipPriceTotal[0] + vipPrice;
            } else {
                double vipPrice = item.getPrice() * (item.getNum()) * Double.parseDouble(finalProportion) / 100.00;
                vipPriceTotal[0] = vipPriceTotal[0] + vipPrice;
            }


        });

        double actualTotal = vipPriceTotal[0];

        actualTotal = actualTotal + commodityOrderSave.getCommodityOrder().getFreight() - commodityOrderSave.getCommodityOrder().getDiscount();

        double orderTotal = commodityOrderSave.getCommodityOrder().getTotal();
        double orderCommodityTotal = commodityOrderSave.getCommodityOrder().getCommodityTotal();
        double orderDiscount = commodityOrderSave.getCommodityOrder().getDiscount();
        double orderDreight = commodityOrderSave.getCommodityOrder().getFreight();
        if (!String.format("%.2f", actualTotal).equals(String.format("%.2f", orderTotal))) {
            return "支付异常，支付金额异常";
        }
        if (proportion.equals("0")) {
            if (commodityOrderSave.getCommodityOrder().getDiscount() != 0) {
                return "支付异常，购物金异常";
            }
        } else {
            double validateDiscount = (commodityOrderSave.getCommodityOrder().getCommodityTotal() + commodityOrderSave.getCommodityOrder().getFreight()) * 0.1;
            Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
            if (wechat.getShoppingMoney() < validateDiscount) {
                validateDiscount = wechat.getShoppingMoney();
            }
            if (!String.format("%.2f", orderDiscount).equals(String.format("%.2f", validateDiscount))) {
                return "支付异常，购物金异常";
            }
        }

        if (!String.format("%.2f", orderCommodityTotal).equals(String.format("%.2f", orderTotal + orderDiscount - orderDreight))) {
            return "支付异常，公式";
        }

        return "";
    }


    /**
     * 商品下单消费积分
     */
    public void updateIntegralByCommodityOrder(CommodityOrderEntity commodityOrder) {
        // 将 BigDecimal 转换为 Integer
        double integer = commodityOrder.getTotal();
        //购买人
        Wechat wechat = wechatDao.selectById(commodityOrder.getUid());
        if (wechat != null) {

            UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
            wrapperIntegralMoney.eq("id", wechat.getId());
            wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() - integer);
            wechatDao.update(null, wrapperIntegralMoney);

            IntegralLogEntity xfLog = new IntegralLogEntity();
            xfLog.setXfBalance((int) integer);
            xfLog.setBeforeBalane(wechat.getIntegralMoney());
            xfLog.setAfterBalance((int) (wechat.getIntegralMoney() - integer));
            xfLog.setCreateBy(wechat.getId());
            xfLog.setUserId(wechat.getId());
            xfLog.setOrderId(commodityOrder.getId());
            xfLog.setXfClassify("POINTS_PAY");
            xfLog.setXfRemark("积分支付");
            integralLogService.save(xfLog);

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
            CommodityOrderEntity commodityOrderEntity = commodityOrderDao.selectById(orderId);
            if (commodityOrderEntity != null) {
                if (commodityOrderEntity.getIsPay() == 1) {
                    return Result.success();
                }
                commodityOrderEntity.setIsPay(1);
                commodityOrderEntity.setPayTime(new Date());
                commodityOrderEntity.setOutTradeNo(orderNo);
                commodityOrderEntity.setTransactionId(tradeNo);
                commodityOrderEntity.setOrderStatus("TO_DELIVER");
                commodityOrderDao.updateById(commodityOrderEntity);
            }
            return Result.success(commodityOrderEntity.getId());
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
    public Result updateCommodityOrderStatus(String orderId, String orderNo, String tradeNo) {
        try {
            CommodityOrderEntity commodityOrderEntity = commodityOrderDao.selectById(orderId);


            if (commodityOrderEntity != null) {
                if (commodityOrderEntity.getIsPay() == 1) {
                    return Result.success();
                }
                //查询有无开启促销活动
                QueryWrapper promotionActivitysQuery = new QueryWrapper<>();
                promotionActivitysQuery.eq("is_open", 1);
                promotionActivitysQuery.eq("is_delete", 0);
                PromotionActivitysEntity promotionActivitys = promotionActivitysDao.selectOne(promotionActivitysQuery);
                if (promotionActivitys != null) {
                    if (new Date().getTime() > promotionActivitys.getStartTime().getTime() && new Date().getTime() < promotionActivitys.getEndTime().getTime()) {
                        commodityOrderEntity.setPromotionActivitysId(promotionActivitys.getId());
                    }
                }

//                BigDecimal total = new BigDecimal(totalAmount);
                commodityOrderEntity.setIsPay(1);
                commodityOrderEntity.setPayTime(new Date());
                commodityOrderEntity.setOutTradeNo(orderNo);
//                commodityOrderEntity.setTotal(total);
                commodityOrderEntity.setTransactionId(tradeNo);
                commodityOrderEntity.setOrderStatus("TO_DELIVER");
                commodityOrderDao.updateById(commodityOrderEntity);

                //订单数据监控 订单号+商品销售价+实际支付价格+会员类型（折扣比例）
//                dataMonitor(orderId, commodityOrderEntity.getUid(), commodityOrderEntity.getTotal());

                //查询下单商品的销售价
                Double salesPrice = commodityOrderCommodityDao.salesPrice(orderId);

                //扣除购物金
                Wechat wechatShopp = wechatDao.selectById(commodityOrderEntity.getUid());
                UpdateWrapper<Wechat> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", wechatShopp.getId());
                updateWrapper.set("shopping_money", wechatShopp.getShoppingMoney() - commodityOrderEntity.getDiscount());
                wechatDao.update(null, updateWrapper);

                if (commodityOrderEntity.getDiscount() > 0) {
                    XfLogEntity discountXfLog = new XfLogEntity();
                    discountXfLog.setXfBalance(commodityOrderEntity.getDiscount());
                    discountXfLog.setBeforeBalane(wechatShopp.getShoppingMoney());
                    discountXfLog.setAfterBalance(wechatShopp.getShoppingMoney() - commodityOrderEntity.getDiscount());
                    discountXfLog.setCreateBy(commodityOrderEntity.getUid());
                    discountXfLog.setUserId(commodityOrderEntity.getUid());
                    discountXfLog.setOrderId(orderId);
                    discountXfLog.setXfAccount(3);
                    discountXfLog.setXfClassify("DISCOUNT");
                    discountXfLog.setXfRemark("购物抵扣");
                    xfLogService.save(discountXfLog);
                }


                //B会员升级B+
                upgradeAmount(commodityOrderEntity);

                //会员日奖励双倍积分
                membershipDay(commodityOrderEntity.getUid(), commodityOrderEntity);

                //根据下单数量，减去库存
                updateInventory(commodityOrderEntity);


                //作为普通用户分享提成————不做绑定的上下级，单次分享有效，,作为普通用户，A分享给B，B买后，A有提成
                //1: 先查询这个订单是否支付（只有支付才会给邀请人返钱）
                QueryWrapper orderUser = new QueryWrapper<>();
                orderUser.eq("id", orderId);
                orderUser.eq("is_pay", 1);
                CommodityOrderEntity orderResult = commodityOrderDao.selectOne(orderUser);
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
                        if (memberLevelOrder.size() > 0) {
                            QueryWrapper level = new QueryWrapper<>();
                            level.eq("id", memberLevelOrder.get(0).getLevelId());
                            MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                            if (memberLevel != null) {

                                sales(memberLevel.getId(), orderResult, salesPrice);
                            }
                        } else {
                            //邀请人不是会员-单次分享有效
                            QueryWrapper queryWrapper = new QueryWrapper();
                            queryWrapper.eq("block", "BLOCK_0015");
                            SysBlock boss = blockDao.selectOne(queryWrapper);

                            Wechat wechat = wechatService.detail(orderResult.getUserId());
                            if (boss != null) {
//                              BigDecimal ratio = new BigDecimal(boss.getRemark());
                                double shareMoney = (salesPrice * Double.parseDouble(boss.getRemark())) / 100;

//                                UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
//                                wrapperTotalBalance.eq("id", wechat.getId());
//                                wrapperTotalBalance.set("total_revenue", shareMoney + wechat.getTotalRevenue());
//                                wechatDao.update(null, wrapperTotalBalance);

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
                    } else {

                    }
                }


                //C会员自动升级A会员
                wechatService.levelCUpgradeTask();
            }
            return Result.success(commodityOrderEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    //根据下单数量，减去库存
    public void updateInventory(CommodityOrderEntity commodityOrder) {
        List<CommodityOrderCommodityEntity> list = commodityOrderCommodityDao.queryCommodityId(commodityOrder.getId());
        if (list.size() > 0) {
            int orderNum = 0;
            for (CommodityOrderCommodityEntity t : list) {
//                orderNum += t.getNum().intValue();
                CommodityEntity commodityEntity = commodityDao.selectById(t.getCommodityId());
                if (commodityEntity != null) {
                    UpdateWrapper<CommodityEntity> wrapper = new UpdateWrapper<>();
                    wrapper.eq("id", t.getCommodityId());
                    wrapper.set("inventory", commodityEntity.getInventory() - t.getNum());
                    commodityDao.update(null, wrapper);
                }
            }
        }
    }

    /**
     * B会员升级B+会员user
     *
     * @param commodityOrderEntity
     * @return boolean
     */
    public void upgradeAmount(CommodityOrderEntity commodityOrderEntity) {
        String uid = commodityOrderEntity.getUid();
        double salesPrice = commodityOrderEntity.getPrice();
        QueryWrapper query = new QueryWrapper<>();
        query.eq("uid", uid);
        query.eq("is_delete", 0);
        query.orderByDesc("pay_time");
        query.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
        if (memberLevelOrder.size() > 0) {
            Wechat wechat = wechatDao.selectById(uid);
            QueryWrapper level = new QueryWrapper<>();
            level.eq("id", memberLevelOrder.get(0).getLevelId());
            MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
            if (memberLevel != null) {
                if (memberLevel.getLevelType().equals("B")) {
                    //查询下单的上级自己下单的总金额
                    Map<String, Object> orderNum = new HashMap<>();
                    orderNum.put("userId", wechat.getParentId());
                    wechat.setTotalOrderNum((wechatDao.totalOrderNum(orderNum) == null) ? 0 : wechatDao.totalOrderNum(orderNum));
//                    wechat.setLevelId(memberLevel.getId());
                    //查询升级金额
                    QueryWrapper upgrade = new QueryWrapper();
                    upgrade.eq("block", "BLOCK_0029");
                    SysBlock upgradeB = blockDao.selectOne(upgrade);
                    //查询下单人的总金额
                    Map<String, Object> orderMy = new HashMap<>();
                    orderMy.put("uid", wechat.getId());
                    wechatDao.orderTotal(orderMy);
                    double orderTotal = ((wechatDao.orderTotal(orderMy) == null) ? 0 : wechatDao.orderTotal(orderMy));
                    wechat.setUpgradeMoeny(wechat.getTotalOrderNum() + orderTotal);
                    if (wechat.getUpgradeMoeny() >= Double.parseDouble(upgradeB.getRemark())) {
                        //升级会员的保存接口
                        memberLevelOrderService.saveUpgradeBMember(memberLevelOrder.get(0).getLevelId(), uid);

//                        //A的折扣价
//                        QueryWrapper queryWrapperA = new QueryWrapper();
//                        queryWrapperA.eq("block", "BLOCK_0016");
//                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
//                        //B的折扣价
//                        QueryWrapper queryWrapperB = new QueryWrapper();
//                        queryWrapperB.eq("block", "BLOCK_0017");
//                        SysBlock bossB = blockDao.selectOne(queryWrapperB);
//
//                        // 计算提成
//                        double shareMoney = salesPrice * (10 / 100.00);
//                        Wechat parent = wechatDao.selectById(wechat.getParentId());
//                        if (parent != null) {
//                            XfLogEntity xfLog = new XfLogEntity();
//                            xfLog.setXfBalance(shareMoney);
//                            xfLog.setBeforeBalane(parent.getBalance());
//                            xfLog.setAfterBalance(parent.getBalance() + shareMoney);
//                            xfLog.setCreateBy(uid);
//                            xfLog.setUserId(wechat.getParentId());
//                            xfLog.setOrderId(commodityOrderEntity.getId());
//                            xfLog.setXfClassify("VIP_JICHA_SHARING");
//                            xfLog.setXfRemark("会员级差分销收入");
//                            xfLog.setXfAccount(0);
//                            xfLogService.save(xfLog);
//                        }
                    }
                }
            }
        }

    }


    /**
     * 下单
     * levelId 分享人的会员ID
     *
     * @return Result
     */
    public void sales(String levelId, CommodityOrderEntity orderResult, double salesPrice) {
        //4：查到了说明邀请人已经购买会员，下一步再查邀请人是A套餐会员还是B套餐会员

        QueryWrapper level = new QueryWrapper<>();
        level.eq("id", levelId);
        MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
        if (memberLevel != null) {
            // 判断是A就代表是A套餐会员
            // 分享人是A
            if (memberLevel.getLevelType().equals("A")) {
                //查询A邀请的购买人是否是会员
                QueryWrapper queryUid = new QueryWrapper<>();
                queryUid.eq("uid", orderResult.getUid());
                queryUid.orderByDesc("pay_time");
                queryUid.eq("is_delete", 0);
                queryUid.eq("is_pay", 1);
                // 下单人级别
                List<MemberLevelOrderEntity> memberLevelUidOrder = memberLevelOrderDao.selectList(queryUid);
                if (memberLevelUidOrder.size() > 0) {
                    //A套餐会员邀请的是B套餐会员
                    QueryWrapper levelB = new QueryWrapper<>();
                    levelB.eq("id", memberLevelUidOrder.get(0).getLevelId());
                    MemberLevelEntity memberLevelB = memberLevelDao.selectOne(levelB);
                    if (memberLevelB != null) {
                        if (memberLevelB.getLevelType().equals("B")) {
                            //判断A是不是B的直属上级，只有A是B的上级才能给A返钱，不是就不能反
                            QueryWrapper superior = new QueryWrapper<>();
                            superior.eq("id", orderResult.getUid());
                            // 下单人
                            // 下单人上级 == 分享人
                            Wechat memberSuperior = wechatDao.selectOne(superior);
                            if (memberSuperior.getParentId().equals(orderResult.getUserId())) {
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
                                    if (bossB != null) {
                                        //查到了就计算 A套餐会员分享B套餐会员购买
                                        //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                                        double shareMoney = salesPrice * (10 / 100.00);
                                        UpdateWrapper<CommodityOrderEntity> order = new UpdateWrapper<>();
                                        order.eq("id", orderResult.getId());
                                        order.set("user_id", "");
                                        commodityOrderDao.update(null, order);

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
                        // B的上级是A，B升级成C后，C下单，还要给A返钱  暂时分开
                        if (memberLevelB.getLevelType().equals("C")) {
                            //判断A是不是B的直属上级，只有A是B的上级才能给A返钱，不是就不能反
                            QueryWrapper superior = new QueryWrapper<>();
                            superior.eq("id", orderResult.getUid());
                            // 下单人
                            // 下单人上级 == 分享人
                            Wechat memberSuperior = wechatDao.selectOne(superior);
                            if (memberSuperior.getParentId().equals(orderResult.getUserId())) {
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
                                    if (bossB != null) {
                                        //查到了就计算 A套餐会员分享B套餐会员购买
                                        //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                                        double shareMoney = salesPrice * (10 / 100.00);
                                        UpdateWrapper<CommodityOrderEntity> order = new UpdateWrapper<>();
                                        order.eq("id", orderResult.getId());
                                        order.set("user_id", "");
                                        commodityOrderDao.update(null, order);

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
                        double shareMoney = salesPrice * ((Double.valueOf(100) - Double.valueOf(boss.getRemark())) / 100);
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
                //判断是B就代表是B套餐会员
            } else if (memberLevel.getLevelType().equals("B")) {
                //B套餐会员邀请的普通用户
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("block", "BLOCK_0017");
                SysBlock boss = blockDao.selectOne(queryWrapper);
                Wechat wechat = wechatService.detail(orderResult.getUserId());
                if (boss != null) {
                    //查到了就计算
                    //计算公式: A赚差价=商品原价*40%（100%-60%）
                    double shareMoney = salesPrice * ((Double.valueOf(100) - Double.valueOf(boss.getRemark())) / 100);
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
                if (!higherUp.getParentId().equals(0)) {
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
                        if (bossB != null) {
                            //B会员邀请普通用户下单，给B按比例把钱 ，给A反级差钱
                            //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                            double shareMoney = salesPrice * (10 / 100.00);

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
            } else if (memberLevel.getLevelType().equals("C")) {
                //B套餐会员邀请的普通用户
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("block", "BLOCK_0030");
                SysBlock boss = blockDao.selectOne(queryWrapper);
                Wechat wechat = wechatService.detail(orderResult.getUserId());
                // 一级上级
                if (boss != null) {
                    //查到了就计算
                    //计算公式: A赚差价=商品原价*40%（100%-60%）
                    double shareMoney = salesPrice * ((Double.valueOf(100) - Double.valueOf(boss.getRemark())) / 100);
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


                // C 的上级给级差
                //查分享人B会员是否有上级
                QueryWrapper userId = new QueryWrapper<>();
                userId.eq("id", orderResult.getUserId());
                Wechat higherUp = wechatDao.selectOne(userId);
                if (!higherUp.getParentId().equals(0)) {
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
                        if (bossB != null) {
                            //B会员邀请普通用户下单，给B按比例把钱 ，给A反级差钱
                            //计算公式: A赚取=商品原价*级差20%（80%-60%） 80%是B的折扣价 60%是A的折扣价
                            double shareMoney = salesPrice * (10 / 100.00);
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
            }
        }
    }

    //会员日购买产品，积分翻倍
    public void membershipDay(String uid, CommodityOrderEntity commodityOrder) {
        //会员日购买产品，积分翻倍
        QueryWrapper query = new QueryWrapper<>();
        query.eq("uid", uid);
        query.eq("is_delete", 0);
        query.orderByDesc("pay_time");
        query.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
        if (memberLevelOrder.size() > 0) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(memberLevelOrder.get(0).getPayTime());
            calendar.add(Calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
            Date date = calendar.getTime();
            //当天日期等于会员日————积分翻倍
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if (formatter.format(date).equals(formatter.format(commodityOrder.getPayTime())) ||
                    formatter.format(memberLevelOrder.get(0).getPayTime()).equals(formatter.format(commodityOrder.getPayTime()))) {

                UpdateWrapper<CommodityOrderEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", commodityOrder.getId());
                updateWrapper.set("points", commodityOrder.getPoints() * 2);
                commodityOrderService.update(null, updateWrapper);


//                UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//                wrapperIntegralMoney.eq("id", wechat.getId());
//                wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integerTotal);
//                wechatDao.update(null, wrapperIntegralMoney);
//
//                UpdateWrapper<Wechat> wrapperIntegerMember = new UpdateWrapper<>();
//                wrapperIntegerMember.eq("id", wechat.getId());
//                wrapperIntegerMember.set("integral_money", wechat.getIntegralMoney() + integerTotal);
//                wechatDao.update(null, wrapperIntegerMember);

//                IntegralLogEntity xfLogMember = new IntegralLogEntity();
//                xfLogMember.setXfBalance(integerTotal);
//                xfLogMember.setBeforeBalane(wechat.getIntegralMoney());
//                xfLogMember.setAfterBalance(wechat.getIntegralMoney() + integerTotal);
//                xfLogMember.setCreateBy(wechat.getId());
//                xfLogMember.setUserId(wechat.getId());
//                xfLogMember.setOrderId(item.getId());
//                xfLogMember.setXfClassify("INTEGRAL");
//                xfLogMember.setXfRemark("下单积分收入");
//                integralLogService.save(xfLogMember);
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
    public boolean updatePayStatus(CommodityOrderEntity commodityOrderEntity) {
        try {
            UpdateWrapper<CommodityOrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", commodityOrderEntity.getId());
            wrapper.set("is_pay", 1);
            wrapper.set("order_status", "TO_DELIVER");
            commodityOrderDao.update(null, wrapper);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 确认收货
     *
     * @param commodityOrderEntity
     * @return boolean
     */
    @Override
    public boolean saveReceiving(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {

                UpdateWrapper<CommodityOrderEntity> updateOrder = new UpdateWrapper<>();
                updateOrder.eq("id", commodityOrderEntity.getId());
                updateOrder.set("update_time", new Date());
                updateOrder.set("update_by", authUtils.AuthUser().getUserId());
                updateOrder.set("order_status", "RECEIVED");
                commodityOrderDao.update(null, updateOrder);


                CommodityOrderEntity commodityOrder = commodityOrderDao.selectById(commodityOrderEntity.getId());

                Wechat wechat = wechatDao.selectById(commodityOrder.getUid());

                //生日当天购买送双倍积分
                if (wechat.getBirthday() != null) {
                    SimpleDateFormat sd = new SimpleDateFormat("MM-dd");
                    if (sd.format(wechat.getBirthday()).equals(sd.format(new Date()))) {
                        this.birthdayBuy(commodityOrder);
                    } else {
                        //在促销活动内下单积分*倍率
                        if (!StringUtils.isBlank(commodityOrder.getPromotionActivitysId())) {
                            PromotionActivitysEntity promotionActivitys = promotionActivitysDao.selectById(commodityOrder.getPromotionActivitysId());
                            if (promotionActivitys != null) {
                                //支付时间在促销时间内
                                this.promotionTimeBuy(commodityOrder, promotionActivitys.getRate());
                                return true;
                            }
                        }
                    }
                }

                //确认收货积分到账
                this.returnPoints(commodityOrder, wechat);

//                //会员日购买产品，积分翻倍----不是会员日就1:1
//                this.memberPoints(commodityOrder);


                // 处理已到账金额
                QueryWrapper<XfLogEntity> xfLogWrapper = new QueryWrapper<>();
                xfLogWrapper.eq("order_id", commodityOrderEntity.getId());
                xfLogWrapper.eq("xf_account", 0);
                List<XfLogEntity> xfLogEntityList = xfLogDao.selectList(xfLogWrapper);
                xfLogEntityList.forEach(xfLogEntity -> {

                    if (xfLogEntity != null) {
                        Wechat wechatReceive = wechatDao.selectById(xfLogEntity.getUserId());
                        if (wechatReceive != null) {
                            UpdateWrapper<XfLogEntity> updateWrapper = new UpdateWrapper<>();
                            updateWrapper.eq("id", xfLogEntity.getId());
                            updateWrapper.set("collected_balances", xfLogEntity.getXfBalance());
                            updateWrapper.set("xf_account", 1);
                            updateWrapper.set("update_time", new Date());
                            updateWrapper.set("before_balane", wechatReceive.getBalanceWithdrawn());
                            updateWrapper.set("after_balance", wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance());
                            xfLogService.update(null, updateWrapper);

                            //当前账户+到账金额
                            double receivedMoney = wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance();
                            UpdateWrapper<Wechat> updateBalanceWrapper = new UpdateWrapper<>();
                            updateBalanceWrapper.eq("id", wechatReceive.getId());
                            updateBalanceWrapper.set("balance_withdrawn", receivedMoney);
                            wechatService.update(null, updateBalanceWrapper);
                        }

                    }
                });


            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 确认收货积分到账
     *
     * @param
     * @return boolean
     */
    public boolean returnPoints(CommodityOrderEntity commodityOrder, Wechat wechat) {
        try {
            if (StringUtils.isBlank(commodityOrder.getId())) {

            } else {
                UpdateWrapper<CommodityOrderEntity> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", commodityOrder.getId());
                wrapper.set("is_return_points", 1);
                commodityOrderDao.update(null, wrapper);

                UpdateWrapper<Wechat> wechatWrapper = new UpdateWrapper<>();
                wechatWrapper.eq("id", wechat.getId());
                wechatWrapper.set("integral_money", wechat.getIntegralMoney() + commodityOrder.getPoints());
                wechatDao.update(null, wechatWrapper);

                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance(commodityOrder.getPoints());
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance(wechat.getIntegralMoney() + commodityOrder.getPoints());
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(commodityOrder.getId());
                xfLog.setXfClassify("SHOPPING_POINTS");
                xfLog.setXfRemark("下单返积分");
                integralLogService.save(xfLog);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

//    /**
//     * 确认收货
//     *
//     * @param commodityOrderEntity
//     * @return boolean
//     */
//    @Override
//    public boolean saveReceiving(CommodityOrderEntity commodityOrderEntity) throws IOException {
//        try {
//            if (StringUtils.isBlank(commodityOrderEntity.getId())) {
//
//            } else {
//                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
//                commodityOrderEntity.setUpdateTime(new Date());
//                commodityOrderEntity.setOrderStatus("RECEIVED");
//                this.updateById(commodityOrderEntity);
//
//
//
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//        return true;
//    }

    /**
     * 确认收货第二天奖励到账-自动处理
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean saveReceivingMall() {
        try {
            List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.oneDayBeforeOrder();
            if (commodityOrderList.size() > 0) {
                commodityOrderList.forEach(item -> {
                    QueryWrapper<XfLogEntity> xfLogWrapper = new QueryWrapper<>();
                    xfLogWrapper.eq("order_id", item.getId());
                    xfLogWrapper.eq("xf_account", 0);
                    XfLogEntity xfLogEntity = xfLogDao.selectOne(xfLogWrapper);
                    if (xfLogEntity != null) {
                        UpdateWrapper<XfLogEntity> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", xfLogEntity.getId());
                        updateWrapper.set("collected_balances", xfLogEntity.getXfBalance());
                        updateWrapper.set("xf_account", 1);
                        xfLogService.update(null, updateWrapper);
                    }
                });
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 7日后到账(待确认收货自动变已确认收货)-自动处理
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean saveToReceivingMall() {
        try {
            List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.sevenDayBeforeOrder();
            commodityOrderList.forEach(item -> {
                //当前用户
                Wechat wechat = wechatDao.selectById(item.getUid());
                //待确认收货变已确认收货
                UpdateWrapper<CommodityOrderEntity> updateOrderStatus = new UpdateWrapper<>();
                updateOrderStatus.eq("id", item.getId());
                updateOrderStatus.set("order_status", "RECEIVED");
                commodityOrderService.update(null, updateOrderStatus);

                QueryWrapper<XfLogEntity> xfLogWrapper = new QueryWrapper<>();
                xfLogWrapper.eq("order_id", item.getId());
                xfLogWrapper.eq("xf_account", 0);
                List<XfLogEntity> xfLogEntityList = xfLogDao.selectList(xfLogWrapper);
                xfLogEntityList.forEach(xfLogEntity -> {
                    if (xfLogEntity != null) {

                        if ("DISCOUNT".equals(xfLogEntity.getXfClassify())) {
                            return;
                        }
                        Wechat wechatReceive = wechatDao.selectById(xfLogEntity.getUserId());

                        if (wechatReceive != null) {
                            UpdateWrapper<XfLogEntity> updateWrapper = new UpdateWrapper<>();
                            updateWrapper.eq("id", xfLogEntity.getId());
                            updateWrapper.set("collected_balances", xfLogEntity.getXfBalance());
                            updateWrapper.set("xf_account", 1);
                            updateWrapper.set("update_time", new Date());
                            updateWrapper.set("before_balane", wechatReceive.getBalanceWithdrawn());
                            updateWrapper.set("after_balance", wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance());
                            xfLogService.update(null, updateWrapper);

                            //当前账户+到账金额
                            double receivedMoney = wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance();
                            UpdateWrapper<Wechat> updateBalanceWrapper = new UpdateWrapper<>();
                            updateBalanceWrapper.eq("id", wechatReceive.getId());
                            updateBalanceWrapper.set("balance_withdrawn", receivedMoney);
                            wechatService.update(null, updateBalanceWrapper);

                        }


                    }
                });


            });
            //7日后下单反积分到账(待确认收货自动变已确认收货)-自动处理
            List<CommodityOrderEntity> commodityPointsOrderList = commodityOrderDao.sevenDayPointsOrder();
            commodityPointsOrderList.forEach(item -> {
                UpdateWrapper<CommodityOrderEntity> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", item.getId());
                wrapper.set("is_return_points", 1);
                commodityOrderDao.update(null, wrapper);

                Wechat wechat = wechatDao.selectById(item.getUid());

                UpdateWrapper<Wechat> wechatWrapper = new UpdateWrapper<>();
                wechatWrapper.eq("id", wechat.getId());
                wechatWrapper.set("integral_money", wechat.getIntegralMoney() + item.getPoints());
                wechatDao.update(null, wechatWrapper);

                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance(item.getPoints());
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance(wechat.getIntegralMoney() + item.getPoints());
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(item.getId());
                xfLog.setXfClassify("SHOPPING_POINTS");
                xfLog.setXfRemark("下单返积分");
                integralLogService.saveInternal(xfLog);
            });

        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 重新处理日志
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean saveToReceivingMallTransfer() {
        try {
            List<XfLogEntity> xfLogEntityList = xfLogDao.saveToReceivingMallTransfer();
            xfLogEntityList.forEach(xfLogEntity -> {
                if (xfLogEntity != null) {
                    // 取 这个人的上一条数据
                    double beforeBalance = 0;
                    double afterBalance = 0;
                    List<XfLogEntity> getFirst = xfLogDao.getFirst(xfLogEntity.getUserId());
                    if (getFirst.size() > 0) {
                        beforeBalance = getFirst.get(0).getAfterBalance();
                    }

                    afterBalance = beforeBalance + xfLogEntity.getCollectedBalances();

                    Wechat wechatReceive = wechatDao.selectById(xfLogEntity.getUserId());
                    if (wechatReceive != null) {
                        UpdateWrapper<XfLogEntity> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", xfLogEntity.getId());
                        updateWrapper.set("before_balane", beforeBalance);
                        updateWrapper.set("after_balance", afterBalance);
                        updateWrapper.set("is_transfer", 1);
                        xfLogService.update(null, updateWrapper);

                    }
                }
            });

        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 会员订单自动到账
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean vipRewardDeal() {
        try {
            List<XfLogEntity> xfLogEntityList = xfLogDao.toReward();
            xfLogEntityList.forEach(xfLogEntity -> {
                if (xfLogEntity != null) {
                    Wechat wechatReceive = wechatDao.selectById(xfLogEntity.getUserId());
                    if (wechatReceive != null) {
                        UpdateWrapper<XfLogEntity> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id", xfLogEntity.getId());
                        updateWrapper.set("collected_balances", xfLogEntity.getXfBalance());
                        updateWrapper.set("xf_account", 1);
                        updateWrapper.set("update_time", new Date());
                        updateWrapper.set("before_balane", wechatReceive.getBalanceWithdrawn());
                        updateWrapper.set("after_balance", wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance());
                        xfLogService.update(null, updateWrapper);

                        //当前账户+到账金额
                        double receivedMoney = wechatReceive.getBalanceWithdrawn() + xfLogEntity.getXfBalance();
                        UpdateWrapper<Wechat> updateBalanceWrapper = new UpdateWrapper<>();
                        updateBalanceWrapper.eq("id", wechatReceive.getId());
                        updateBalanceWrapper.set("balance_withdrawn", receivedMoney);
                        wechatService.update(null, updateBalanceWrapper);
                    }
                }
            });

        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 确认收货后第二天返积分
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean saveReceivingPoints() {
        try {

            List<CommodityOrderEntity> commodityOrderList = commodityOrderDao.oneDayBeforeOrder();
            if (commodityOrderList.size() > 0) {
                commodityOrderList.forEach(item -> {
                    //确认收货大于1天且未返积分
//                    long diffInHours = new Date().getTime() - item.getUpdateTime().getTime();
//                    long hours = diffInHours / 1000 / 60/60;
//                    if (hours >=24) {
                    CommodityOrderEntity commodityOrder = commodityOrderDao.selectById(item.getId());
                    Wechat wechat = wechatDao.selectById(commodityOrder.getUid());
                    //生日当天购买送双倍积分
                    if (wechat.getBirthday() != null) {
                        SimpleDateFormat sd = new SimpleDateFormat("MM-dd");
                        if (sd.format(wechat.getBirthday()).equals(sd.format(new Date()))) {
                            this.birthdayBuy(commodityOrder);
                            return;
                        } else {
                            //在促销活动内下单积分*倍率
                            if (!StringUtils.isBlank(commodityOrder.getPromotionActivitysId())) {
                                PromotionActivitysEntity promotionActivitys = promotionActivitysDao.selectById(commodityOrder.getPromotionActivitysId());
                                if (promotionActivitys != null) {
                                    //支付时间在促销时间内
                                    this.promotionTimeBuy(commodityOrder, promotionActivitys.getRate());
                                    return;
                                }
                            }
                        }
                    }

                });
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

//    //
//    public boolean memberPoints(CommodityOrderEntity commodityOrder) {
//        try {
//            if (StringUtils.isBlank(commodityOrder.getId())) {
//
//            } else {
//                //会员日购买产品，积分翻倍
//                QueryWrapper query = new QueryWrapper<>();
//                query.eq("uid", commodityOrder.getUid());
//                query.eq("is_delete", 0);
//                query.eq("is_pay", 1);
//                MemberLevelOrderEntity memberLevelOrder = memberLevelOrderDao.selectOne(query);
//                if (memberLevelOrder != null) {
//                    Calendar calendar = new GregorianCalendar();
//                    calendar.setTime(memberLevelOrder.getPayTime());
//                    calendar.add(Calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                    Date date = calendar.getTime();
//                    //当天日期等于会员日————积分翻倍
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                    if (formatter.format(date).equals(formatter.format(commodityOrder.getPayTime()))) {
//                        // 将 BigDecimal 转换为 Integer
//                        Integer integerMember = commodityOrder.getTotal().intValue();
//                        Integer integerTotal = integerMember * 2;
//
//                        UpdateWrapper<Wechat> wrapperIntegerMember = new UpdateWrapper<>();
//                        wrapperIntegerMember.eq("id", wechat.getId());
//                        wrapperIntegerMember.set("integral_money", wechat.getIntegralMoney() + integerTotal);
//                        wechatDao.update(null, wrapperIntegerMember);
//
//                        IntegralLogEntity xfLogMember = new IntegralLogEntity();
//                        xfLogMember.setXfBalance(integerTotal);
//                        xfLogMember.setBeforeBalane(wechat.getIntegralMoney());
//                        xfLogMember.setAfterBalance(wechat.getIntegralMoney() + integerTotal);
//                        xfLogMember.setCreateBy(wechat.getId());
//                        xfLogMember.setUserId(wechat.getId());
//                        xfLogMember.setOrderId(commodityOrderEntity.getId());
//                        xfLogMember.setXfClassify("INTEGRAL");
//                        xfLogMember.setXfRemark("下单积分收入");
//                        integralLogService.save(xfLogMember);
//
//                    } else {
//                        //购买产品送积分
//                        //2: 购买产品送积分：购买送积分，1元=1积分
//
//                        // 将 BigDecimal 转换为 Integer
//                        Integer integer = commodityOrder.getTotal().intValue();
//
//                        UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//                        wrapperIntegralMoney.eq("id", wechat.getId());
//                        wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
//                        wechatDao.update(null, wrapperIntegralMoney);
//
//                        IntegralLogEntity xfLog = new IntegralLogEntity();
//                        xfLog.setXfBalance(integer);
//                        xfLog.setBeforeBalane(wechat.getIntegralMoney());
//                        xfLog.setAfterBalance(wechat.getIntegralMoney() + integer);
//                        xfLog.setCreateBy(wechat.getId());
//                        xfLog.setUserId(wechat.getId());
//                        xfLog.setOrderId(commodityOrderEntity.getId());
//                        xfLog.setXfClassify("INTEGRAL");
//                        xfLog.setXfRemark("下单积分收入");
//                        integralLogService.save(xfLog);
//                    }
//                } else {
//                    //购买产品送积分
//                    //2: 购买产品送积分：购买送积分，1元=1积分
//
//                    // 将 BigDecimal 转换为 Integer
//                    Integer integer = commodityOrder.getTotal().intValue();
//
//                    UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//                    wrapperIntegralMoney.eq("id", wechat.getId());
//                    wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
//                    wechatDao.update(null, wrapperIntegralMoney);
//
//                    IntegralLogEntity xfLog = new IntegralLogEntity();
//                    xfLog.setXfBalance(integer);
//                    xfLog.setBeforeBalane(wechat.getIntegralMoney());
//                    xfLog.setAfterBalance(wechat.getIntegralMoney() + integer);
//                    xfLog.setCreateBy(wechat.getId());
//                    xfLog.setUserId(wechat.getId());
//                    xfLog.setOrderId(commodityOrderEntity.getId());
//                    xfLog.setXfClassify("INTEGRAL");
//                    xfLog.setXfRemark("下单积分收入");
//                    integralLogService.save(xfLog);
//                }
//            }
//        } catch (Exception ex) {
//            throw ex;
//        }
//        return true;
//    }

    //促销活动内购买商品返积分
    public boolean promotionTimeBuy(CommodityOrderEntity commodityOrderEntity, int rate) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                //3: 促销活动内购买商品返积分：购买送积分，1元=1积分

                // 将 BigDecimal 转换为 Integer
                double integer = commodityOrderEntity.getTotal() * rate;
                //购买人
                Wechat wechat = wechatDao.selectById(commodityOrderEntity.getUid());

                UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
                wrapperIntegralMoney.eq("id", wechat.getId());
                wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
                wechatDao.update(null, wrapperIntegralMoney);

                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance((int) integer);
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance((int) (wechat.getIntegralMoney() + integer));
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(commodityOrderEntity.getId());
                xfLog.setXfClassify("PROMOTION_INTEGRAL");
                xfLog.setXfRemark("促销活动下单积分收入");
                integralLogService.save(xfLog);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    //生日当天购买商品返积分
    public boolean birthdayBuy(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                //4: 生日当天购买商品返积分：购买送积分，1元=1积分

                // 将 BigDecimal 转换为 Integer
                double integer = commodityOrderEntity.getTotal() * 2;
                //购买人
                Wechat wechat = wechatDao.selectById(commodityOrderEntity.getUid());

                UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
                wrapperIntegralMoney.eq("id", wechat.getId());
                wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + integer);
                wechatDao.update(null, wrapperIntegralMoney);

                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance((int) integer);
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance((int) (wechat.getIntegralMoney() + integer));
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(commodityOrderEntity.getId());
                xfLog.setXfClassify("BIRTHDAY_INTEGRAL");
                xfLog.setXfRemark("生日当天下单积分收入");
                integralLogService.save(xfLog);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 退货申请
     *
     * @param commodityOrderEntity
     * @return boolean
     */
    @Override
    public boolean saveRefund(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                UpdateWrapper<CommodityOrderEntity> updateOrderStatus = new UpdateWrapper<>();
                updateOrderStatus.eq("id", commodityOrderEntity.getId());
                updateOrderStatus.set("approve_status", "TO_APPROVE");
                updateOrderStatus.set("order_status", "APPLY_REFUND");
                updateOrderStatus.set("refund_express_no", commodityOrderEntity.getRefundExpressNo());
                updateOrderStatus.set("refund_reason", commodityOrderEntity.getRefundReason());
                commodityOrderService.update(null, updateOrderStatus);

//                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
//                commodityOrderEntity.setUpdateTime(new Date());
//                commodityOrderEntity.setApproveStatus("TO_APPROVE");
//                commodityOrderEntity.setOrderStatus("APPLY_REFUND");
//                this.updateById(commodityOrderEntity);
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
    public boolean editAddress(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUpdateTime(new Date());
                this.updateById(commodityOrderEntity);
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
    public boolean save(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUpdateTime(new Date());
                commodityOrderEntity.setOrderStatus("TO_DELIVER");
                this.updateById(commodityOrderEntity);
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
    public boolean saveProcess(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderEntity.setUpdateTime(new Date());
                commodityOrderEntity.setOrderStatus("TO_RECEIVE");
                this.updateById(commodityOrderEntity);
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
     * 获取数据库中最大 编号
     *
     * @return
     */
    public long maxNo() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            List<CommodityOrderEntity> scheduleList = commodityOrderDao.selectList(new QueryWrapper<CommodityOrderEntity>()
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
    public CommodityOrderEntity detail(String id) {
        return commodityOrderDao.selectById(id);
    }

    /**
     * 查询该用户是否购买过新人专享商品
     *
     * @return
     */
    @Override
    public Result newOrderdetail() {
        try {
            QueryWrapper<CommodityOrderEntity> query = new QueryWrapper<>();
            query.eq("uid", authUtils.AuthUser().getUserId());
            query.eq("is_pay", 1);
            query.eq("is_new_order", 1);
            CommodityOrderEntity commodityOrderEntity = commodityOrderDao.selectOne(query);
            if (commodityOrderEntity != null) {
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
            UpdateWrapper<CommodityOrderEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", id);
            wrapper.set("approve_status", "PASS");
            wrapper.set("order_status", "REFUNDED");
            commodityOrderDao.update(null, wrapper);

            CommodityOrderEntity commodityOrder = commodityOrderDao.selectById(id);
            //该订单使用购物金
            if (commodityOrder.getDiscount() > 0) {
                //下单人信息
                Wechat wechat = wechatDao.selectById(commodityOrder.getUid());
                if (wechat != null) {
                    double orderShoppingMoney = 0;
                    orderShoppingMoney = wechat.getShoppingMoney() + commodityOrder.getDiscount();
                    UpdateWrapper<Wechat> applyWrapper = new UpdateWrapper<>();
                    applyWrapper.eq("id", wechat.getId());
                    applyWrapper.set("shopping_money", orderShoppingMoney);
                    wechatDao.update(null, applyWrapper);

                    XfLogEntity discountXfLog = new XfLogEntity();
                    discountXfLog.setXfBalance(commodityOrder.getDiscount());
                    discountXfLog.setBeforeBalane(wechat.getShoppingMoney());
                    discountXfLog.setAfterBalance(orderShoppingMoney);
                    discountXfLog.setCreateBy(commodityOrder.getUid());
                    discountXfLog.setUserId(commodityOrder.getUid());
                    discountXfLog.setOrderId(id);
                    discountXfLog.setXfAccount(3);
                    discountXfLog.setXfClassify("DISCOUNT_REFUND");
                    discountXfLog.setXfRemark("购物金退还");
                    xfLogService.save(discountXfLog);
                }
            }

            if (commodityOrder != null) {
                QueryWrapper<XfLogEntity> queryLog = new QueryWrapper<>();
                queryLog.eq("order_id", id);
                queryLog.ne("xf_balance", 0);
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

        } catch (Exception ex) {
            throw ex;
        }
    }


    /**
     * 驳回退货申请
     */
    @Override
    public boolean reject(CommodityOrderEntity commodityOrderEntity) {
        try {
            if (StringUtils.isBlank(commodityOrderEntity.getId())) {

            } else {
//                commodityOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
//                commodityOrderEntity.setUpdateTime(new Date());
//                commodityOrderEntity.setApproveStatus("REJECT");
//                this.updateById(commodityOrderEntity);

                UpdateWrapper<CommodityOrderEntity> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", commodityOrderEntity.getId());
                wrapper.set("approve_status", "REJECT");
                wrapper.set("update_time", new Date());
                wrapper.set("approve_remark", commodityOrderEntity.getApproveRemark());
                commodityOrderDao.update(null, wrapper);


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
    public CommodityOrderEntity orderDetail(String id) {
        CommodityOrderEntity commodityOrderEntity = commodityOrderDao.selectById(id);
        //取详情多张图片
        UpdateWrapper<CommodityOrderCommodityEntity> wrapperOrder = new UpdateWrapper<>();
        wrapperOrder.eq("order_id", commodityOrderEntity.getId());
        wrapperOrder.eq("is_delete", 0);
        List<CommodityOrderCommodityEntity> orderList = commodityOrderCommodityDao.selectList(wrapperOrder);
        orderList.forEach(item -> {
            CommoditySpecEntity commoditySpecEntity = commoditySpecService.detail(item.getSpecId());
            if (commoditySpecEntity != null) {
                item.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
            }
        });
        commodityOrderEntity.setOrderList(orderList);
        return commodityOrderEntity;
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
            CommodityOrderEntity removeEntity = commodityOrderDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityOrderDao.updateById(removeEntity);
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
                CommodityOrderEntity removeEntity = commodityOrderDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderDao.updateById(removeEntity);
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
            CommodityOrderListener listener = new CommodityOrderListener();
            EasyExcel.read(file.getInputStream(), CommodityOrderEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityOrderEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityOrderDao.insert(uploadItem);
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
        //待付款的数量
        repMap.put("noIsPayNum", commodityOrderDao.orderNoIsPay(uid).size());
        //待发货的数量
        repMap.put("toDeliverNum", commodityOrderDao.orderToDeliverNum(uid).size());
        //待收货的数量
        repMap.put("toReceiveNum", commodityOrderDao.orderToReceiveNum(uid).size());
        return Result.success(repMap);
    }

//    //订单数据监控 订单号+商品销售价+实际支付价格+会员类型（折扣比例）
//    public void dataMonitor(String orderId, String orderUid, BigDecimal total) {
//
//        //查下单人属于什么会员
//        QueryWrapper query = new QueryWrapper<>();
//        query.eq("uid", orderUid);
//        query.eq("is_delete", 0);
//        query.orderByDesc("pay_time");
//        query.eq("is_pay", 1);
//        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
//        String levelType = "";
//        if (memberLevelOrder.size() > 0) {
//            //会员类型
//            MemberLevelEntity memberLevel = memberLevelDao.selectById(memberLevelOrder.get(0).getLevelId());
//            if (memberLevel != null) {
//                levelType = memberLevel.getLevelType();
//            }
//        } else {
//            levelType = "普通会员";
//        }
//        //查询不同会员的折扣比例
//        // block
//        String proportion = "";
//        QueryWrapper queryWrapperBlock = new QueryWrapper();
//        List<SysBlock> blockList = blockDao.selectList(queryWrapperBlock);
//        if ("A".equals(levelType)) {
//            Optional<SysBlock> blockA = blockList.stream().filter(s -> "BLOCK_0016".equals(s.getBlock())).findFirst();
//            proportion = blockA.get().getRemark();
//        } else if ("B".equals(levelType)) {
//            Optional<SysBlock> blockB = blockList.stream().filter(s -> "BLOCK_0017".equals(s.getBlock())).findFirst();
//            proportion = blockB.get().getRemark();
//        } else if ("C".equals(levelType)) {
//            Optional<SysBlock> blockC = blockList.stream().filter(s -> "BLOCK_0030".equals(s.getBlock())).findFirst();
//            proportion = blockC.get().getRemark();
//        } else {
//            proportion = "0";
//        }
//        List<CommodityOrderCommodityEntity> commodityOrderCommodityEntity = commodityOrderCommodityDao.queryOrderId(orderId);
//        String finalProportion = proportion;
//        String finalLevelType = levelType;
//        commodityOrderCommodityEntity.forEach(item -> {
//            //查询该订单购买产品的销售价
//            CommodityEntity commodity = commodityDao.selectById(item.getCommodityId());
//            if (commodity != null) {
//                String dataMonitor = "";
//                dataMonitor = orderId + commodity.getPrice() + total + finalLevelType + finalProportion;
//            }
//        });
//    }

}
