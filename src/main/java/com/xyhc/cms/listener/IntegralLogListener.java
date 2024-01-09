package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.IntegralLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-08-17 15:26:30
 */
public class IntegralLogListener extends AnalysisEventListener<IntegralLogEntity> {
    private static Logger logger = LoggerFactory.getLogger(IntegralLogListener.class);

    private List<IntegralLogEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(IntegralLogEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<IntegralLogEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(IntegralLogEntity financeBilling) {

    }
}