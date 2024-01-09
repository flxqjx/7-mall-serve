package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.UserLevelOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 数据导入监听
 *
 * @author apollo
 * @since 2023-02-14 13:58:59
 */
public class UserLevelOrderListener extends AnalysisEventListener<UserLevelOrderEntity> {
    private static Logger logger = LoggerFactory.getLogger(UserLevelOrderListener.class);

    private List<UserLevelOrderEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(UserLevelOrderEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<UserLevelOrderEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(UserLevelOrderEntity financeBilling) {

    }
}