package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.ReceiveOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-09-28 19:30:42
 */
public class ReceiveOrderListener extends AnalysisEventListener<ReceiveOrderEntity> {
    private static Logger logger = LoggerFactory.getLogger(ReceiveOrderListener.class);

    private List<ReceiveOrderEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(ReceiveOrderEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<ReceiveOrderEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(ReceiveOrderEntity financeBilling) {

    }
}