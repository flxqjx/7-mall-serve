package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.ApplyContractEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.entity.ApplyJoinEntity;
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
 * @since 2023-05-24 15:55:30
 */
public interface ApplyContractService extends IService<ApplyContractEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<ApplyContractEntity>
     */
    public List<ApplyContractEntity> all(Map<String, Object> params);


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
     * @param  applyContract applyContract
     * @return boolean
     */
    public Result saveContract(ApplyContractEntity applyContract);

    /**
     * 详情
     *
     * @return
     */
    public ApplyContractEntity detail(String id);

    /**
    * 详情
    *
    * @return
    */
    public ApplyContractEntity detailContract(String levelId,String isUpgrade);

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
     * 查询当前用户的合同
     *
     * @return
     */
    public ApplyContractEntity detailByUserId() ;
}
