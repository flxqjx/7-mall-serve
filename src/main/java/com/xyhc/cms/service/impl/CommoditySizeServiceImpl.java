package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommoditySizeDao;
import com.xyhc.cms.entity.CommoditySizeEntity;
import com.xyhc.cms.service.CommoditySizeService;
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
import com.xyhc.cms.listener.CommoditySizeListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-06-24 19:24:09
 */
@Service("commoditySizeService")
public class CommoditySizeServiceImpl extends ServiceImpl<CommoditySizeDao, CommoditySizeEntity> implements CommoditySizeService {

    @Resource
    CommoditySizeDao  commoditySizeDao;

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
    public List<CommoditySizeEntity> all(Map<String, Object> params) {
        params.put("commodityId", params.get("commodityId"));
        return commoditySizeDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("commodityId", params.get("commodityId"));
        IPage<CommoditySizeEntity> page = new Query<CommoditySizeEntity>().getPage(params, "create_time", false);
        List<CommoditySizeEntity> records = commoditySizeDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commoditySize
     * @return boolean
     */
    @Override
    public boolean save(CommoditySizeEntity commoditySize) {
        try {
            if (StringUtils.isBlank(commoditySize.getId())){
                commoditySize.setId(commonUtitls.createKey());
                commoditySize.setCreateBy(authUtils.AuthUser().getUserId());
                commoditySize.setCreateTime(new Date());
                super.save(commoditySize);
            }else{
                commoditySize.setUpdateBy(authUtils.AuthUser().getUserId());
                commoditySize.setUpdateTime(new Date());
                this.updateById(commoditySize);
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
    public CommoditySizeEntity detail(String id) {
        return commoditySizeDao.selectById(id);
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
            CommoditySizeEntity removeEntity = commoditySizeDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commoditySizeDao.updateById(removeEntity);
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
                CommoditySizeEntity removeEntity = commoditySizeDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commoditySizeDao.updateById(removeEntity);
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
            CommoditySizeListener listener = new CommoditySizeListener();
            EasyExcel.read(file.getInputStream(), CommoditySizeEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommoditySizeEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commoditySizeDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
