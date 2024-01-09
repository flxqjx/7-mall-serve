package com.xyhc.cms.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.config.auth.TokenUtils;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.MsCashOutEntity;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.MemberLevelOrderService;
import com.xyhc.cms.service.WechatService;
import com.xyhc.cms.utils.CommonUtitls;
import com.xyhc.cms.utils.QrCodeUtil;
import com.xyhc.cms.utils.WechatUtils;
import com.xyhc.cms.vo.common.WechatAccessTokenDto;
import com.xyhc.cms.vo.qrcode.WeChatQueryObject;
import com.xyhc.cms.vo.system.AppLoginDto;
import com.xyhc.cms.vo.system.LoginRepDto;
import com.xyhc.cms.vo.system.OpenIdDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 账号
 *
 * @Author apollo
 * 2022-05-11  10:55:00
 */
@Slf4j
@Service
public class WechatServiceImpl extends ServiceImpl<WechatDao, Wechat> implements WechatService {


    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    AuthUtils authUtils;

    @Resource
    CommonUtitls commonUtitls;

    @Resource
    MemberLevelOrderDao memberLevelOrderDao;

    @Resource
    CommodityOrderDao commodityOrderDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    MemberLevelDao memberLevelDao;
    @Resource
    ReceiveInfoDao receiveInfoDao;

    @Resource
    BlockDao blockDao;

    @Resource
    XfLogDao xfLogDao;


    @Value("${miniProgram.appId}")
    private String appId;
    @Value("${miniProgram.appSecret}")
    private String appSecret;


    @Value("${wechatProgram.appId}")
    private String wechatAppId;
    @Value("${wechatProgram.appSecret}")
    private String wechatAppSecret;

    @Resource
    WechatUtils wechatUtils;
    @Resource
    ApplyJoinDao applyJoinDao;
    @Resource
    MemberLevelOrderService memberLevelOrderService;
    @Resource
    SysAuthTokenDao sysAuthTokenDao;

    @Autowired
    QrCodeUtil qrCodeUtil;
    @Resource
    WechatService wechatService;


    /**
     * 分页查询商家分类
     *
     * @param params typeClass 查询几级的分类
     *               可以给oneId,查询某个一级下的二级
     * @return
     */
    @Override
    public PageUtils wechatPage(Map<String, Object> params) {
        IPage<Wechat> list = new Query<Wechat>().getPage(params, "create_time", false);
        List<Wechat> records = wechatDao.page(list, params);
        list.setRecords(records);
        PageUtils ownerVm = new PageUtils(records, ((int) list.getTotal()), ((int) list.getSize()), ((int) list.getCurrent()));
        return ownerVm;
    }

