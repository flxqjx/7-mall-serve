package com.xyhc.cms.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.SysAccessTokenDao;
import com.xyhc.cms.vo.common.WechatAccessTokenDto;
import com.xyhc.cms.vo.common.WxJsapiSignatureVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

/**
 * 地图相关
 *
 * @param
 * @return
 */
@Slf4j
@Component
public class WechatUtils {

    @Autowired
    AuthUtils authUtils;

    @Resource
    RestTemplate restTemplate;

    @Value("${wechatProgram.appId}")
    private String wechatAppId;
    @Value("${wechatProgram.appSecret}")
    private String wechatAppSecret;

    @Resource
    SysAccessTokenDao sysAccessTokenDao;

    /**
     * 获取token
     *
     * @return
     */
    public WechatAccessTokenDto getAccessToken(String code, String openid) {
        WechatAccessTokenDto wechatAccessTokenDto = new WechatAccessTokenDto();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openid);

        boolean refresh = true;
//        SysAccessTokenEntity sysAccessTokenEntity = sysAccessTokenDao.selectOne(queryWrapper);
//        if (sysAccessTokenEntity != null) {
//            long expireTime = sysAccessTokenEntity.getExpireTime();
//            // 已超期 重新获取
//            if (expireTime < System.currentTimeMillis()) {
//                refresh = true;
//            } else {
//                wechatAccessTokenDto.setAccessToken(sysAccessTokenEntity.getToken());
//                wechatAccessTokenDto.setOpenid(openid);
//                refresh = false;
//            }
//        } else {
//            refresh = true;
//            sysAccessTokenEntity = new SysAccessTokenEntity();
//            sysAccessTokenEntity.setId(0);
//        }

        if (refresh) {
            String reqUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=" + wechatAppId +
                    "&secret=" + wechatAppSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            String resultInfo = restTemplate.getForObject(reqUrl, String.class);
            JSONObject resultUserObject = JSONObject.parseObject(resultInfo);
            String accessToken = resultUserObject.getString("access_token");
            long expireTime = System.currentTimeMillis() + 7200;
            openid = resultUserObject.getString("openid");
            wechatAccessTokenDto.setAccessToken(accessToken);
            wechatAccessTokenDto.setOpenid(openid);
//            sysAccessTokenEntity.setToken(accessToken);
//            sysAccessTokenEntity.setOpenid(openid);
//            sysAccessTokenEntity.setExpireTime(expireTime);
//            sysAccessTokenEntity.setUpdateTime(new Date());
//            if (sysAccessTokenEntity.getId() == 0) {
//                sysAccessTokenDao.insert(sysAccessTokenEntity);
//            } else {
//                sysAccessTokenDao.updateById(sysAccessTokenEntity);
//            }
        }
        return wechatAccessTokenDto;
    }

    /**
     * 获取 JsapiTicket
     *
     * @return
     */
    private String getJsapiTicket() {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?";
        String params = "grant_type=client_credential&appid=" + wechatAppId + "&secret=" + wechatAppSecret + "";
        String result = HttpRequestUtils.httpGet(requestUrl + params);
        String access_token = JSONObject.parseObject(result).getString("access_token");
        requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";
        params = "access_token=" + access_token + "&type=jsapi";
        result = HttpRequestUtils.httpGet(requestUrl + params);
        String jsapi_ticket = JSONObject.parseObject(result).getString("ticket");
        int activeTime = Integer.parseInt(JSONObject.parseObject(result).getString("expires_in"));
        return jsapi_ticket;
    }

    /**
     * 获取 生成签名
     *
     * @return
     */
    public WxJsapiSignatureVo createTicketSign(String url) {
        String jsapi_ticket = getJsapiTicket();
        String noncestr = getNonceStr();
        long timestamp = getTimeStamp();
        String sign = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WxJsapiSignatureVo signatureVo = new WxJsapiSignatureVo();
        signatureVo.setAppId(wechatAppId);
        signatureVo.setNonceStr(noncestr);
        signatureVo.setTimestamp(timestamp);
        signatureVo.setSignature(signature);
        signatureVo.setJsapiTicket(jsapi_ticket);
        signatureVo.setUrl(url);
        return signatureVo;
    }

    /**
     * 算法
     *
     * @return
     */
    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 算法
     *
     * @return
     */
    private String getNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 算法
     *
     * @return
     */
    private long getTimeStamp() {
        return System.currentTimeMillis() / 1000;

    }
}
