package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityCommentImgEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 评论图片数据导入监听
 *
 * @author apollo
 * @since 2023-03-22 15:20:27
 */
public class CommodityCommentImgListener extends AnalysisEventListener<CommodityCommentImgEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityCommentImgListener.class);

    private List<CommodityCommentImgEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityCommentImgEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityCommentImgEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityCommentImgEntity financeBilling) {

    }
}