package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityClassifyEntity;
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
 * 商品分类表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface CommodityClassifyService extends IService<CommodityClassifyEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityClassifyEntity>
     */
    public List<CommodityClassifyEntity> all(Map<String, Object> params);

    /**
     * 根据二级id查询三级
     *
     * @param params
     * @return List<CommodityClassifyEntity>
     */
    public List<CommodityClassifyEntity> lastLevelAll(Map<String, Object> params);


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
     * @param  commodityClassify commodityClassify
     * @return boolean
     */
    public boolean saveClassify(CommodityClassifyEntity commodityClassify);

    /**
    * 详情
    *
    * @return
    */
    public CommodityClassifyEntity detail(String id);

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
