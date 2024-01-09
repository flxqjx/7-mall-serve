package com.xyhc.cms.utils;

import com.alibaba.fastjson.JSONObject;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.entity.Wechat;
import com.xyhc.cms.vo.SysUserDto;
import com.xyhc.cms.vo.common.CommonRepDto;
import com.xyhc.cms.vo.infra.CashOutVo;
import com.xyhc.cms.vo.infra.DictDto;
import com.xyhc.cms.vo.infra.RefundVo;
import com.xyhc.cms.vo.sms.SmsDto;
import com.xyhc.cms.vo.user.SysRoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图相关
 *
 * @param
 * @return
 */
@Slf4j
@Component
public class AuthorityUtils {

    @Autowired
    AuthUtils authUtils;

    @Value(value = "${base.server}")
    private String baseServer;

    @Value(value = "${dougong.notifyurl}")
    private String dougongNotifyUrl;

    /**
     * 同步用户
     *
     * @param
     * @return
     */
    public CommonRepDto saveUser(SysUserDto sysUserDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/user/save";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SysUserDto> request = new HttpEntity<SysUserDto>(sysUserDto, headers);
        Map<String, Object> paramMap = new HashMap<>();

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.POST, request, CommonRepDto.class, paramMap);

        return response.getBody();
    }


    /**
     * 同步用户
     *
     * @param
     * @return
     */
    public CommonRepDto setRole(SysUserDto sysUserDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/user/setRole";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SysUserDto> request = new HttpEntity<SysUserDto>(sysUserDto, headers);
        Map<String, Object> paramMap = new HashMap<>();

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.POST, request, CommonRepDto.class, paramMap);

        return response.getBody();
    }


    /**
     * 删除用户
     *
     * @param
     * @return
     */
    public CommonRepDto removeUser(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/user/remove?userId=" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, CommonRepDto.class, paramMap);

        return response.getBody();
    }


    /**
     * 同步角色
     *
     * @param
     * @return
     */
    public CommonRepDto saveRole(SysRoleDto sysRoleDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/role/save";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SysRoleDto> request = new HttpEntity<SysRoleDto>(sysRoleDto, headers);
        Map<String, Object> paramMap = new HashMap<>();

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.POST, request, CommonRepDto.class, paramMap);

        return response.getBody();
    }


//    /**
//     * 获取账号
//     *
//     * @return
//     */
//    public List<Wechat> wechatList() {
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, Object> mapList = new HashMap<>();
//        String url = baseServer + "/api/user/internal/wechatList";
//        List<Wechat> dict = new ArrayList<>();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("authToken", authUtils.authToken());
//        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
//        Map<String, Object> paramMap = new HashMap<>();
//        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);
//
//        List<Wechat> list = JSONObject.parseArray(response.getBody(), Wechat.class);
//
//        return list;
//    }


    /**
     * 小程序账号单条查询
     *
     * @return
     */
    public Wechat wechatByVipNo(String vipNo) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/user/internal/wechatByVipNo?vipNo=" + vipNo;
        List<Wechat> dict = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vipNo", vipNo);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);

        Wechat wechat = JSONObject.parseObject(response.getBody(), Wechat.class);

        return wechat;
    }


    /**
     * 小程序账号单条查询
     *
     * @return
     */
    public Wechat wechatByUserId(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/user/internal/wechatByUserId?userId=" + userId;
        List<Wechat> dict = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);

        Wechat wechat = JSONObject.parseObject(response.getBody(), Wechat.class);

        return wechat;
    }


    /**
     * 请求支付
     *
     * @param
     * @return
     */
    public CommonRepDto requestPay(Map<String, Object> paramMap) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/dougong/dougongTaskPayRequest";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        paramMap.put("totalAmount", 1.00);
        paramMap.put("body", "1");
        paramMap.put("outTradeNo", "1");
        paramMap.put("openId", "1");
        paramMap.put("groupId", "1");
        paramMap.put("moduleKey", "1");
        paramMap.put("notifyUrl", dougongNotifyUrl + "?moduleKey=1");

        HttpEntity request = new HttpEntity<>(paramMap, headers);

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.POST, request, CommonRepDto.class, paramMap);

        return response.getBody();
    }


    /**
     * 同步角色
     *
     * @param
     * @return
     */
    public String getWechatOpenId(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/task/getWechatOpenId?code=" + code;

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());

        HttpEntity request = new HttpEntity<>(null, headers);

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, CommonRepDto.class);

        return response.getBody().getData().toString();
    }

    /**
     * 付款到零钱 新版本已停用
     *
     * @param
     * @return
     */
    public Map<String, String> cashOutTransfer(CashOutVo cashOutVo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/infra/cashOutTransfer";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<CashOutVo> request = new HttpEntity<CashOutVo>(cashOutVo, headers);
        ResponseEntity<CommonRepDto> response = restTemplate.postForEntity(url, request, CommonRepDto.class);
        return (Map) response.getBody().getData();
    }

    /**
     * 商家转账到零钱
     * applicationCode 赋值 QIAISHIJIA_MINIPROGRAM
     *
     * @param
     * @return
     */
    public Map<String, String> cashOutV2Transfer(CashOutVo cashOutVo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/infra/cashOutV2Transfer";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<CashOutVo> request = new HttpEntity<CashOutVo>(cashOutVo, headers);
        ResponseEntity<CommonRepDto> response = restTemplate.postForEntity(url, request, CommonRepDto.class);
        if (response.getBody().getCode() == -1) {
            Map<String, String> repMap = new HashMap<>();
            repMap.put("returnCode", "ERROR");
            repMap.put("returnMsg", response.getBody().getMessage());
            return repMap;
        } else {
            return (Map) response.getBody().getData();
        }
    }

    /**
     * 增加字段 退款 状态 SUCCESS FAIL
     * 退款 返回 200 存 outRefundNo 退款单号
     * 返回 -1  存   outRefundRemark  退款备注
     * @param
     * @return
     */
    public Map<String, String> refundTransfer(RefundVo refundVo) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/infra/refundTransfer";

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<RefundVo> request = new HttpEntity<RefundVo>(refundVo, headers);
        ResponseEntity<CommonRepDto> response = restTemplate.postForEntity(url, request, CommonRepDto.class);
        return (Map) response.getBody().getData();
    }

    /**
     * 查询提现明细结果
     *
     * @param
     * @return
     */
    public String queryCashOutDetailTransferRestul(String outBatchNo, String outDetailNo, String applicationCode) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/infra/queryCashOutDetailTransferRestul?outBatchNo=" + outBatchNo + "&outDetailNo=" + outDetailNo + "&applicationCode=" + applicationCode;

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity request = new HttpEntity<>(null, headers);

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, CommonRepDto.class);
        return response.getBody().getData().toString();
    }


    /**
     * 发布正常提醒
     *
     * @return
     */
    public void sendConsoleNormal(String title, String remark) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/common/sendConsoleNormal?title=" + title + "&remark=" + remark;

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);
        log.info("发布正常提醒-" + response.getBody());
    }

    /**
     * 发布正常提醒
     *
     * @return
     */
    public void sendConsoleError(String title, String remark) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/common/sendConsoleError?title=" + title + "&remark=" + remark;

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);
        log.info("发布异常提醒-" + response.getBody());
    }
}
