package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MatchClassifyDao;
import com.xyhc.cms.dao.MsLotteryDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.listener.MatchClassifyListener;
import com.xyhc.cms.service.MatchClassifyService;
import com.xyhc.cms.service.MsLotteryService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("msLotteryService")
public class MsLotteryServiceImpl extends ServiceImpl<MsLotteryDao, MsLotteryEntity> implements MsLotteryService {

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
    public List<MsLotteryEntity> all(Map<String, Object> params) {

        return msLotteryDao.all(params);
    }

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public Result randomLotteryNum(Map<String, Object> params) {
        Map<String, Object> mapRep = new HashMap<>();
        //是否在活动期间内
        params.put("nowDate",params.get("nowDate"));
        List<MsLotteryEntity> msLotteryNum = msLotteryDao.randomLotteryNum(params);
        //在活动期间内
        if (msLotteryNum.size()>0){
            mapRep.put("isActivity",1);
        }
        //是否生日
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
        String dateString = formatter.format(wechat.getBirthday());
        if (formatter.format(wechat.getBirthday()).contains(params.get("birth").toString())){
            mapRep.put("isBirthday",1);
        }

        return Result.success(mapRep);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MsLotteryEntity> page = new Query<MsLotteryEntity>().getPage(params, "create_time", false);
        List<MsLotteryEntity> records = msLotteryDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     * @param  msLottery
     * @return boolean
     */
    @Override
    public boolean save(MsLotteryEntity msLottery) {
        try {
            if (StringUtils.isBlank(msLottery.getId())){
                msLottery.setId(commonUtitls.createKey());
                msLottery.setCreateBy(authUtils.AuthUser().getUserId());
                msLottery.setCreateTime(new Date());
                super.save(msLottery);
            }else{
                msLottery.setUpdateBy(authUtils.AuthUser().getUserId());
                msLottery.setUpdateTime(new Date());
                this.updateById(msLottery);
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
    public MsLotteryEntity detail(String id) {
        return msLotteryDao.selectById(id);
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
            MsLotteryEntity removeEntity = msLotteryDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            msLotteryDao.updateById(removeEntity);
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
                MsLotteryEntity removeEntity = msLotteryDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                msLotteryDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 最新的抽奖活动
     *
     * @return
     */
    @Override
    public MsLotteryEntity lastLottery() {
        QueryWrapper<MsLotteryEntity> query = new QueryWrapper<>();
        query.eq("is_delete", 0);
        query.orderByDesc("create_time");
        List<MsLotteryEntity> msLotteryList = msLotteryDao.selectList(query);
        MsLotteryEntity msLotteryEntity = msLotteryList.get(0);
        return msLotteryEntity;
    }
}
