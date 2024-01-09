package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.PointsmallClassifyEntity;
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
 * @since 2023-09-12 16:35:38
 */
public interface PointsmallClassifyService extends IService<PointsmallClassifyEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<PointsmallClassifyEntity>
     */
    public List<PointsmallClassifyEntity> all(Map<String, Object> params);


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
     * @param  pointsmallClassify pointsmallClassify
     * @return boolean
     */
    public boolean save(PointsmallClassifyEntity pointsmallClassify);

    /**
    * 详情
    *
    * @return
    */
    public PointsmallClassifyEntity detail(String id);

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
