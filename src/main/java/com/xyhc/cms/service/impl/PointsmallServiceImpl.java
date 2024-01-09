package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.PointsmallImgDao;
import com.xyhc.cms.dao.PointsmallDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.service.PointsmallService;
import com.xyhc.cms.vo.wechatMsg.PointsmallImgDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
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
import com.xyhc.cms.listener.PointsmallListener;
import com.xyhc.cms.service.PointsmallImgService;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
/**
 * 商品主表Service实现类
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
@Service("pointsmallService")
public class PointsmallServiceImpl extends ServiceImpl<PointsmallDao, PointsmallEntity> implements PointsmallService {

    @Autowired
    PointsmallImgDao pointsmallImgDao;

    @Resource
    PointsmallDao  pointsmallDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;
    @Autowired
    PointsmallImgService pointsmallImgService;
    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<PointsmallEntity> all(Map<String, Object> params) {
        return pointsmallDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<PointsmallEntity> page = new Query<PointsmallEntity>().getPage(params, "id", true);
        List<PointsmallEntity> records = pointsmallDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /*@Override
    public boolean save(PointsmallEntity pointsmall) {
        try {
            if (StringUtils.isBlank(pointsmall.getId())){
                pointsmall.setId(commonUtitls.createKey());
                pointsmall.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmall.setCreateTime(new Date());
                super.save(pointsmall);
            }else{
                pointsmall.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmall.setUpdateTime(new Date());
                this.updateById(pointsmall);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }*/



    /**
     * 保存1118888
     *
     * @param  pointsmall
     * @return boolean
     */
    @Override
    public Result save(@RequestBody PointsmallImgDto pointsmallSave) {
        try {
            PointsmallEntity pointsmallEntity = pointsmallSave.getPointsmallForm();
            if (StringUtils.isBlank(pointsmallEntity.getId())) {
                pointsmallEntity.setId(commonUtitls.createKey());
                pointsmallEntity.setCreateBy(authUtils.AuthUser().getUserId());
                pointsmallEntity.setCreateTime(new Date());
                super.save(pointsmallEntity);
                UpdateWrapper<PointsmallImgEntity> style = new UpdateWrapper<>();
                //style.eq("pointsmall_id", pointsmallEntity.getId());
                style.eq("id", pointsmallEntity.getId());
                pointsmallImgDao.delete(style);
                List<PointsmallImgEntity> imgList = pointsmallSave.getPointsmallImgurlList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommodityId(pointsmallEntity.getId());
                        item.setId(0);
                        pointsmallImgService.save(item);
                    });
                }
            } else {
                pointsmallEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallEntity.setUpdateTime(new Date());
                this.updateById(pointsmallEntity);
                UpdateWrapper<PointsmallImgEntity> style = new UpdateWrapper<>();
                //style.eq("pointsmall_id", pointsmallEntity.getId());
                style.eq("id", pointsmallEntity.getId());

                pointsmallImgDao.delete(style);
                List<PointsmallImgEntity> imgList = pointsmallSave.getPointsmallImgurlList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommodityId(pointsmallEntity.getId());
                        item.setId(0);
                        pointsmallImgService.save(item);
                    });
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public PointsmallEntity detail(String id) {
        //return pointsmallDao.selectById(id);

        PointsmallEntity pointsmallEntity = pointsmallDao.selectById(id);

        //店铺是否收藏
       /* QueryWrapper<PointsmallCollectEntity> collect = new QueryWrapper<>();
        collect.eq("uid", authUtils.AuthUser().getUserId());
        collect.eq("is_delete", 0);
        collect.eq("pointsmall_id", id);
        collect.eq("is_collect", 1);*/
       /* 店铺收藏
       List<PointsmallCollectEntity> pointsmallCollectList = pointsmallCollectDao.selectList(collect);
        if (pointsmallCollectList.size() > 0) {
            pointsmallEntity.setIsCommodityCollect(1);
        } else {
            pointsmallEntity.setIsCommodityCollect(0);
        }*/

        //取详情多张图片
        UpdateWrapper<PointsmallImgEntity> wrapperImg = new UpdateWrapper<>();
        wrapperImg.eq("commodity_id", pointsmallEntity.getId());
        wrapperImg.eq("is_delete", 0);
        wrapperImg.orderByAsc("id");
        List<PointsmallImgEntity> imgList = pointsmallImgDao.selectList(wrapperImg);
        pointsmallEntity.setImgList(imgList);


       /* QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
        member.eq("is_delete", 0);
        member.eq("is_pay", 1);
        MemberLevelOrderEntity memberLevelOrder = memberLevelOrderDao.selectOne(member);
        if (memberLevelOrder != null) {
            QueryWrapper level = new QueryWrapper<>();
            level.eq("id", memberLevelOrder.getLevelId());
            MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
            if (memberLevel != null) {
                if (memberLevel.getLevelType().equals("A")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0016");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        pointsmallEntity.setSalesPrice(pointsmallEntity.getPrice());
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPriceName("会员价");
                        pointsmallEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("B")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0017");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        pointsmallEntity.setSalesPrice(pointsmallEntity.getPrice());
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPrice(setScale);
                        pointsmallEntity.setPriceName("会员价");
                    }
                } else if (memberLevel.getLevelType().equals("C")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0030");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        pointsmallEntity.setSalesPrice(pointsmallEntity.getPrice());
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPrice(setScale);
                        pointsmallEntity.setPriceName("会员价");
                    }
                }
            }
        } else {
            pointsmallEntity.setPriceName("销售价");
        }
*/
        return pointsmallEntity;

    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public PointsmallEntity detailOrder(String id) {
        PointsmallEntity pointsmallEntity = pointsmallDao.selectById(id);
        //取详情多张图片
        UpdateWrapper<PointsmallImgEntity> wrapperImg = new UpdateWrapper<>();
        //wrapperImg.eq("pointsmall_id", pointsmallEntity.getId());
        wrapperImg.eq("id", pointsmallEntity.getId());
        wrapperImg.eq("is_delete", 0);
        wrapperImg.orderByDesc("create_time");
        List<PointsmallImgEntity> imgList = pointsmallImgDao.selectList(wrapperImg);
        //pointsmallEntity.setImgList(imgList);

        /*QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
        member.eq("is_delete", 0);
        member.eq("is_pay", 1);
        MemberLevelOrderEntity memberLevelOrder = memberLevelOrderDao.selectOne(member);
        if (memberLevelOrder != null) {
            QueryWrapper level = new QueryWrapper<>();
            level.eq("id", memberLevelOrder.getLevelId());
            MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
            if (memberLevel != null) {
                if (memberLevel.getLevelType().equals("A")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0016");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("B")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0017");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("C")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0030");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        double discountMembers = (pointsmallEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        pointsmallEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = pointsmallEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        pointsmallEntity.setPrice(setScale);
                    }
                }
            }
        } else {

        }*/

        return pointsmallEntity;
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
            PointsmallEntity removeEntity = pointsmallDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            pointsmallDao.updateById(removeEntity);
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
                PointsmallEntity removeEntity = pointsmallDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                pointsmallDao.updateById(removeEntity);
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
            PointsmallListener listener = new PointsmallListener();
            EasyExcel.read(file.getInputStream(), PointsmallEntity.class, listener).sheet().doRead();
            //获取数据
            List<PointsmallEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                pointsmallDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
