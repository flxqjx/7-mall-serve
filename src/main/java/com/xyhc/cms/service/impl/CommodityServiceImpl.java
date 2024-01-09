package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.*;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CommodityClassifyService;
import com.xyhc.cms.service.CommodityImgService;
import com.xyhc.cms.service.CommodityService;
import com.xyhc.cms.vo.common.CommoditySummaryVo;
import com.xyhc.cms.vo.wechatMsg.CommodityImgDto;
import com.xyhc.cms.vo.wechatMsg.CommodityOrderSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.xyhc.cms.listener.CommodityListener;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import static cn.hutool.poi.excel.sax.AttributeName.t;

/**
 * 商品主表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("commodityService")
public class CommodityServiceImpl extends ServiceImpl<CommodityDao, CommodityEntity> implements CommodityService {

    @Resource
    CommodityDao commodityDao;
    @Resource
    CommodityCollectDao commodityCollectDao;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;

    @Autowired
    CommodityClassifyDao commodityClassifyDao;

    @Autowired
    CommodityOrderCommodityDao commodityOrderCommodityDao;

    @Autowired
    CommodityOrderDao commodityOrderDao;

    @Autowired
    MemberLevelOrderDao memberLevelOrderDao;

    @Autowired
    MemberLevelDao memberLevelDao;

    @Autowired
    CommoditySpecDao commoditySpecDao;

    @Autowired
    BlockDao blockDao;

    @Autowired
    CommodityImgService commodityImgService;

    @Autowired
    CommodityClassifyService commodityClassifyService;

    @Autowired
    CommodityImgDao commodityImgDao;

    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityEntity> all(Map<String, Object> params) {
        params.put("isRecommendCart", params.get("isRecommendCart"));
        params.put("isRecommendCollect", params.get("isRecommendCollect"));
        List<CommodityEntity> list = commodityDao.all(params);
        list.forEach(item -> {
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
                            item.setSalesPrice(item.getPrice());
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
                            item.setSalesPrice(item.getPrice());
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
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                        }
                    }
                }
            }
        });
        return list;
    }

    /**
     * 查询推荐
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityEntity> allRecommend(Map<String, Object> params) {
        List<CommodityEntity> records = commodityDao.allRecommend(params);
        records.forEach(item -> {
            QueryWrapper<CommodityImgEntity> query = new QueryWrapper<>();
            query.eq("commodity_id", item.getId());
            query.eq("is_delete", 0);
            List<CommodityImgEntity> imgList = commodityImgDao.selectList(query);
            item.setImgList(imgList);
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
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    } else if (memberLevel.getLevelType().equals("B")) {
                        QueryWrapper queryWrapperA = new QueryWrapper();
                        queryWrapperA.eq("block", "BLOCK_0017");
                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
                        if (bossA != null) {
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    } else if (memberLevel.getLevelType().equals("C")) {
                        QueryWrapper queryWrapperA = new QueryWrapper();
                        queryWrapperA.eq("block", "BLOCK_0030");
                        SysBlock bossA = blockDao.selectOne(queryWrapperA);
                        if (bossA != null) {
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    }
                }
            } else {
                item.setPriceName("销售价");
            }
        });
        return records;
    }

    /**
     * 查询推荐
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityEntity> allTimeLimit(Map<String, Object> params) {
        List<CommodityEntity> records = commodityDao.allTimeLimit(params);
        //查销量
        List<CommodityOrderCommodityEntity> allList = commodityOrderCommodityDao.all(params);
        //查销量订单
        List<CommodityOrderEntity> allOrderList = commodityOrderDao.all(params);
        records.forEach(item -> {
            QueryWrapper<CommodityImgEntity> query = new QueryWrapper<>();
            query.eq("commodity_id", item.getId());
            query.eq("is_delete", 0);
            List<CommodityImgEntity> imgList = commodityImgDao.selectList(query);
            item.setImgList(imgList);
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
                            item.setSalesPrice(item.getPrice());
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
                            item.setSalesPrice(item.getPrice());
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
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                        }
                    }
                }
            }
            //查询销量
            List<CommodityOrderCommodityEntity> commodityList = allList.stream().filter(s -> s.getCommodityId().equals(item.getId())).collect(Collectors.toList());
            if (commodityList.size() > 0) {
                int orderNum = 0;
                for (CommodityOrderCommodityEntity t : commodityList) {
                    List<CommodityOrderEntity> salesVolumeList = allOrderList.stream().filter(a -> t.getOrderId().equals(a.getId()) && a.getIsPay() == 1).collect(Collectors.toList());
                    orderNum += salesVolumeList.size();
                    item.setSalesVolume(orderNum);
                }
            }
        });
        return records;
    }

    /**
     * 查询推荐详情
     *
     * @param params
     * @return Result
     */
    @Override
    public List<CommodityEntity> allIsDetailRecommend(Map<String, Object> params) {
        List<CommodityEntity> records = commodityDao.allIsDetailRecommend(params);
        records.forEach(item -> {
            QueryWrapper<CommodityImgEntity> query = new QueryWrapper<>();
            query.eq("commodity_id", item.getId());
            query.eq("is_delete", 0);
            List<CommodityImgEntity> imgList = commodityImgDao.selectList(query);
            item.setImgList(imgList);
        });
        return records;
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        params.put("isNew", params.get("isNew"));
        params.put("isOnShelf", params.get("isOnShelf"));
        IPage<CommodityEntity> page = new Query<CommodityEntity>().getPage(params, "create_time", false);
        List<CommodityEntity> records = commodityDao.page(page, params);
        page.setRecords(records);
        //查分类
        List<CommodityClassifyEntity> allClassifyList = commodityClassifyDao.all(params);
        //查是否购买会员
        QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
        member.eq("is_delete", 0);
        member.orderByDesc("pay_time");
        member.eq("is_pay", 1);
        List<MemberLevelOrderEntity> memberLevelOrder = memberLevelOrderDao.selectList(member);
        //查图片
        List<CommodityImgEntity> allimgList = commodityImgDao.all(params);
        //查销量
        List<CommodityOrderCommodityEntity> allList = commodityOrderCommodityDao.all(params);
        //查销量订单
        List<CommodityOrderEntity> allOrderList = commodityOrderDao.all(params);

        // 会员级别
        QueryWrapper queryWrapperLevel = new QueryWrapper();
        List<MemberLevelEntity> memberLevelEntities = memberLevelDao.selectList(queryWrapperLevel);

        // block
        QueryWrapper queryWrapperBlock = new QueryWrapper();
        List<SysBlock> blockList = blockDao.selectList(queryWrapperBlock);

        // 全部产品销量
        List<CommoditySummaryVo> commodityorderSummary = commodityOrderCommodityDao.commodityorderSummary();
        Optional<SysBlock> blockA = blockList.stream().filter(s -> "BLOCK_0016".equals(s.getBlock())).findFirst();
        Optional<SysBlock> blockB = blockList.stream().filter(s -> "BLOCK_0017".equals(s.getBlock())).findFirst();
        Optional<SysBlock> blockC = blockList.stream().filter(s -> "BLOCK_0030".equals(s.getBlock())).findFirst();
        records.forEach(item -> {
            List<CommodityImgEntity> imgList = allimgList.stream().filter(a -> item.getId().equals(a.getCommodityId()) && a.getIsDelete() == 0).collect(Collectors.toList());
            item.setImgList(imgList);
            Optional<CommodityClassifyEntity> findClassify = allClassifyList.stream().filter(s -> s.getId().equals(item.getClassifyId())).findFirst();
            if (findClassify.isPresent()) {
                item.setComClassifyName(findClassify.get().getClassifyName());
            }
            if (memberLevelOrder.size()>0) {
                Optional<MemberLevelEntity> memberLevel = memberLevelEntities.stream().filter(s -> s.getId().equals(memberLevelOrder.get(0).getLevelId())).findAny();
                if (memberLevel.isPresent()) {
                    if (memberLevel.get().getLevelType().equals("A")) {
                        if (blockA.isPresent()) {
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(blockA.get().getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    } else if (memberLevel.get().getLevelType().equals("B")) {
                        if (blockB.isPresent()) {
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(blockB.get().getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    } else if (memberLevel.get().getLevelType().equals("C")) {
                        if (blockC.isPresent()) {
                            item.setSalesPrice(item.getPrice());
                            double discountMembers = (item.getPrice().doubleValue() * Double.valueOf(blockC.get().getRemark())) / 100;
                            item.setPrice(BigDecimal.valueOf(discountMembers));
                            BigDecimal setScale = item.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                            item.setPrice(setScale);
                            item.setPriceName("会员价");
                        }
                    }
                }
            } else {
                item.setPriceName("销售价");
            }
            //查询销量
            Optional<CommoditySummaryVo> commoditySummaryVo = commodityorderSummary.stream().filter(s -> s.getCommodityId().equals(item.getId())).findFirst();
            if (commoditySummaryVo.isPresent()) {
                item.setSalesVolume(commoditySummaryVo.get().getCommodityTotal());
            } else {
                item.setSalesVolume(0);
            }

        });
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils pageIsNew(Map<String, Object> params) {
        params.put("isOnShelf", params.get("isOnShelf"));
        IPage<CommodityEntity> page = new Query<CommodityEntity>().getPage(params, "create_time", false);
        List<CommodityEntity> records = commodityDao.page(page, params);
        page.setRecords(records);
        List<CommodityClassifyEntity> allClassifyList = commodityClassifyDao.all(params);
        records.forEach(item -> {
            QueryWrapper<CommodityImgEntity> query = new QueryWrapper<>();
            query.eq("commodity_id", item.getId());
            query.eq("is_delete", 0);
            List<CommodityImgEntity> imgList = commodityImgDao.selectList(query);
            item.setImgList(imgList);
            Optional<CommodityClassifyEntity> findClassify = allClassifyList.stream().filter(s -> s.getId().equals(item.getClassifyId())).findFirst();
            if (findClassify.isPresent()) {
                item.setComClassifyName(findClassify.get().getClassifyName());
            }
//            //查询销量
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("commodity_id", item.getId());
//            List<CommodityOrderCommodityEntity> commodityList = commodityOrderCommodityDao.selectList(queryWrapper);
//            if (commodityList.size() > 0) {
//                commodityList.forEach(t -> {
//                    QueryWrapper queryOrder = new QueryWrapper();
//                    queryOrder.eq("id", t.getOrderId());
//                    queryOrder.eq("is_pay", 1);
//                    List<CommodityOrderEntity> salesVolumeList = commodityOrderDao.selectList(queryOrder);
//                    item.setSalesVolume(salesVolumeList.size());
//                });
//            }
        });
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param
     */
    @Override
    public Result save(@RequestBody CommodityImgDto commoditySave) {
        try {
            CommodityEntity commodityEntity = commoditySave.getCommodityForm();
            if (StringUtils.isBlank(commodityEntity.getId())) {
                commodityEntity.setId(commonUtitls.createKey());
                commodityEntity.setCreateBy(authUtils.AuthUser().getUserId());
                commodityEntity.setCreateTime(new Date());
                super.save(commodityEntity);
                UpdateWrapper<CommodityImgEntity> style = new UpdateWrapper<>();
                style.eq("commodity_id", commodityEntity.getId());
                commodityImgDao.delete(style);
                List<CommodityImgEntity> imgList = commoditySave.getCommodityImgurlList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommodityId(commodityEntity.getId());
                        commodityImgService.save(item);
                    });
                }
            } else {
                commodityEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityEntity.setUpdateTime(new Date());
                this.updateById(commodityEntity);
                UpdateWrapper<CommodityImgEntity> style = new UpdateWrapper<>();
                style.eq("commodity_id", commodityEntity.getId());
                commodityImgDao.delete(style);
                List<CommodityImgEntity> imgList = commoditySave.getCommodityImgurlList();
                if (imgList.size() > 0) {
                    imgList.forEach(item -> {
                        item.setCommodityId(commodityEntity.getId());
                        commodityImgService.save(item);
                    });
                }
            }
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 保存推荐
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean saveRecommend(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsRecommend().equals("1")) {
                    commodity.setIsRecommend("0");
                } else {
                    commodity.setIsRecommend("1");
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 设置为新人专享
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean saveIsNew(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsNew().equals("1")) {
                    commodity.setIsNew("0");
                } else {
                    commodity.setIsNew("1");
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 设置为新人专享
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean saveTimeLimit(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getTimeLimit().equals("1")) {
                    commodity.setTimeLimit("0");
                } else {
                    commodity.setTimeLimit("1");
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 保存推荐
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean saveIsDetailRecommend(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsDetailRecommend().equals("1")) {
                    commodity.setIsDetailRecommend("0");
                } else {
                    commodity.setIsDetailRecommend("1");
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存是否上架下架
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean saveIsBuy(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsOnShelf().equals("1")) {
                    commodity.setIsOnShelf("0");
                } else {
                    commodity.setIsOnShelf("1");
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存是否新品
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean isNewCommodity(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsNewCommodity() == 1) {
                    commodity.setIsNewCommodity(0);
                } else {
                    commodity.setIsNewCommodity(1);
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
     * 推荐购物车页面
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean isRecommendCart(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsRecommendCart() == 1) {
                    commodity.setIsRecommendCart(0);
                } else {
                    commodity.setIsRecommendCart(1);
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 推荐收藏页面
     *
     * @param commodity
     * @return boolean
     */
    @Override
    public boolean isRecommendCollect(CommodityEntity commodity) {
        try {
            if (StringUtils.isBlank(commodity.getId())) {

            } else {
                commodity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodity.setUpdateTime(new Date());
                if (commodity.getIsRecommendCollect() == 1) {
                    commodity.setIsRecommendCollect(0);
                } else {
                    commodity.setIsRecommendCollect(1);
                }
                this.updateById(commodity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 保存收藏
     *
     * @param commodityEntity
     * @return boolean
     */
    @Override
    public boolean saveIsCollect(CommodityEntity commodityEntity) {
        try {
            commodityEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityEntity.setUpdateTime(new Date());
            commodityEntity.setIsCollect("1");
            commodityEntity.setCollectUid(authUtils.AuthUser().getUserId());
            this.updateById(commodityEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 取消收藏
     *
     * @param commodityEntity
     * @return boolean
     */
    @Override
    public boolean saveCloseIsCollect(CommodityEntity commodityEntity) {
        try {
            commodityEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityEntity.setUpdateTime(new Date());
            commodityEntity.setIsCollect("0");
            commodityEntity.setCollectUid(authUtils.AuthUser().getUserId());
            this.updateById(commodityEntity);
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
    public CommodityEntity detail(String id,String uid) {
        CommodityEntity commodityEntity = commodityDao.selectById(id);

        CommodityClassifyEntity commodityClassify = commodityClassifyDao.selectById(commodityEntity.getClassifyId());
        if (commodityClassify != null) {
            commodityEntity.setClassifyName(commodityClassify.getClassifyName());
        }
        // 全部产品销量
        List<CommoditySummaryVo> commodityorderSummary = commodityOrderCommodityDao.commodityorderSummary();
        //是否收藏
        QueryWrapper<CommodityCollectEntity> collect = new QueryWrapper<>();
        collect.eq("uid", uid);
        collect.eq("is_delete", 0);
        collect.eq("commodity_id", id);
        collect.eq("is_collect", 1);
        List<CommodityCollectEntity> commodityCollectList = commodityCollectDao.selectList(collect);
        if (commodityCollectList.size() > 0) {
            commodityEntity.setIsCommodityCollect(1);
        } else {
            commodityEntity.setIsCommodityCollect(0);
        }

        //取详情多张图片
        UpdateWrapper<CommodityImgEntity> wrapperImg = new UpdateWrapper<>();
        wrapperImg.eq("commodity_id", commodityEntity.getId());
        wrapperImg.eq("is_delete", 0);
        wrapperImg.orderByAsc("id");
        List<CommodityImgEntity> imgList = commodityImgDao.selectList(wrapperImg);
        commodityEntity.setImgList(imgList);


        QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", uid);
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
                        commodityEntity.setSalesPrice(commodityEntity.getPrice());
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPriceName("会员价");
                        commodityEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("B")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0017");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        commodityEntity.setSalesPrice(commodityEntity.getPrice());
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPrice(setScale);
                        commodityEntity.setPriceName("会员价");
                    }
                } else if (memberLevel.getLevelType().equals("C")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0030");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        commodityEntity.setSalesPrice(commodityEntity.getPrice());
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPrice(setScale);
                        commodityEntity.setPriceName("会员价");
                    }
                }
            }
        } else {
            commodityEntity.setPriceName("销售价");
        }
        //查询销量
        Optional<CommoditySummaryVo> commoditySummaryVo = commodityorderSummary.stream().filter(s -> s.getCommodityId().equals(commodityEntity.getId())).findFirst();
        if (commoditySummaryVo.isPresent()) {
            commodityEntity.setSalesVolume(commoditySummaryVo.get().getCommodityTotal());
        } else {
            commodityEntity.setSalesVolume(0);
        }
        return commodityEntity;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public CommodityEntity detailOrder(String id) {
        CommodityEntity commodityEntity = commodityDao.selectById(id);
        //取详情多张图片
        UpdateWrapper<CommodityImgEntity> wrapperImg = new UpdateWrapper<>();
        wrapperImg.eq("commodity_id", commodityEntity.getId());
        wrapperImg.eq("is_delete", 0);
        wrapperImg.orderByDesc("create_time");
        List<CommodityImgEntity> imgList = commodityImgDao.selectList(wrapperImg);
        commodityEntity.setImgList(imgList);

        QueryWrapper<MemberLevelOrderEntity> member = new QueryWrapper<>();
        member.eq("uid", authUtils.AuthUser().getUserId());
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
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("B")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0017");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPrice(setScale);
                    }
                } else if (memberLevel.getLevelType().equals("C")) {
                    QueryWrapper queryWrapperA = new QueryWrapper();
                    queryWrapperA.eq("block", "BLOCK_0030");
                    SysBlock bossA = blockDao.selectOne(queryWrapperA);
                    if (bossA != null) {
                        double discountMembers = (commodityEntity.getPrice().doubleValue() * Double.valueOf(bossA.getRemark())) / 100;
                        commodityEntity.setPrice(BigDecimal.valueOf(discountMembers));
                        BigDecimal setScale = commodityEntity.getPrice().setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        commodityEntity.setPrice(setScale);
                    }
                }
            }
        } else {

        }

        return commodityEntity;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public CommodityEntity detailNew(String id) {
        CommodityEntity commodityEntity = commodityDao.selectById(id);
        //取详情多张图片
        UpdateWrapper<CommodityImgEntity> wrapperImg = new UpdateWrapper<>();
        wrapperImg.eq("commodity_id", commodityEntity.getId());
        wrapperImg.eq("is_delete", 0);
        List<CommodityImgEntity> imgList = commodityImgDao.selectList(wrapperImg);
        commodityEntity.setImgList(imgList);
        return commodityEntity;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public CommodityEntity detailOne(String id) {
        return commodityDao.selectById(id);
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
            CommodityEntity removeEntity = commodityDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            commodityDao.updateById(removeEntity);
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
                CommodityEntity removeEntity = commodityDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                commodityDao.updateById(removeEntity);
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

            CommodityListener listener = new CommodityListener();
            EasyExcel.read(file.getInputStream(), CommodityEntity.class, listener).sheet().doRead();
            //获取数据
            List<CommodityEntity> uploadData = listener.getUploadData();


            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                commodityDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
