package com.xyhc.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.entity.CommodityCommentEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.vo.wechatMsg.CommodityCommentsDto;
import com.xyhc.cms.vo.wechatMsg.CommodityImgDto;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品评论Service接口
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
public interface CommodityCommentService extends IService<CommodityCommentEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<CommodityCommentEntity>
     */
    public List<CommodityCommentEntity> all(Map<String, Object> params);

    /**
     * 获取评价
     */
    public List<CommodityCommentEntity> commentsIsReply(Map<String, Object> params);


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
     * @param  commodityComment commodityComment
     * @return boolean
     */
    public boolean save(CommodityCommentEntity commodityComment);

    /**
     * 保存评价
     *
     * @param
     * @return boolean
     */
    public Result saveComments(CommodityCommentsDto commentsSave);

    /**
    * 详情
    *
    * @return
    */
    public CommodityCommentEntity detail(String id);

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
