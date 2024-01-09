package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.ApplyJoinEntity;
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
 * @since 2023-05-24 13:40:41
 */
public interface ApplyJoinService extends IService<ApplyJoinEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<ApplyJoinEntity>
     */
    public List<ApplyJoinEntity> all(Map<String, Object> params);


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
     * @param  applyJoin applyJoin
     * @return boolean
     */
    public boolean save(ApplyJoinEntity applyJoin);

    /**
     * 厨师申请审核通过
     */
    public void audit(String id);

    /**
     * 驳回厨师申请
     */
    public boolean reject(ApplyJoinEntity applyJoinEntity);

    /**
    * 详情
    *
    * @return
    */
    public ApplyJoinEntity detail(String id);

    /**
     * 查询用户是否申请加盟
     */
    public ApplyJoinEntity whetherApplyJoin();

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
