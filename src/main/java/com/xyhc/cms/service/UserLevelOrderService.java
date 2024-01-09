package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.UserLevelEntity;
import com.xyhc.cms.entity.UserLevelOrderEntity;

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
 * @since 2023-02-14 13:58:59
 */
public interface UserLevelOrderService extends IService<UserLevelOrderEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<UserLevelOrderEntity>
     */
    public List<UserLevelOrderEntity> all(Map<String, Object> params);


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
     * @param  userLevelOrder userLevelOrder
     * @return boolean
     */
    public boolean save(UserLevelOrderEntity userLevelOrder);

    /**
     * 保存会员级别
     *
     * @param  userLevelOrder userLevelOrder
     * @return boolean
     */
    public boolean saveOrder(UserLevelOrderEntity userLevelOrder);

    /**
    * 详情
    *
    * @return
    */
    public UserLevelOrderEntity detail(String id);

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
     * 查询当前人会员级别
     *
     * @param
     * @return Result
     */
    public UserLevelEntity userLevel() ;

}
