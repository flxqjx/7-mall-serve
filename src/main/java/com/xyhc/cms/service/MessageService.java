package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.InformationEntity;
import com.xyhc.cms.entity.MessageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 资讯主表Service接口
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
public interface MessageService extends IService<MessageEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<InformationEntity>
     */
    public List<MessageEntity> all(Map<String, Object> params);


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
     * @param  information information
     * @return boolean
     */
    public boolean save(MessageEntity messageEntity);

    /**
    * 详情
    *
    * @return
    */
    public MessageEntity detail(String id);

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
