package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.PointsmallClassifyEntity;
import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.entity.PointsmallOrderEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.entity.PointsmallShoppingCartEntity;
import com.xyhc.cms.vo.wechatMsg.PointsmallOrderSaveDto;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品订单表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
public interface PointsmallOrderService extends IService<PointsmallOrderEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<PointsmallOrderEntity>
     */
    public List<PointsmallOrderEntity> all(Map<String, Object> params);
    /**
     * 查询————我的订单状态各数量
     *
     */
    public Result myOrderNum();


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils page(Map<String, Object> params);

    /**
     * 查询我分享的普通用户下的单
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils pageCommon(Map<String, Object> params);


    /**
     * 分页查询全部订单
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils pageEntire(Map<String, Object> params);

    /**
     * 分页查询退货订单
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils pageRefund(Map<String, Object> params);

    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public Result saveOrder(PointsmallOrderSaveDto pointsmallOrderSave);

    /**
     * 更新商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updatePointsmallOrderStatus(String orderId, String orderNo, String tradeNo);

    /**
     * 更新新人专享商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    //public Result updatePointsmallNewOrderStatus(String orderId, String orderNo, String tradeNo);
    /**
     * 更新新人专享商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updateCommodityNewOrderStatus(String orderId, String orderNo, String tradeNo);

    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public boolean saveReceiving(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 退货申请
     */
    public boolean saveRefund(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 修改订单地址
     */
    public boolean editAddress(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 修改支付状态
     *
     * @param
     * @return boolean
     */
    public boolean updatePayStatus(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 详情
     *
     * @return
     */
    public PointsmallOrderEntity detail(String id);

    /**
     * 查询该用户是否购买过新人专享商品
     *
     * @return
     */
    public Result newOrderdetail();

    /**
     * 退货审核通过
     */
    public void audit(String id);

    /**
     * 驳回退货申请
     */
    public boolean reject(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 根据orderid查询订单详情
     *
     * @return
     */
    public PointsmallOrderEntity orderDetail(String id);

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

    /**
     * 保存
     */
    public boolean save(PointsmallOrderEntity pointsmallOrderEntity);

    /**
     * 修改订单处理
     */
    public boolean saveProcess(PointsmallOrderEntity pointsmallOrderEntity);

    public List<PointsmallOrderEntity> allExport(Map<String, Object> params);

}
