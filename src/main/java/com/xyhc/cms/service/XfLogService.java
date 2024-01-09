package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.entity.XfLogEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
public interface XfLogService extends IService<XfLogEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MsCashOutEntity>
     */
    public List<XfLogEntity> all(Map<String, Object> params);

    /**
     * 已到账
     *
     * @param params
     * @return Result
     */
    public List<XfLogEntity> receivedLog(Map<String, Object> params) ;
    /**
     * 购物金明细
     *
     * @param params
     * @return List<MsCashOutEntity>
     */
    public List<XfLogEntity> shoppingMoneyall(Map<String, Object> params);


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
    public PageUtils balanceDetail(Map<String, Object> params);

    /**
     * 订单收入订单列表
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils orderRevenue(Map<String, Object> params);

    /**
     * 查询已到账的
     *
     * @return
     */
    public XfLogEntity collectedBalances();

    /**
     * 分页查询
     * 查询充值记录
     * @param params
     * @return PageUtils
     */
    public PageUtils payPage(Map<String, Object> params);

    /**
     * 保存
     *
     * @param  xfLogEntity xfLogEntity
     * @return boolean
     */
    public boolean save(XfLogEntity xfLogEntity);

    /**
     * 保存提现金额
     *
     * @param
     * @return boolean
     */
    public Result saveWithdrawMoney(XfLogEntity xfLogEntity);

    /**
     * 根据id更新提现金额的审核状态
     *
     * @return
     */
    public void saveAuditStatus(String id);


    /**
     * 驳回提现
     *
     * @return
     */
    public void reject(Map<String, Object> params);

    /**
    * 详情
    *
    * @return
    */
    public XfLogEntity detail(String id);

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
     * 更新支付
     *
     * @param orderId
     * @return boolean
     */
    public boolean updatePay(String orderId) ;

    /**
     * 处理未到账
     *
     * @param
     */

    public void notCompleteBalance() ;

    /**
     * 购物金使用明细
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils deducPage(Map<String, Object> params) ;


    /**
     * 团队业绩明细
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils teamYjPage(Map<String, Object> params) ;

    /**
     * 用户分佣明细
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils userDetailPage(Map<String, Object> params) ;


    /**
     * 分页查询 根据订单ID查询
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils balanceDetailByOrderId(Map<String, Object> params) ;


    /**
     * 已到账明细
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils balanceWithdrawnLogByUserId(Map<String, Object> params) ;
}
