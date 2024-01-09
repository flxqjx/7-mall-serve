package com.xyhc.cms.config.task;

import com.xyhc.cms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ServerOrderTask {


    @Autowired
    XfLogService xfLogService;

    @Autowired
    CommodityOrderService commodityOrderService;

    @Autowired
    DashBoardService dashBoardService;

    @Autowired
    MsCashOutService msCashOutService;
    @Autowired
    WechatService wechatService;
    @Value("${env}")
    private String env;

    /**
     * 更新提现状态
     *
     * @return
     */
    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void updateCashStatus() throws IOException {
        msCashOutService.updateCashTransferStatus();
        log.info("更新提现状态-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
    }


    /**
     * 7日后到账(待确认收货自动变已确认收货)-自动处理  启用
     *
     * @return
     */
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void saveToReceivingMall() throws IOException {
        commodityOrderService.saveToReceivingMall();
        log.info("7日后到账(待确认收货自动变已确认收货)-自动处理-" + new Date() + "-");
    }


    /**
     * 余额监控
     */
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void balanceConsole() throws IOException {
        if ("dev".equals(env)) {
            log.info("余额监控-DEV-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
            return;
        }
        dashBoardService.balanceConsole();
        log.info("余额监控-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "");
    }

}
