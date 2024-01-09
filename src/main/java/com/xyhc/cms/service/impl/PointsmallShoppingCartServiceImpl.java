package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallShoppingCartDao;
import com.xyhc.cms.entity.PointsmallShoppingCartEntity;
import com.xyhc.cms.service.PointsmallShoppingCartService;
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
import com.xyhc.cms.listener.PointsmallShoppingCartListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 赛事申请表Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
@Service("pointsmallShoppingCartService")
public class PointsmallShoppingCartServiceImpl extends ServiceImpl<PointsmallShoppingCartDao, PointsmallShoppingCartEntity> implements PointsmallShoppingCartService {

    @Resource
    PointsmallShoppingCartDao  pointsmallShoppingCartDao;

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
    public List<PointsmallShoppingCartEntity> all(Map<String, Object> params) {
        return pointsmallShoppingCartDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallShoppingCartEntity> page = new Query<PointsmallShoppingCartEntity>().getPage(params, "id", true);
        List<PointsmallShoppingCartEntity> records = pointsmallShoppingCartDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  pointsmallShoppingCart
     * @return boolean
     */
    @Override
    public boolean save(PointsmallShoppingCartEntity pointsmallShoppingCart) {
        try {
            if (StringUtils.isBlank(pointsmallShoppingCart.getId())){
                pointsmallShoppingCart.setId(commonUtitls.createKey());
                pointsmallShoppingCart.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallShoppingCart.setCreateTime(new Date());
                super.save(pointsmallShoppingCart);
            }else{
                pointsmallShoppingCart.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallShoppingCart.setUpdateTime(new Date());
                this.updateById(pointsmallShoppingCart);
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
    public PointsmallShoppingCartEntity detail(String id) {
        return pointsmallShoppingCartDao.selectById(id);
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
            PointsmallShoppingCartEntity removeEntity = pointsmallShoppingCartDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallShoppingCartDao.updateById(removeEntity);
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
                PointsmallShoppingCartEntity removeEntity = pointsmallShoppingCartDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallShoppingCartDao.updateById(removeEntity);
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
            PointsmallShoppingCartListener listener = new PointsmallShoppingCartListener();
            EasyExcel.read(file.getInputStream(), PointsmallShoppingCartEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallShoppingCartEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallShoppingCartDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
