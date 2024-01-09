package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MsBankEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 18:10:04
 */
public class MsBankListener extends AnalysisEventListener<MsBankEntity> {
    private static Logger logger = LoggerFactory.getLogger(MsBankListener.class);

    private List<MsBankEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MsBankEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MsBankEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MsBankEntity financeBilling) {

    }
}