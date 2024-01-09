package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.PointsmallSpecEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品规格数据导入监听
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
public class PointsmallSpecListener extends AnalysisEventListener<PointsmallSpecEntity> {
    private static Logger logger = LoggerFactory.getLogger(PointsmallSpecListener.class);

    private List<PointsmallSpecEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(PointsmallSpecEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<PointsmallSpecEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(PointsmallSpecEntity financeBilling) {

    }
}