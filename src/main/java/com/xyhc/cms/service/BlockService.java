package com.xyhc.cms.service;

import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.entity.SysBlock;

import java.util.List;
import java.util.Map;


public interface BlockService {
    /**
     * 分页查询商家分类
     *
     * @param params typeClass 查询几级的分类
     *               可以给oneId,查询某个一级下的二级
     * @return
     */
    public PageUtils page(Map<String, Object> params);


    /**
     * 添加修改 分类
     *
     * @param
     */
    public String save(SysBlock SysMenu);


    /**
     * 单条详情
     *
     * @param
     */
    public SysBlock detail(String Id);


    /**
     * 单条详情
     *
     * @param
     */

    public SysBlock detailByBlock(String block) ;
    /**
     * 删除
     *
     * @param id
     * @return boolean
     */
    public boolean remove(String id);

    /**
     * indexBanner
     *
     * @param
     */
    public List<SysBlock> indexBanner();

    /**
     * 快捷入口
     *
     * @param
     */
    public List<SysBlock> typeBanner();


}
