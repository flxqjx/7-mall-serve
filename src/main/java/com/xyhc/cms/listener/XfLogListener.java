package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.XfLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 15:51:48
 */
public class XfLogListener extends AnalysisEventListener<XfLogEntity> {
    private static Logger logger = LoggerFactory.getLogger(XfLogListener.class);

    private List<XfLogEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(XfLogEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<XfLogEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(XfLogEntity financeBilling) {

    }
}