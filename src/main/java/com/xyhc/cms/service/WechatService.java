package com.xyhc.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.ApplyContractEntity;
import com.xyhc.cms.entity.MsCashOutEntity;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.vo.system.AppLoginDto;
import com.xyhc.cms.vo.system.LoginRepDto;
import com.xyhc.cms.vo.system.OpenIdDto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


public interface WechatService extends IService<Wechat> {


    public List<Wechat> all(Map<String, Object> params);

    /**
     * 获取openid
     *
     * @param
     */
    public OpenIdDto getOpenId(String code) throws UnsupportedEncodingException;

    /**
     * 小程序登录
     *
     * @param
     */
    public LoginRepDto appLogin(AppLoginDto loginDto);

    /**
     * 更新
     *
     * @param
     * @return boolean
     */
    public boolean updatePid(Wechat wechat);

    /**
     * 小程序账号信息
     *
     * @param
     */
    public Wechat userInfo();

    /**
     * 小程序账号信息
     *
     * @param
     */
    public Wechat mineInfo();

    /**
     * 详情
     *
     * @return
     */
    public Wechat detail(String id);

    /**
     * 查询用户团队
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils teamPage(Map<String, Object> params);

    /**
     * 查询我的团队
     *
     * @return
     */
    public Wechat team();

    /**
     * 详情
     *
     * @return
     */
    public Wechat userDetail(int ranNo);

    /**
     * 查询用户是否A套餐会员
     */
    public Result userInfoVip();


    /**
     * 根据openid 获取信息
     *
     * @param
     */
    public Wechat infoByOpenId(String openid);

    /**
     * 小程序账号
     *
     * @param
     */
    public List<Wechat> wechatList();


    /**
     * 保存头像和昵称
     *
     * @param wechat wechat
     * @return Result
     */
    public Result saveMessage(Wechat wechat);

    /**
     * 完善个人信息
     *
     * @param wechat wechat
     * @return Result
     */
    public Result saveInfo(Wechat wechat);


    /**
     * 小程序账号单条查询
     *
     * @param
     */

    public Wechat wechatByVipNo(String vipNo);

    public Wechat wechatByScene(String vipNo);

    public Wechat wechatByUserId(String userId);

    public PageUtils wechatPage(Map<String, Object> params);

    /**
     * 公众号登录
     *
     * @param
     */
    public LoginRepDto wechatLogin(String code, String openid) throws IOException;

    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    public PageUtils page(Map<String, Object> params);

    /**
     * 解密用户手机号
     *
     * @return
     * @throws Exception
     * @date 2019年05月08日
     */
    public String enPhoneNumber(String sessionKey, String ivData, String encryptedData) throws Exception;

    /**
     * 获取 sessionKey
     *
     * @param
     */
    public OpenIdDto sessionKey(String code) throws UnsupportedEncodingException;

    /**
     * 备注
     *
     * @param id
     * @return boolean
     */
    public boolean saveUserRemark(String id, String remark);

    /**
     * C会员自动升级A会员
     */
    public void levelCUpgradeTask();

    /**
     * 个人中心取会员团队信息
     */
    public Result userInfoTeam();
}
