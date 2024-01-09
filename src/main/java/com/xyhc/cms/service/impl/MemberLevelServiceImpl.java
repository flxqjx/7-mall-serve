package com.xyhc.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.common.page.PageUtils;
import com.xyhc.cms.common.page.Query;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.dao.MemberLevelDao;
import com.xyhc.cms.dao.MemberLevelImgDao;
import com.xyhc.cms.entity.MemberLevelEntity;
import com.xyhc.cms.entity.MemberLevelImgEntity;
import com.xyhc.cms.listener.MemberLevelListener;
import com.xyhc.cms.service.MemberLevelService;
import com.xyhc.cms.utils.CommonUtitls;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 会员级别Service实现类
 *
 * @author apollo
 * @since 2023-03-22 15:18:26
 */
@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Resource
    MemberLevelDao memberLevelDao;
    @Resource
    MemberLevelImgDao memberLevelImgDao;
    @Resource
    MemberLevelImgServiceImpl memberLevelImgServiceImpl;

    @Autowired
    AuthUtils authUtils;
    @Autowired
    CommonUtitls commonUtitls;
    /**
     * 查询全部
     *
     * @param params
     * @return Result
     */
    @Override
    public List<MemberLevelEntity> all(Map<String, Object> params) {
        return memberLevelDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<MemberLevelEntity> page = new Query<MemberLevelEntity>().getPage(params, "id", true);
        List<MemberLevelEntity> records = memberLevelDao.page(page, params);
        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 保存
     *
     * @param  memberLevel
     * @return boolean
     */
    @Override
    public boolean save(MemberLevelEntity memberLevel) {
        try {
            if (StringUtils.isBlank(memberLevel.getId())){
                memberLevel.setId(commonUtitls.createKey());
                memberLevel.setCreateBy(authUtils.AuthUser().getUserId());
                memberLevel.setCreateTime(new Date());
                super.save(memberLevel);
            }else{
                memberLevel.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevel.setUpdateTime(new Date());
                this.updateById(memberLevel);
            }
            //保存商铺图片
            List<MemberLevelImgEntity> imgList = memberLevel.getImgList();
            if (imgList != null) {
                for (MemberLevelImgEntity img : imgList) {
                    if (StringUtils.isBlank(img.getId())) {
                        img.setLevelId(memberLevel.getId());
                        memberLevelImgServiceImpl.save(img);
                    } else if (Objects.equals("", img.getImgurl())) {
                        memberLevelImgServiceImpl.remove(img.getId());
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    /**
     * 详情
     *
     * @return
     */
    @Override
    public MemberLevelEntity detail(String id) {
        MemberLevelEntity memberLevel = memberLevelDao.selectById(id);
        if(memberLevel!=null){
            List<MemberLevelImgEntity> imgList = memberLevelImgDao.queryByLevelId(memberLevel.getId());
            memberLevel.setImgList(imgList);
        }
        return memberLevel;
    }

    /**
     * 删除
     *
     * @param id
     * @return Result
     */
    @Override
    public boolean remove(String id) {
        try {
            MemberLevelEntity removeEntity = memberLevelDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            memberLevelDao.updateById(removeEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return true;

    }

    /**
      * 批量删除
      *
      * @param ids
      * @return boolean
      */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatch(String[] ids) {

        try {
            for (String id : ids) {
                MemberLevelEntity removeEntity = memberLevelDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                memberLevelDao.updateById(removeEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }


    /**
      * 导入
      *
      * @param file
      * @return boolean
      */
    @Override
    public Result upload(MultipartFile file) throws IOException {
        try {
            MemberLevelListener listener = new MemberLevelListener();
            EasyExcel.read(file.getInputStream(), MemberLevelEntity.class, listener).sheet().doRead();
            //获取数据
            List<MemberLevelEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                memberLevelDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
