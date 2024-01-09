package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallSizeDao;
import com.xyhc.cms.entity.PointsmallSizeEntity;
import com.xyhc.cms.service.PointsmallSizeService;
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
import com.xyhc.cms.listener.PointsmallSizeListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallSizeService")
public class PointsmallSizeServiceImpl extends ServiceImpl<PointsmallSizeDao, PointsmallSizeEntity> implements PointsmallSizeService {

    @Resource
    PointsmallSizeDao  pointsmallSizeDao;

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
    public List<PointsmallSizeEntity> all(Map<String, Object> params) {
        return pointsmallSizeDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallSizeEntity> page = new Query<PointsmallSizeEntity>().getPage(params, "id", true);
        List<PointsmallSizeEntity> records = pointsmallSizeDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallSize
     * @return boolean
     */
    @Override
    public boolean save(PointsmallSizeEntity pointsmallSize) {
        try {
            if (StringUtils.isBlank(pointsmallSize.getId())){
                pointsmallSize.setId(commonUtitls.createKey());
                pointsmallSize.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallSize.setCreateTime(new Date());
                super.save(pointsmallSize);
            }else{
                pointsmallSize.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallSize.setUpdateTime(new Date());
                this.updateById(pointsmallSize);
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
    public PointsmallSizeEntity detail(String id) {
        return pointsmallSizeDao.selectById(id);
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
            PointsmallSizeEntity removeEntity = pointsmallSizeDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallSizeDao.updateById(removeEntity);
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
                PointsmallSizeEntity removeEntity = pointsmallSizeDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallSizeDao.updateById(removeEntity);
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
            PointsmallSizeListener listener = new PointsmallSizeListener();
            EasyExcel.read(file.getInputStream(), PointsmallSizeEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallSizeEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallSizeDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
