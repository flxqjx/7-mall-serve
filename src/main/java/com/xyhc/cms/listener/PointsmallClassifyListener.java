package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.PointsmallClassifyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品分类表数据导入监听
 *
 * @author apollo
 * @since 2023-09-12 16:35:38
 */
public class PointsmallClassifyListener extends AnalysisEventListener<PointsmallClassifyEntity> {
    private static Logger logger = LoggerFactory.getLogger(PointsmallClassifyListener.class);

    private List<PointsmallClassifyEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(PointsmallClassifyEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<PointsmallClassifyEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(PointsmallClassifyEntity financeBilling) {

    }
}