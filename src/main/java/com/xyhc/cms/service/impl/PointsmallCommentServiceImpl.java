package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallCommentDao;
import com.xyhc.cms.entity.PointsmallCommentEntity;
import com.xyhc.cms.service.PointsmallCommentService;
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
import com.xyhc.cms.listener.PointsmallCommentListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品评论Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallCommentService")
public class PointsmallCommentServiceImpl extends ServiceImpl<PointsmallCommentDao, PointsmallCommentEntity> implements PointsmallCommentService {

    @Resource
    PointsmallCommentDao  pointsmallCommentDao;

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
    public List<PointsmallCommentEntity> all(Map<String, Object> params) {
        return pointsmallCommentDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallCommentEntity> page = new Query<PointsmallCommentEntity>().getPage(params, "id", true);
        List<PointsmallCommentEntity> records = pointsmallCommentDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallComment
     * @return boolean
     */
    @Override
    public boolean save(PointsmallCommentEntity pointsmallComment) {
        try {
            if (StringUtils.isBlank(pointsmallComment.getId())){
                pointsmallComment.setId(commonUtitls.createKey());
                //pointsmallComment.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallComment.setCreateTime(new Date());
                super.save(pointsmallComment);
            }else{
                //pointsmallComment.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallComment.setUpdateTime(new Date());
                this.updateById(pointsmallComment);
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
    public PointsmallCommentEntity detail(String id) {
        return pointsmallCommentDao.selectById(id);
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
            PointsmallCommentEntity removeEntity = pointsmallCommentDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallCommentDao.updateById(removeEntity);
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
                PointsmallCommentEntity removeEntity = pointsmallCommentDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
               // removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentDao.updateById(removeEntity);
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
            PointsmallCommentListener listener = new PointsmallCommentListener();
            EasyExcel.read(file.getInputStream(), PointsmallCommentEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallCommentEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                //uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallCommentDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
