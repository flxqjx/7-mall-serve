package com.xyhc.cms.utils;

import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.vo.common.CommonRepDto;
import com.xyhc.cms.vo.infra.QrCodeDto;
import com.xyhc.cms.vo.qrcode.WeChatQueryObject;
import com.xyhc.cms.vo.user.SysRoleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 地图相关
 *
 * @param
 * @return
 */
@Slf4j
@Component
public class QrCodeUtil {

    @Autowired
    AuthUtils authUtils;

    @Value(value = "${base.server}")
    private String baseServer;


    /**
     * 微信二维码
     *
     * @param
     * @return
     */
    public String createQrCode(String toUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/Common/CreateQrCode";
        Map<String, String> map = new HashMap<>();
        map.put("toUrl", toUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map, headers);
        ResponseEntity<QrCodeDto> response = restTemplate.postForEntity(url, request, QrCodeDto.class);
        return response.getBody().getData();
    }


    /**
     * 生成小程序二维码
     *
     * @param
     * @return
     */
    public String createMiniProgramQrCode(WeChatQueryObject weChatQueryObject) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseServer + "/api/common/v2/createMiniProgramQrCode";


        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<WeChatQueryObject> request = new HttpEntity<WeChatQueryObject>(weChatQueryObject, headers);
        Map<String, Object> paramMap = new HashMap<>();

        ResponseEntity<CommonRepDto> response = restTemplate.exchange(url.toString(), HttpMethod.POST, request, CommonRepDto.class, paramMap);


        return response.getBody().getData().toString();
    }
}
