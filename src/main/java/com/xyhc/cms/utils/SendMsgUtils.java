package com.xyhc.cms.utils;

import cn.hutool.json.JSONConverter;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.vo.infra.QrCodeDto;
import com.xyhc.cms.vo.sms.SmsDto;
import com.xyhc.cms.vo.wechatMsg.TemplateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 地图相关
 *
 * @param
 * @return
 */
@Slf4j
@Component
public class SendMsgUtils {

    @Autowired
    AuthUtils authUtils;

    @Value(value = "${sms.server}")
    private String smsServer;

    @Resource
    RestTemplate restTemplate;

    /**
     * 同步账号信息
     *
     * @return
     */
    public void synWechatList() {
        String url = smsServer + "/api/Common/SynWechatList";
        restTemplate.getForEntity(url, String.class);
    }

    /**
     * 坐标解析
     *
     * @param
     * @return
     */
    public String sendMsg(TemplateDto templateDto) {
        String url = smsServer + "/api/Common/SendWechatMsg";

//        templateDto = new TemplateDto();
//        templateDto.setUrl("");
//        // 从表 CMS_Engineer 中根据userId 查询到 uid
//        templateDto.setToUid("12523d1d-42ca-4fc0-b31c-27f7ac638aa1");
//        templateDto.setTemplate_id("MBT_X_8MQBJKWY4W7gLSA6AREcfQMjkkfo3lz3KQkTs");
//
//        Map<String, DataKeyDto> dataDto = new HashMap<>();
//        DataKeyDto keyDto = new DataKeyDto();
//        keyDto.setValue("dddd");
//        dataDto.put("first", keyDto);
//        templateDto.setData(dataDto);


        HttpHeaders headers = new HttpHeaders();
//        headers.add("authToken", authUtils.authToken());
        HttpEntity<TemplateDto> request = new HttpEntity<TemplateDto>(templateDto, headers);
        ResponseEntity<QrCodeDto> response = restTemplate.postForEntity(url, request, QrCodeDto.class);
        return response.getBody().getData();
    }


    public String sendSMS(SmsDto smsDto) {
        String url = smsServer + "/api/Common/SendSmsMsgDigital";

        log.info("SendSmsMsgDigital--");
        log.info(JSON.toJSONString(smsDto));

        HttpHeaders headers = new HttpHeaders();
//        headers.add("authToken", authUtils.authToken());
        HttpEntity<SmsDto> request = new HttpEntity<SmsDto>(smsDto, headers);
        ResponseEntity<QrCodeDto> response = restTemplate.postForEntity(url, request, QrCodeDto.class);
        return response.getBody().getData();
    }


}
