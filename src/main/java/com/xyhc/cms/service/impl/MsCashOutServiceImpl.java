package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MsCashOutDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.MsBankEntity;
import com.xyhc.cms.entity.MsCashOutEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.entity.XfLogEntity;
import com.xyhc.cms.listener.MsCashOutListener;
import com.xyhc.cms.service.MsBankService;
import com.xyhc.cms.service.MsCashOutService;
import com.xyhc.cms.service.WechatService;
import com.xyhc.cms.service.XfLogService;
import com.xyhc.cms.utils.AuthorityUtils;
import com.xyhc.cms.utils.CommonUtitls;
import com.xyhc.cms.vo.infra.CashOutVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
@Service("msCashOutService")
public class MsCashOutServiceImpl extends ServiceImpl<MsCashOutDao, MsCashOutEntity> implements MsCashOutService {

    @Resource
    MsCashOutDao msCashOutDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Resource
    WechatService wechatService;

    @Resource
    MsBankService msBankService;

    @Resource
    MsCashOutService msCashOutService;

    @Resource
    WechatDao wechatDao;


    @Resource
    XfLogService xfLogService;
    @Resource
    AuthorityUtils authorityUtils;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MsCashOutEntity> all(Map<String, Object> params) {
        return msCashOutDao.all(params);
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
        IPage<MsCashOutEntity> page = new Query<MsCashOutEntity>().getPage(params, "T.create_time", false);
        List<MsCashOutEntity> records = msCashOutDao.page(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(msCashOut -> {
            Wechat wechat = wechatService.detail(msCashOut.getUserId());
            if (wechat != null) {
                msCashOut.setUserName(wechat.getNickName());
            }
            if ("BANK".equals(msCashOut.getCashOutType())) {
                MsBankEntity msBankEntity = msBankService.detail(msCashOut.getBankId());
                if (msBankEntity != null) {
                    msCashOut.setBankName(msBankEntity.getBankName());
                }
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询
     * 后台提现明细
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pageBack(Map<String, Object> params) {
        IPage<MsCashOutEntity> page = new Query<MsCashOutEntity>().getPage(params, "T.create_time", false);
        List<MsCashOutEntity> records = msCashOutDao.pageBack(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(msCashOut -> {
            //实际提现金额
            double realityCashMoney = msCashOut.getApplyMoney() - msCashOut.getServiceCharge();
            msCashOut.setRealityCashMoney(realityCashMoney);
            Wechat wechat = wechatService.detail(msCashOut.getUserId());
            if (wechat != null) {
                msCashOut.setUserName(wechat.getCardName());
                msCashOut.setMobile(wechat.getMobile());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (wechat.getCreateTime().after(formatter.parse("2023-12-19"))) {
                        msCashOut.setIsNewUsers("1");
                    } else {
                        msCashOut.setIsNewUsers("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                if (formatter.format(new Date(2023-12-19))< formatter.format(wechat.getCreateTime())) {
//                    msCashOut.setIsNewUsers("1");
//                } else {
//                    msCashOut.setIsNewUsers("0");
//                }

            }
            if ("BANK".equals(msCashOut.getCashOutType())) {
                MsBankEntity msBankEntity = msBankService.detail(msCashOut.getBankId());
                if (msBankEntity != null) {
                    msCashOut.setBankName(msBankEntity.getBankName());
                    msCashOut.setBankCode(msBankEntity.getBankCode());
                    msCashOut.setBankNo(msBankEntity.getBankNo());
                    msCashOut.setDeposit(msBankEntity.getDeposit());
                    msCashOut.setRealName(msBankEntity.getRealName());
                    msCashOut.setMobileBank(msBankEntity.getMobile());
                }
            }
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
    public PageUtils pageByUid(Map<String, Object> params) {
        params.put("uid", params.get("uid"));
        IPage<MsCashOutEntity> page = new Query<MsCashOutEntity>().getPage(params, "T.create_time", false);
        List<MsCashOutEntity> records = msCashOutDao.pageByUid(page, params);
        //再查提现这个人的姓名和电话
        records.forEach(msCashOut -> {
            Wechat wechat = wechatService.detail(msCashOut.getUserId());
            if (wechat != null) {
                msCashOut.setUserName(wechat.getNickName());
                msCashOut.setMobile(wechat.getMobile());
            }
            if ("BANK".equals(msCashOut.getCashOutType())) {
                MsBankEntity msBankEntity = msBankService.detail(msCashOut.getBankId());
                if (msBankEntity != null) {
                    msCashOut.setBankName(msBankEntity.getBankName());
                    msCashOut.setBankCode(msBankEntity.getBankCode());
                    msCashOut.setBankNo(msBankEntity.getBankNo());
                    msCashOut.setDeposit(msBankEntity.getDeposit());
                    msCashOut.setRealName(msBankEntity.getRealName());
                    msCashOut.setMobileBank(msBankEntity.getMobile());
                }
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param msCashOut
     * @return boolean
     */
    @Override
    public boolean save(MsCashOutEntity msCashOut) {
        try {
            if (StringUtils.isBlank(msCashOut.getId())) {
                msCashOut.setId(commonUtitls.createKey());
                msCashOut.setCreateBy(authUtils.AuthUser().getUserId());
                msCashOut.setCreateTime(new Date());
                super.save(msCashOut);
            } else {
                msCashOut.setUpdateBy(authUtils.AuthUser().getUserId());
                msCashOut.setUpdateTime(new Date());
                this.updateById(msCashOut);
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
    public MsCashOutEntity detail(String id) {
        return msCashOutDao.selectById(id);
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
            MsCashOutEntity removeEntity = msCashOutDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            msCashOutDao.updateById(removeEntity);
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
                MsCashOutEntity removeEntity = msCashOutDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                msCashOutDao.updateById(removeEntity);
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
            MsCashOutListener listener = new MsCashOutListener();
            EasyExcel.read(file.getInputStream(), MsCashOutEntity.class, listener).sheet().doRead();
            //获取数据
            List<MsCashOutEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                msCashOutDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 保存提现金额
     *
     * @param
     * @return boolean
     */
    @Override
    public Result saveWithdrawMoney(@RequestBody MsCashOutEntity msCashOutEntity) {
        try {
            //转换类型
//            double doubleXfBalance = Double.valueOf(Integer.valueOf(intXfBalance));
            //查询当前人
            Wechat wechat = wechatService.detail(authUtils.AuthUser().getUserId());
            if (wechat != null) {
                if (msCashOutEntity.getApplyMoney() <= wechat.getBalanceWithdrawn()) {
                    if (msCashOutEntity.getApplyMoney() < 10) {
                        return Result.error("满10元可提现！");
                    }
                    //查变更前的金额
                    double beforeBalane = wechat.getBalanceWithdrawn();
                    //查变更后的金额
                    double afterBalance = wechat.getBalanceWithdrawn() - msCashOutEntity.getApplyMoney();
                    //扣除%1的手续费
                    double sumBalance = msCashOutEntity.getApplyMoney() * 0.01;
                    //用户总金额
                    //applyMoney
                    MsCashOutEntity msCashOut = new MsCashOutEntity();
                    msCashOut.setOriginalMoney(msCashOutEntity.getOriginalMoney());
                    msCashOut.setApplyMoney(msCashOutEntity.getApplyMoney());
                    msCashOut.setBankId(msCashOutEntity.getBankId());
                    msCashOut.setCashOutType(msCashOutEntity.getCashOutType());
                    msCashOut.setApayAccount(msCashOutEntity.getApayAccount());
                    msCashOut.setApayName(msCashOutEntity.getApayName());
                    msCashOut.setBeforeBalane(beforeBalane);
                    msCashOut.setAfterBalance(afterBalance);
                    msCashOut.setServiceCharge(sumBalance);
                    msCashOut.setCreateBy(wechat.getId());
                    msCashOut.setUserId(wechat.getId());
                    msCashOut.setUid(authUtils.AuthUser().getUserId());
                    msCashOut.setIsPass("0");
                    msCashOut.setRemark("用户提现" + msCashOutEntity.getApplyMoney());
                    msCashOutService.save(msCashOut);

                    //更新用户提现之后的总金额
                    UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
                    wrapperTotalBalance.eq("id", wechat.getId());
                    wrapperTotalBalance.set("balance_withdrawn", afterBalance);
//                    wrapperTotalBalance.set("collected_balances", wechat.getCollectedBalances()-msCashOutEntity.getApplyMoney());
                    wechatDao.update(null, wrapperTotalBalance);


                    //更新当前人余额日志
                    XfLogEntity xfLog = new XfLogEntity();
                    xfLog.setXfBalance(msCashOutEntity.getApplyMoney());
                    xfLog.setBeforeBalane(beforeBalane);
                    xfLog.setAfterBalance(afterBalance);
                    xfLog.setCreateBy(wechat.getId());
                    xfLog.setUserId(wechat.getId());
                    xfLog.setXfClassify("CASH_OUT_COLLECTED");
                    xfLog.setXfRemark("总收益提现");
                    xfLogService.save(xfLog);

                } else {
                    return Result.error("金额不足");
                }
            }
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 根据id更新提现金额的审核状态
     *
     * @return
     */
    @Override
    public void saveAuditStatus(@RequestBody MsCashOutEntity msCashOutEntity) {
        try {
            Date date = new Date();
            UpdateWrapper<MsCashOutEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", msCashOutEntity.getId());
            wrapper.set("is_pass", "1");
            wrapper.set("apply_money", msCashOutEntity.getApplyMoney());
            wrapper.set("reject_remark", msCashOutEntity.getRejectRemark());
            msCashOutDao.update(null, wrapper);

            MsCashOutEntity msCashOut = msCashOutDao.selectById(msCashOutEntity.getId());
            if (msCashOut != null) {
                if ("WECHAT".equals(msCashOut.getCashOutType())) {
                    //查用户openid
                    Wechat wechat = wechatDao.selectById(msCashOut.getUid());

                    CashOutVo cashOutVo = new CashOutVo();
                    cashOutVo.setAmount(msCashOut.getApplyMoney() - msCashOut.getServiceCharge());
                    cashOutVo.setApplicationCode("QIAISHIJIA_MINIPROGRAM");
                    cashOutVo.setOpenId(wechat.getOpenid());

                    Map<String, String> cashOutV2Transfer = authorityUtils.cashOutV2Transfer(cashOutVo);


                    UpdateWrapper<MsCashOutEntity> updateCashOut = new UpdateWrapper<>();
                    updateCashOut.eq("id", msCashOut.getId());
                    updateCashOut.set("payment_no", cashOutV2Transfer.get("paymentNo"));
                    updateCashOut.set("return_code", cashOutV2Transfer.get("returnCode"));
                    updateCashOut.set("return_msg", cashOutV2Transfer.get("returnMsg"));
                    updateCashOut.set("err_code_des", cashOutV2Transfer.get("errCodeDes"));
                    updateCashOut.set("out_batch_no", cashOutV2Transfer.get("outBatchNo"));
                    updateCashOut.set("partner_trade_no", cashOutV2Transfer.get("partnerTradeNo"));
                    updateCashOut.set("result_code", cashOutV2Transfer.get("resultCode"));
                    updateCashOut.set("out_detail_no", cashOutV2Transfer.get("outDetailNo"));
                    if ("ERROR".equals(cashOutV2Transfer.get("returnCode"))) {
                        updateCashOut.set("is_update_done", 1);
                        updateCashOut.set("is_pass", 0);
                        updateCashOut.set("merchant_pay_status", "FAIL");
                        updateCashOut.set("merchant_pay_remark", cashOutV2Transfer.get("returnMsg"));
                    } else {
                        updateCashOut.set("is_update_done", 0);
                    }

                    msCashOutDao.update(null, updateCashOut);

                    // 更新商户状态
                    updateCashTransferStatus();
                }

            }
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
            //更新驳回状态
            UpdateWrapper<MsCashOutEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", params.get("id"));
            wrapper.set("is_pass", "-1");
            wrapper.set("reject_remark", params.get("rejectRemark"));
            msCashOutDao.update(null, wrapper);
            //查询提现记录
            MsCashOutEntity msCashOutEntity = detail(params.get("id").toString());
            //用户详情
            Wechat wechat = wechatService.detail(msCashOutEntity.getUserId());
            //提现申请金额
            double applyMoney = msCashOutEntity.getApplyMoney();
            //查变更前的金额
            double beforeBalane = wechat.getBalanceWithdrawn();
            //查变更后的金额
            double afterBalance = wechat.getBalanceWithdrawn() + applyMoney;


            //更新用户提现之后的总金额
            UpdateWrapper<Wechat> wrapperTotalBalance = new UpdateWrapper<>();
            wrapperTotalBalance.eq("id", wechat.getId());
            wrapperTotalBalance.set("balance_withdrawn", afterBalance);
            wechatDao.update(null, wrapperTotalBalance);


            //更新当前人余额日志
            XfLogEntity xfLog = new XfLogEntity();
            xfLog.setXfBalance(applyMoney);
            xfLog.setCollectedBalances(applyMoney);
            xfLog.setBeforeBalane(wechat.getBalanceWithdrawn());
            xfLog.setAfterBalance(afterBalance);
            xfLog.setCreateBy(wechat.getId());
            xfLog.setUserId(wechat.getId());
            xfLog.setXfClassify("CASH_OUT_REJECT");
            xfLog.setXfRemark("余额提现驳回");
            xfLog.setXfAccount(1);
            xfLogService.save(xfLog);


        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 更新提现状态
     *
     * @return boolean
     */
    @Override
    public void updateCashTransferStatus() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_update_done", 0);
        queryWrapper.ne("out_detail_no", "");

        List<MsCashOutEntity> cashOutEntityList = msCashOutDao.selectList(queryWrapper);
        cashOutEntityList.forEach(item -> {
            String cashDetail = authorityUtils.queryCashOutDetailTransferRestul(item.getOutBatchNo(), item.getOutDetailNo(), "QIAISHIJIA_MINIPROGRAM");
            JSONObject jsonObject = JSONObject.parseObject(cashDetail);
            String detailStatus = jsonObject.getString("detail_status");
            UpdateWrapper<MsCashOutEntity> wrapper = new UpdateWrapper<>();

            String merchantPayRemark = "";
            if ("FAIL".equals(detailStatus)) {
                String failReason = jsonObject.getString("fail_reason");
                merchantPayRemark = failReason;
                wrapper.set("is_update_done", 1);
                wrapper.set("is_pass", 0);
            }
            if ("WAIT_PAY".equals(detailStatus)) {
                String failReason = jsonObject.getString("failReason");
                merchantPayRemark = "待确认。待商户确认, 符合免密条件时, 系统会自动扭转为转账中";

            }

            if ("PROCESSING".equals(detailStatus)) {
                merchantPayRemark = "转账中。正在处理中，转账结果尚未明确";
            }
            if ("SUCCESS".equals(detailStatus)) {
                merchantPayRemark = "转账成功";
                wrapper.set("is_update_done", 1);
            }
            wrapper.eq("id", item.getId());
            wrapper.set("merchant_pay_status", detailStatus);
            wrapper.set("merchant_pay_remark", merchantPayRemark);
            msCashOutDao.update(null, wrapper);
        });

    }

}
