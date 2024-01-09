package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.MsLotteryInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface MsLotteryInfoService extends IService<MsLotteryInfoEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public List<MsLotteryInfoEntity> all(Map<String, Object> params);


    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils page(Map<String, Object> params);

    /**
     * 保存
     *
     * @param  msLotteryInfo msLotteryInfo
     * @return boolean
     */
    public boolean save(MsLotteryInfoEntity msLotteryInfo);

    /**
    * 详情
    *
    * @return
    */
    public MsLotteryInfoEntity detail(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    public boolean removeBatch(String[] ids);

    /**
     *随机返回抽奖奖品
     *
     * @return
     */
    public Result randomLotteryInfo(Map<String, Object> params);

}
