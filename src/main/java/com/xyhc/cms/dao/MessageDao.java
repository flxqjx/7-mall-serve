package com.xyhc.cms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.entity.InformationEntity;
import com.xyhc.cms.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资讯主表Dao
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MessageEntity> all(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MessageEntity> page(IPage<MessageEntity> page, @Param("params") Map<String, Object> params);
}
