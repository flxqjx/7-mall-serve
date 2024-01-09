package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品主表数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public class CommodityListener extends AnalysisEventListener<CommodityEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityListener.class);

    private List<CommodityEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityEntity financeBilling) {

    }
}