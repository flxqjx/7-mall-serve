package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityService;
import com.xyhc.cms.service.CommodityShoppingCartService;
import com.xyhc.cms.service.CommoditySizeService;
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
import com.xyhc.cms.listener.CommodityShoppingCartListener;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * 赛事申请表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("commodityShoppingCartService")
public class CommodityShoppingCartServiceImpl extends ServiceImpl<CommodityShoppingCartDao, CommodityShoppingCartEntity> implements CommodityShoppingCartService {

    @Resource
    CommodityShoppingCartDao commodityShoppingCartDao;
    @Resource
    CommodityDao commodityDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    BlockDao blockDao;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    MemberLevelOrderDao memberLevelOrderDao;

    @Autowired
    CommoditySizeService commoditySizeService;

    @Autowired
    MemberLevelDao memberLevelDao;

    @Autowired
    CommoditySpecService commoditySpecService;

    @Autowired
    CommodityShoppingCartService commodityShoppingCartService;

    @Autowired
    CommodityService commodityService;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityShoppingCartEntity> all(Map<String, Object> params) {
        return commodityShoppingCartDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("uid", authUtils.AuthUser().getUserId());
        IPage<CommodityShoppingCartEntity> page = new Query<CommodityShoppingCartEntity>().getPage(params, "create_time", false);
        List<CommodityShoppingCartEntity> records = commodityShoppingCartDao.page(page, params);
        //查是否购买会员
        QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
        member.eq("is_delete", 0);
        member.orderByDesc("pay_time");
        member.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(member);
        // block
        QueryWrapper queryWrapperBlock = new QueryWrapper();
        List<SysBlock> blockList = blockDao.selectList(queryWrapperBlock);
        // 会员级别
        QueryWrapper queryWrapperLevel = new QueryWrapper();
        List<MemberLevelEntity> memberLevelEntities = memberLevelDao.selectList(queryWrapperLevel);

        Optional<SysBlock> blockA = blockList.stream().filter(s -> "BLOCK_0016".equals(s.getBlock())).findFirst();
        Optional<SysBlock> blockB = blockList.stream().filter(s -> "BLOCK_0017".equals(s.getBlock())).findFirst();
        Optional<SysBlock> blockC = blockList.stream().filter(s -> "BLOCK_0030".equals(s.getBlock())).findFirst();
        //根据spce_id规格id去查规格的图片和名称
        records.forEach(commoditySpec -> {
            CommoditySpecEntity commoditySpecEntity = commoditySpecService.detail(commoditySpec.getSpceId());
            if (commoditySpecEntity != null) {
                commoditySpec.setSpecName(commoditySpecEntity.getSpecName());
                commoditySpec.setSpecImgurl(commoditySpecEntity.getSpecImgurl());
                commoditySpec.setOriginalPrice(commoditySpecEntity.getOriginalPrice());
                commoditySpec.setPoints(commoditySpecEntity.getPoints());
            }
            CommodityEntity commodityEntity = commodityDao.commodityDetail(commoditySpec.getCommodityId());
            if (commodityEntity != null) {
                commoditySpec.setCommodityName(commodityEntity.getCommodityName());
                commoditySpec.setFreight(commodityEntity.getFreight());
            }
            CommoditySizeEntity commoditySizeEntity = commoditySizeService.detail(commoditySpec.getSizeId());
            if (commoditySizeEntity != null) {
                commoditySpec.setSizeName(commoditySizeEntity.getSizeName());
            }

            if (memberLevelOrder.size() > 0) {
                Optional<MemberLevelEntity> memberLevel = memberLevelEntities.stream().filter(s -> s.getId().equals(memberLevelOrder.get(0).getLevelId())).findAny();
                if (memberLevel.isPresent()) {
                    if (memberLevel.get().getLevelType().equals("A")) {
                        if (blockA.isPresent()) {
                            double discountMembers = (commoditySpec.getSalesPrice().doubleValue() * Double.valueOf(blockA.get().getRemark())) / 100;
                            commoditySpec.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = commoditySpec.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            commoditySpec.setPrice(setScale);
                            commoditySpec.setPriceName("会员价");
                        }
                    } else if (memberLevel.get().getLevelType().equals("B")) {
                        if (blockB.isPresent()) {
                            double discountMembers = (commoditySpec.getSalesPrice().doubleValue() * Double.valueOf(blockB.get().getRemark())) / 100;
                            commoditySpec.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = commoditySpec.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            commoditySpec.setPrice(setScale);
                            commoditySpec.setPriceName("会员价");
                        }
                    } else if (memberLevel.get().getLevelType().equals("C")) {
                        if (blockC.isPresent()) {
                            double discountMembers = (commoditySpec.getSalesPrice().doubleValue() * Double.valueOf(blockC.get().getRemark())) / 100;
                            commoditySpec.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = commoditySpec.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            commoditySpec.setPrice(setScale);
                            commoditySpec.setPriceName("会员价");
                        }
                    }
                }
            } else {
                commoditySpec.setPriceName("销售价");
            }

        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param commodityShoppingCart
     * @return boolean
     */
    @Override
    public boolean save(CommodityShoppingCartEntity commodityShoppingCart) {
        try {
//            if (StringUtils.isBlank(commodityShoppingCart.getId())){
//                commodityShoppingCart.setId(commonUtitls.createKey());
//                commodityShoppingCart.setCreateBy(authUtils.AuthUser().getUserId());
//                commodityShoppingCart.setUid(authUtils.AuthUser().getUserId());
//                commodityShoppingCart.setCreateTime(new Date());
//                super.save(commodityShoppingCart);
//            }else{
//                commodityShoppingCart.setUpdateBy(authUtils.AuthUser().getUserId());
//                commodityShoppingCart.setUpdateTime(new Date());
//                this.updateById(commodityShoppingCart);
//            }


            if (StringUtils.isBlank(commodityShoppingCart.getId())) {
                QueryWrapper<CommodityShoppingCartEntity> query = new QueryWrapper<>();
                query.eq("uid", authUtils.AuthUser().getUserId());
                query.eq("commodity_id", commodityShoppingCart.getCommodityId());
                query.eq("spce_id", commodityShoppingCart.getSpceId());
                query.eq("is_delete", 0);
                CommodityShoppingCartEntity commodityShoppingCartEntity = commodityShoppingCartDao.selectOne(query);
                if (commodityShoppingCartEntity != null) {
                    commodityShoppingCartEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                    commodityShoppingCartEntity.setUpdateTime(new Date());
                    commodityShoppingCartEntity.setNum(commodityShoppingCartEntity.getNum() + commodityShoppingCart.getNum());
                    commodityShoppingCartEntity.setTotal(BigDecimal.valueOf((commodityShoppingCart.getNum() * commodityShoppingCart.getPrice().intValue()) + commodityShoppingCartEntity.getTotal().intValue()));
                    this.updateById(commodityShoppingCartEntity);
                } else {
                    commodityShoppingCart.setId(commonUtitls.createKey());
                    commodityShoppingCart.setCreateBy(authUtils.AuthUser().getUserId());
                    commodityShoppingCart.setUid(authUtils.AuthUser().getUserId());
                    commodityShoppingCart.setCreateTime(new Date());
                    super.save(commodityShoppingCart);
                }
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
    public CommodityShoppingCartEntity detail(String id) {
        return commodityShoppingCartDao.selectById(id);
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
            CommodityShoppingCartEntity removeEntity = commodityShoppingCartDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityShoppingCartDao.updateById(removeEntity);
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
                CommodityShoppingCartEntity removeEntity = commodityShoppingCartDao.selectById(id);
//                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityShoppingCartDao.updateById(removeEntity);
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
            CommodityShoppingCartListener listener = new CommodityShoppingCartListener();
            EasyExcel.read(file.getInputStream(), CommodityShoppingCartEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityShoppingCartEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityShoppingCartDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
