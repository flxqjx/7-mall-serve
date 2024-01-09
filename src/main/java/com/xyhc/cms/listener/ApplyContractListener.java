package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.ApplyContractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-05-24 15:55:30
 */
public class ApplyContractListener extends AnalysisEventListener<ApplyContractEntity> {
    private static Logger logger = LoggerFactory.getLogger(ApplyContractListener.class);

    private List<ApplyContractEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(ApplyContractEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<ApplyContractEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(ApplyContractEntity financeBilling) {

    }
}