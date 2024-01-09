package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityCommentImgService;
import com.xyhc.cms.service.CommodityCommentReplyService;
import com.xyhc.cms.service.CommodityCommentService;
import com.xyhc.cms.service.WechatService;
import com.xyhc.cms.vo.wechatMsg.CommodityCommentsDto;
import com.xyhc.cms.vo.wechatMsg.CommodityImgDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import com.xyhc.cms.config.auth.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.utils.CommonUtitls;
import com.alibaba.excel.EasyExcel;
import com.xyhc.cms.listener.CommodityCommentListener;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商品评论Service实现类
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Service("commodityCommentService")
public class CommodityCommentServiceImpl extends ServiceImpl<CommodityCommentDao, CommodityCommentEntity> implements CommodityCommentService {

    @Resource
    CommodityCommentDao commodityCommentDao;

    @Resource
    WechatDao wechatDao;

    @Resource
    CommodityCommentImgDao commodityCommentImgDao;

    @Resource
    CommodityDao commodityDao;

    @Resource
    CommodityOrderDao commodityOrderDao;

    @Resource
    CommodityCommentReplyDao commodityCommentReplyDao;

    @Resource
    CommodityOrderCommodityDao commodityOrderCommodityDao;

    @Resource
    CommodityCommentReplyService commodityCommentReplyService;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    CommodityCommentImgService commodityCommentImgService;

    @Autowired
    WechatService wechatService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityCommentEntity> all(Map<String, Object> params) {
        return commodityCommentDao.all(params);
    }


