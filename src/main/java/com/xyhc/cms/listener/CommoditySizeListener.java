package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommoditySizeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-06-24 19:24:09
 */
public class CommoditySizeListener extends AnalysisEventListener<CommoditySizeEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommoditySizeListener.class);

    private List<CommoditySizeEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommoditySizeEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommoditySizeEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommoditySizeEntity financeBilling) {

    }
}