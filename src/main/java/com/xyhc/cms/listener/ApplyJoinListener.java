package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.ApplyJoinEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-05-24 13:40:41
 */
public class ApplyJoinListener extends AnalysisEventListener<ApplyJoinEntity> {
    private static Logger logger = LoggerFactory.getLogger(ApplyJoinListener.class);

    private List<ApplyJoinEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(ApplyJoinEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<ApplyJoinEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(ApplyJoinEntity financeBilling) {

    }
}