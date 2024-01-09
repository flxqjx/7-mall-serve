package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityCommentReplyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 评论回复数据导入监听
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
public class CommodityCommentReplyListener extends AnalysisEventListener<CommodityCommentReplyEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityCommentReplyListener.class);

    private List<CommodityCommentReplyEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityCommentReplyEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityCommentReplyEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityCommentReplyEntity financeBilling) {

    }
}