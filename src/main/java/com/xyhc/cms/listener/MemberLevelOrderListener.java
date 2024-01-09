package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MemberLevelOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-03-22 18:48:06
 */
public class MemberLevelOrderListener extends AnalysisEventListener<MemberLevelOrderEntity> {
    private static Logger logger = LoggerFactory.getLogger(MemberLevelOrderListener.class);

    private List<MemberLevelOrderEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MemberLevelOrderEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MemberLevelOrderEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MemberLevelOrderEntity financeBilling) {

    }
}