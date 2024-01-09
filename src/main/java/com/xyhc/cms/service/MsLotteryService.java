package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MatchClassifyEntity;
import com.xyhc.cms.entity.MsLotteryEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 赛事分类表Service接口
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public interface MsLotteryService extends IService<MsLotteryEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public List<MsLotteryEntity> all(Map<String, Object> params);

    /**
     * 查询全部
     *
     * @param params
     * @return List<MatchClassifyEntity>
     */
    public Result randomLotteryNum(Map<String, Object> params);

    /**
      * 分页查询
      *
      * @param params
      * @return PageUtils
      */
    public PageUtils page(Map<String, Object> params);

    /**
     * 保存
     *
     * @param  msLottery msLottery
     * @return boolean
     */
    public boolean save(MsLotteryEntity msLottery);

    /**
    * 详情
    *
    * @return
    */
    public MsLotteryEntity detail(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    public boolean removeBatch(String[] ids);

    /**
     * 最近的一条抽奖活动
     *
     * @return
     */
    public MsLotteryEntity lastLottery();

}
