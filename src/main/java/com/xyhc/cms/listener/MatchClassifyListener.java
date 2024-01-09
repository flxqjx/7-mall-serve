package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MatchClassifyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 赛事分类表数据导入监听
 *
 * @author apollo
 * @since 2023-02-28 21:46:28
 */
public class MatchClassifyListener extends AnalysisEventListener<MatchClassifyEntity> {
    private static Logger logger = LoggerFactory.getLogger(MatchClassifyListener.class);

    private List<MatchClassifyEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MatchClassifyEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MatchClassifyEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MatchClassifyEntity financeBilling) {

    }
}