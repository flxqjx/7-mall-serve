package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MessageClassifyEntity;
import com.xyhc.cms.entity.MessageClassifyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯分类数据导入监听
 *
 * @author apollo
 * @since 2023-03-31 10:15:41
 */
public class MessageClassifyListener extends AnalysisEventListener<MessageClassifyEntity> {
    private static Logger logger = LoggerFactory.getLogger(MessageClassifyListener.class);

    private List<MessageClassifyEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MessageClassifyEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MessageClassifyEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MessageClassifyEntity financeBilling) {

    }
}