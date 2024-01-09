package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CouponUsersEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 优惠券领取数据导入监听
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
public class CouponUsersListener extends AnalysisEventListener<CouponUsersEntity> {
    private static Logger logger = LoggerFactory.getLogger(CouponUsersListener.class);

    private List<CouponUsersEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CouponUsersEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CouponUsersEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CouponUsersEntity financeBilling) {

    }
}