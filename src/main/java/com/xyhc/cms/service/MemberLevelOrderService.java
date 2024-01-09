package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.MemberLevelEntity;
import com.xyhc.cms.entity.MemberLevelOrderEntity;
import com.xyhc.cms.entity.Wechat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author apollo
 * @since 2023-03-22 18:48:06
 */
public interface MemberLevelOrderService extends IService<MemberLevelOrderEntity> {

    /**
     * 查询全部
     *
     * @param params
     * @return List<MemberLevelOrderEntity>
     */
    public List<MemberLevelOrderEntity> all(Map<String, Object> params);

    /**
     * 查询我的全部数据
     *
     * @param params
     * @return List<MemberLevelOrderEntity>
     */
    public List<MemberLevelOrderEntity> myContractAll(Map<String, Object> params);


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
     * @param  memberLevelOrder memberLevelOrder
     * @return boolean
     */
    public Result saveOrder(MemberLevelOrderEntity memberLevelOrder);

    /**
     * 保存
     *
     * @param  memberLevelOrder memberLevelOrder
     * @return boolean
     */
    public Result saveUpgrade(MemberLevelOrderEntity memberLevelOrder);

    /**
     * 升级B+会员
     *
     */
    public Result saveUpgradeBMember(String levelId,String userId);

    /**
     * 更新会员下单支付回调
     *
     * @param orderId
     * @return boolean
     */
    public Result updateLevelOrderStatus(String orderId, String orderNo, String tradeNo);

    /**
     * 查该用户是否是会员
     */
    public MemberLevelOrderEntity isMember();

    /**
    * 详情
    *
    * @return
    */
    public MemberLevelOrderEntity detail(String id);

    /**
      * 删除
      *
      * @param id
      * @return boolean
      */
    public boolean remove(String id);

    public boolean updatePayStatus(String id);

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    public boolean removeBatch(String[] ids);

    /**
      * 导入
      *
      * @param file
      * @return boolean
      */
    public Result upload(MultipartFile file) throws IOException;

    /**
     * 查询当前人会员级别
     *
     * @param
     * @return Result
     */
    public MemberLevelEntity userLevel() ;

    /**
     * 会员升级——百银+ 升级 百金
     */
    public Result updateUpgrade(Wechat wechat);
}
