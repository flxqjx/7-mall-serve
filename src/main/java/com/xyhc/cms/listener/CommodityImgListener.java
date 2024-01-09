package com.xyhc.cms.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.entity.CommodityImgEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * 商品图片数据导入监听
 *
 * @author apollo
 * @since 2023-03-13 09:29:28
 */
public class CommodityImgListener extends AnalysisEventListener<CommodityImgEntity> {
    private static Logger logger = LoggerFactory.getLogger(CommodityImgListener.class);

    private List<CommodityImgEntity> uploadData =  new ArrayList<>();


    @Override
    public void invoke(CommodityImgEntity o, AnalysisContext analysisContext) {
        logger.info("导入数据{}", JSON.toJSONString(o));
        uploadData.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    private void doSomething(Object object) {

    }

    public List<CommodityImgEntity> getUploadData() {
        return uploadData;
    }


    public void doAfterAllAnalysed(CommodityImgEntity financeBilling) {

    }
}