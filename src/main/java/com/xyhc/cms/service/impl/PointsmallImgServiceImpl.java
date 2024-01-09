package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallImgDao;
import com.xyhc.cms.entity.PointsmallImgEntity;
import com.xyhc.cms.service.PointsmallImgService;
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
import com.xyhc.cms.listener.PointsmallImgListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品图片Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallImgService")
public class PointsmallImgServiceImpl extends ServiceImpl<PointsmallImgDao, PointsmallImgEntity> implements PointsmallImgService {

    @Resource
    PointsmallImgDao  pointsmallImgDao;

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
    public List<PointsmallImgEntity> all(Map<String, Object> params) {
        return pointsmallImgDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallImgEntity> page = new Query<PointsmallImgEntity>().getPage(params, "create_time", false);
        List<PointsmallImgEntity> records = pointsmallImgDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallImg
     * @return boolean
     */
    @Override
    public boolean save(PointsmallImgEntity pointsmallImg) {
        try {
            //if (StringUtils.isBlank(pointsmallImg.getId())){
            if (pointsmallImg.getId()==0){
                //pointsmallImg.setId(commonUtitls.createKey());
                //pointsmallImg.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallImg.setCreateTime(new Date());
                super.save(pointsmallImg);
            }else{
                //pointsmallImg.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallImg.setUpdateTime(new Date());
                this.updateById(pointsmallImg);
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
    public PointsmallImgEntity detail(String id) {
        return pointsmallImgDao.selectById(id);
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
            PointsmallImgEntity removeEntity = pointsmallImgDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallImgDao.updateById(removeEntity);
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
                PointsmallImgEntity removeEntity = pointsmallImgDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                //removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallImgDao.updateById(removeEntity);
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
            PointsmallImgListener listener = new PointsmallImgListener();
            EasyExcel.read(file.getInputStream(), PointsmallImgEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallImgEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                //uploadItem.setId(commonUtitls.createKey());
                //uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallImgDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
