package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallCommentReplyDao;
import com.xyhc.cms.entity.PointsmallCommentReplyEntity;
import com.xyhc.cms.service.PointsmallCommentReplyService;
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
import com.xyhc.cms.listener.PointsmallCommentReplyListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 评论回复Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallCommentReplyService")
public class PointsmallCommentReplyServiceImpl extends ServiceImpl<PointsmallCommentReplyDao, PointsmallCommentReplyEntity> implements PointsmallCommentReplyService {

    @Resource
    PointsmallCommentReplyDao  pointsmallCommentReplyDao;

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
    public List<PointsmallCommentReplyEntity> all(Map<String, Object> params) {
        return pointsmallCommentReplyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallCommentReplyEntity> page = new Query<PointsmallCommentReplyEntity>().getPage(params, "id", true);
        List<PointsmallCommentReplyEntity> records = pointsmallCommentReplyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallCommentReply
     * @return boolean
     */
    @Override
    public boolean save(PointsmallCommentReplyEntity pointsmallCommentReply) {
        try {
            if (StringUtils.isBlank(pointsmallCommentReply.getId())){
                pointsmallCommentReply.setId(commonUtitls.createKey());
                //pointsmallCommentReply.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentReply.setCreateTime(new Date());
                super.save(pointsmallCommentReply);
            }else{
                //pointsmallCommentReply.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentReply.setUpdateTime(new Date());
                this.updateById(pointsmallCommentReply);
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
    public PointsmallCommentReplyEntity detail(String id) {
        return pointsmallCommentReplyDao.selectById(id);
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
            PointsmallCommentReplyEntity removeEntity = pointsmallCommentReplyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallCommentReplyDao.updateById(removeEntity);
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
                PointsmallCommentReplyEntity removeEntity = pointsmallCommentReplyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentReplyDao.updateById(removeEntity);
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
            PointsmallCommentReplyListener listener = new PointsmallCommentReplyListener();
            EasyExcel.read(file.getInputStream(), PointsmallCommentReplyEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallCommentReplyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                //uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallCommentReplyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
