package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CommodityClassifyDao;
import com.xyhc.cms.entity.CommodityClassifyEntity;
import com.xyhc.cms.entity.CommodityOrderCommodityEntity;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.service.CommodityClassifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.util.*;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import com.xyhc.cms.config.auth.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.utils.CommonUtitls;
import com.alibaba.excel.EasyExcel;
import com.xyhc.cms.listener.CommodityClassifyListener;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
/**
 * 商品分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("commodityClassifyService")
public class CommodityClassifyServiceImpl extends ServiceImpl<CommodityClassifyDao, CommodityClassifyEntity> implements CommodityClassifyService {

    @Resource
    CommodityClassifyDao  commodityClassifyDao;

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
    public List<CommodityClassifyEntity> all(Map<String, Object> params) {
        List<CommodityClassifyEntity> records = commodityClassifyDao.all(params);
        QueryWrapper<CommodityClassifyEntity> query = new QueryWrapper<>();
        query.eq("parent_id", '0');
        query.eq("is_delete", 0);
        List<CommodityClassifyEntity> orderList = commodityClassifyDao.selectList(query);
        orderList.forEach(item -> {
            //查无限极
            item.setChildren(subjectTreeItem(records, item.getId()));
        });
        return orderList;
    }

    /**
     * 根据二级id查询三级
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityClassifyEntity> lastLevelAll(Map<String, Object> params) {
        params.put("classifyId", params.get("id"));
        List<CommodityClassifyEntity> records = commodityClassifyDao.lastLevelAll(params);
        return records;
    }

    /**
     * 递归算法
     *
     * @param
     * @return
     */
    public List<CommodityClassifyEntity> subjectTreeItem(List<CommodityClassifyEntity> records, String parentId) {
        List<CommodityClassifyEntity> classifyList = records.stream().filter(s -> s.getParentId().equals(parentId)).collect(Collectors.toList());
        classifyList.forEach(item -> {
            item.setChildren(subjectTreeItem(records, item.getId()));
        });
        return classifyList;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CommodityClassifyEntity> page = new Query<CommodityClassifyEntity>().getPage(params, "create_time", false);
        List<CommodityClassifyEntity> records = commodityClassifyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityClassify
     * @return boolean
     */
    @Override
    public boolean saveClassify(CommodityClassifyEntity commodityClassify) {
        try {
            if (StringUtils.isBlank(commodityClassify.getId())){
                commodityClassify.setId(commonUtitls.createKey());
                commodityClassify.setCreateBy(authUtils.AuthUser().getUserId());
                commodityClassify.setCreateTime(new Date());
                super.save(commodityClassify);
            }else{
                commodityClassify.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityClassify.setUpdateTime(new Date());
                this.updateById(commodityClassify);
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
    public CommodityClassifyEntity detail(String id) {
        return commodityClassifyDao.selectById(id);
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
            CommodityClassifyEntity removeEntity = commodityClassifyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityClassifyDao.updateById(removeEntity);
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
                CommodityClassifyEntity removeEntity = commodityClassifyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityClassifyDao.updateById(removeEntity);
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
            CommodityClassifyListener listener = new CommodityClassifyListener();
            EasyExcel.read(file.getInputStream(), CommodityClassifyEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityClassifyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityClassifyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
