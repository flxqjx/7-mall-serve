package com.xyhc.cms.config.auth;


import com.xyhc.cms.entity.Wechat;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token 认证
 *
 * @author xushuang9
 * @email xushuang9@lenovo.com
 * @date 2021-06-21 10:00:20
 */
@Component
@Slf4j
public class TokenUtils {


    /**
     * token 认证接口
     */


    /**
     * 获取所有判定条件属性信息
     *
     * @param accessToken
     * @return
     */
    public AuthInfo volidateToken(String accessToken) {

        try {


            //生成token
            Key key = Keys.hmacShaKeyFor("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI2a2EJ7m872v0afyoSDJT2o1+SitIeJSWtLJU8/Wz2m7gStexajkeD+Lka6DSTy8gt9UwfgVQo6uKjVLG5Ex7PiGOODVqAEghBuS7JzIYU5RvI543nNDAPfnJsas96mSA7L/mD7RTE2drj6hf3oZjJpMPZUQI/B1Qjb5H3K3PNwIDAQAB".getBytes());

            //解析token
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            Claims body = claimsJws.getBody();
            AuthInfo authInfo = new AuthInfo();
            authInfo.setUserId(body.get("userId").toString());
            authInfo.setUserAccount(body.get("account").toString());
//            authInfo.setVendorId(body.get("vendorId").toString());
//            authInfo.setUserName(body.get("userName").toString());

            return authInfo;


        } catch (Exception exception) {
            log.error("111");
        }
        return null;
    }


    /**
     * 加密生成token
     *
     * @return
     */
    public String createToken(Wechat wechat) throws UnsupportedEncodingException {
        //私钥和加密算法
        String secretKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI2a2EJ7m872v0afyoSDJT2o1+SitIeJSWtLJU8/Wz2m7gStexajkeD+Lka6DSTy8gt9UwfgVQo6uKjVLG5Ex7PiGOODVqAEghBuS7JzIYU5RvI543nNDAPfnJsas96mSA7L/mD7RTE2drj6hf3oZjJpMPZUQI/B1Qjb5H3K3PNwIDAQAB";

        Map<String, Object> claims = new HashMap<String, Object>();
        Long iat = System.currentTimeMillis() / 1000;
        claims.put("account", wechat.getAppId());
        claims.put("userId", wechat.getId());
        claims.put("userName", wechat.getNickName());
        long expMillis = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 10;
        Date exp = new Date(expMillis);
        claims.put("exp", exp);

        String accessToken = Jwts.builder()
                .setSubject("") // Subject 一般填用户 ID
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(exp) // 这里把失效期定为 15 天之后
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // JWT_KEY 是一个字符串私钥，需要足够长，不然会报错
                .compact();
        log.info(accessToken);
        return accessToken;
    }
}
