package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommodityCommentImgDao;
import com.xyhc.cms.entity.CommodityCommentImgEntity;
import com.xyhc.cms.service.CommodityCommentImgService;
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
import com.xyhc.cms.listener.CommodityCommentImgListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 评论图片Service实现类
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
@Service("commodityCommentImgService")
public class CommodityCommentImgServiceImpl extends ServiceImpl<CommodityCommentImgDao, CommodityCommentImgEntity> implements CommodityCommentImgService {

    @Resource
    CommodityCommentImgDao  commodityCommentImgDao;

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
    public List<CommodityCommentImgEntity> all(Map<String, Object> params) {
        return commodityCommentImgDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityCommentImgEntity> page = new Query<CommodityCommentImgEntity>().getPage(params, "id", true);
        List<CommodityCommentImgEntity> records = commodityCommentImgDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils imgPage(Map<String, Object> params) {
        params.put("commentId", params.get("commentId"));
        IPage<CommodityCommentImgEntity> page = new Query<CommodityCommentImgEntity>().getPage(params, "id", true);
        List<CommodityCommentImgEntity> records = commodityCommentImgDao.imgPage(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityCommentImg
     * @return boolean
     */
    @Override
    public boolean save(CommodityCommentImgEntity commodityCommentImg) {
        try {
            if (StringUtils.isBlank(commodityCommentImg.getId())){
                commodityCommentImg.setId(commonUtitls.createKey());
//                commodityCommentImg.setCreateBy(authUtils.AuthUser().getUserId());
                commodityCommentImg.setCreateTime(new Date());
                super.save(commodityCommentImg);
            }else{
//                commodityCommentImg.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCommentImg.setUpdateTime(new Date());
                this.updateById(commodityCommentImg);
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
    public CommodityCommentImgEntity detail(String id) {
        return commodityCommentImgDao.selectById(id);
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
            CommodityCommentImgEntity removeEntity = commodityCommentImgDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityCommentImgDao.updateById(removeEntity);
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
                CommodityCommentImgEntity removeEntity = commodityCommentImgDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCommentImgDao.updateById(removeEntity);
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
            CommodityCommentImgListener listener = new CommodityCommentImgListener();
            EasyExcel.read(file.getInputStream(), CommodityCommentImgEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityCommentImgEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityCommentImgDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
