package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import com.xyhc.cms.entity.MsLotteryUserEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.service.MsLotteryInfoService;
import com.xyhc.cms.service.MsLotteryService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 赛事分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("msLotteryInfoService")
public class MsLotteryInfoServiceImpl extends ServiceImpl<MsLotteryInfoDao, MsLotteryInfoEntity> implements MsLotteryInfoService {

    @Resource
    MsLotteryInfoDao msLotteryInfoDao;
    @Resource
    MsLotteryUserDao msLotteryUserDao;
    @Resource
    MsLotteryDao msLotteryDao;
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
    public List<MsLotteryInfoEntity> all(Map<String, Object> params) {
        params.put("lotteryId", params.get("lotteryId"));
        return msLotteryInfoDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MsLotteryInfoEntity> page = new Query<MsLotteryInfoEntity>().getPage(params, "id", true);
        List<MsLotteryInfoEntity> records = msLotteryInfoDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param msLotteryInfo
     * @return boolean
     */
    @Override
    public boolean save(MsLotteryInfoEntity msLotteryInfo) {
        try {
            if (StringUtils.isBlank(msLotteryInfo.getId())) {
                msLotteryInfo.setId(commonUtitls.createKey());
                msLotteryInfo.setCreateBy(authUtils.AuthUser().getUserId());
                msLotteryInfo.setCreateTime(new Date());
                super.save(msLotteryInfo);
            } else {
                msLotteryInfo.setUpdateBy(authUtils.AuthUser().getUserId());
                msLotteryInfo.setUpdateTime(new Date());
                this.updateById(msLotteryInfo);
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
    public MsLotteryInfoEntity detail(String id) {
        return msLotteryInfoDao.selectById(id);
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
            MsLotteryInfoEntity removeEntity = msLotteryInfoDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            msLotteryInfoDao.updateById(removeEntity);
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
                MsLotteryInfoEntity removeEntity = msLotteryInfoDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                msLotteryInfoDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 随机返回抽奖奖品
     *
     * @param
     * @return Result
     */
    @Override
    public Result randomLotteryInfo(Map<String, Object> params) {
        try {
            Result validateResult = lotteryPower(params);

            if (validateResult.getCode() != 200) {
                return Result.error("暂无抽奖次数") ;
            }
            MsLotteryInfoEntity bottleInfo = new MsLotteryInfoEntity();
            MsLotteryEntity lottery = msLotteryDao.selectById(params.get("lotteryId").toString());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.orderByDesc("create_time");
            queryWrapper.eq("is_delete", 0);
            queryWrapper.eq("lottery_id", params.get("lotteryId"));
            List<MsLotteryInfoEntity> records = msLotteryInfoDao.selectList(queryWrapper);


            List<MsLotteryInfoEntity> lotterResult = new ArrayList<>();
            records.forEach(item -> {
                for (int i = 0; i <= item.getLotteryRate() * 100; i++) {
                    MsLotteryInfoEntity msLotteryInfoCreate = new MsLotteryInfoEntity();
                    BeanUtils.copyProperties(item, msLotteryInfoCreate);
                    lotterResult.add(msLotteryInfoCreate);
                }
            });

            if (lotterResult.size() > 0) {
                Random rand = new Random();
                int randNum = rand.nextInt(lotterResult.size() + 1);
                bottleInfo = lotterResult.get(randNum);
            }
            return Result.success(bottleInfo);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 判断是否可以抽奖
     *
     * @param
     * @return Result
     */
    public Result lotteryPower(Map<String, Object> params) {
        try {
            //是否在活动期间内
            params.put("nowDate", params.get("nowDate"));
            List<MsLotteryEntity> msLotteryNum = msLotteryDao.randomLotteryNum(params);
            //是否生日
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
            if (wechat.getBirthday() != null && params.get("birth").toString() != "") {
                //生日
                if (formatter.format(wechat.getBirthday()).contains(params.get("birth").toString())) {
                    //此次活动是否已抽奖
                    QueryWrapper queryWrapperLottery = new QueryWrapper();
                    queryWrapperLottery.eq("is_delete", 0);
                    queryWrapperLottery.eq("uid", authUtils.AuthUser().getUserId());
                    queryWrapperLottery.eq("start_time", params.get("startTime"));
                    queryWrapperLottery.eq("end_time", params.get("endTime"));
                    List<MsLotteryUserEntity> lotteryUserList = msLotteryUserDao.selectList(queryWrapperLottery);
                    if (lotteryUserList.size() > 1) {
                        return Result.error("暂无抽奖次数");
                    }
                } else {
                    //不在在活动期间内
                    if (msLotteryNum.size() < 1) {
                        return Result.error("暂无抽奖次数");
                    }
                    //此次活动是否已抽奖
                    QueryWrapper queryWrapperLottery = new QueryWrapper();
                    queryWrapperLottery.eq("is_delete", 0);
                    queryWrapperLottery.eq("uid", authUtils.AuthUser().getUserId());
                    queryWrapperLottery.eq("start_time", params.get("startTime"));
                    queryWrapperLottery.eq("end_time", params.get("endTime"));
                    List<MsLotteryUserEntity> lotteryUserList = msLotteryUserDao.selectList(queryWrapperLottery);
                    if (lotteryUserList.size() > 0) {
                        return Result.error("暂无抽奖次数");
                    }
                }
            }


        } catch (Exception ex) {
            throw ex;
        }
        return Result.success();
    }
}
