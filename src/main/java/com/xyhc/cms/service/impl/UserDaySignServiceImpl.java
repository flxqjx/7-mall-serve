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
import com.xyhc.cms.dao.PointsmallDao;
import com.xyhc.cms.dao.UserDaySignDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.listener.PointsmallListener;
import com.xyhc.cms.service.IntegralLogService;
import com.xyhc.cms.service.PointsmallService;
import com.xyhc.cms.service.UserDaySignService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 签到记录表Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
@Service("userDaySignService")
public class UserDaySignServiceImpl extends ServiceImpl<UserDaySignDao, UserDaySignEntity> implements UserDaySignService {

    @Resource
    UserDaySignDao userDaySignDao;
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
    public List<UserDaySignEntity> all(Map<String, Object> params) {
        params.put("uid",authUtils.AuthUser().getUserId());
        return userDaySignDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<UserDaySignEntity> page = new Query<UserDaySignEntity>().getPage(params, "id", true);
        List<UserDaySignEntity> records = userDaySignDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param userDaySign
     * @return boolean
     */
    @Override
    public Result saveDaySign(UserDaySignEntity userDaySign) {
        try {
            if (StringUtils.isBlank(userDaySign.getId())) {
                //当前日期
                QueryWrapper queryWrapperVip = new QueryWrapper();
                queryWrapperVip.eq("uid", authUtils.AuthUser().getUserId());
                queryWrapperVip.eq("is_delete", 0);
                queryWrapperVip.eq("sign_day", userDaySign.getSignDay());
                List<UserDaySignEntity> levelOrderList = userDaySignDao.selectList(queryWrapperVip);
                if (levelOrderList.size() > 0) {
                    return Result.error("今天已签到");
                }
                Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
                int signPoints = wechat.getIntegralMoney()+userDaySign.getPoint();

                userDaySign.setId(commonUtitls.createKey());
                userDaySign.setCreateBy(authUtils.AuthUser().getUserId());
                userDaySign.setCreateTime(new Date());
                super.save(userDaySign);

                //更新log
                IntegralLogEntity xfLog = new IntegralLogEntity();
                xfLog.setXfBalance(userDaySign.getPoint());
                xfLog.setBeforeBalane(wechat.getIntegralMoney());
                xfLog.setAfterBalance(signPoints);
                xfLog.setCreateBy(wechat.getId());
                xfLog.setUserId(wechat.getId());
                xfLog.setOrderId(userDaySign.getId());
                xfLog.setXfClassify("SIGN_INTEGRAL");
                xfLog.setXfRemark("签到奖励积分");
                integralLogService.save(xfLog);
                //更新用户积分
                UpdateWrapper<Wechat> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", wechat.getId());
                wrapper.set("integral_money", signPoints);
                wechatDao.update(null, wrapper);
            } else {
                userDaySign.setUpdateBy(authUtils.AuthUser().getUserId());
                userDaySign.setUpdateTime(new Date());
                this.updateById(userDaySign);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return Result.success();
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public UserDaySignEntity detail(String id) {
        return userDaySignDao.selectById(id);
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
            UserDaySignEntity removeEntity = userDaySignDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            userDaySignDao.updateById(removeEntity);
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
                UserDaySignEntity removeEntity = userDaySignDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                userDaySignDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }
}
