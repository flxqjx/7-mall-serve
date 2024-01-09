package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryInfoEntity;
import com.xyhc.cms.entity.MsLotteryUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface MsLotteryUserService extends IService<MsLotteryUserEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public List<MsLotteryUserEntity> all(Map<String, Object> params);

    /**
     * 查询全部用户中奖记录
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public List<MsLotteryUserEntity> userAll(Map<String, Object> params);


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
     * @param  msLotteryUser msLotteryUser
     * @return boolean
     */
    public Result saveOrder(MsLotteryUserEntity msLotteryUser);

    /**
    * 详情
    *
    * @return
    */
    public MsLotteryUserEntity detail(String id);

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
     * 更新奖品运费支付回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updateLotteryOrderStatus(String orderId, String orderNo, String tradeNo);

    /**
     * 奖品发货
     *
     * @return
     */
    public void sendLottery(Map<String, Object> params);

    /**
     * 更新抽奖积分
     *
     * @return
     */
    public void updateLotteryPoints(Map<String, Object> params);
}
