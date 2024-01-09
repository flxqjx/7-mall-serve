package com.xyhc.cms.utils;

import com.alibaba.fastjson.JSONObject;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.vo.infra.DictDto;
import com.xyhc.cms.vo.sms.SmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
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
public class DictUtils {

    @Autowired
    AuthUtils authUtils;

    @Value(value = "${base.server}")
    private String baseServer;

    @Resource
    RestTemplate restTemplate;

    /**
     * 获取字典
     *
     * @return
     */
    public Map<String, Object> dictList(String key) {
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/dict/dictItemSelect?key=" + key;
        List<DictDto> dict = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);

        List<DictDto> list = JSONObject.parseArray(response.getBody(), DictDto.class);
        list.forEach(item -> {
            mapList.put(item.getValue(), item.getText());
        });
        return mapList;
    }

    public Map<String, Object> productList(String key) {
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/product/dictItemSelect?key=" + key;
        List<DictDto> dict = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(null, headers);
        Map<String, Object> paramMap = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class, paramMap);

        List<DictDto> list = JSONObject.parseArray(response.getBody(), DictDto.class);
        list.forEach(item -> {
            mapList.put(item.getValue(), item.getText());
        });
        return mapList;
    }
}
