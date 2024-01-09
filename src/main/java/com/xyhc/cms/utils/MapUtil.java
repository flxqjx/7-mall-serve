package com.xyhc.cms.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.xyhc.cms.vo.infra.MapDto;

import javax.annotation.Resource;
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
public class MapUtil {
    @Resource
    RestTemplate restTemplate;

    /**
     * 坐标解析
     *
     * @param
     * @return
     */
    public MapDto resolveMap(String latitude, String longitude) {

        String url = "https://apis.map.qq.com/ws/geocoder/v1/?location=" + latitude + "," + longitude + "&key=BCXBZ-IYTYJ-HE5FO-K5DNZ-STMDF-VPFX3";
//        String rep = restTemplate.getForObject(url, String.class);
//        log.info(rep);
        ResponseEntity<MapDto> responseEntity = restTemplate.getForEntity(url, MapDto.class);
        return responseEntity.getBody();
    }

    public String retrievalCoordinate(String address) {
        String Key = "BCXBZ-IYTYJ-HE5FO-K5DNZ-STMDF-VPFX3";
        String url = "https://apis.map.qq.com/ws/geocoder/v1/?address={address}&key={key}";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("address", address);
        paramMap.put("key", Key);
        return restTemplate.getForObject(url, String.class, paramMap);
    }


}
