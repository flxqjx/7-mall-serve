package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommodityOrderCommodityDao;
import com.xyhc.cms.dao.CommoditySpecDao;
import com.xyhc.cms.entity.CommodityOrderCommodityEntity;
import com.xyhc.cms.entity.CommoditySpecEntity;
import com.xyhc.cms.service.CommodityOrderCommodityService;
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
import com.xyhc.cms.listener.CommodityOrderCommodityListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品订单明细表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("commodityOrderCommodityService")
public class CommodityOrderCommodityServiceImpl extends ServiceImpl<CommodityOrderCommodityDao, CommodityOrderCommodityEntity> implements CommodityOrderCommodityService {

    @Resource
    CommodityOrderCommodityDao  commodityOrderCommodityDao;
    @Resource
    CommoditySpecDao commoditySpecDao;

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
    public List<CommodityOrderCommodityEntity> all(Map<String, Object> params) {
        return commodityOrderCommodityDao.all(params);
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityOrderCommodityEntity> page = new Query<CommodityOrderCommodityEntity>().getPage(params, "T.id", true);
        List<CommodityOrderCommodityEntity> records = commodityOrderCommodityDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityOrderCommodity
     * @return boolean
     */
    @Override
    public boolean save(CommodityOrderCommodityEntity commodityOrderCommodity) {
        try {
            if (StringUtils.isBlank(commodityOrderCommodity.getId())){
                commodityOrderCommodity.setId(commonUtitls.createKey());
                commodityOrderCommodity.setCreateBy(authUtils.AuthUser().getUserId());
                commodityOrderCommodity.setCreateTime(new Date());
                super.save(commodityOrderCommodity);
            }else{
                commodityOrderCommodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderCommodity.setUpdateTime(new Date());
                this.updateById(commodityOrderCommodity);
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
    public CommodityOrderCommodityEntity detail(String id) {
        CommodityOrderCommodityEntity commodityOrderCommodity = commodityOrderCommodityDao.selectById(id);
        if(commodityOrderCommodity!=null){
            CommoditySpecEntity commoditySpec = commoditySpecDao.selectById(commodityOrderCommodity.getSpecId());
            if(commoditySpec!=null){
                commodityOrderCommodity.setSpecImgurl(commoditySpec.getSpecImgurl());
            }
        }
        return commodityOrderCommodity;
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
            CommodityOrderCommodityEntity removeEntity = commodityOrderCommodityDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityOrderCommodityDao.updateById(removeEntity);
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
                CommodityOrderCommodityEntity removeEntity = commodityOrderCommodityDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityOrderCommodityDao.updateById(removeEntity);
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
            CommodityOrderCommodityListener listener = new CommodityOrderCommodityListener();
            EasyExcel.read(file.getInputStream(), CommodityOrderCommodityEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityOrderCommodityEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityOrderCommodityDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
