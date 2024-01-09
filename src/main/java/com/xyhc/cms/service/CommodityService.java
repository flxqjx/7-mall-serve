package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.vo.wechatMsg.CommodityImgDto;
import com.xyhc.cms.vo.wechatMsg.CommodityOrderSaveDto;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品主表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface CommodityService extends IService<CommodityEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityEntity>
     */
    public List<CommodityEntity> all(Map<String, Object> params);

    /**
     * 查询推荐
     *
     * @param params
     * @return List<CommodityEntity>
     */
    public List<CommodityEntity> allRecommend(Map<String, Object> params);

    /**
     * 查询限时秒杀商品
     *
     * @param params
     * @return List<CommodityEntity>
     */
    public List<CommodityEntity> allTimeLimit(Map<String, Object> params);

    /**
     * 查询推荐详情
     *
     * @param params
     * @return List<CommodityEntity>
     */
    public List<CommodityEntity> allIsDetailRecommend(Map<String, Object> params);


    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils page(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils pageIsNew(Map<String, Object> params);


    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public Result save(CommodityImgDto commoditySave);

    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public boolean saveRecommend(CommodityEntity commodity);

    /**
     * 设置为新人专享
     *
     * @param
     * @return boolean
     */
    public boolean saveIsNew(CommodityEntity commodity);

    /**
     * 设置为限时秒杀
     *
     * @param
     * @return boolean
     */
    public boolean saveTimeLimit(CommodityEntity commodity);


    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public boolean saveIsDetailRecommend(CommodityEntity commodity);

    /**
     * 收藏
     *
     * @param
     * @return boolean
     */
    public boolean saveIsCollect(CommodityEntity commodityEntity);

    /**
     * 取消收藏
     *
     * @param
     * @return boolean
     */
    public boolean saveCloseIsCollect(CommodityEntity commodityEntity);

    /**
     * 保存是否上架下架
     *
     * @param
     * @return boolean
     */
    public boolean saveIsBuy(CommodityEntity commodity);


    /**
    * 详情
    *
    * @return
    */
    public CommodityEntity detail(String id,String uid);

    /**
     * 详情
     *
     * @return
     */
    public CommodityEntity detailOrder(String id);

    /**
     * 详情
     *
     * @return
     */
    public CommodityEntity detailNew(String id);

    /**
     * 详情
     *
     * @return
     */
    public CommodityEntity detailOne(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);
    /**
     * 保存是否新品
     *
     * @param
     * @return boolean
     */
    public boolean isNewCommodity(CommodityEntity commodity);

    /**
     * 推荐购物车页面
     *
     * @param
     * @return boolean
     */
    public boolean isRecommendCart(CommodityEntity commodity);

    /**
     * 推荐收藏页面
     *
     * @param
     * @return boolean
     */
    public boolean isRecommendCollect(CommodityEntity commodity);

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    public boolean removeBatch(String[] ids);

    /**
      * 导入
      *
      * @param file
      * @return boolean
      */
    public Result upload(MultipartFile file) throws IOException;
}
