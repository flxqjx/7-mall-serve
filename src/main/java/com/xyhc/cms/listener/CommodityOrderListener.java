package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品订单表数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 21:42:14
 */
public class CommodityOrderListener extends AnalysisEventListener<CommodityOrderEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityOrderListener.class);

    private List<CommodityOrderEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityOrderEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityOrderEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityOrderEntity financeBilling) {

    }
}