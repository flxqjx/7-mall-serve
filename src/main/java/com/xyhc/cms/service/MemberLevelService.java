package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MemberLevelEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 会员级别Service接口
 *
 * @author apollo
 * @since 2023-03-22 15:18:26
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MemberLevelEntity>
     */
    public List<MemberLevelEntity> all(Map<String, Object> params);


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
     * @param  memberLevel memberLevel
     * @return boolean
     */
    public boolean save(MemberLevelEntity memberLevel);

    /**
    * 详情
    *
    * @return
    */
    public MemberLevelEntity detail(String id);

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
