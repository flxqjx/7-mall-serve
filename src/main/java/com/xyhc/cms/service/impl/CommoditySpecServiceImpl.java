package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityClassifyService;
import com.xyhc.cms.service.CommodityService;
import com.xyhc.cms.service.CommoditySpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
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
import com.xyhc.cms.listener.CommoditySpecListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 商品规格Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("commoditySpecService")
public class CommoditySpecServiceImpl extends ServiceImpl<CommoditySpecDao, CommoditySpecEntity> implements CommoditySpecService {

    @Resource
    CommoditySpecDao commoditySpecDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    CommodityService commodityService;
    @Autowired
    CommodityDao commodityDao;

    @Autowired
    MemberLevelOrderDao memberLevelOrderDao;

    @Autowired
    MemberLevelDao memberLevelDao;

    @Autowired
    BlockDao blockDao;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommoditySpecEntity> all(Map<String, Object> params) {
        params.put("commodityId", params.get("commodityId"));
        List<CommoditySpecEntity> records = commoditySpecDao.all(params);
        QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
        member.eq("is_delete", 0);
        member.orderByDesc("pay_time");
        member.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(member);
        records.forEach(item -> {
            item.setSalesPrice(item.getPrice());
            if (memberLevelOrder.size()>0) {
                QueryWrapper level = new QueryWrapper<>();
                level.eq("id", memberLevelOrder.get(0).getLevelId());
                MemberLevelEntity memberLevel = memberLevelDao.selectOne(level);
                if (memberLevel != null) {
                    if (memberLevel.getLevelType().equals("A")) {
                        QueryWrapper queryWrapperA = new QueryWrapper();
                        queryWrapperA.eq("block", "BLOCK_0016");
                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
                        if (bossA != null) {
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                        }
                    } else if (memberLevel.getLevelType().equals("B")) {
                        QueryWrapper queryWrapperA = new QueryWrapper();
                        queryWrapperA.eq("block", "BLOCK_0017");
                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
                        if (bossA != null) {
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                        }
                    } else if (memberLevel.getLevelType().equals("C")) {
                        QueryWrapper queryWrapperA = new QueryWrapper();
                        queryWrapperA.eq("block", "BLOCK_0030");
                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
                        if (bossA != null) {
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                        }
                    }
                }
            } else {

            }
        });
        return records;
    }

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommoditySpecEntity> allNew(Map<String, Object> params) {
        params.put("commodityId", params.get("commodityId"));
        List<CommoditySpecEntity> records = commoditySpecDao.all(params);
        return records;
    }

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommoditySpecEntity> allSpec(Map<String, Object> params) {
        params.put("commodityId", params.get("id"));
        return commoditySpecDao.allSpec(params);
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
        IPage<CommoditySpecEntity> page = new Query<CommoditySpecEntity>().getPage(params, "create_time", false);
        List<CommoditySpecEntity> records = commoditySpecDao.page(page, params);
        page.setRecords(records);
        records.forEach(item -> {
            CommodityEntity commodityEntity = commodityDao.selectById(item.getCommodityId());
            if (commodityEntity != null) {
                item.setCommodityName(commodityEntity.getCommodityName());
            }
        });
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param commoditySpec
     * @return boolean
     */
    @Override
    public boolean save(CommoditySpecEntity commoditySpec) {
        try {
            if (StringUtils.isBlank(commoditySpec.getId())) {
                commoditySpec.setId(commonUtitls.createKey());
                commoditySpec.setCreateBy(authUtils.AuthUser().getUserId());
                commoditySpec.setCreateTime(new Date());
                super.save(commoditySpec);
            } else {
                commoditySpec.setUpdateBy(authUtils.AuthUser().getUserId());
                commoditySpec.setUpdateTime(new Date());
                this.updateById(commoditySpec);
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
    public CommoditySpecEntity detail(String id) {
        return commoditySpecDao.selectById(id);
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
            CommoditySpecEntity removeEntity = commoditySpecDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commoditySpecDao.updateById(removeEntity);
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
                CommoditySpecEntity removeEntity = commoditySpecDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commoditySpecDao.updateById(removeEntity);
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
            CommoditySpecListener listener = new CommoditySpecListener();
            EasyExcel.read(file.getInputStream(), CommoditySpecEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommoditySpecEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commoditySpecDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
