package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.SysAccessTokenEntity;

import java.util.List;
import java.util.Map;

/**
 * 微信认证TOKENService接口
 *
 * @author apollo
 * @since 2023-03-29 20:25:52
 */
public interface SysAccessTokenService extends IService<SysAccessTokenEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<SysAccessTokenEntity>
     */
    public List<SysAccessTokenEntity> all(Map<String, Object> params);


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
     * @param  sysAccessToken sysAccessToken
     * @return boolean
     */
    public boolean save(SysAccessTokenEntity sysAccessToken);

    /**
    * 详情
    *
    * @return
    */
    public SysAccessTokenEntity detail(String id);

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
