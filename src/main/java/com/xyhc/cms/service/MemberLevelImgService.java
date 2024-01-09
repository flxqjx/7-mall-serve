package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.entity.MemberLevelImgEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 商品图片Service接口
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
public interface MemberLevelImgService extends IService<MemberLevelImgEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityImgEntity>
     */
    public List<MemberLevelImgEntity> all(Map<String, Object> params);


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
     * @param  commodityImg commodityImg
     * @return boolean
     */
    public boolean save(MemberLevelImgEntity memberLevelImgEntity);

    /**
    * 详情
    *
    * @return
    */
    public MemberLevelImgEntity detail(String id);

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
