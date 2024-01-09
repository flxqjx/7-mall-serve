package com.xyhc.cms.utils;

import com.alibaba.fastjson.JSONObject;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.vo.infra.DictDto;
import com.xyhc.cms.vo.sms.SmsDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Component
public class CommonUtitls {
    @Autowired
    AuthUtils authUtils;
    @Value(value = "${base.server}")
    private String baseServer;
    @Resource
    RestTemplate restTemplate;
    public String createKey() {
        String uid = UUID.randomUUID().toString();
        return uid;
    }

    public String generate(int n) {

        //定义取值范围
        String str = "0123456789";
        //容量为4
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            //遍历4次，拿到某个字符并且拼接
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }

        return sb.toString();
    }

    /**
     * @Description: 生成唯一图片名称
     * @Param: fileName
     * @return: 云服务器fileName
     */
    public static String getRandomImgName(String fileName) {

        int index = fileName.lastIndexOf(".");

        if (fileName.isEmpty() || index == -1){
            throw new IllegalArgumentException();
        }
        // 获取文件后缀
        String suffix = fileName.substring(index).toLowerCase();
        // 生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 生成上传至云服务器的路径
        return "userAvatar:" + uuid + suffix;
    }

    public static String encryptToMD5(String str) {
        return DigestUtils.md5Hex(str);
    }


    /**
     * 身份证号加*号
     */
    public  String encryNo(String realNo) {
        String newNo = "";
        if (realNo.length() == 15){
            newNo = realNo.replaceAll("(\\d{4})\\d{7}(\\d{4})", "$1*******$2");
        }
        if (realNo.length() == 18){
            newNo = realNo.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1**********$2");
        }
        return newNo;
    }

    /**
     * 名字加*号
     * @param realname
     * @return
     */
    public  String encryName(String realname) {
        char[] r = realname.toCharArray();
        String name = "";
        if (r.length == 1){
            name = realname;
        }

        if (r.length == 2){
            name = realname.replaceFirst(realname.substring(1), "*");
        }

        if (r.length > 2){
            name = realname.replaceFirst(realname.substring(1, r.length - 1), "*");

        }
        return name;
    }

    /**
     * 手机号加*号
     * @param realPhone
     * @return
     */
    public  String encryPhone(String realPhone) {
        String phoneNumber;
        if (realPhone.length() == 11){
            phoneNumber = realPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }else{
            phoneNumber = realPhone;
        }
        return phoneNumber;
    }



    /**
     * 查询城市
     *
     * @return
     */
    public Map<String, Object> cityList(String pCode) {
        Map<String, Object> mapList = new HashMap<>();
        String url = baseServer + "/api/city/options?pCode=" + pCode;
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
