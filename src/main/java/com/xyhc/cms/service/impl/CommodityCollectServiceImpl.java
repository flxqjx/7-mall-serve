package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.BlockDao;
import com.xyhc.cms.dao.CommodityCollectDao;
import com.xyhc.cms.dao.MemberLevelDao;
import com.xyhc.cms.dao.MemberLevelOrderDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityCollectService;
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
import com.xyhc.cms.listener.CommodityCollectListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 店铺收藏Service实现类
 *
 * @author apollo
 * @since 2023-09-20 10:26:49
 */
@Service("commodityCollectService")
public class CommodityCollectServiceImpl extends ServiceImpl<CommodityCollectDao, CommodityCollectEntity> implements CommodityCollectService {

    @Resource
    CommodityCollectDao  commodityCollectDao;

    @Autowired
    MemberLevelOrderDao memberLevelOrderDao;

    @Autowired
    MemberLevelDao memberLevelDao;

    @Autowired
    BlockDao blockDao;

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
    public List<CommodityCollectEntity> all(Map<String, Object> params) {
        return commodityCollectDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("uid",authUtils.AuthUser().getUserId());
        IPage<CommodityCollectEntity> page = new Query<CommodityCollectEntity>().getPage(params, "id", true);
        List<CommodityCollectEntity> records = commodityCollectDao.page(page, params);
        records.forEach(item->{
            QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
            member.eq("uid", params.get("uid"));
            member.eq("is_delete", 0);
            member.orderByDesc("pay_time");
            member.eq("is_pay", 1);
            List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(member);
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
                    } else if ("C".equals(memberLevel.getLevelType())) {
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
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  commodityCollect
     * @return boolean
     */
    @Override
    public boolean save(CommodityCollectEntity commodityCollect) {
        try {
            QueryWrapper<CommodityCollectEntity> collect = new QueryWrapper<>();
            collect.eq("uid", authUtils.AuthUser().getUserId());
            collect.eq("is_delete", 0);
            collect.eq("is_collect", 0);
            collect.eq("commodity_id", commodityCollect.getCommodityId());
            CommodityCollectEntity collectEntity = commodityCollectDao.selectOne(collect);
            if (collectEntity==null){
                commodityCollect.setId(commonUtitls.createKey());
                commodityCollect.setCreateBy(authUtils.AuthUser().getUserId());
                commodityCollect.setUid(authUtils.AuthUser().getUserId());
                commodityCollect.setCreateTime(new Date());
                super.save(commodityCollect);
            }else{
                CommodityCollectEntity commodityCollectUpdate = commodityCollectDao.selectById(collectEntity.getId());
                commodityCollectUpdate.setIsCollect(1);
                commodityCollectUpdate.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCollectUpdate.setUpdateTime(new Date());
                this.updateById(commodityCollectUpdate);
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
    public CommodityCollectEntity detail(String id) {
        return commodityCollectDao.selectById(id);
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
            CommodityCollectEntity removeEntity = commodityCollectDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityCollectDao.updateById(removeEntity);
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
                CommodityCollectEntity removeEntity = commodityCollectDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityCollectDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 是否收藏
     *
     * @return
     */
    @Override
    public CommodityCollectEntity isCollect(String commodityId) {
        QueryWrapper<CommodityCollectEntity> collect = new QueryWrapper<>();
        collect.eq("uid", authUtils.AuthUser().getUserId());
        collect.eq("is_delete", 0);
        collect.eq("commodity_id", commodityId);
        CommodityCollectEntity commodityCollect = commodityCollectDao.selectOne(collect);
        return commodityCollect;
    }


    /**
     * 取消收藏
     *
     * @param commodityId
     * @return boolean
     */
    @Override
    public boolean saveCloseIsCollect(String commodityId) {
        try {
            UpdateWrapper<CommodityCollectEntity> wrapper = new UpdateWrapper<>();
            wrapper.eq("uid", authUtils.AuthUser().getUserId());
            wrapper.eq("commodity_id", commodityId);
            wrapper.set("is_collect", 0);
            commodityCollectDao.update(null, wrapper);
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
            CommodityCollectListener listener = new CommodityCollectListener();
            EasyExcel.read(file.getInputStream(), CommodityCollectEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityCollectEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityCollectDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
