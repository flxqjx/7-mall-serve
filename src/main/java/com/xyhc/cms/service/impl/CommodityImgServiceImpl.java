package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommodityImgDao;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.service.CommodityImgService;
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
import com.xyhc.cms.listener.CommodityImgListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品图片Service实现类
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
@Service("commodityImgService")
public class CommodityImgServiceImpl extends ServiceImpl<CommodityImgDao, CommodityImgEntity> implements CommodityImgService {

    @Resource
    CommodityImgDao  commodityImgDao;

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
    public List<CommodityImgEntity> all(Map<String, Object> params) {
        return commodityImgDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityImgEntity> page = new Query<CommodityImgEntity>().getPage(params, "id", true);
        List<CommodityImgEntity> records = commodityImgDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityImg
     * @return boolean
     */
    @Override
    public boolean save(CommodityImgEntity commodityImg) {
        try {
//            if (StringUtils.isBlank(commodityImg.getId())) {
//                commodityImg.setId(commonUtitls.createKey());
            if (commodityImg.getId()==0){
                commodityImg.setCreateTime(new Date());
                super.save(commodityImg);
            }else{
                commodityImg.setUpdateTime(new Date());
                commodityImg.setIsDelete(0);
                this.updateById(commodityImg);
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
    public CommodityImgEntity detail(String id) {
        return commodityImgDao.selectById(id);
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
            CommodityImgEntity removeEntity = commodityImgDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityImgDao.updateById(removeEntity);
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
                CommodityImgEntity removeEntity = commodityImgDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityImgDao.updateById(removeEntity);
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
            CommodityImgListener listener = new CommodityImgListener();
            EasyExcel.read(file.getInputStream(), CommodityImgEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityImgEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
//                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityImgDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
