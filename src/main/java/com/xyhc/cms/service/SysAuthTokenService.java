package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.SysAuthTokenEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统登录TOKENService接口
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
public interface SysAuthTokenService extends IService<SysAuthTokenEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<SysAuthTokenEntity>
     */
    public List<SysAuthTokenEntity> all(Map<String, Object> params);


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
     * @param  sysAuthToken sysAuthToken
     * @return boolean
     */
    public boolean save(SysAuthTokenEntity sysAuthToken);

    /**
    * 详情
    *
    * @return
    */
    public SysAuthTokenEntity detail(String id);

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


}
