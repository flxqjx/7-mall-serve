package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.InformationDao;
import com.xyhc.cms.dao.MessageDao;
import com.xyhc.cms.entity.InformationEntity;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.entity.MessageClassifyEntity;
import com.xyhc.cms.entity.MessageEntity;
import com.xyhc.cms.listener.InformationListener;
import com.xyhc.cms.service.InformationService;
import com.xyhc.cms.service.MessageClassifyService;
import com.xyhc.cms.service.MessageService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 资讯主表Service实现类
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

    @Resource
    MessageDao messageDao;

    @Resource
    MessageClassifyService messageClassifyService;

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
    public List<MessageEntity> all(Map<String, Object> params) {
        return messageDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MessageEntity> page = new Query<MessageEntity>().getPage(params, "create_time", false);
        List<MessageEntity> records = messageDao.page(page, params);
        Map<String, Object> map = new HashMap<>();
        List<MessageClassifyEntity> classifyEntityList = messageClassifyService.all(map);
        records.forEach(item -> {
            Optional<MessageClassifyEntity> classifyEntity = classifyEntityList.stream().filter(s -> s.getId().equals(item.getClassifyId())).findFirst();
            if (classifyEntity.isPresent()) {
                item.setClassifyName(classifyEntity.get().getClassifyName());
            }
        });
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  information
     * @return boolean
     */
    @Override
    public boolean save(MessageEntity messageEntity) {
        try {
            if (StringUtils.isBlank(messageEntity.getId())){
                messageEntity.setId(commonUtitls.createKey());
                messageEntity.setCreateBy(authUtils.AuthUser().getUserId());
                messageEntity.setCreateTime(new Date());
                super.save(messageEntity);
            }else{
                messageEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                messageEntity.setUpdateTime(new Date());
                this.updateById(messageEntity);
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
    public MessageEntity detail(String id) {
        return messageDao.selectById(id);
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
            MessageEntity removeEntity = messageDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            messageDao.updateById(removeEntity);
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
                MessageEntity removeEntity = messageDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                messageDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

}
