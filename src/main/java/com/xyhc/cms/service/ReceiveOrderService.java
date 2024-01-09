package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.entity.ReceiveOrderEntity;
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
 * Service接口
 *
 * @author apollo
 * @since 2023-09-28 19:30:42
 */
public interface ReceiveOrderService extends IService<ReceiveOrderEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<ReceiveOrderEntity>
     */
    public List<ReceiveOrderEntity> all(Map<String, Object> params);

    /**
     * 查询我的领取记录
     *
     * @param params
     * @return List<ReceiveOrderEntity>
     */
    public List<ReceiveOrderEntity> myOrder(Map<String, Object> params);


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
     * @param  receiveOrder receiveOrder
     * @return boolean
     */
    public Result saveOrder(ReceiveOrderEntity receiveOrder);

    /**
     * 修改订单处理
     */
    public boolean saveProcess(ReceiveOrderEntity receiveOrder);

    /**
    * 详情
    *
    * @return
    */
    public ReceiveOrderEntity detail(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);

    /**
     * 查询我是否已领取
     *
     */
    public Result isWhetherCollect();

    /**
     * 保存
     *
     * @param
     * @return boolean
     */
    public boolean saveReceiving(String id);

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
