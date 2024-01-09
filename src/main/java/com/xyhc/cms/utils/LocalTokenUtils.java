package com.xyhc.cms.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalTokenUtils {
    private static final long MAX_TIME = 30 * 1000;

    public synchronized boolean getLocalToken() {
        String access_token = "";
        File file = new File("qiaishijia-localToken.json");
        try {
            //文件不存在，则创建文件
            if (!file.exists()) file.createNewFile();
            //若文件为空，则第一次访问
            if (file.length() == 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("last_time", System.currentTimeMillis() + "");
                this.writeToken(file, map);
            } else {
                //从磁盘读取文件
                String json = this.readToken(file);
                JSONObject jsonObject = JSONObject.parseObject(json);
                if (jsonObject.get("last_time") != null) {
                    long saveTime = Long.parseLong((String) jsonObject.get("last_time"));
                    long nowTime = System.currentTimeMillis();
                    long remainTime = nowTime - saveTime;
                    //若间隔时间小于2小时
                    if (remainTime < MAX_TIME) {
                        return false;
                    } else {
                        //若间隔时间大于等于2小时，需要刷新access_token,重新写入文件
                        HashMap<String, String> map = new HashMap<>();
                        map.put("last_time", System.currentTimeMillis() + "");
                        this.writeToken(file, map);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //token持久化到磁盘
    private void writeToken(File file, Map map) {
        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            String json = JSON.toJSONString(map);
            fos.write(json.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //从磁盘读取token
    private String readToken(File file) {
        //若文件存在access_token，则读取access_token
        FileInputStream fis = null;
        String json = "";
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];

            int len;
            while ((len = fis.read(b)) != -1) {
                json += new String(b, 0, len);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}
