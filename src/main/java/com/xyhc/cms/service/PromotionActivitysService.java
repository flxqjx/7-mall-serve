package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsLotteryEntity;
import com.xyhc.cms.entity.PromotionActivitysEntity;

import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface PromotionActivitysService extends IService<PromotionActivitysEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public List<PromotionActivitysEntity> all(Map<String, Object> params);


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
     * @param  promotionActivitys promotionActivitys
     * @return boolean
     */
    public boolean save(PromotionActivitysEntity promotionActivitys);

    /**
    * 详情
    *
    * @return
    */
    public PromotionActivitysEntity detail(String id);

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
     * 开启
     *
     * @return
     */
    public Result open(String id);

    /**
     * 关闭
     *
     * @return
     */
    public void cancelOpen(String id);
}
