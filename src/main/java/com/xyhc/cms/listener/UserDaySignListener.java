package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.PointsmallEntity;
import com.xyhc.cms.entity.UserDaySignEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品主表数据导入监听
 *
 * @author apollo
 * @since 2023-09-12 16:34:35
 */
public class UserDaySignListener extends AnalysisEventListener<UserDaySignEntity> {
    private static Logger logger = LoggerFactory.getLogger(UserDaySignListener.class);

    private List<UserDaySignEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(UserDaySignEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<UserDaySignEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(UserDaySignEntity financeBilling) {

    }
}
