package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.entity.CouponReceiveEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 商品图片Service接口
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
public interface CouponReceiveService extends IService<CouponReceiveEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityImgEntity>
     */
    public List<CouponReceiveEntity> all(Map<String, Object> params);


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
     * @param  couponReceive couponReceive
     * @return boolean
     */
    public boolean save(CouponReceiveEntity couponReceive);

    /**
    * 详情
    *
    * @return
    */
    public CouponReceiveEntity detail(String id);

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
     * 查询我的优惠券
     *
     * @param params
     * @return Result
     */
    public List<CouponReceiveEntity> myCoupon(Map<String, Object> params) ;

}
