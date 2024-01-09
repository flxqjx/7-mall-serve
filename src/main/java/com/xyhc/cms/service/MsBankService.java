package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MsBankEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author apollo
 * @since 2023-02-28 18:10:04
 */
public interface MsBankService extends IService<MsBankEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MsBankEntity>
     */
    public List<MsBankEntity> all(Map<String, Object> params);


    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils bankList(Map<String, Object> params);

    /**
     * 保存
     *
     * @param  msBank msBank
     * @return boolean
     */
    public boolean saveBank(MsBankEntity msBank);

    /**
    * 详情
    *
    * @return
    */
    public MsBankEntity detail(String id);

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
