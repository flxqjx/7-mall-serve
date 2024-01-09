package com.xyhc.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.BlockDao;
import com.xyhc.cms.entity.InformationEntity;
import com.xyhc.cms.entity.SysBlock;
import com.xyhc.cms.service.BlockService;
import com.xyhc.cms.utils.CommonUtitls;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 账号
 *
 * @Author apollo
 * 2022-05-11  10:55:00
 */
@Slf4j
@Service
public class BlockServiceImpl implements BlockService {


    @Resource
    BlockDao blockDao;

    @Resource
    CommonUtitls commonUtitls;
    @Resource
    AuthUtils authUtils;

    @Value("${manage.admin}")
    private String manageAdmin;

    /**
     * 分页查询商家分类
     *
     * @param params typeClass 查询几级的分类
     *               可以给oneId,查询某个一级下的二级
     * @return
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<SysBlock> list = new Query<SysBlock>().getPage(params, "create_time", false);
        List<SysBlock> records = blockDao.page(list, params);
        list.setRecords(records);
        PageUtils ownerVm = new PageUtils(records, ((int) list.getTotal()), ((int) list.getSize()), ((int) list.getCurrent()));
        return ownerVm;
    }

    /**
     * 添加修改 分类
     *
     * @param
     */
    @Override
    public String save(SysBlock sysMenu) {
        try {
            if (!manageAdmin.equals(authUtils.AuthUser().getUserAccount())) {
                return "操作异常";
            }
            Date date = new Date();
            if (!StringUtils.isNotEmpty(sysMenu.getId())) {
                sysMenu.setCreateTime(date);
                sysMenu.setId(commonUtitls.createKey());
                sysMenu.setBlock(createMax());
                blockDao.insert(sysMenu);
            } else {
                sysMenu.setUpdateTime(date);
                blockDao.updateById(sysMenu);
            }

            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    /**
     * 单条详情
     *
     * @param
     */
    @Override
    public SysBlock detail(String Id) {
        try {
            SysBlock boss = blockDao.selectById(Id);
            return boss;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 单条详情
     *
     * @param
     */
    @Override
    public SysBlock detailByBlock(String block) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("block", block);
            SysBlock boss = blockDao.selectOne(queryWrapper);
            return boss;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除分类
     *
     * @param
     */
    @Override
    public boolean remove(String id) {
        try {
            SysBlock block = blockDao.selectById(id);
            block.setIsDelete(1);
            block.setUpdateTime(new Date());
            blockDao.updateById(block);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }


    /**
     * 生成Id
     *
     * @return
     */
    public String createId() {
        try {
            int rannum = (int) (1 + Math.random() * (99 - 1 + 1));
            String ranNumStr = String.format("%02d", rannum);
            String strPre = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            return strPre + ranNumStr + String.format("%04d", (maxNo() + 1));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 生成Id
     *
     * @return
     */
    public String createMax() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            List<SysBlock> scheduleList = blockDao.selectList(new QueryWrapper<SysBlock>());
            return "BLOCK_" + String.format("%04d", (scheduleList.stream().count() + 1));


        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取数据库中最大 编号
     *
     * @return
     */
    public long maxNo() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            List<SysBlock> scheduleList = blockDao.selectList(new QueryWrapper<SysBlock>()
                    .gt("create_time", minDate).lt("create_time", maxDate));
            return scheduleList.stream().count();
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * indexBanner
     *
     * @param
     */
    @Override
    public List<SysBlock> indexBanner() {
        try {
            List<String> list = new ArrayList<>();
            list.add("BLOCK_0018");
            list.add("BLOCK_0019");
            list.add("BLOCK_0020");
            list.add("BLOCK_0036");
            list.add("BLOCK_0037");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("block", list);
            queryWrapper.eq("is_delete", 0);
            List<SysBlock> boss = blockDao.selectList(queryWrapper);
            return boss;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 快捷入口
     *
     * @param
     */
    @Override
    public List<SysBlock> typeBanner() {
        try {
            List<String> list = new ArrayList<>();
            list.add("BLOCK_0006");
            list.add("BLOCK_0007");
            list.add("BLOCK_0008");
            list.add("BLOCK_0009");
            list.add("BLOCK_0010");
            list.add("BLOCK_0011");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("block", list);
            queryWrapper.eq("is_delete", 0);
            queryWrapper.orderByAsc("block");
            List<SysBlock> boss = blockDao.selectList(queryWrapper);
            return boss;
        } catch (Exception e) {
            return null;
        }
    }


}

