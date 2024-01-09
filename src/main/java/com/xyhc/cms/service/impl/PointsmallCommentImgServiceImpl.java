package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallCommentImgDao;
import com.xyhc.cms.entity.PointsmallCommentImgEntity;
import com.xyhc.cms.service.PointsmallCommentImgService;
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
import com.xyhc.cms.listener.PointsmallCommentImgListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 评论图片Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallCommentImgService")
public class PointsmallCommentImgServiceImpl extends ServiceImpl<PointsmallCommentImgDao, PointsmallCommentImgEntity> implements PointsmallCommentImgService {

    @Resource
    PointsmallCommentImgDao  pointsmallCommentImgDao;

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
    public List<PointsmallCommentImgEntity> all(Map<String, Object> params) {
        return pointsmallCommentImgDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallCommentImgEntity> page = new Query<PointsmallCommentImgEntity>().getPage(params, "id", true);
        List<PointsmallCommentImgEntity> records = pointsmallCommentImgDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallCommentImg
     * @return boolean
     */
    @Override
    public boolean save(PointsmallCommentImgEntity pointsmallCommentImg) {
        try {
            if (StringUtils.isBlank(pointsmallCommentImg.getId())){
                pointsmallCommentImg.setId(commonUtitls.createKey());
                //pointsmallCommentImg.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentImg.setCreateTime(new Date());
                super.save(pointsmallCommentImg);
            }else{
                //pointsmallCommentImg.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentImg.setUpdateTime(new Date());
                this.updateById(pointsmallCommentImg);
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
    public PointsmallCommentImgEntity detail(String id) {
        return pointsmallCommentImgDao.selectById(id);
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
            PointsmallCommentImgEntity removeEntity = pointsmallCommentImgDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallCommentImgDao.updateById(removeEntity);
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
                PointsmallCommentImgEntity removeEntity = pointsmallCommentImgDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallCommentImgDao.updateById(removeEntity);
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
            PointsmallCommentImgListener listener = new PointsmallCommentImgListener();
            EasyExcel.read(file.getInputStream(), PointsmallCommentImgEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallCommentImgEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                //uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallCommentImgDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
