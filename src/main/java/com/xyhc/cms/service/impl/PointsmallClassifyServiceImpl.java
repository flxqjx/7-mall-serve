package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallClassifyDao;
import com.xyhc.cms.entity.PointsmallClassifyEntity;
import com.xyhc.cms.service.PointsmallClassifyService;
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
import com.xyhc.cms.listener.PointsmallClassifyListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品分类表Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallClassifyService")
public class PointsmallClassifyServiceImpl extends ServiceImpl<PointsmallClassifyDao, PointsmallClassifyEntity> implements PointsmallClassifyService {

    @Resource
    PointsmallClassifyDao  pointsmallClassifyDao;

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
    public List<PointsmallClassifyEntity> all(Map<String, Object> params) {
        return pointsmallClassifyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallClassifyEntity> page = new Query<PointsmallClassifyEntity>().getPage(params, "id", true);
        List<PointsmallClassifyEntity> records = pointsmallClassifyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallClassify
     * @return boolean
     */
    @Override
    public boolean save(PointsmallClassifyEntity pointsmallClassify) {
        try {
            if (StringUtils.isBlank(pointsmallClassify.getId())){
                pointsmallClassify.setId(commonUtitls.createKey());
                pointsmallClassify.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallClassify.setCreateTime(new Date());
                super.save(pointsmallClassify);
            }else{
                pointsmallClassify.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallClassify.setUpdateTime(new Date());
                this.updateById(pointsmallClassify);
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
    public PointsmallClassifyEntity detail(String id) {
        return pointsmallClassifyDao.selectById(id);
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
            PointsmallClassifyEntity removeEntity = pointsmallClassifyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallClassifyDao.updateById(removeEntity);
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
                PointsmallClassifyEntity removeEntity = pointsmallClassifyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallClassifyDao.updateById(removeEntity);
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
            PointsmallClassifyListener listener = new PointsmallClassifyListener();
            EasyExcel.read(file.getInputStream(), PointsmallClassifyEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallClassifyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallClassifyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
