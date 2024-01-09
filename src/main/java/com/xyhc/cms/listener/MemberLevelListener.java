package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MemberLevelEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员级别数据导入监听
 *
 * @author apollo
 * @since 2023-03-22 15:18:26
 */
public class MemberLevelListener extends AnalysisEventListener<MemberLevelEntity> {
    private static Logger logger = LoggerFactory.getLogger(MemberLevelListener.class);

    private List<MemberLevelEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MemberLevelEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MemberLevelEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MemberLevelEntity financeBilling) {

    }
}