package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.MatchCollectEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 赛事收藏表数据导入监听
 *
 * @author apollo
 * @since 2023-03-10 12:04:37
 */
public class MatchCollectListener extends AnalysisEventListener<MatchCollectEntity> {
    private static Logger logger = LoggerFactory.getLogger(MatchCollectListener.class);

    private List<MatchCollectEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(MatchCollectEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<MatchCollectEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(MatchCollectEntity financeBilling) {

    }
}