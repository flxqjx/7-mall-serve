package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsCashOutEntity;
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
public interface MsCashOutService extends IService<MsCashOutEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MsCashOutEntity>
     */
    public List<MsCashOutEntity> all(Map<String, Object> params);


    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils page(Map<String, Object> params);

    /**
     * 分页查询
     * 后台提现明细
     * @param params
     * @return PageUtils
     */
    public PageUtils pageBack(Map<String, Object> params);

    /**
     * 分页查询
     * 用户提现明细
     * @param params
     * @return PageUtils
     */
    public PageUtils pageByUid(Map<String, Object> params);

    /**
     * 保存
     *
     * @param  msCashOut msCashOut
     * @return boolean
     */
    public boolean save(MsCashOutEntity msCashOut);

    /**
     * 保存提现金额
     *
     * @param
     * @return boolean
     */
    public Result saveWithdrawMoney(MsCashOutEntity msCashOutEntity);

    /**
     * 根据id更新提现金额的审核状态
     *
     * @return
     */
    public void saveAuditStatus(MsCashOutEntity msCashOutEntity);


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
    public MsCashOutEntity detail(String id);

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
     * 更新提现状态
     *
     * @return boolean
     */
    public void updateCashTransferStatus() ;
}
