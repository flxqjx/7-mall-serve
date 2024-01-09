package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.UserLevelEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商学院分类数据导入监听
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
public class UserLevelListener extends AnalysisEventListener<UserLevelEntity> {
    private static Logger logger = LoggerFactory.getLogger(UserLevelListener.class);

    private List<UserLevelEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(UserLevelEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<UserLevelEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(UserLevelEntity financeBilling) {

    }
}