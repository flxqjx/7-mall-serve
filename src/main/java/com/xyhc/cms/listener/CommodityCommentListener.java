package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityCommentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品评论数据导入监听
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
public class CommodityCommentListener extends AnalysisEventListener<CommodityCommentEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityCommentListener.class);

    private List<CommodityCommentEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityCommentEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityCommentEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityCommentEntity financeBilling) {

    }
}