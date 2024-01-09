package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.PointsmallSizeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
public class PointsmallSizeListener extends AnalysisEventListener<PointsmallSizeEntity> {
    private static Logger logger = LoggerFactory.getLogger(PointsmallSizeListener.class);

    private List<PointsmallSizeEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(PointsmallSizeEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<PointsmallSizeEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(PointsmallSizeEntity financeBilling) {

    }
}