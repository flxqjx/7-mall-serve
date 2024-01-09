package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.IntegralLogEntity;
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
 * @since 2023-08-17 15:26:30
 */
public interface IntegralLogService extends IService<IntegralLogEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<IntegralLogEntity>
     */
    public List<IntegralLogEntity> all(Map<String, Object> params);

    /**
     * 后台查询用户全部数据
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils pcAllByUid(Map<String, Object> params);
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
     * @param  integralLog integralLog
     * @return boolean
     */
    public boolean save(IntegralLogEntity integralLog);

    /**
    * 详情
    *
    * @return
    */
    public IntegralLogEntity detail(String id);

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
     *
     * @param  integralLog
     * @return boolean
     */
    public boolean saveInternal(IntegralLogEntity integralLog) ;
}