    @Override
    public List<Wechat> all(Map<String, Object> params) {
        return wechatDao.all(params);
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("memberLevel", params.get("memberLevel"));
        params.put("uid", params.get("uid"));
        IPage<Wechat> page = new Query<Wechat>().getPage(params, "create_time", false);
        List<Wechat> records = wechatDao.page(page, params);
        Map<String, Object> maps = new HashMap<>();
        List<Wechat> wechatList = wechatDao.all(maps);
        page.setRecords(records);
        //查询申请会员的信息
//        List<ApplyJoinEntity> apllyList = applyJoinDao.allJoin();
        //查询购买的会员身份
//        List<MemberLevelEntity> memberLevelList = memberLevelDao.allList();
        //查询是否购买会员
//        List<MemberLevelOrderEntity> memberOrderLevelList = memberLevelOrderDao.allList();
//        List<ReceiveInfoEntity> siteList = receiveInfoDao.allCity();
        Map<String, Object> addressList = commonUtitls.cityList("-1");
        records.forEach(item -> {
            //所属人
            Optional<Wechat> parent = wechatList.stream().filter(s -> s.getId().equals(item.getParentId())).findFirst();
            if (parent.isPresent() && parent != null) {
                item.setParentName(parent.get().getNickName());
            }
            item.setBalance(item.getSumBalance());

//            //查询我的团队总共有多少人
//            QueryWrapper query = new QueryWrapper();
//            query.eq("parent_id", item.getId());
//            List<Wechat> list = wechatDao.selectList(query);
//            list.forEach(itemPid -> {
//                QueryWrapper levelOrder = new QueryWrapper<>();
//                levelOrder.eq("uid", itemPid.getId());
//                levelOrder.eq("is_delete", 0);
//                levelOrder.orderByDesc("pay_time");
//                levelOrder.eq("is_pay", 1);
//                List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(levelOrder);
//                if (memberLevelOrder.size() > 0) {
//                    List<Wechat> parentList = wechatDao.selectList(query);
//                    item.setParentSize(parentList.size());
//                    item.setTeamList(parentList);
//                }
//            });
            QueryWrapper memberLevel = new QueryWrapper<>();
            memberLevel.eq("uid", item.getId());
            memberLevel.eq("is_delete", 0);
            memberLevel.orderByDesc("pay_time");
            memberLevel.eq("is_pay", 1);
            List<MemberLevelOrderEntity> order = memberLevelOrderDao.selectList(memberLevel);
            if (order.size() > 0) {
//                item.setPhone(findJoin.getPhone());
//                item.setName(findJoin.getName());
//                item.setCardNoRearUrl(findJoin.getCardNoRearUrl());
//                item.setCardNoFrontUrl(findJoin.getCardNoFrontUrl());
                QueryWrapper memberLevelType = new QueryWrapper<>();
                memberLevelType.eq("id", order.get(0).getLevelId());
                MemberLevelEntity type = memberLevelDao.selectOne(memberLevelType);
                if (type != null) {
                    item.setMemberLevel(type.getLevelType());
                }
            }
//            QueryWrapper aplly = new QueryWrapper<>();
//            aplly.eq("uid", item.getId());
//            aplly.eq("is_delete", 0);
//            ApplyJoinEntity findJoin = applyJoinDao.selectOne(aplly);
//            if (findJoin != null) {
//                QueryWrapper memberLevel = new QueryWrapper<>();
//                memberLevel.eq("level_id", findJoin.getLevelId());
//                memberLevel.eq("uid", findJoin.getUid());
//                memberLevel.eq("is_pay", 1);
//                MemberLevelOrderEntity order = memberLevelOrderDao.selectOne(memberLevel);
//                if (order != null) {
//                    item.setPhone(findJoin.getPhone());
//                    item.setName(findJoin.getName());
//                    item.setCardNoRearUrl(findJoin.getCardNoRearUrl());
//                    item.setCardNoFrontUrl(findJoin.getCardNoFrontUrl());
//
//                    QueryWrapper memberLevelType = new QueryWrapper<>();
//                    memberLevelType.eq("id", findJoin.getLevelId());
//                    MemberLevelEntity type = memberLevelDao.selectOne(memberLevelType);
//                    if (type != null) {
//                        item.setMemberLevel(type.getLevelType());
//                    }
//                }
//            }

            //        List<ReceiveInfoEntity> siteList = receiveInfoDao.allCity();
            QueryWrapper memberLevelType = new QueryWrapper<>();
            memberLevelType.eq("user_id", item.getId());
            memberLevelType.eq("is_default", 1);
            ReceiveInfoEntity findSite = receiveInfoDao.selectOne(memberLevelType);
            if (findSite != null) {
                if (StringUtils.isNotEmpty(findSite.getProvinceId())) {
                    item.setProvinceId(addressList.get(findSite.getProvinceId()).toString());
                } else {
                    item.setProvinceId("无");
                }
                if (StringUtils.isNotEmpty(findSite.getCityId())) {
                    item.setCityId(addressList.get(findSite.getCityId()).toString());
                } else {
                    item.setCityId("无");
                }
                if (StringUtils.isNotEmpty(findSite.getAreaId())) {
                    item.setAreaId(addressList.get(findSite.getAreaId()).toString());
                } else {
                    item.setAreaId("无");
                }
                item.setAddress(findSite.getReceiveAddress());
            }

        });
        Map<String, Object> map = new HashMap<>();
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public Wechat detail(String id) {
        return wechatDao.selectById(id);
    }

    /**
     * 查询用户团队
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils teamPage(Map<String, Object> params) {
        IPage<Wechat> page = new Query<Wechat>().getPage(params, "T.create_time", false);
        List<Wechat> records = wechatDao.teamPage(page, params);
        records.forEach(item -> {
//            QueryWrapper levelOrder = new QueryWrapper<>();
//            levelOrder.eq("uid", item.getId());
//            levelOrder.eq("is_delete", 0);
//            levelOrder.orderByDesc("pay_time");
//            levelOrder.eq("is_pay", 1);
//            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(levelOrder);
//            if (memberLevelOrder.size() > 0) {
//                //查询我的下级的业绩
//                //1: 我的下级自己下的单
//                Map<String, Object> mapMyB = new HashMap<>();
//                mapMyB.put("uid", item.getId());
//                mapMyB.put("levelPayTime", memberLevelOrder.get(0).getPayTime());
//                //2: 我的下级分享的人下的单
//                Map<String, Object> mapMyBPu = new HashMap<>();
//                mapMyBPu.put("userId", item.getId());
//                item.setTeamTotalPerforman(
//                        (wechatDao.totalMyB(mapMyB) == null) ? 0 : wechatDao.totalMyB(mapMyB)
//                                + (wechatDao.totalMyBPu(mapMyBPu) == null ? 0 : wechatDao.totalMyBPu(mapMyBPu)));
//                //查询我的团队的总业绩
//                item.setTotalPerforman(item.getTotalPerforman() + item.getTeamTotalPerforman());
//            }
//
//            //查询下级的下级
//            QueryWrapper queryParent = new QueryWrapper();
//            queryParent.eq("parent_id", item.getId());
//            List<Wechat> listParent = wechatDao.selectList(queryParent);
//            listParent.forEach(itemPid -> {
//                QueryWrapper levelOrderPid = new QueryWrapper<>();
//                levelOrderPid.eq("uid", itemPid.getId());
//                levelOrderPid.eq("is_delete", 0);
//                levelOrderPid.orderByDesc("pay_time");
//                levelOrderPid.eq("is_pay", 1);
//                List<MemberLevelOrderEntity> memberLevelPidOrder = memberLevelOrderDao.selectList(levelOrderPid);
//                if (memberLevelPidOrder.size() > 0) {
//                    //查询我的下级的业绩
//                    //1: 我的下级自己下的单
//                    Map<String, Object> mapMyB = new HashMap<>();
//                    mapMyB.put("uid", itemPid.getId());
//                    mapMyB.put("levelPayTime", memberLevelPidOrder.get(0).getPayTime());
//                    //2: 我的下级分享的人下的单
//                    Map<String, Object> mapMyBPu = new HashMap<>();
//                    mapMyBPu.put("userId", itemPid.getId());
//                    itemPid.setTeamTotalPerforman(
//                            (wechatDao.totalMyB(mapMyB) == null) ? 0 : wechatDao.totalMyB(mapMyB)
//                                    + (wechatDao.totalMyBPu(mapMyBPu) == null ? 0 : wechatDao.totalMyBPu(mapMyBPu)));
//                    //查询我的团队的总业绩
//                    itemPid.setTotalPerforman(itemPid.getTotalPerforman() + itemPid.getTeamTotalPerforman());
//                }
//            });
//            item.setChildren(listParent);

        });
        page.setRecords(records);
        Map<String, Object> map = new HashMap<>();
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

//    /**
//     * 递归算法
//     *
//     * @param
//     * @return
//     */
//    public List<Wechat> subjectTreeItem(List<Wechat> records, String parentId) {
//        List<Wechat> wechatsList = records.stream().filter(s -> s.getParentId().equals(parentId)).collect(Collectors.toList());
//        wechatsList.forEach(item -> {
//            item.setChildren(subjectTreeItem(records, item.getId()));
//        });
//        return wechatsList;
//    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public Wechat team() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", authUtils.AuthUser().getUserId());
        Wechat wechat = wechatDao.selectOne(queryWrapper);
        if (wechat != null) {
            //查询我的团队总共有多少人
            QueryWrapper query = new QueryWrapper();
            query.eq("parent_id", wechat.getId());
            query.eq("is_member", 1);
            List<Wechat> list = wechatDao.selectList(query);
//            list.forEach(itemPid -> {
//                itemPid.setTotalPerforman(wechatDao.wechatTotalPerforman(itemPid.getId()).stream().mapToDouble(s -> s.getSumXfBalance()).sum());
//            });
            wechat.setParentSize(list.size());
            wechat.setTeamList(list);

//            list.forEach(itemPid -> {
//
////                ApplyJoinEntity applyJoin = applyJoinDao.queryUid(itemPid.getId());
////                if(applyJoin!=null){
////                    itemPid.setJoinName(applyJoin.getName());
////                    itemPid.setJoinPhone(applyJoin.getPhone());
////                }
//
//                QueryWrapper levelOrder = new QueryWrapper<>();
//                levelOrder.eq("uid", itemPid.getId());
//                levelOrder.eq("is_delete", 0);
//                levelOrder.orderByDesc("pay_time");
//                levelOrder.eq("is_pay", 1);
//                List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(levelOrder);
//                if (memberLevelOrder.size() > 0) {
//                    //查询团队下面的人数详情
//                    //查询我的下级的业绩
//                    //1: 我的下级自己下的单
//                    Map<String, Object> mapMyB = new HashMap<>();
//                    mapMyB.put("uid", itemPid.getId());
//                    mapMyB.put("levelPayTime", memberLevelOrder.get(0).getPayTime());
//                    //2: 我的下级分享的人下的单
//                    Map<String, Object> mapMyBPu = new HashMap<>();
//                    mapMyBPu.put("userId", itemPid.getId());
//                    itemPid.setTeamTotalPerforman(
//                            (wechatDao.totalMyB(mapMyB) == null) ? 0 : wechatDao.totalMyB(mapMyB)
//                                    + (wechatDao.totalMyBPu(mapMyBPu) == null ? 0 : wechatDao.totalMyBPu(mapMyBPu)));
//                    //查询我的团队的总业绩
//                    wechat.setTotalPerforman(wechat.getTotalPerforman() + itemPid.getTeamTotalPerforman());
//                }
//            });
            wechat.setTotalPerforman(wechatDao.wechatTotalPerforman(wechat.getId()).stream().mapToDouble(s -> s.getSumXfBalance()).sum());
            //查询我的分享的普通用户的成交单量
            QueryWrapper order = new QueryWrapper();
            order.eq("user_id", wechat.getId());
            order.eq("is_pay", 1);
            List<CommodityOrderEntity> orderList = commodityOrderDao.selectList(order);
            wechat.setOrderSize(orderList.size());
            //查询我的分享的普通用户的成交金额
            Map<String, Object> orderNum = new HashMap<>();
            orderNum.put("userId", wechat.getId());
            wechat.setTotalOrderNum((wechatDao.totalOrderNum(orderNum) == null) ? 0 : wechatDao.totalOrderNum(orderNum));
            //如果是B+会员——就差B+自己下的单和分享普通用户下的单的总金额大于升级金额
            QueryWrapper levelOrder = new QueryWrapper<>();
            levelOrder.eq("uid", wechat.getId());
            levelOrder.eq("is_delete", 0);
            levelOrder.orderByDesc("pay_time");
            levelOrder.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(levelOrder);
            if (memberLevelOrder.size() > 0) {
                QueryWrapper level = new QueryWrapper<>();
                level.eq("id", memberLevelOrder.get(0).getLevelId());
                MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                if (memberLevel != null) {
                    if (memberLevel.getLevelType().equals("C") || memberLevel.getLevelType().equals("B")) {
                        wechat.setLevelId(memberLevel.getId());
                        //查询升级金额
                        QueryWrapper upgrade = new QueryWrapper();
                        upgrade.eq("block", "BLOCK_0023");
                        SysBlock upgradeB = blockDao.selectOne(upgrade);
                        //查询B自己下的单的总金额
                        Map<String, Object> orderMy = new HashMap<>();
                        orderMy.put("uid", wechat.getId());
                        wechatDao.orderTotal(orderMy);
                        double orderTotal = ((wechatDao.orderTotal(orderMy) == null) ? 0 : wechatDao.orderTotal(orderMy));
                        wechat.setUpgradeMoeny(wechat.getTotalOrderNum() + orderTotal);
                        if (wechat.getUpgradeMoeny() >= Double.parseDouble(upgradeB.getRemark())) {
                            wechat.setIsUpgrade(1);
                        } else {
                            wechat.setIsUpgrade(0);
                        }
                    }
                }
            }

            //查询我的总收益
            Map<String, Object> xfLog = new HashMap<>();
            xfLog.put("userId", wechat.getId());
            wechatDao.xfLogTotal(xfLog);
            wechat.setTotalRevenue((wechatDao.xfLogTotal(xfLog) == null) ? 0 : wechatDao.xfLogTotal(xfLog));

            //查询我提现的已到账的消费日志
            Map<String, Object> paramsCollected = new HashMap<>();
            paramsCollected.put("userId", wechat.getId());
            wechat.setCashOutMoney((wechatDao.xfLogCollectedTotal(paramsCollected) == null) ? 0 : wechatDao.xfLogCollectedTotal(paramsCollected));

            //查询我的余额（余额=确认收货+未确认收货的收益-已提现，不包含已退货的）
            Map<String, Object> params = new HashMap<>();
            params.put("userId", wechat.getId());
            wechat.setBalanceOutstanding((wechatDao.xfLogCollectedBalances(params) == null) ? 0 : wechatDao.xfLogCollectedBalances(params));
            //驳回提现
            wechat.setBalanceOutstandingReject((wechatDao.xfLogCollectedBalancesReject(params) == null) ? 0 : wechatDao.xfLogCollectedBalancesReject(params));
            //减提现
            wechat.setCollectedBalances(wechat.getBalanceOutstanding() - wechat.getCashOutMoney() + wechat.getBalanceOutstandingReject());

            //可提现=所有确认收货未提现的收益
            Map<String, Object> paramsCanWithdraw = new HashMap<>();
            paramsCanWithdraw.put("userId", wechat.getId());
            wechat.setPaymentReceived((wechatDao.xfLogCanWithdraw(paramsCanWithdraw) == null) ? 0 : wechatDao.xfLogCanWithdraw(paramsCanWithdraw));

            //已退款
            Map<String, Object> paramsRefund = new HashMap<>();
            paramsRefund.put("userId", wechat.getId());
            wechat.setRefundTotal((wechatDao.xfLogRefund(paramsRefund) == null) ? 0 : wechatDao.xfLogRefund(paramsRefund));

            wechat.setBalanceWithdrawn(wechat.getPaymentReceived() - wechat.getCashOutMoney());

//            UpdateWrapper<Wechat> wrapperCollectedBalances = new UpdateWrapper<>();
//            wrapperCollectedBalances.eq("id", wechat.getId());
//            wrapperCollectedBalances.set("balance_withdrawn", wechat.getCollectedBalances() - wechat.getCashOutMoney());
//            wechatDao.update(null, wrapperCollectedBalances);


        }
        wechat.setBalanceWithdrawn(Double.parseDouble(String.format("%.2f", wechat.getBalanceWithdrawn())));
        wechat.setCollectedBalances(Double.parseDouble(String.format("%.2f", wechat.getCollectedBalances())));
        return wechat;
    }

    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    @Override
    public boolean updatePid(Wechat wechatParent) {
        try {
            // 我要成为扫码人的下级
            // 我不能是扫码人的上级
            // A不能存在上级
            String userLevelType = userLevelByUID(authUtils.AuthUser().getUserId());
            if ("A".equals(userLevelType)) {
                return false;
            }
            Wechat user = wechatDao.selectById(authUtils.AuthUser().getUserId());
            // 查询上级是否存在
            if (!"0".equals(wechatParent.getParentId())) {
                Wechat fromParent = wechatDao.selectById(wechatParent.getParentId());
                if (fromParent != null) {
                    if (user != null) {
                        if (!fromParent.getParentId().equals(user.getId())) {
                            if (user.getParentId().equals("0")) {
                                if (!(wechatParent.getParentId()).equals(user.getId())) {
                                    user.setParentId(wechatParent.getParentId());
                                    this.updateById(user);
                                }
                            }
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
     * 查询用户级别
     */
    public String userLevelByUID(String uid) {
        try {
            String levelType = "";
            QueryWrapper query = new QueryWrapper<>();
            query.eq("uid", authUtils.AuthUser().getUserId());
            query.eq("is_delete", 0);
            query.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
            if (memberLevelOrder.size() > 0) {
                QueryWrapper level = new QueryWrapper<>();
                level.eq("id", memberLevelOrder.get(0).getLevelId());
                MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                if (memberLevel != null) {
                    if (memberLevel.getLevelType().equals("A")) {
                        levelType = "A";
                    } else if (memberLevel.getLevelType().equals("B")) {
                        levelType = "B";
                    } else if (memberLevel.getLevelType().equals("C")) {
                        levelType = "C";
                    }
                }
            } else {

            }
            return levelType;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 查询用户是否A套餐会员
     */
    @Override
    public Result userInfoVip() {
        try {
            QueryWrapper query = new QueryWrapper<>();
            query.eq("uid", authUtils.AuthUser().getUserId());
            query.eq("is_delete", 0);
            query.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
            if (memberLevelOrder.size() > 0) {
                QueryWrapper level = new QueryWrapper<>();
                level.eq("id", memberLevelOrder.get(0).getLevelId());
                MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                if (memberLevel != null) {
                    if (memberLevel.getLevelType().equals("A")) {
                        return Result.error("A");
                    } else if (memberLevel.getLevelType().equals("B")) {
                        return Result.error("B");
                    } else if (memberLevel.getLevelType().equals("C")) {
                        return Result.error("C");
                    }
                }
            } else {

            }
            return Result.error("");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public Wechat userDetail(int ranNo) {
        QueryWrapper<Wechat> query = new QueryWrapper<>();
        query.eq("ran_no", ranNo);
        Wechat wechat = wechatDao.selectOne(query);
        return wechat;
    }


    /**
     * 获取openid
     *
     * @param
     */
    @Override
    public OpenIdDto getOpenId(String code) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        String responseEntity = restTemplate.getForObject(url, String.class);
        // 账号自动登录
        OpenIdDto openIdDto = JSONObject.parseObject(responseEntity, OpenIdDto.class);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("unionid", openIdDto.getOpenid());
        Wechat wechat = wechatDao.selectOne(queryWrapper);
        if (wechat != null) {
            wechat.setUnionid(openIdDto.getOpenid());
            wechat.setModifyTime(new Date());
            if (StringUtils.isNotEmpty(openIdDto.getOpenid())) {
                wechat.setOpenid(openIdDto.getOpenid());
            }
            if (!StringUtils.isNotEmpty(wechat.getVipNo())) {
                wechat.setVipNo(maxVipNo());
            }
            if (!StringUtils.isNotEmpty(wechat.getAppId())) {
                wechat.setAppId(appId);
            }
            wechatDao.updateById(wechat);
        } else {
            wechat = new Wechat();
            String vipNo = maxVipNo();
            String avatarUrl = "https://oss.51oms.cn/cook-online-files/default-avatar-image-a.png";
            String nickName = "新用户" + vipNo;
            wechat.setId(commonUtitls.createKey());
            wechat.setAvatarUrl(avatarUrl);
            wechat.setNickName(nickName);
            wechat.setOpenid(openIdDto.getOpenid());
            wechat.setUnionid(openIdDto.getOpenid());
            wechat.setVipNo(vipNo);
            wechat.setCreateTime(new Date());
            wechat.setAppId(appId);
            wechat.setRanNo(new Date().getTime() / 1000);
            wechatDao.insert(wechat);
        }

        String token = tokenUtils.createToken(wechat);
        openIdDto.setToken(token);
        openIdDto.setUserId(wechat.getId());
        openIdDto.setInformactionComplete(wechat.getInformactionComplete());

        QueryWrapper queryWrapperToken = new QueryWrapper();
        queryWrapperToken.eq("uid", wechat.getId());

        SysAuthTokenEntity sysAuthTokenEntity = sysAuthTokenDao.selectOne(queryWrapperToken);
        if (sysAuthTokenEntity != null) {
            sysAuthTokenEntity.setOpenid(openIdDto.getOpenid());
            sysAuthTokenEntity.setUid(wechat.getId());
            sysAuthTokenEntity.setToken(token);
            sysAuthTokenEntity.setUpdateTime(new Date());
            sysAuthTokenDao.updateById(sysAuthTokenEntity);
        } else {
            sysAuthTokenEntity = new SysAuthTokenEntity();
            sysAuthTokenEntity.setOpenid(openIdDto.getOpenid());
            sysAuthTokenEntity.setUid(wechat.getId());
            sysAuthTokenEntity.setToken(token);
            sysAuthTokenEntity.setUpdateTime(new Date());
            sysAuthTokenDao.insert(sysAuthTokenEntity);
        }
        return openIdDto;
    }

    /**
     * 小程序登录
     *
     * @param
     */
    @Override
    public LoginRepDto appLogin(AppLoginDto loginDto) {
        //
        LoginRepDto loginRepDto = new LoginRepDto();
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("openid", loginDto.getOpenid());

            queryWrapper.eq("unionid", loginDto.getOpenid());
            Wechat wechat = wechatDao.selectOne(queryWrapper);
            if (wechat != null) {
                wechat.setAvatarUrl(loginDto.getAvatarUrl());
                wechat.setNickName(loginDto.getNickName());
                wechat.setUnionid(loginDto.getOpenid());
                wechat.setModifyTime(new Date());
                if (StringUtils.isNotEmpty(loginDto.getOpenid())) {
                    wechat.setOpenid(loginDto.getOpenid());
                }
                if (!StringUtils.isNotEmpty(wechat.getVipNo())) {
                    wechat.setVipNo(maxVipNo());
                }
                if (!StringUtils.isNotEmpty(wechat.getAppId())) {
                    wechat.setAppId(appId);
                }
                wechat.setInformactionComplete(loginDto.getInformactionComplete());
                wechatDao.updateById(wechat);
            } else {
                wechat = new Wechat();
                wechat.setId(commonUtitls.createKey());
                wechat.setAvatarUrl(loginDto.getAvatarUrl());
                wechat.setNickName(loginDto.getNickName());
                wechat.setOpenid(loginDto.getOpenid());
                wechat.setUnionid(loginDto.getOpenid());
                wechat.setVipNo(maxVipNo());
                wechat.setCreateTime(new Date());
                wechat.setAppId(appId);
                wechat.setInformactionComplete(loginDto.getInformactionComplete());
                wechatDao.insert(wechat);
            }

            String token = tokenUtils.createToken(wechat);
            loginRepDto.setToken(token);
            loginRepDto.setUserId(wechat.getId());

            QueryWrapper queryWrapperToken = new QueryWrapper();
            queryWrapperToken.eq("uid", wechat.getId());

            SysAuthTokenEntity sysAuthTokenEntity = sysAuthTokenDao.selectOne(queryWrapperToken);
            if (sysAuthTokenEntity != null) {
                sysAuthTokenEntity.setOpenid(loginDto.getOpenid());
                sysAuthTokenEntity.setUid(wechat.getId());
                sysAuthTokenEntity.setToken(token);
                sysAuthTokenEntity.setUpdateTime(new Date());
                sysAuthTokenDao.updateById(sysAuthTokenEntity);
            } else {
                sysAuthTokenEntity = new SysAuthTokenEntity();
                sysAuthTokenEntity.setOpenid(loginDto.getOpenid());
                sysAuthTokenEntity.setUid(wechat.getId());
                sysAuthTokenEntity.setToken(token);
                sysAuthTokenEntity.setUpdateTime(new Date());
                sysAuthTokenDao.insert(sysAuthTokenEntity);
            }


        } catch (Exception e) {
            loginRepDto.setUserId("-1");
        }
        return loginRepDto;
    }

    /**
     * 小程序账号信息
     *
     * @param
     */
    public String maxVipNo() {

        QueryWrapper queryWrapper = new QueryWrapper();
        int maxCount = wechatDao.selectCount(queryWrapper);
        maxCount = maxCount + 1;
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        String numFormat = decimalFormat.format(maxCount);
        return numFormat;
    }

    /**
     * 小程序账号信息
     *
     * @param
     */
    @Override
    public Wechat mineInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", authUtils.AuthUser().getUserId());
        Wechat wechat = wechatDao.selectOne(queryWrapper);
        if (StringUtils.isEmpty(wechat.getShopQrCode())) {
            String shopQrCode = generateVerificationShopQrCode(wechat.getVipNo());
            wechat.setShopQrCode(shopQrCode);
            UpdateWrapper<Wechat> code = new UpdateWrapper<>();
            code.eq("id", wechat.getId());
            code.set("shop_qr_code", shopQrCode);
            wechatService.update(null, code);
        }
        if (!"0".equals(wechat.getParentId())) {
            //查询我的上级是A-B-C什么会员
            QueryWrapper parent = new QueryWrapper();
            parent.eq("uid", wechat.getParentId());
            parent.eq("is_delete", 0);
            parent.orderByDesc("pay_time");
            parent.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(parent);
            if (memberLevelOrder.size() > 0) {
                MemberLevelEntity memberLevelEntity = memberLevelDao.selectById(memberLevelOrder.get(0).getLevelId());
                if (memberLevelEntity != null) {
                    wechat.setParentLevelType(memberLevelEntity.getLevelType());
                    if ("A".equals(memberLevelEntity.getLevelType())) {
                        QueryWrapper<MemberLevelEntity> queryType = new QueryWrapper<>();
                        queryType.eq("level_type", "B");
                        queryType.eq("is_delete", 0);
                        MemberLevelEntity memberLevel = memberLevelDao.selectOne(queryType);
                        if (memberLevel != null) {
                            wechat.setLevelId(memberLevel.getId());
                        }
                    }
                }
            }
        }
        return wechat;
    }

    /**
     * 生成个人二维码
     *
     * @param vipNo
     * @return
     */
    public String generateVerificationShopQrCode(String vipNo) {
        WeChatQueryObject weChatQueryObject = new WeChatQueryObject();
        weChatQueryObject.setPage("pages/mall/index");
        weChatQueryObject.setScene(vipNo);
        weChatQueryObject.setFileName("mallDetail_" + vipNo + ".jpg");
        weChatQueryObject.setApplicationCode("7_MALL_MINIPROGRAM");
        String shopQrCode = qrCodeUtil.createMiniProgramQrCode(weChatQueryObject);
        return shopQrCode;
    }

    /**
     * 小程序账号信息
     *
     * @param
     */
    @Override
    public Wechat userInfo() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", authUtils.AuthUser().getUserId());
        Wechat wechat = wechatDao.selectOne(queryWrapper);
        if (StringUtils.isEmpty(wechat.getQrCode())) {
            String qrcode = generateVerificationQrcode(wechat.getVipNo());
            wechat.setQrCode(qrcode);
            UpdateWrapper<Wechat> code = new UpdateWrapper<>();
            code.eq("id", wechat.getId());
            code.set("qr_code", qrcode);
            wechatService.update(null, code);
        }
        // 购买会员时间
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("uid", authUtils.AuthUser().getUserId());
        queryWrapper1.eq("is_delete", 0);
        queryWrapper.orderByDesc("pay_time");
        queryWrapper1.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrderEntity = memberLevelOrderDao.selectList(queryWrapper1);
        if (memberLevelOrderEntity.size() > 0) {
            wechat.setBuyVipTime(memberLevelOrderEntity.get(0).getPayTime());
            //查询我自己购买的会员是A-B-C什么会员
            MemberLevelEntity memberLevelEntity = memberLevelDao.selectById(memberLevelOrderEntity.get(0).getLevelId());
            if (memberLevelEntity != null) {
                wechat.setLevelType(memberLevelEntity.getLevelType());
                wechat.setLevelId(memberLevelEntity.getId());
            }
        }
        if (!"0".equals(wechat.getParentId())) {
            //查询我的上级是A-B-C什么会员
            QueryWrapper parent = new QueryWrapper();
            parent.eq("uid", wechat.getParentId());
            parent.eq("is_delete", 0);
            parent.orderByDesc("pay_time");
            parent.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(parent);
            if (memberLevelOrder.size() > 0) {
                MemberLevelEntity memberLevelEntity = memberLevelDao.selectById(memberLevelOrder.get(0).getLevelId());
                if (memberLevelEntity != null) {
                    wechat.setParentLevelType(memberLevelEntity.getLevelType());
                    if ("A".equals(memberLevelEntity.getLevelType())) {
                        QueryWrapper<MemberLevelEntity> queryType = new QueryWrapper<>();
                        queryType.eq("level_type", "B");
                        queryType.eq("is_delete", 0);
                        MemberLevelEntity memberLevel = memberLevelDao.selectOne(queryType);
                        if (memberLevel != null) {
                            wechat.setLevelId(memberLevel.getId());
                        }
                    }
                }
            }
        }
        return wechat;
    }

    /**
     * 生成核销二维码
     *
     * @param vipNo
     * @return
     */
    public String generateVerificationQrcode(String vipNo) {
        WeChatQueryObject weChatQueryObject = new WeChatQueryObject();
        weChatQueryObject.setPage("pages/join-in/index");
        weChatQueryObject.setScene(vipNo);
        weChatQueryObject.setFileName("JoinInfoDetail_" + vipNo + ".jpg");
        weChatQueryObject.setApplicationCode("7_MALL_MINIPROGRAM");
        String qrcode = qrCodeUtil.createMiniProgramQrCode(weChatQueryObject);

        /*MsSuperMarketOrderEntity msSuperMarketOrderEntity = msSuperMarketOrderDao.selectById(id);
        msSuperMarketOrderEntity.setQrcode(qrcode);*/
        return qrcode;
    }

    /**
     * 根据openid 获取信息
     *
     * @param
     */
    @Override
    public Wechat infoByOpenId(String openid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);
        Wechat wechat = wechatDao.selectOne(queryWrapper);
        return wechat;
    }


    /**
     * 小程序账号
     *
     * @param
     */
    @Override
    public List<Wechat> wechatList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<Wechat> wechats = wechatDao.selectList(queryWrapper);
        return wechats;
    }

    /**
     * 保存头像和昵称
     *
     * @param wechat
     * @return boolean
     */
    @Override
    public Result saveMessage(@RequestBody Wechat wechat) {
        try {
            Wechat wechat1 = wechatDao.selectById(authUtils.AuthUser().getUserId());
            wechat1.setAvatarUrl(wechat.getAvatarUrl());
            wechat1.setNickName(wechat.getNickName());
            wechat1.setMobile(wechat.getMobile());
            if (wechat1.getEditNumBirthday() < 2) {
                if (wechat1.getBirthday() != wechat.getBirthday()) {
                    wechat1.setBirthday(wechat.getBirthday());
                    wechat1.setEditNumBirthday(wechat1.getEditNumBirthday() + 1);
                }
            }
            this.updateById(wechat1);
            return Result.success(wechat.getId());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 完善个人信息
     *
     * @param wechat
     * @return boolean
     */
    @Override
    public Result saveInfo(@RequestBody Wechat wechat) {
        try {
            Wechat updateWechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
            updateWechat.setCardName(wechat.getCardName());
            updateWechat.setCardNumber(wechat.getCardNumber());
            updateWechat.setCardNoFrontUrl(wechat.getCardNoFrontUrl());
            updateWechat.setCardNoRearUrl(wechat.getCardNoRearUrl());
            this.updateById(updateWechat);
            return Result.success(wechat.getId());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 小程序账号单条查询
     *
     * @param
     */
    @Override
    public Wechat wechatByVipNo(String vipNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vip_no", vipNo);
        Wechat user = wechatDao.selectOne(queryWrapper);
        if (user != null) {
            Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());

            // A不能存在上级
            String userLevelType = userLevelByUID(authUtils.AuthUser().getUserId());
            if (!"A".equals(userLevelType)) {
                if (wechat != null) {
                    if ("0".equals(wechat.getParentId())) {
                        // 我要成为扫码人的下级
                        // 我不能是扫码人的上级
                        if (!user.getParentId().equals(wechat.getId())) {
                            if (!(user.getId()).equals(wechat.getId())) {
                                try {
                                    UpdateWrapper<Wechat> pid = new UpdateWrapper<>();
                                    pid.eq("id", wechat.getId());
                                    pid.set("parent_id", user.getId());
                                    wechatService.update(null, pid);
                                } catch (Exception ex) {
                                    throw ex;
                                }
                            }
                        }
                    }
                }
                //查询邀请人是否是A会员
                QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
                member.eq("uid", user.getId());
                member.eq("is_delete", 0);
                member.orderByDesc("pay_time");
                member.eq("is_pay", 1);
                List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(member);
                if (memberLevelOrder.size() > 0) {
                    QueryWrapper level = new QueryWrapper<>();
                    level.eq("id", memberLevelOrder.get(0).getLevelId());
                    MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                    if (memberLevel != null) {
                        user.setLevelType(memberLevel.getLevelType());
                    }
                }

            }


        }
        return user;
    }

    /**
     * 小程序账号单条查询
     *
     * @param
     */
    @Override
    public Wechat wechatByScene(String vipNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vip_no", vipNo);
        Wechat user = wechatDao.selectOne(queryWrapper);
        return user;
    }

    /**
     * 小程序账号单条查询
     *
     * @param
     */
    @Override
    public Wechat wechatByUserId(String vipNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", vipNo);
        return wechatDao.selectOne(queryWrapper);
    }

    /**
     * 公众号登录
     *
     * @param
     */
    @Override
    public LoginRepDto wechatLogin(String code, String openid) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("wechatLogin:--" + code);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        WechatAccessTokenDto wechatAccessTokenDto = wechatUtils.getAccessToken(code, openid);
        System.out.println("请求获取access_token:" + wechatAccessTokenDto.getAccessToken());

        //请求获取userInfo
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + wechatAccessTokenDto.getAccessToken() +
                "&openid=" + wechatAccessTokenDto.getOpenid() +
                "&lang=zh_CN";

        String resultInfo = restTemplate.getForObject(infoUrl, String.class);
        System.out.println("请求获取userinfo:" + resultInfo);
        JSONObject resultUserObject = JSONObject.parseObject(resultInfo);

        String nickname = resultUserObject.getString("nickname");
        String headimgurl = resultUserObject.getString("headimgurl");
        String unionid = resultUserObject.getString("unionid");


        openid = wechatAccessTokenDto.getOpenid();
        LoginRepDto loginRepDto = new LoginRepDto();
        System.out.println("user:-1");
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("unionid", unionid);

            Wechat wechat = wechatDao.selectOne(queryWrapper);
            if (wechat != null) {
                wechat.setAvatarUrl(headimgurl);
                wechat.setNickName(nickname);
                wechat.setUnionid(unionid);
                wechat.setModifyTime(new Date());
                if (StringUtils.isNotEmpty(openid)) {
                    wechat.setWechatOpenid(openid);
                }
                if (!StringUtils.isNotEmpty(wechat.getVipNo())) {
                    wechat.setVipNo(maxVipNo());
                }
                if (!StringUtils.isNotEmpty(wechat.getAppId())) {
                    wechat.setAppId(appId);
                }
                wechatDao.updateById(wechat);
            } else {
                wechat = new Wechat();
                wechat.setId(commonUtitls.createKey());
                wechat.setAvatarUrl(headimgurl);
                wechat.setNickName(nickname);
                wechat.setWechatOpenid(openid);
                wechat.setUnionid(unionid);
                wechat.setVipNo(maxVipNo());
                wechat.setCreateTime(new Date());
                wechat.setAppId(appId);
                wechatDao.insert(wechat);
            }

            String token = tokenUtils.createToken(wechat);
            loginRepDto.setToken(token);
            loginRepDto.setUserId(wechat.getId());
            loginRepDto.setOpenid(openid);

            QueryWrapper queryWrapperToken = new QueryWrapper();
            queryWrapperToken.eq("uid", wechat.getId());

            SysAuthTokenEntity sysAuthTokenEntity = sysAuthTokenDao.selectOne(queryWrapperToken);
            if (sysAuthTokenEntity != null) {
                sysAuthTokenEntity.setOpenid(openid);
                sysAuthTokenEntity.setUid(wechat.getId());
                sysAuthTokenEntity.setToken(token);
                sysAuthTokenEntity.setUpdateTime(new Date());
                sysAuthTokenDao.updateById(sysAuthTokenEntity);
            } else {
                sysAuthTokenEntity = new SysAuthTokenEntity();
                sysAuthTokenEntity.setOpenid(openid);
                sysAuthTokenEntity.setUid(wechat.getId());
                sysAuthTokenEntity.setToken(token);
                sysAuthTokenEntity.setUpdateTime(new Date());
                sysAuthTokenDao.insert(sysAuthTokenEntity);
            }


        } catch (Exception e) {
            System.out.println("err");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            loginRepDto.setUserId("-1");
        }

        return loginRepDto;

    }

    /**
     * 获取 sessionKey
     *
     * @param
     */
    @Override
    public OpenIdDto sessionKey(String code) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        String responseEntity = restTemplate.getForObject(url, String.class);
        OpenIdDto openIdDto = JSONObject.parseObject(responseEntity, OpenIdDto.class);
        return openIdDto;
    }

    /**
     * 解密用户手机号
     *
     * @return
     * @throws Exception
     * @date 2019年05月08日
     */
    @Override
    public String enPhoneNumber(String sessionKey, String ivData, String encryptedData) throws Exception {
        byte[] encData = cn.hutool.core.codec.Base64.decode(encryptedData);
        byte[] iv = cn.hutool.core.codec.Base64.decode(ivData);
        byte[] key = Base64.decode(sessionKey);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(encData), "UTF-8");
    }

    /**
     * 驳回支付凭证
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean saveUserRemark(String id, String remark) {
        try {
//            CommodityApplyEntity applyEntity = commodityApplyDao.selectById(id);
            UpdateWrapper<Wechat> remarkWrapper = new UpdateWrapper<>();
            remarkWrapper.eq("id", id);
            remarkWrapper.set("remark", remark);
            wechatDao.update(null, remarkWrapper);

        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }

    /**
     * C会员自动升级A会员
     */
    @Override
    public void levelCUpgradeTask() {
        // 取出全部的 C会员
        List<Wechat> wechatList = wechatDao.levelCList();
        wechatList.forEach(wechat -> {
            //查询升级金额
            QueryWrapper upgrade = new QueryWrapper();
            upgrade.eq("block", "BLOCK_0023");
            SysBlock upgradeB = blockDao.selectOne(upgrade);

            //查询我的分享的普通用户的成交金额
            Map<String, Object> orderNum = new HashMap<>();
            orderNum.put("userId", wechat.getId());
            wechat.setTotalOrderNum((wechatDao.totalOrderNum(orderNum) == null) ? 0 : wechatDao.totalOrderNum(orderNum));

            //查询B自己下的单的总金额
            Map<String, Object> orderMy = new HashMap<>();
            orderMy.put("uid", wechat.getId());
            wechatDao.orderTotal(orderMy);

            double orderTotal = ((wechatDao.orderTotal(orderMy) == null) ? 0 : wechatDao.orderTotal(orderMy));

            double totalPerformance = wechat.getTotalOrderNum() + orderTotal;
            // 自动升级
            if (totalPerformance >= Double.parseDouble(upgradeB.getRemark())) {
                memberLevelOrderService.updateUpgrade(wechat);
            }
        });


    }


    /**
     * 个人中心取会员团队信息
     */
    @Override
    public Result userInfoTeam() {
        Map<String, Object> mapRep = new HashMap<>();
        mapRep.put("normalUserNum", wechatDao.normalUser(authUtils.AuthUser().getUserId()).size());
        mapRep.put("performanceTotal", String.format("%.2f", wechatDao.normalUserOrder(authUtils.AuthUser().getUserId()).stream().mapToDouble(s -> s.getCommodityTotal()).sum()));
        return Result.success(mapRep);
    }
}

