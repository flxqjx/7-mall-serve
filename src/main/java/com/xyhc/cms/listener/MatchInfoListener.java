package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MatchInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 赛事主表数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 21:42:15
 */
public class MatchInfoListener extends AnalysisEventListener<MatchInfoEntity> {
    private static Logger logger = LoggerFactory.getLogger(MatchInfoListener.class);

    private List<MatchInfoEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MatchInfoEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MatchInfoEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MatchInfoEntity financeBilling) {

    }
}