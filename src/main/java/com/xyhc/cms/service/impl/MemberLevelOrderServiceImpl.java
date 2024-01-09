package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
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
import com.xyhc.cms.listener.MemberLevelOrderListener;
import com.xyhc.cms.service.*;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-03-22 18:48:06
 */
@Service("memberLevelOrderService")
public class MemberLevelOrderServiceImpl extends ServiceImpl<MemberLevelOrderDao, MemberLevelOrderEntity> implements MemberLevelOrderService {

    @Resource
    MemberLevelOrderDao memberLevelOrderDao;

    @Resource
    ApplyContractDao applyContractDao;
    @Resource
    ApplyJoinDao applyJoinDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    MemberLevelService memberLevelService;
    @Resource
    IntegralLogService integralLogService;
    @Resource
    MemberLevelOrderService memberLevelOrderService;

    @Resource
    XfLogService xfLogService;

    @Resource
    MemberLevelDao memberLevelDao;

    @Resource
    BlockDao blockDao;

    @Resource
    WechatService wechatService;

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
    public List<MemberLevelOrderEntity> all(Map<String, Object> params) {
        return memberLevelOrderDao.all(params);
    }

    /**
     * 查询我的全部数据
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MemberLevelOrderEntity> myContractAll(Map<String, Object> params) {
        List<MemberLevelOrderEntity> pidOrderList = new ArrayList<>();
        QueryWrapper query = new QueryWrapper<>();
        query.eq("parent_id", authUtils.AuthUser().getUserId());
        List<Wechat> pid = wechatDao.selectList(query);
        if (pid.size() > 0) {
            pid.forEach(items -> {
                params.put("uid", authUtils.AuthUser().getUserId());
                params.put("pid", items.getId());
                List<MemberLevelOrderEntity> myContractAll = memberLevelOrderDao.myContractAll(params);
                myContractAll.forEach(item -> {
                    QueryWrapper<ApplyContractEntity> queryApply = new QueryWrapper<>();
                    queryApply.eq("id", item.getContractId());
                    queryApply.eq("is_delete", 0);
                    ApplyContractEntity applyContractEntity = applyContractDao.selectOne(queryApply);
                    if (applyContractEntity != null) {
                        item.setContract(applyContractEntity.getTitle());
                        item.setRemark(applyContractEntity.getRemark());
                    }
                    Wechat wechat = wechatService.detail(item.getUid());
                    if (wechat != null) {
                        item.setUserName(wechat.getNickName());
                        if (!Objects.equals("0", wechat.getParentId())) {
                            QueryWrapper superior = new QueryWrapper<>();
                            superior.eq("id", wechat.getParentId());
                            Wechat parent = wechatDao.selectOne(superior);
                            if (parent != null) {
                                item.setPartyCName(parent.getNickName());
                            } else {
                                item.setPartyCName("");
                            }
                        }
                    }
                });
                pidOrderList.addAll(myContractAll);
            });
        } else {
            params.put("uid", authUtils.AuthUser().getUserId());
            List<MemberLevelOrderEntity> myContractAll = memberLevelOrderDao.myContractAll(params);
            myContractAll.forEach(item -> {
                QueryWrapper<ApplyContractEntity> queryApply = new QueryWrapper<>();
                queryApply.eq("id", item.getContractId());
                queryApply.eq("is_delete", 0);
                ApplyContractEntity applyContractEntity = applyContractDao.selectOne(queryApply);
                if (applyContractEntity != null) {
                    item.setContract(applyContractEntity.getTitle());
                    item.setRemark(applyContractEntity.getRemark());
                }
                Wechat wechat = wechatService.detail(item.getUid());
                if (wechat != null) {
                    item.setUserName(wechat.getNickName());
                    if (!Objects.equals("0", wechat.getParentId())) {
                        QueryWrapper superior = new QueryWrapper<>();
                        superior.eq("id", wechat.getParentId());
                        Wechat parent = wechatDao.selectOne(superior);
                        if (parent != null) {
                            item.setPartyCName(parent.getNickName());
                        } else {
                            item.setPartyCName("");
                        }
                    }
                }
            });
            pidOrderList.addAll(myContractAll);
        }

        return pidOrderList;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("userName", params.get("userName"));
        params.put("parentName", params.get("parentName"));
        IPage<MemberLevelOrderEntity> page = new Query<MemberLevelOrderEntity>().getPage(params, "create_time", false);
        List<MemberLevelOrderEntity> records = memberLevelOrderDao.page(page, params);
        records.forEach(item -> {
            MemberLevelEntity memberLevel = memberLevelDao.selectById(item.getLevelId());
            if (memberLevel != null) {
                item.setLevelName(memberLevel.getName());
            }
            Wechat wechat = wechatDao.selectById(item.getUid());
            if (wechat != null) {
                item.setUserName(wechat.getNickName());
                item.setMobile(wechat.getMobile());
                if (!"0".equals(wechat.getParentId())) {
                    Wechat pid = wechatDao.selectById(wechat.getParentId());
                    if (pid != null) {
                        item.setParentName(pid.getNickName());
                        item.setParentMobile(pid.getMobile());
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
     * 保存
     *
     * @param memberLevelOrder
     * @return boolean
     */
    @Override
    public Result saveOrder(MemberLevelOrderEntity memberLevelOrder) {
        try {
            if (StringUtils.isBlank(memberLevelOrder.getId())) {
                memberLevelOrder.setId(commonUtitls.createKey());
                memberLevelOrder.setCreateBy(authUtils.AuthUser().getUserId());
                memberLevelOrder.setUid(authUtils.AuthUser().getUserId());
                memberLevelOrder.setCreateTime(new Date());
                memberLevelOrder.setIsPay(0);
                memberLevelOrder.setOrderNo(createNo());
                super.save(memberLevelOrder);
            } else {
                memberLevelOrder.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelOrder.setUpdateTime(new Date());
                this.updateById(memberLevelOrder);
            }
            return Result.success(memberLevelOrder.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存
     *
     * @param memberLevelOrder
     * @return boolean
     */
    @Override
    public Result saveUpgrade(MemberLevelOrderEntity memberLevelOrder) {
        try {
            if (StringUtils.isBlank(memberLevelOrder.getId())) {

                UpdateWrapper<MemberLevelOrderEntity> order = new UpdateWrapper<>();
                order.eq("uid", authUtils.AuthUser().getUserId());
                order.set("is_delete", 1);
                order.set("order_remark", "已失效");
                memberLevelOrderDao.update(null, order);

                memberLevelOrder.setId(commonUtitls.createKey());
                memberLevelOrder.setCreateBy(authUtils.AuthUser().getUserId());
                memberLevelOrder.setUid(authUtils.AuthUser().getUserId());
                memberLevelOrder.setCreateTime(new Date());
                memberLevelOrder.setIsPay(1);
                memberLevelOrder.setPayTime(new Date());
                memberLevelOrder.setOrderNo(createNo());
                memberLevelOrder.setOrderRemark("升级会员协议订单");
                super.save(memberLevelOrder);

                Wechat wechat = wechatService.detail(authUtils.AuthUser().getUserId());
                QueryWrapper queryParent = new QueryWrapper();
                queryParent.eq("id", wechat.getParentId());
                Wechat wechat1Money = wechatDao.selectOne(queryParent);
                if (wechat1Money != null) {
                    //查询B会员升级A会员一次性反钱
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("block", "BLOCK_0024");
                    SysBlock boss = blockDao.selectOne(queryWrapper);

                    UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                    wrapperTotalBalance.eq("id", wechat.getParentId());
                    wrapperTotalBalance.set("balance_withdrawn", Double.valueOf(boss.getRemark()) + wechat1Money.getBalanceWithdrawn());
                    wechatDao.update(null, wrapperTotalBalance);

                    UpdateWrapper<Wechat> noParent = new UpdateWrapper<>();
                    noParent.eq("id", authUtils.AuthUser().getUserId());
                    noParent.set("parent_id", 0);
                    wechatDao.update(null, noParent);

                    XfLogEntity xfLog = new XfLogEntity();
                    xfLog.setXfBalance(Double.valueOf(boss.getRemark()));
                    xfLog.setBeforeBalane(wechat1Money.getBalanceWithdrawn());
                    xfLog.setAfterBalance(wechat1Money.getBalanceWithdrawn() + Double.valueOf(boss.getRemark()));
                    xfLog.setCreateBy(authUtils.AuthUser().getUserId());
                    xfLog.setUserId(wechat.getParentId());
                    xfLog.setCollectedBalances(Double.valueOf(boss.getRemark()));
                    xfLog.setXfAccount(1);
                    xfLog.setXfClassify("UPGRADE");
                    xfLog.setXfRemark("B会员升级A会员一次性反钱");
                    xfLogService.save(xfLog);
                }
            } else {
                memberLevelOrder.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelOrder.setUpdateTime(new Date());
                this.updateById(memberLevelOrder);
            }
            return Result.success(memberLevelOrder.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 保存
     */
    @Override
    public Result saveUpgradeBMember(String levelId, String userId) {
        try {
            UpdateWrapper<MemberLevelOrderEntity> order = new UpdateWrapper<>();
            order.eq("uid", userId);
            order.eq("level_id", levelId);
            order.set("is_delete", 1);
            order.set("order_remark", "已失效");
            memberLevelOrderDao.update(null, order);

            QueryWrapper<MemberLevelEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("level_type", "C");
            queryWrapper.eq("is_delete", 0);
            MemberLevelEntity memberLevel = memberLevelDao.selectOne(queryWrapper);
            if (memberLevel != null) {
                MemberLevelOrderEntity memberLevelOrder = new MemberLevelOrderEntity();
                memberLevelOrder.setId(commonUtitls.createKey());
                memberLevelOrder.setCreateBy(authUtils.AuthUser().getUserId());
                memberLevelOrder.setUid(userId);
                memberLevelOrder.setLevelId(memberLevel.getId());
                memberLevelOrder.setCreateTime(new Date());
                memberLevelOrder.setIsPay(1);
                memberLevelOrder.setPayTime(new Date());
                memberLevelOrder.setOrderNo(createNo());
                memberLevelOrder.setOrderRemark("百银升级百银+会员");
                memberLevelOrderService.save(memberLevelOrder);
            }


            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 更新会员下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    @Override
    public Result updateLevelOrderStatus(String orderId, String orderNo, String tradeNo) {
        try {
            MemberLevelOrderEntity memberLevelOrderEntity = memberLevelOrderDao.selectById(orderId);
            if (memberLevelOrderEntity != null) {
                if (memberLevelOrderEntity.getIsPay() == 1) {
                    return Result.success();
                }
                memberLevelOrderEntity.setIsPay(1);
                memberLevelOrderEntity.setPayTime(new Date());
                memberLevelOrderEntity.setOutTradeNo(orderNo);
                memberLevelOrderEntity.setTransactionId(tradeNo);
                memberLevelOrderEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelOrderEntity.setUpdateTime(new Date());
                memberLevelOrderDao.updateById(memberLevelOrderEntity);

                //是否是会员
                UpdateWrapper<Wechat> status = new UpdateWrapper<>();
                status.eq("id", memberLevelOrderEntity.getUid());
                status.set("is_member", 1);
                if (StringUtils.isNotEmpty(memberLevelOrderEntity.getUserId())) {
                    status.set("parent_id", memberLevelOrderEntity.getUserId());
                }
                wechatDao.update(null, status);

                //加盟提成
                QueryWrapper query = new QueryWrapper<>();
                query.eq("uid", memberLevelOrderEntity.getUid());
                query.eq("is_delete", 0);
                query.orderByDesc("pay_time");
                query.eq("is_pay", 1);
                List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(query);
                if (memberLevelOrder.size() > 0) {

                    //查我的上级属于A会员还是B会员
                    Wechat user = wechatDao.selectById(memberLevelOrderEntity.getUid());
                    if (user != null) {
                        if (!user.getParentId().equals("0")) {
                            QueryWrapper queryMember = new QueryWrapper<>();
                            queryMember.eq("uid", user.getParentId());
                            queryMember.eq("is_delete", 0);
                            queryMember.orderByDesc("pay_time");
                            queryMember.eq("is_pay", 1);
                            List<MemberLevelOrderEntity> memberOrder = memberLevelOrderDao.selectList(queryMember);
                            if (memberOrder.size() > 0) {
                                QueryWrapper levelType = new QueryWrapper<>();
                                levelType.eq("id", memberOrder.get(0).getLevelId());
                                MemberLevelEntity memberLevelType = memberLevelDao.selectOne(levelType);
                                if (memberLevelType != null) {
                                    //客户上级属于B，当客户购买会员后，与B脱离关系，奖励B会员40元，次日到账，该客户与B脱离关系
                                    if ("B".equals(memberLevelType.getLevelType())) {

                                        Wechat pid = wechatDao.selectById(user.getParentId());
                                        if (pid != null) {
                                            UpdateWrapper<Wechat> wrapperBalance = new UpdateWrapper<>();
                                            wrapperBalance.eq("id", pid.getId());
                                            wrapperBalance.set("balance_withdrawn", 40 + pid.getBalanceWithdrawn());
                                            wechatDao.update(null, wrapperBalance);

                                            XfLogEntity xfLogBalance = new XfLogEntity();
                                            xfLogBalance.setCollectedBalances(40);
                                            xfLogBalance.setXfBalance(40);
                                            xfLogBalance.setBeforeBalane(pid.getBalanceWithdrawn());
                                            xfLogBalance.setAfterBalance(pid.getBalanceWithdrawn() + 40);
                                            xfLogBalance.setCreateBy(memberLevelOrder.get(0).getUid());
                                            xfLogBalance.setUserId(pid.getId());
                                            xfLogBalance.setOrderId(orderId);
                                            xfLogBalance.setXfAccount(1);
                                            xfLogBalance.setXfClassify("CUT_REWARD");
                                            xfLogBalance.setXfRemark("脱离百银会员奖励");
                                            xfLogService.save(xfLogBalance);
                                        }

                                        UpdateWrapper<Wechat> updateWrapper = new UpdateWrapper<>();
                                        updateWrapper.eq("id", user.getId());
                                        updateWrapper.set("parent_id", "0");
                                        wechatDao.update(null, updateWrapper);
                                    }
                                }
                            }
                        }
                    }

                    QueryWrapper level = new QueryWrapper<>();
                    level.eq("id", memberLevelOrder.get(0).getLevelId());
                    MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                    if (memberLevel != null) {
                        if (memberLevel.getLevelType().equals("B")) {
                            QueryWrapper pid = new QueryWrapper<>();
                            pid.eq("id", memberLevelOrderEntity.getUid());
                            Wechat wechat = wechatDao.selectOne(pid);
                            if (wechat != null) {
                                if (!wechat.getParentId().equals("0")) {

                                    //会员加盟分销比例
                                    QueryWrapper queryWrapperA = new QueryWrapper();
                                    queryWrapperA.eq("block", "BLOCK_0025");
                                    SysBlock join = blockDao.selectOne(queryWrapperA);
                                    double pushMoney = (memberLevelOrder.get(0).getTotal() * Double.parseDouble(join.getRemark())) / 100;

                                    Wechat wechatParent = wechatDao.selectById(wechat.getParentId());
                                    if (wechatParent != null) {

                                        UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                                        wrapperTotalBalance.eq("id", wechatParent.getId());
                                        wrapperTotalBalance.set("balance_withdrawn", pushMoney + wechatParent.getBalanceWithdrawn());
                                        wechatDao.update(null, wrapperTotalBalance);

                                        XfLogEntity xfLog = new XfLogEntity();
                                        xfLog.setXfBalance(pushMoney);
                                        xfLog.setBeforeBalane(wechatParent.getBalanceWithdrawn());
                                        xfLog.setAfterBalance(wechatParent.getBalanceWithdrawn() + pushMoney);
                                        xfLog.setCreateBy(memberLevelOrder.get(0).getUid());
                                        xfLog.setUserId(wechatParent.getId());
                                        xfLog.setOrderId(orderId);
                                        xfLog.setCollectedBalances(pushMoney);
                                        xfLog.setXfAccount(1);
                                        xfLog.setXfClassify("JOIN_MONEY");
                                        xfLog.setXfRemark("加盟分销收入");
                                        xfLogService.save(xfLog);

                                        //百金会员（A套餐）邀请百银会员奖励：
                                        // 1、凡成功邀请一名“百银会员”，平台奖励40元，不限名额，7日后可提现
                                        UpdateWrapper<Wechat> wrapperBalance = new UpdateWrapper<>();
                                        wrapperBalance.eq("id", wechatParent.getId());
                                        wrapperBalance.set("balance_withdrawn", 40 + wechatParent.getBalanceWithdrawn());
                                        wechatDao.update(null, wrapperBalance);

                                        XfLogEntity xfLogBalance = new XfLogEntity();
                                        xfLogBalance.setXfBalance(40);
                                        xfLogBalance.setCollectedBalances(40);
                                        xfLogBalance.setBeforeBalane(wechatParent.getBalanceWithdrawn());
                                        xfLogBalance.setAfterBalance(wechatParent.getBalanceWithdrawn() + 40);
                                        xfLogBalance.setCreateBy(memberLevelOrder.get(0).getUid());
                                        xfLogBalance.setUserId(wechatParent.getId());
                                        xfLogBalance.setOrderId(orderId);
                                        xfLogBalance.setXfAccount(1);
                                        xfLogBalance.setXfClassify("INVITE_REWARD");
                                        xfLogBalance.setXfRemark("邀请百银会员奖励");
                                        xfLogService.save(xfLogBalance);

                                    }


                                }
                            }
                            //用户购买B会员送积分
//                            UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//                            wrapperIntegralMoney.eq("id", wechat.getId());
//                            wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + memberLevel.getIntegral());
//                            wechatDao.update(null, wrapperIntegralMoney);

//                            IntegralLogEntity xfLog = new IntegralLogEntity();
//                            xfLog.setXfAccount(0);
//                            xfLog.setXfBalance(memberLevel.getIntegral());
//                            xfLog.setBeforeBalane(wechat.getIntegralMoney());
//                            xfLog.setAfterBalance(wechat.getIntegralMoney() + memberLevel.getIntegral());
//                            xfLog.setCreateBy(wechat.getId());
//                            xfLog.setUserId(wechat.getId());
//                            xfLog.setOrderId(orderId);
//                            xfLog.setXfClassify("INTEGRAL");
//                            xfLog.setXfRemark("会员积分收入");
//                            integralLogService.save(xfLog);

                            //用户购买B会员得200元现金奖励
                            UpdateWrapper<Wechat> wrapperMoney = new UpdateWrapper<>();
                            wrapperMoney.eq("id", wechat.getId());
                            wrapperMoney.set("shopping_money", 200);
                            wechatDao.update(null, wrapperMoney);

                            XfLogEntity xfLogMoney = new XfLogEntity();
                            xfLogMoney.setXfAccount(0);
                            xfLogMoney.setXfBalance(200);
                            xfLogMoney.setBeforeBalane(wechat.getShoppingMoney());
                            xfLogMoney.setAfterBalance(200);
                            xfLogMoney.setCreateBy(wechat.getId());
                            xfLogMoney.setUserId(wechat.getId());
                            xfLogMoney.setOrderId(orderId);
                            xfLogMoney.setXfClassify("MEMBER_AWARD");
                            xfLogMoney.setXfRemark("百银会员入会奖励");
                            xfLogService.save(xfLogMoney);


                        } else if (memberLevel.getLevelType().equals("A")) {
                            Wechat wechat = wechatDao.selectById(memberLevelOrderEntity.getUid());
//                            //用户购买A会员送积分
//                            UpdateWrapper<Wechat> wrapperIntegralMoney = new UpdateWrapper<>();
//                            wrapperIntegralMoney.eq("id", wechat.getId());
//                            wrapperIntegralMoney.set("integral_money", wechat.getIntegralMoney() + memberLevel.getIntegral());
//                            wechatDao.update(null, wrapperIntegralMoney);

//                            IntegralLogEntity xfLog = new IntegralLogEntity();
//                            xfLog.setXfAccount(0);
//                            xfLog.setXfBalance(memberLevel.getIntegral());
//                            xfLog.setBeforeBalane(wechat.getIntegralMoney());
//                            xfLog.setAfterBalance(wechat.getIntegralMoney() + memberLevel.getIntegral());
//                            xfLog.setCreateBy(wechat.getId());
//                            xfLog.setUserId(wechat.getId());
//                            xfLog.setOrderId(orderId);
//                            xfLog.setXfClassify("INTEGRAL");
//                            xfLog.setXfRemark("会员积分收入");
//                            integralLogService.save(xfLog);
                        }
                    }
                }

                //当普通用户购买百银或百金会员后，名称前新用户自动变更为百银会员，例如：新用户000123 购买了百银会员，则后台自动更名为：百银会员 000123
                Wechat wechatUpdate = wechatDao.selectById(memberLevelOrderEntity.getUid());
                if (wechatUpdate != null) {
                    MemberLevelEntity memberLevelEntity = memberLevelDao.selectById(memberLevelOrderEntity.getLevelId());
                    if (memberLevelEntity != null) {
                        UpdateWrapper<Wechat> updateWechatName = new UpdateWrapper<>();
                        updateWechatName.eq("id", wechatUpdate.getId());
                        updateWechatName.set("nick_name", wechatUpdate.getNickName().replace("新用户", memberLevelEntity.getName()));
                        wechatDao.update(null, updateWechatName);
                    }
                }

            }
            return Result.success(memberLevelOrderEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 查该用户是否是会员
     */
    @Override
    public MemberLevelOrderEntity isMember() {
        QueryWrapper<MemberLevelOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", authUtils.AuthUser().getUserId());
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByDesc("pay_time");
        queryWrapper.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(queryWrapper);
        if (memberLevelOrder.size() > 0) {
            MemberLevelEntity memberLevel = memberLevelDao.selectById(memberLevelOrder.get(0).getLevelId());
            if (memberLevel != null) {
                memberLevelOrder.get(0).setLevelType(memberLevel.getLevelType());
            }
        }
        return memberLevelOrder.get(0);
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MemberLevelOrderEntity detail(String id) {
        return memberLevelOrderDao.selectById(id);
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
            String strPre = new SimpleDateFormat("yyyyMMddHH").format(new Date());
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
            List<MemberLevelOrderEntity> scheduleList = memberLevelOrderDao.selectList(new QueryWrapper<MemberLevelOrderEntity>()
                    .gt("create_time", minDate).lt("create_time", maxDate));
            return scheduleList.stream().count();
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean updatePayStatus(String id) {
        try {
            MemberLevelOrderEntity removeEntity = memberLevelOrderDao.selectById(id);
            removeEntity.setIsPay(1);
            removeEntity.setPayTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            removeEntity.setUpdateTime(new Date());
            memberLevelOrderDao.updateById(removeEntity);

            UpdateWrapper<Wechat> status = new UpdateWrapper<>();
            status.eq("id", authUtils.AuthUser().getUserId());
            status.set("is_member", 1);
            wechatDao.update(null, status);
//            MemberLevelOrderEntity removeEntity = memberLevelOrderDao.selectById(id);
//            removeEntity.setIsDelete(1);
//            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
//            memberLevelOrderDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

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
            MemberLevelOrderEntity removeEntity = memberLevelOrderDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            memberLevelOrderDao.updateById(removeEntity);
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
                MemberLevelOrderEntity removeEntity = memberLevelOrderDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelOrderDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 根据状态查询记录
     *
     * @param
     * @return Result
     */
    @Override
    public MemberLevelEntity userLevel() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("is_delete", 0);
        queryWrapper.eq("is_pay", 1);
        queryWrapper.eq("user_id", authUtils.AuthUser().getUserId());
        List<MemberLevelOrderEntity> records = memberLevelOrderDao.selectList(queryWrapper);

        MemberLevelEntity memberLevel = memberLevelService.detail(records.get(0).getLevelId());


        return memberLevel;
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
            MemberLevelOrderListener listener = new MemberLevelOrderListener();
            EasyExcel.read(file.getInputStream(), MemberLevelOrderEntity.class, listener).sheet().doRead();
            //获取数据
            List<MemberLevelOrderEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                memberLevelOrderDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 会员升级——百银+ 升级 百金
     */
    @Override
    public Result updateUpgrade(Wechat wechat) {
        try {
            UpdateWrapper<MemberLevelOrderEntity> order = new UpdateWrapper<>();
            order.eq("uid", wechat.getId());
            order.set("is_delete", 1);
            order.set("order_remark", "已失效");
            memberLevelOrderDao.update(null, order);

            QueryWrapper queryMemberLevel = new QueryWrapper();
            queryMemberLevel.eq("level_type", "A");
            MemberLevelEntity memberLevel = memberLevelDao.selectOne(queryMemberLevel);

            MemberLevelOrderEntity memberLevelOrder = new MemberLevelOrderEntity();
            memberLevelOrder.setId(commonUtitls.createKey());
            memberLevelOrder.setCreateBy(wechat.getId());
            memberLevelOrder.setUid(wechat.getId());
            memberLevelOrder.setLevelId(memberLevel.getId());
            memberLevelOrder.setCreateTime(new Date());
            memberLevelOrder.setIsPay(1);
            memberLevelOrder.setPayTime(new Date());
            memberLevelOrder.setOrderNo(createNo());
            memberLevelOrder.setOrderRemark("升级会员协议订单");
            super.save(memberLevelOrder);

            QueryWrapper queryParent = new QueryWrapper();
            queryParent.eq("id", wechat.getParentId());
            Wechat wechat1Money = wechatDao.selectOne(queryParent);
            if (wechat1Money != null) {
                //查询B会员升级A会员一次性反钱
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("block", "BLOCK_0024");
                SysBlock boss = blockDao.selectOne(queryWrapper);

                UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                wrapperTotalBalance.eq("id", wechat.getParentId());
                wrapperTotalBalance.set("balance_withdrawn", Double.valueOf(boss.getRemark()) + wechat1Money.getBalanceWithdrawn());
                wechatDao.update(null, wrapperTotalBalance);

                UpdateWrapper<Wechat> noParent = new UpdateWrapper<>();
                noParent.eq("id", wechat.getId());
                noParent.set("parent_id", 0);
                wechatDao.update(null, noParent);

                XfLogEntity xfLog = new XfLogEntity();
                xfLog.setXfBalance(Double.valueOf(boss.getRemark()));
                xfLog.setBeforeBalane(wechat1Money.getBalanceWithdrawn());
                xfLog.setAfterBalance(wechat1Money.getBalanceWithdrawn() + Double.valueOf(boss.getRemark()));
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getParentId());
                xfLog.setCollectedBalances(Double.valueOf(boss.getRemark()));
                xfLog.setXfAccount(1);
                xfLog.setXfClassify("UPGRADE");
                xfLog.setXfRemark("B会员升级A会员一次性反钱");
                xfLogService.save(xfLog);
            }
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

}
