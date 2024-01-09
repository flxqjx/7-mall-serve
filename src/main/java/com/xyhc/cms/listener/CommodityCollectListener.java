package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityCollectEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 店铺收藏数据导入监听
 *
 * @author apollo
 * @since 2023-09-20 10:26:49
 */
public class CommodityCollectListener extends AnalysisEventListener<CommodityCollectEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityCollectListener.class);

    private List<CommodityCollectEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityCollectEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityCollectEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityCollectEntity financeBilling) {

    }
}