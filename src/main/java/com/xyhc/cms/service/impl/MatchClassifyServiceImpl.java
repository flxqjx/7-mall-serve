package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.MatchClassifyDao;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.service.MatchClassifyService;
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
import com.xyhc.cms.listener.MatchClassifyListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 赛事分类表Service实现类
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
@Service("matchClassifyService")
public class MatchClassifyServiceImpl extends ServiceImpl<MatchClassifyDao, MatchClassifyEntity> implements MatchClassifyService {

    @Resource
    MatchClassifyDao  matchClassifyDao;

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
    public List<MatchClassifyEntity> all(Map<String, Object> params) {
        return matchClassifyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MatchClassifyEntity> page = new Query<MatchClassifyEntity>().getPage(params, "id", true);
        List<MatchClassifyEntity> records = matchClassifyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  matchClassify
     * @return boolean
     */
    @Override
    public boolean save(MatchClassifyEntity matchClassify) {
        try {
            if (StringUtils.isBlank(matchClassify.getId())){
                matchClassify.setId(commonUtitls.createKey());
                matchClassify.setCreateBy(authUtils.AuthUser().getUserId());
                matchClassify.setCreateTime(new Date());
                super.save(matchClassify);
            }else{
                matchClassify.setUpdateBy(authUtils.AuthUser().getUserId());
                matchClassify.setUpdateTime(new Date());
                this.updateById(matchClassify);
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
    public MatchClassifyEntity detail(String id) {
        return matchClassifyDao.selectById(id);
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
            MatchClassifyEntity removeEntity = matchClassifyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            matchClassifyDao.updateById(removeEntity);
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
                MatchClassifyEntity removeEntity = matchClassifyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                matchClassifyDao.updateById(removeEntity);
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
            MatchClassifyListener listener = new MatchClassifyListener();
            EasyExcel.read(file.getInputStream(), MatchClassifyEntity.class, listener).sheet().doRead();
            //获取数据
            List<MatchClassifyEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                matchClassifyDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
