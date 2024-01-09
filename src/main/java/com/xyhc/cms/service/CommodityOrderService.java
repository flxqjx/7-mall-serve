package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.vo.wechatMsg.CommodityOrderSaveDto;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
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
public interface CommodityOrderService extends IService<CommodityOrderEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityOrderEntity>
     */
    public List<CommodityOrderEntity> all(Map<String, Object> params);
    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityOrderEntity>
     */
    public List<CommodityOrderEntity> allByUid(Map<String, Object> params);


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
     * 查询————我的订单状态各数量
     */
    public Result myOrderNum();

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
    public Result saveOrder(CommodityOrderSaveDto commodityOrderSave);

    /**
     * 更新商品下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updateCommodityOrderStatus(String orderId, String orderNo, String tradeNo);

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
    public boolean saveReceiving(CommodityOrderEntity commodityOrderEntity) throws IOException;

    /**
     * 退货申请
     */
    public boolean saveRefund(CommodityOrderEntity commodityOrderEntity);

    /**
     * 修改订单地址
     */
    public boolean editAddress(CommodityOrderEntity commodityOrderEntity);

    /**
     * 修改支付状态
     *
     * @param
     * @return boolean
     */
    public boolean updatePayStatus(CommodityOrderEntity commodityOrderEntity);

    /**
     * 详情
     *
     * @return
     */
    public CommodityOrderEntity detail(String id);

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
    public boolean reject(CommodityOrderEntity commodityOrderEntity);

    /**
     * 根据orderid查询订单详情
     *
     * @return
     */
    public CommodityOrderEntity orderDetail(String id);

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
    public boolean save(CommodityOrderEntity commodityOrderEntity);

    /**
     * 修改订单处理
     */
    public boolean saveProcess(CommodityOrderEntity commodityOrderEntity);

    public List<CommodityOrderEntity> allExport(Map<String, Object> params);

    /**
     * 下单两天后返积分
     */
    public boolean saveReceivingPoints();

    /**
     * 下单两天后返钱
     */
    public boolean saveReceivingMall();

    /**
     * 7日后到账(待确认收货自动变已确认收货)-自动处理
     *
     * @param
     */

    public boolean saveToReceivingMall();

    /**
     * 会员订单自动到账
     *
     * @param
     * @return boolean
     */
    public boolean vipRewardDeal();


    /**
     * 重新处理日志
     *
     * @param
     * @return boolean
     */
    public boolean saveToReceivingMallTransfer();
}
