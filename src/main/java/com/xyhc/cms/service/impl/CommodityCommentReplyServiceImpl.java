package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommodityCommentDao;
import com.xyhc.cms.dao.CommodityCommentReplyDao;
import com.xyhc.cms.entity.CommodityCommentEntity;
import com.xyhc.cms.entity.CommodityCommentReplyEntity;
import com.xyhc.cms.entity.CommodityOrderEntity;
import com.xyhc.cms.service.CommodityCommentReplyService;
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
import com.xyhc.cms.listener.CommodityCommentReplyListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 评论回复Service实现类
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Service("commodityCommentReplyService")
public class CommodityCommentReplyServiceImpl extends ServiceImpl<CommodityCommentReplyDao, CommodityCommentReplyEntity> implements CommodityCommentReplyService {

    @Resource
    CommodityCommentReplyDao  commodityCommentReplyDao;

    @Resource
    CommodityCommentDao commodityCommentDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;
    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityCommentReplyEntity> all(Map<String, Object> params) {
        return commodityCommentReplyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityCommentReplyEntity> page = new Query<CommodityCommentReplyEntity>().getPage(params, "id", true);
        List<CommodityCommentReplyEntity> records = commodityCommentReplyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityCommentReply
     * @return boolean
     */
    @Override
    public boolean save(CommodityCommentReplyEntity commodityCommentReply) {
        try {
            if (StringUtils.isBlank(commodityCommentReply.getId())){
                commodityCommentReply.setId(commonUtitls.createKey());
                commodityCommentReply.setCreateTime(new Date());
                super.save(commodityCommentReply);

                CommodityCommentEntity commodityCommentEntity = commodityCommentDao.selectById(commodityCommentReply.getCommentId());
                commodityCommentEntity.setIsReply(1);
                commodityCommentEntity.setUpdateTime(new Date());
                commodityCommentDao.updateById(commodityCommentEntity);

            }else{
                commodityCommentReply.setUpdateTime(new Date());
                this.updateById(commodityCommentReply);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public CommodityCommentReplyEntity detail(String id) {
        return commodityCommentReplyDao.selectById(id);
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
            CommodityCommentReplyEntity removeEntity = commodityCommentReplyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityCommentReplyDao.updateById(removeEntity);
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
                CommodityCommentReplyEntity removeEntity = commodityCommentReplyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCommentReplyDao.updateById(removeEntity);
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
            CommodityCommentReplyListener listener = new CommodityCommentReplyListener();
            EasyExcel.read(file.getInputStream(), CommodityCommentReplyEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityCommentReplyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityCommentReplyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
