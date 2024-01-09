package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityEntity;
import com.xyhc.cms.entity.PointsmallEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;

import com.xyhc.cms.vo.wechatMsg.PointsmallImgDto;
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
 * 商品主表Service接口
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
public interface PointsmallService extends IService<PointsmallEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<PointsmallEntity>
     */
    public List<PointsmallEntity> all(Map<String, Object> params);


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
     * @param  pointsmall
     * @return boolean
     */

    public Result save(PointsmallImgDto pointsmallSave);
    /**
    * 详情
    *
    * @return
    */
    public PointsmallEntity detail(String id);
    /**
     * 详情
     *
     * @return
     */
    public PointsmallEntity detailOrder(String id);
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