    /**
     * 查询评价
     */
    @Override
    public List<CommodityCommentEntity> commentsIsReply(Map<String, Object> params) {
        params.put("commodityId", params.get("commodityId"));
        List<CommodityCommentEntity> records = commodityCommentDao.commentsIsReply(params);
        records.forEach(item -> {
            QueryWrapper<CommodityCommentImgEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("comment_id", item.getId());
            wrapper.eq("is_delete", 0);
            List<CommodityCommentImgEntity> imgList = commodityCommentImgDao.selectList(wrapper);
            item.setImgList(imgList);
            Wechat wechat = wechatDao.selectById(item.getUid());
            if (wechat != null) {
                item.setNickName(wechat.getNickName());
                item.setAvatarUrl(wechat.getAvatarUrl());
            }else {
                item.setNickName(item.getCommentsName());
                item.setAvatarUrl(item.getCommentsImg());
            }
            QueryWrapper<CommodityCommentReplyEntity> wrapperReply = new QueryWrapper<>();
            wrapperReply.eq("comment_id", item.getId());
            CommodityCommentReplyEntity commodityCommentReplyEntity = commodityCommentReplyDao.selectOne(wrapperReply);
            if (commodityCommentReplyEntity != null) {
                item.setReplyRemark(commodityCommentReplyEntity.getReplyRemark());
            }
        });
        return records;
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityCommentEntity> page = new Query<CommodityCommentEntity>().getPage(params, "T.create_time", false);
        List<CommodityCommentEntity> records = commodityCommentDao.page(page, params);
        records.forEach(item -> {
            Wechat wechat = wechatDao.selectById(item.getUid());
            if (wechat != null) {
                item.setNickName(wechat.getNickName());
            }else {
                item.setNickName(item.getCommentsName());
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param commodityComment
     * @return boolean
     */
    @Override
    public boolean save(CommodityCommentEntity commodityComment) {
        try {
            if (StringUtils.isBlank(commodityComment.getId())) {
                commodityComment.setId(commonUtitls.createKey());
                commodityComment.setCreateTime(new Date());
                super.save(commodityComment);
            } else {
                commodityComment.setUpdateTime(new Date());
                this.updateById(commodityComment);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 保存评价
     *
     * @param
     */
    @Override
    public Result saveComments(@RequestBody CommodityCommentsDto commentsSave) {
        try {
            CommodityCommentEntity commodityCommentEntity = commentsSave.getCommentsForm();
            if (StringUtils.isBlank(commodityCommentEntity.getId())) {
                commodityCommentEntity.setId(commonUtitls.createKey());
                commodityCommentEntity.setUid(authUtils.AuthUser().getUserId());
                commodityCommentEntity.setCreateTime(new Date());
                super.save(commodityCommentEntity);
                List<CommodityCommentImgEntity> imgList = commentsSave.getImgList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommentId(commodityCommentEntity.getId());
                        commodityCommentImgService.save(item);
                    });
                }
                //更改订单表的状态
                UpdateWrapper<CommodityOrderCommodityEntity> wrapperLevel = new UpdateWrapper<>();
                wrapperLevel.eq("order_id", commodityCommentEntity.getOrderId());
                List<CommodityOrderCommodityEntity> commodityOrderCommodity = commodityOrderCommodityDao.selectList(wrapperLevel);
                if (commodityOrderCommodity.size() > 0) {
                    CommodityOrderCommodityEntity updataCommodity = commodityOrderCommodityDao.selectById(commodityCommentEntity.getCommodityOrderCommodityId());
                    updataCommodity.setCommentStatus("1");
                    commodityOrderCommodityDao.updateById(updataCommodity);
                    //查询一条订单对应多条商品表的已评价商品
                    UpdateWrapper<CommodityOrderCommodityEntity> commentStatus = new UpdateWrapper<>();
                    commentStatus.eq("comment_status", '1');
                    commentStatus.eq("order_id", commodityCommentEntity.getOrderId());
                    List<CommodityOrderCommodityEntity> commentStatusSize = commodityOrderCommodityDao.selectList(commentStatus);
                    //查询一条订单对应多条商品表的商品
                    UpdateWrapper<CommodityOrderCommodityEntity> commodityOrderWrapper = new UpdateWrapper<>();
                    commodityOrderWrapper.eq("order_id", commodityCommentEntity.getOrderId());
                    List<CommodityOrderCommodityEntity> order = commodityOrderCommodityDao.selectList(commodityOrderWrapper);
                    if (commentStatusSize.size() > 0 && order.size() > 0) {
                        //如果这条订单里的多条商品表里的已评价的商品条数等于这条订单里的多条商品表里的商品
                        if (commentStatusSize.size() == order.size()) {
                            //更新状态
                            CommodityOrderEntity commodityOrderStatus = commodityOrderDao.selectById(commodityCommentEntity.getOrderId());
                            commodityOrderStatus.setOrderStatus("COMMENT");
                            commodityOrderDao.updateById(commodityOrderStatus);
                        }
                    }
                }
//                //更改订单表的状态
//                CommodityOrderEntity commodityOrderEntity = commodityOrderDao.selectById(commodityCommentEntity.getOrderId());
//                if(commodityOrderEntity!=null){
//                    CommodityOrderEntity commodityOrderStatus= commodityOrderDao.selectById(commodityOrderEntity.getId());
//                    commodityOrderStatus.setOrderStatus("COMMENT");
//                    commodityOrderDao.updateById(commodityOrderStatus);
//                }

            } else {
                commodityCommentEntity.setUpdateTime(new Date());
                this.updateById(commodityCommentEntity);
                List<CommodityCommentImgEntity> imgList = commentsSave.getImgList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommentId(commodityCommentEntity.getId());
                        commodityCommentImgService.save(item);
                    });
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 详情
     *
     * @return
     */
    @Override
    public CommodityCommentEntity detail(String id) {
        return commodityCommentDao.selectById(id);
    }

    /**
     * 删除
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean remove(String id) {
        try {
            CommodityCommentEntity removeEntity = commodityCommentDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityCommentDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(String[] ids) {

        try {
            for (String id : ids) {
                CommodityCommentEntity removeEntity = commodityCommentDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCommentDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 导入
     *
     * @param file
     * @return boolean
     */
    @Override
    public Result upload(MultipartFile file) throws IOException {
        try {
            CommodityCommentListener listener = new CommodityCommentListener();
            EasyExcel.read(file.getInputStream(), CommodityCommentEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityCommentEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityCommentDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
