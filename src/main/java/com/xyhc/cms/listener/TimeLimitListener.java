package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.TimeLimitEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-05-31 16:33:43
 */
public class TimeLimitListener extends AnalysisEventListener<TimeLimitEntity> {
    private static Logger logger = LoggerFactory.getLogger(TimeLimitListener.class);

    private List<TimeLimitEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(TimeLimitEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<TimeLimitEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(TimeLimitEntity financeBilling) {

    }
}