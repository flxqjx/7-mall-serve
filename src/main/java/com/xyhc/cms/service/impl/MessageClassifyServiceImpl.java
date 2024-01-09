package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MessageClassifyDao;
import com.xyhc.cms.entity.MessageClassifyEntity;
import com.xyhc.cms.service.MessageClassifyService;
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
 * 资讯分类Service实现类
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
@Service("messageClassifyService")
public class MessageClassifyServiceImpl extends ServiceImpl<MessageClassifyDao, MessageClassifyEntity> implements MessageClassifyService {

    @Resource
    MessageClassifyDao  messageClassifyDao;

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
    public List<MessageClassifyEntity> all(Map<String, Object> params) {
        return messageClassifyDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MessageClassifyEntity> page = new Query<MessageClassifyEntity>().getPage(params, "create_time", false);
        List<MessageClassifyEntity> records = messageClassifyDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  messageClassifyEntity
     * @return boolean
     */
    @Override
    public boolean save(MessageClassifyEntity messageClassifyEntity) {
        try {
            if (StringUtils.isBlank(messageClassifyEntity.getId())){
                messageClassifyEntity.setId(commonUtitls.createKey());
                messageClassifyEntity.setCreateBy(authUtils.AuthUser().getUserId());
                messageClassifyEntity.setCreateTime(new Date());
                super.save(messageClassifyEntity);
            }else{
                messageClassifyEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                messageClassifyEntity.setUpdateTime(new Date());
                this.updateById(messageClassifyEntity);
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
    public MessageClassifyEntity detail(String id) {
        return messageClassifyDao.selectById(id);
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
            MessageClassifyEntity removeEntity = messageClassifyDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            messageClassifyDao.updateById(removeEntity);
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
                MessageClassifyEntity removeEntity = messageClassifyDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                messageClassifyDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }
}
