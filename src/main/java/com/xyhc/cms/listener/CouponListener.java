package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CouponEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 平台优惠券数据导入监听
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
public class CouponListener extends AnalysisEventListener<CouponEntity> {
    private static Logger logger = LoggerFactory.getLogger(CouponListener.class);

    private List<CouponEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CouponEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CouponEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CouponEntity financeBilling) {

    }
}