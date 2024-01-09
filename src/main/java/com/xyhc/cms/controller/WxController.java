package com.xyhc.cms.controller;

import com.alibaba.fastjson.JSON;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.service.*;
import com.xyhc.cms.utils.AuthorityUtils;
import com.xyhc.cms.utils.XMLHelper;
import com.xyhc.cms.vo.pay.PayRepVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RestController
public class WxController {

    @Autowired
    AuthorityUtils authorityUtils;


    @Autowired
    XfLogService xfLogService;

    @Autowired
    CommodityOrderService commodityOrderService;

    @Autowired
    MatchApplyVoteService matchApplyVoteService;

    @Autowired
    MemberLevelOrderService memberLevelOrderService;

    @Autowired
    MsLotteryUserService msLotteryUserService;


    /**
     * 任务平台获取OPENID
     *
     * @return
     */
    @GetMapping(value = "/login/getWechatOpenId")
    public Result getWechatOpenId(@RequestParam String code) {
        String openId = authorityUtils.getWechatOpenId(code);
        if (StringUtils.isNotEmpty(openId)) {
            return Result.success(openId);
        } else {
            return Result.error("OPENID为空");
        }
    }

    /**
     * 请求支付
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/pay/dougongTaskPayRequest")
    public Result dougongTaskPayRequest(Map<String, Object> paramMap) {
        return Result.success(authorityUtils.requestPay(paramMap));
    }

    /**
     * 支付回调
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/pay/dougongTaskPayNotify")
    public Result dougongTaskPayNotify(@RequestBody PayRepVo payRepVo, Map<String, Object> paramMap) {
        log.info(JSON.toJSONString(payRepVo));
        log.info(JSON.toJSONString(paramMap));
        return Result.success();
    }


    /**
     * 微信统一下单接口返回
     *
     * @return
     */
    @PostMapping("/wx/responseUnifiedOrder")
    public String responseUnifiedOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("微信支付回调");
        try {
            //解析xml
            InputStream a = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(a, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //xml帮助类
            XMLHelper xh = new XMLHelper(sb.toString(), "UTF-8");
            log.info("返回的参数：" + sb.toString());
            //订单号
            String orderNo = xh.readByName("out_trade_no");
            String tradeNo = xh.readByName("transaction_id");
            String totalFee = xh.readByName("total_fee");
            //状态码
            String resultCode = xh.readByName("result_code");
            String sign = xh.readByName("sign");
            log.info("orderId：" + xh.readByName("orderId"));
            log.info("orderMoudle：" + xh.readByName("orderMoudle"));

            if ("COMMODITY_APPLY".equals(xh.readByName("orderMoudle"))) {
                String orderId = xh.readByName("orderId");
                commodityOrderService.updateCommodityOrderStatus(orderId, orderNo, tradeNo);
            }

            if ("COMMODITY_NEW_APPLY".equals(xh.readByName("orderMoudle"))) {
                String orderId = xh.readByName("orderId");
                commodityOrderService.updateCommodityNewOrderStatus(orderId, orderNo, tradeNo);
            }

            if ("VIPORDER_PAY".equals(xh.readByName("orderMoudle"))) {
                String orderId = xh.readByName("orderId");
                memberLevelOrderService.updateLevelOrderStatus(orderId, orderNo, tradeNo);
            }

            if ("VOTE".equals(xh.readByName("orderMoudle"))) {
                String orderId = xh.readByName("orderId");
                matchApplyVoteService.updateVotePayStatus(orderId, orderNo, tradeNo);
            }
            //领取奖品付运费
            if ("LOTTERY_APPLY".equals(xh.readByName("orderMoudle"))) {
                String orderId = xh.readByName("orderId");
                msLotteryUserService.updateLotteryOrderStatus(orderId, orderNo, tradeNo);
            }
        } catch (Exception ex) {

        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }


}
