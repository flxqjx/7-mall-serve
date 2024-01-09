package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.TimeLimitDao;
import com.xyhc.cms.entity.TimeLimitEntity;
import com.xyhc.cms.service.TimeLimitService;
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
import com.xyhc.cms.listener.TimeLimitListener;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service实现类
 *
 * @author apollo
 * @since 2023-05-31 16:33:43
 */
@Service("timeLimitService")
public class TimeLimitServiceImpl extends ServiceImpl<TimeLimitDao, TimeLimitEntity> implements TimeLimitService {

    @Resource
    TimeLimitDao  timeLimitDao;

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
    public List<TimeLimitEntity> all(Map<String, Object> params) {
        return timeLimitDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<TimeLimitEntity> page = new Query<TimeLimitEntity>().getPage(params, "id", true);
        List<TimeLimitEntity> records = timeLimitDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  timeLimit
     * @return boolean
     */
    @Override
    public boolean save(TimeLimitEntity timeLimit) {
        try {
            if (StringUtils.isBlank(timeLimit.getId())){
                timeLimit.setId(commonUtitls.createKey());
                timeLimit.setCreateBy(authUtils.AuthUser().getUserId());
                timeLimit.setCreateTime(new Date());
                super.save(timeLimit);
            }else{
                timeLimit.setUpdateBy(authUtils.AuthUser().getUserId());
                timeLimit.setUpdateTime(new Date());

                this.updateById(timeLimit);
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
    public TimeLimitEntity detail(String id) {
        TimeLimitEntity timeLimit = timeLimitDao.selectById(id);
        if(timeLimit!=null){
            if(new Date().getTime() < timeLimit.getStartTimeSeckill().getTime()){
                timeLimit.setDistance("开始");
            }else {
                timeLimit.setDistance("结束");
            }
        }
        return timeLimit;
    }

    /**
     * 修改状态
     *
     * @return
     */
    @Override
    public boolean updateTime(String id) {
        try {
            TimeLimitEntity removeEntity = timeLimitDao.selectById(id);
            removeEntity.setState(0);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            timeLimitDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;
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
            TimeLimitEntity removeEntity = timeLimitDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            timeLimitDao.updateById(removeEntity);
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
                TimeLimitEntity removeEntity = timeLimitDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                timeLimitDao.updateById(removeEntity);
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
            TimeLimitListener listener = new TimeLimitListener();
            EasyExcel.read(file.getInputStream(), TimeLimitEntity.class, listener).sheet().doRead();
            //获取数据
            List<TimeLimitEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                timeLimitDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
