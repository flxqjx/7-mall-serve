package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallSpecDao;
import com.xyhc.cms.entity.PointsmallSpecEntity;
import com.xyhc.cms.service.PointsmallSpecService;
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
import com.xyhc.cms.listener.PointsmallSpecListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品规格Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallSpecService")
public class PointsmallSpecServiceImpl extends ServiceImpl<PointsmallSpecDao, PointsmallSpecEntity> implements PointsmallSpecService {

    @Resource
    PointsmallSpecDao  pointsmallSpecDao;

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
    public List<PointsmallSpecEntity> all(Map<String, Object> params) {
        return pointsmallSpecDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallSpecEntity> page = new Query<PointsmallSpecEntity>().getPage(params, "id", true);
        List<PointsmallSpecEntity> records = pointsmallSpecDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallSpec
     * @return boolean
     */
    @Override
    public boolean save(PointsmallSpecEntity pointsmallSpec) {
        try {
            if (StringUtils.isBlank(pointsmallSpec.getId())){
                pointsmallSpec.setId(commonUtitls.createKey());
                pointsmallSpec.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallSpec.setCreateTime(new Date());
                super.save(pointsmallSpec);
            }else{
                pointsmallSpec.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallSpec.setUpdateTime(new Date());
                this.updateById(pointsmallSpec);
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
    public  PointsmallSpecEntity detail(String id) {
        return pointsmallSpecDao.selectById(id);
    }

    public  PointsmallSpecEntity detailbyid(String id) {
        return pointsmallSpecDao.selectById(id);
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
            PointsmallSpecEntity removeEntity = pointsmallSpecDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallSpecDao.updateById(removeEntity);
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
                PointsmallSpecEntity removeEntity = pointsmallSpecDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallSpecDao.updateById(removeEntity);
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
            PointsmallSpecListener listener = new PointsmallSpecListener();
            EasyExcel.read(file.getInputStream(), PointsmallSpecEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallSpecEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallSpecDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
