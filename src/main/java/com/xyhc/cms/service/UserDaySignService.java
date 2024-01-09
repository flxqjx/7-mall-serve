package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.UserDaySignEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 签到记录表Service接口
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
public interface UserDaySignService extends IService<UserDaySignEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<UserDaySignEntity>
     */
    public List<UserDaySignEntity> all(Map<String, Object> params);


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
     * @param  userDaySign
     * @return boolean
     */
    public Result saveDaySign(UserDaySignEntity userDaySign);

    /**
    * 详情
    *
    * @return
    */
    public UserDaySignEntity detail(String id);

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
