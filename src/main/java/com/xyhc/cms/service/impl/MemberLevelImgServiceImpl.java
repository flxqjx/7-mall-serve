package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.CommodityImgDao;
import com.xyhc.cms.dao.MemberLevelImgDao;
import com.xyhc.cms.entity.CommodityImgEntity;
import com.xyhc.cms.entity.MemberLevelImgEntity;
import com.xyhc.cms.listener.CommodityImgListener;
import com.xyhc.cms.service.CommodityImgService;
import com.xyhc.cms.service.MemberLevelImgService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品图片Service实现类
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
@Service("memberLevelImgService")
public class MemberLevelImgServiceImpl extends ServiceImpl<MemberLevelImgDao, MemberLevelImgEntity> implements MemberLevelImgService {

    @Resource
    MemberLevelImgDao  memberLevelImgDao;

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
    public List<MemberLevelImgEntity> all(Map<String, Object> params) {
        return memberLevelImgDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MemberLevelImgEntity> page = new Query<MemberLevelImgEntity>().getPage(params, "id", true);
        List<MemberLevelImgEntity> records = memberLevelImgDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  memberLevelImgEntity
     * @return boolean
     */
    @Override
    public boolean save(MemberLevelImgEntity memberLevelImgEntity) {
        try {
            if (StringUtils.isBlank(memberLevelImgEntity.getId())){
                memberLevelImgEntity.setId(commonUtitls.createKey());
                memberLevelImgEntity.setCreateTime(new Date());
                super.save(memberLevelImgEntity);
            }else{
                memberLevelImgEntity.setUpdateTime(new Date());
                this.updateById(memberLevelImgEntity);
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
    public MemberLevelImgEntity detail(String id) {
        return memberLevelImgDao.selectById(id);
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
            MemberLevelImgEntity memberLevelImgEntity = memberLevelImgDao.selectById(id);
            memberLevelImgEntity.setIsDelete(1);
            memberLevelImgEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            memberLevelImgDao.updateById(memberLevelImgEntity);
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
                MemberLevelImgEntity removeEntity = memberLevelImgDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelImgDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

}
