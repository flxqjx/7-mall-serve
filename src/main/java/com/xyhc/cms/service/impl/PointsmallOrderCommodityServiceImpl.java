package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallOrderCommodityDao;
import com.xyhc.cms.entity.PointsmallOrderCommodityEntity;
import com.xyhc.cms.service.PointsmallOrderCommodityService;
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
import com.xyhc.cms.listener.PointsmallOrderCommodityListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品订单明细表Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallOrderCommodityService")
public class PointsmallOrderCommodityServiceImpl extends ServiceImpl<PointsmallOrderCommodityDao, PointsmallOrderCommodityEntity> implements PointsmallOrderCommodityService {

    @Resource
    PointsmallOrderCommodityDao  pointsmallOrderCommodityDao;

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
    public List<PointsmallOrderCommodityEntity> all(Map<String, Object> params) {
        return pointsmallOrderCommodityDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallOrderCommodityEntity> page = new Query<PointsmallOrderCommodityEntity>().getPage(params, "id", true);
        List<PointsmallOrderCommodityEntity> records = pointsmallOrderCommodityDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallOrderCommodity
     * @return boolean
     */
    @Override
    public boolean save(PointsmallOrderCommodityEntity pointsmallOrderCommodity) {
        try {
            if (StringUtils.isBlank(pointsmallOrderCommodity.getId())){
                pointsmallOrderCommodity.setId(commonUtitls.createKey());
                pointsmallOrderCommodity.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderCommodity.setCreateTime(new Date());
                super.save(pointsmallOrderCommodity);
            }else{
                pointsmallOrderCommodity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderCommodity.setUpdateTime(new Date());
                this.updateById(pointsmallOrderCommodity);
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
    public PointsmallOrderCommodityEntity detail(String id) {
        return pointsmallOrderCommodityDao.selectById(id);
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
            PointsmallOrderCommodityEntity removeEntity = pointsmallOrderCommodityDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallOrderCommodityDao.updateById(removeEntity);
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
                PointsmallOrderCommodityEntity removeEntity = pointsmallOrderCommodityDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallOrderCommodityDao.updateById(removeEntity);
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
            PointsmallOrderCommodityListener listener = new PointsmallOrderCommodityListener();
            EasyExcel.read(file.getInputStream(), PointsmallOrderCommodityEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallOrderCommodityEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallOrderCommodityDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
