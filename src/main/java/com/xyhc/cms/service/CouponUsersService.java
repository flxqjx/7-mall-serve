package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CouponUsersEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 优惠券领取Service接口
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
//public class IntegralLogServiceImpl extends ServiceImpl<IntegralLogDao, IntegralLogEntity> implements IntegralLogService {
  public interface CouponUsersService extends IService<CouponUsersEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CouponUsersEntity>
     */
    public List<CouponUsersEntity> all(Map<String, Object> params);


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
     * @param  couponUsers couponUsers
     * @return boolean
     */
    //public Result save(CouponUsersEntity couponUsers,String couponId);
      public Result saveUsers(CouponUsersEntity couponUsers);


  /**
   * 通过复制券码准备赠送给别人，此时先向COUPONUSERS表中插入一条记录
   *
   * @param  couponUsersEN couponUsersEN
   * @return boolean
   */
      public Result copycode(CouponUsersEntity couponUsersEN);
    /**
    * 详情
    *
    * @return
    */

    public CouponUsersEntity detail(String id);
    /**
     * 通过复制办法得到券码后，进行兑换
     *
     * @return
     */
    //public boolean exchangecode(String integralcode,CouponUsersEntity couponUsers);
    public Result exchangecode(CouponUsersEntity couponUsers);

    /**
     * 通过分享链接办法得到优惠券
     *
     * @param  couponUsers   4445555
     * @return boolean
     */
    //public String datelshare(CouponUsersEntity couponUsers);
    public Result  datelshare(CouponUsersEntity couponUsers);

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
      * 导入
      *
      * @param file
      * @return boolean
      */
    public Result upload(MultipartFile file) throws IOException;
}
