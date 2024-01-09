package com.xyhc.cms.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyhc.cms.dao.CouponUsersDao;
import com.xyhc.cms.dao.CouponDao;
import com.xyhc.cms.dao.*;

import com.xyhc.cms.dao.MemberLevelDao;
import com.xyhc.cms.dao.WechatDao;
import com.xyhc.cms.entity.*;
import com.xyhc.cms.service.CouponReceiveService;
import com.xyhc.cms.service.CouponUsersService;
import com.xyhc.cms.service.WechatService;
import com.xyhc.cms.service.IntegralLogService;
import com.xyhc.cms.utils.DictUtils;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xyhc.cms.common.page.PageUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import com.xyhc.cms.config.auth.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xyhc.cms.common.page.Query;
import org.apache.commons.lang.StringUtils;
import java.util.Date;
import com.xyhc.cms.common.Result;
import com.xyhc.cms.utils.CommonUtitls;
import com.alibaba.excel.EasyExcel;
import com.xyhc.cms.listener.CouponUsersListener;
import java.math.BigDecimal;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
/**
 * 优惠券领取Service实现类
 *
 * @author apollo
 * @since 2023-04-19 15:39:22
 */
@Service("couponUsersService")
public class CouponUsersServiceImpl extends ServiceImpl<CouponUsersDao, CouponUsersEntity> implements CouponUsersService {
  /*  @Resource
    CouponEntity coupon;*/

    @Resource
    BlockDao blockDao;
    @Resource
    CouponReceiveDao couponReceiveDao;

    @Autowired
    DictUtils dictUtils;

    @Resource
    WechatService wechatService;
    @Resource
    CouponReceiveService couponReceiveService;
    @Resource
    IntegralLogService integralLogService;

    @Resource
    WechatDao wechatDao;
    @Resource
    CouponUsersDao  couponUsersDao;

    @Resource
    CouponDao  couponDao;

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
    public List<CouponUsersEntity> all(Map<String, Object> params) {
        return couponUsersDao.all(params);
    }


    /**
     * 分页查询
     *
     * @param params
     * @return PageUtils
     */
    @Override
    public PageUtils page(Map<String, Object> params) {
        IPage<CouponUsersEntity> page = new Query<CouponUsersEntity>().getPage(params, "id", true);
        List<CouponUsersEntity> records = couponUsersDao.page(page, params);

        Map<String, Object> couponTypeList = dictUtils.dictList("couponType");

        records.forEach(item -> {

            if (item.getCouponType() != null) {
                if (couponTypeList.get(item.getCouponType()) != null) {
                    item.setCouponType(couponTypeList.get(item.getCouponType()).toString());
                }
            }

            QueryWrapper<Wechat> wechatQuery = new QueryWrapper<>();
            wechatQuery.eq("id", item.getReceiveUid());
            Wechat wechat = wechatDao.selectOne(wechatQuery);
            if(wechat!=null){
                item.setNickName(wechat.getNickName());
            }
            else{
                item.setNickName("");
            }

            if(item.getIsGive()!=null ){
                if(item.getIsGive()==1){
                    item.setIsGiveState("已赠送");
                }
                else if(item.getIsGive()==0){
                    item.setIsGiveState("未赠送");
                }
                item.setIsGiveState("已赠送");
            }
            else{
                item.setIsGiveState("未赠送");
            }
        });

        page.setRecords(records);
        return new PageUtils(records, ((int) page.getTotal()), ((int) page.getSize()), ((int) page.getCurrent()));
    }

    /**
     * 兑换优惠券
     *
     * @param  couponUsersEntity
     * @return boolean
     */
    @Override
    public Result saveUsers(CouponUsersEntity couponUsersEntity) {
        try {
            if (StringUtils.isBlank(couponUsersEntity.getId())) {
                //查询当前用户积分余额
                Wechat wechat = wechatDao.selectById(authUtils.AuthUser().getUserId());
                CouponEntity coupon = couponDao.selectById(couponUsersEntity.getCouponId());
                if(wechat !=null) {
                    if (coupon != null) {
                        //判断余额是否够兑换
                        if (wechat.getIntegralMoney() < coupon.getCouponPrice()) {
                            return Result.error("余额不足，不能兑换此优惠券");
                        }
                    }
                }
                couponUsersEntity.setId(commonUtitls.createKey());
                couponUsersEntity.setCreateTime(new Date());
                couponUsersEntity.setIntegral((int)coupon.getCouponPrice());
                couponUsersEntity.setIntegralCodeUser(coupon.getIntegralCodetemp());
                super.save(couponUsersEntity);

                //领取表加数据
                CouponReceiveEntity couponReceiveEntity = new CouponReceiveEntity();
                couponReceiveEntity.setUid(authUtils.AuthUser().getUserId());
                couponReceiveEntity.setCouponId(couponUsersEntity.getCouponId());
                couponReceiveEntity.setCreateBy(authUtils.AuthUser().getUserId());
                couponReceiveService.save(couponReceiveEntity);

                //用户积分减少
                UpdateWrapper<Wechat> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", wechat.getId());
                wrapper.set("integral_money", wechat.getIntegralMoney()-coupon.getCouponPrice());
                wechatDao.update(null, wrapper);
                //log记录
                IntegralLogEntity integralLog = new IntegralLogEntity();
                integralLog.setXfBalance((int)coupon.getCouponPrice());
                integralLog.setBeforeBalane(wechat.getIntegralMoney());
                integralLog.setAfterBalance((int)wechat.getIntegralMoney()-(int)coupon.getCouponPrice());
                integralLog.setCreateBy(authUtils.AuthUser().getUserId());
                integralLog.setUserId(authUtils.AuthUser().getUserId());
                integralLog.setOrderId(couponUsersEntity.getId());
                integralLog.setXfClassify("POINTS_COUPON");
                integralLog.setXfRemark("积分兑换积分优惠券");
                integralLog.setXfAccount(1);
                integralLogService.save(integralLog);

            } else {
//                couponUsersEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                couponUsersEntity.setUpdateTime(new Date());
                this.updateById(couponUsersEntity);
            }
            return Result.success(couponUsersEntity.getId());
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }



    //public Result  save(CouponUsersEntity couponUsers,String couponId) {
//      public Result  saveresult(CouponUsersEntity couponUsers) {
//        try {
//            //先判断USER表中某优惠券某用户是否已存在 已存在就
//            //减少wechat表该下单用户的积分-integral_money字段
//            //查询当前人
//            Wechat wechat = wechatService.detail(authUtils.AuthUser().getUserId());
//            if (wechat != null) {
//                QueryWrapper level = new QueryWrapper<>();
//                level.eq("id", couponUsers.getCouponId());
//                //level.eq("id", couponId);//原先就有注销 没有用couponId
//                CouponEntity coupon = couponDao.selectOne(level);
//                //BLOCK_0040中的优惠券和积分的兑换比例
//                /*QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("block", "BLOCK_0040");
//                SysBlock boss = blockDao.selectOne(queryWrapper);
//*/
//
//               // if (boss != null) {
//                   // BigDecimal ratio = new BigDecimal(boss.getRemark());
//                    //double shareMoney = (orderResult.getCommodityTotal().doubleValue() * Double.parseDouble(boss.getRemark())) / 100;
//                    //int exchangeratio= Integer.parseInt(boss.getRemark());
//               //
//
//
//               // if (coupon.getCouponPrice().doubleValue()*exchangeratio<= wechat.getIntegralMoney()) {//原来用BLOCK比例代码
//                //if (couponUsers.getCouponPrice().doubleValue()*20<= wechat.getIntegralMoney()) {
//                if (coupon.getCouponPrice().doubleValue()<= wechat.getIntegralMoney()) {
//
//                    //查变更前的金额
//                    double beforeBalane = wechat.getIntegralMoney();
//                    //查变更后的金额
//                    double afterIntegralMoney = wechat.getIntegralMoney() - coupon.getCouponPrice().doubleValue();//@
//                    //double afterIntegralMoney = wechat.getIntegralMoney() - couponUsers.getCouponPrice().doubleValue()*20;//@
//                    //更新用户购物之后的总金额
//                    UpdateWrapper<Wechat> wrapperTotalIntegralMoney = new UpdateWrapper<>();
//                    wrapperTotalIntegralMoney.eq("id", wechat.getId());
//                    wrapperTotalIntegralMoney.set("integral_money", afterIntegralMoney);//@
//                    wechatDao.update(null, wrapperTotalIntegralMoney);
//
//                    if (StringUtils.isBlank(couponUsers.getId())){
//                        //if (StringUtils.isBlank(couponUsers.getCouponId())){
//                        couponUsers.setId(commonUtitls.createKey());
//                        couponUsers.setReceiveUid(authUtils.AuthUser().getUserId());
//                        couponUsers.setCreateTime(new Date());
//                        couponUsers.setIntegralCodeUser(commonUtitls.createKey());
//                        super.save(couponUsers);
//                        //this.save(couponUsers);
//
//                        //写进积分流水记录表
//                        IntegralLogEntity integralLog = new IntegralLogEntity();
//                        //integralLog.setXfBalance(pointsmallOrderEntity.getTotal().doubleValue());
//                        //integralLog.setXfBalance(coupon.getCouponPrice().intValue()*exchangeratio);//
//                        integralLog.setXfBalance(coupon.getCouponPrice().intValue());
//
//                        integralLog.setBeforeBalane(wechat.getIntegralMoney());
//                        //integralLog.setBeforeBalane(beforeBalane.intValue());
//
//                        //integralLog.setAfterBalance(wechat.getIntegralMoney() + pointsmallOrderEntity.getTotal().doubleValue());
//                        //integralLog.setAfterBalance(wechat.getIntegralMoney() - coupon.getCouponPrice().intValue()*exchangeratio);
//                        integralLog.setAfterBalance(wechat.getIntegralMoney() - coupon.getCouponPrice().intValue());
//                        // integralLog.setAfterBalance(afterIntegralMoney.intValue());
//
//                        integralLog.setCreateBy(authUtils.AuthUser().getUserId());
//                        integralLog.setUserId(authUtils.AuthUser().getUserId());
//                        //integralLog.setOrderId(pointsmallOrderEntity.getId());
//                        integralLog.setOrderId(createNo());
//
//                        integralLog.setXfClassify("POINTS_COUPON");
//                        integralLog.setXfRemark("积分兑换积分优惠券");
//                        integralLog.setXfAccount(1);
//                        integralLogService.save(integralLog);
//                    }else{
//                        // couponUsers.setUpdateBy(authUtils.AuthUser().getUserId());
//                        //couponUsers.setUpdateTime(new Date());
//                        //this.updateById(couponUsers);
//                    }
//
//                } else {
//                    return Result.error("notenough");//积分不足
//                    //return false;
//                }
//                    //对应boss
//                /*}else{
//                    return Result.error("hasnoblock");
//                }*/
//            }
//
//
//        } catch (Exception ex) {
//            throw ex;
//        }
//        //return true;
//        return Result.success();
//    }

    /**
     * 生成单号
     *
     * @return
     */
    public String createNo() {
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
     * 获取数据库中最大编号
     *
     * @return
     */
    public long maxNo() {
        try {
            String minDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date(f.parse(minDate).getTime() + 24 * 3600 * 1000);
            String maxDate = f.format(d);
            //List<PointsmallOrderEntity> scheduleList = pointsmallOrderDao.selectList(new QueryWrapper<PointsmallOrderEntity>().gt("create_time", minDate).lt("create_time", maxDate));


              List<CouponUsersEntity> scheduleList = couponUsersDao.selectList(new QueryWrapper<CouponUsersEntity>().gt("create_time", minDate).lt("create_time", maxDate));

            return scheduleList.stream().count();
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 通过复制办法得到券码后，进行兑换
     *
     * @param  couponUsers   5566
     * @return boolean
     */
    @Override
    //public boolean exchangecode(String integralcode,CouponUsersEntity couponUsers) {
    public Result exchangecode(CouponUsersEntity couponUsers) {
        try {

            QueryWrapper level = new QueryWrapper<>();
            level.eq("integralcodeuser", couponUsers.getIntegralCodeUser());
            level.eq("is_delete", 0);

            CouponUsersEntity couponuser = couponUsersDao.selectOne(level);

            //if (StringUtils.isBlank(couponuser.getId())){
            //if (couponuser.getId()==null){
            if (couponuser==null){
                return Result.error("nodata");

            }
            else{
              if(couponuser.getIsGive() !=null) {
                  return Result.error("isgive");
                }
                else{
                   /* else if(couponuser.getIsUse()==1){

                return Result.error("isuse");
            }*/

                  //先看该券是否过期

                  QueryWrapper level2 = new QueryWrapper<>();
                  //level.eq("integralcode", integralcode);
                  level2.eq("id", couponUsers.getCouponId());
                  level2.eq("is_delete", 0);
                  CouponEntity coupon = couponDao.selectOne(level2);
               /* if(coupon.getEndTime()<new Date()){

                    return Result.error("isexpire");
                }*/


                  QueryWrapper level3 = new QueryWrapper<>();
                  //level.eq("integralcode", integralcode);
                  level3.eq("id", authUtils.AuthUser().getUserId());
                  level3.eq("is_delete", 0);
                  Wechat wechat = wechatDao.selectOne(level3);

                  //if (StringUtils.isBlank(couponUsers.getCouponId())){
                  //couponuser.setId(couponUsers.getId());
                  couponuser.setGiveUserId(authUtils.AuthUser().getUserId());

                  couponuser.setGiveUser(wechat.getNickName());
                  couponuser.setGiveDateTime(new Date());
                  couponuser.setIsGive(1);
                  couponuser.setGiveType("复制券码获得");
                  //super.save(couponUsers);
                  this.updateById(couponuser);

                  }

              }

        } catch (Exception ex) {
            throw ex;
        }
        return Result.success();
    }

    /**
     * 通过分享链接办法得到优惠券4445555666
     *
     * @param  couponUsers
     * @return boolean
     */
    //public String datelshare(CouponUsersEntity couponUsers) {
      public Result datelshare(CouponUsersEntity couponUsersEN) {
        try {

            QueryWrapper level = new QueryWrapper<>();
            level.eq("coupon_id", couponUsersEN.getCouponId());
            level.eq("receiveuid", couponUsersEN.getReceiveUid());
            level.eq("is_delete", 0);

            CouponUsersEntity couponusers = couponUsersDao.selectOne(level);

           // if (couponusers.getId()==null || couponusers.getId()=="") {
            //if (couponusers.getId()==null) {
            if (couponusers != null) {
                //如果COUPONUSER表中有某个ID记录


                //该优惠券已经赠送过人了，不能再赠送了！
                if (couponusers.getIsGive() !=null && couponusers.getIsGive()==1){
                    return Result.error("8");
                }

                //该优惠券已经赠送过人了，不能再赠送了！
                //if (!couponuser.getGiveuserid().equals("''")){
                if (couponusers.getGiveUser() != null ){
                    return Result.error("5");
                }

                //Receiveuid不为空 说明该积分兑换券已抵扣过积分,积分券必须先抵扣成功才能赠送给别人
                if (couponusers.getReceiveUid().equals("") && couponusers.getCouponType().equals("INTEGRAL")){
                    //return "3";
                    return Result.error("3");
                }


                // if (couponusers.getCouponType().equals("NEW") || couponusers.getCouponType().equals("COMMON")){
                //更新赠送人 时间等信息

                couponusers.setGiveUserId(authUtils.AuthUser().getUserId());
                couponusers.setGiveUser(authUtils.AuthUser().getUserName());
                couponusers.setGiveDateTime(new Date());
                couponusers.setIntegralCodeUser(commonUtitls.createKey());//积分兑换券券码
                couponusers.setGiveType("微信分享链接获得");
                couponusers.setIsGive(1);

                this.updateById(couponusers);
                // }

                //积分兑换券赠送
                /*if (couponUsersEN.getCouponType().equals("INTEGRAL")) {
                    couponUsersEN.setGiveuserid(authUtils.AuthUser().getUserId());
                    couponUsersEN.setGiveuser(authUtils.AuthUser().getUserName());
                    couponUsersEN.setGivedatetime(new Date());
                    couponUsersEN.setIsGive(1);
                    couponUsersEN.setGivetype("微信分享链接获得");
                    //super.save(couponUsers);
                    this.updateById(couponUsersEN);

                }*/

                //该券不是积分兑换券，不能兑换
                //if (couponuser.getSource().equals("积分兑换")){
               /* if (couponUsersEN.getCouponType()=="INTEGRAL"){
                   // return "2";
                }
                else{
                    return Result.error("2");
                }*/



                /*if ( couponuser.getGiveuserid() !=""){
                    return Result.error("5");
                }*/
               /* else{

                }*/

                //if (couponuser.getReceiveuid()==""){

                //if (StringUtils.isBlank(couponUsers.getCouponId())){
                //couponuser.setId(couponUsers.getId());

            }else{
                ////////下面是CONPONUSER表中找不到相关记录情况//////////////////////////////////////

                //如果是新人券或通用券,则在coupon_users表中生成一条记录 然后去更新；但积分券必须要在coupon_users表
                //有一条记录 表明已经用积分兑换过了
                //if (couponUsersEN.getCouponType() == "NEW" || couponUsersEN.getCouponType() == "COMMON"){
                if (couponUsersEN.getCouponType().equals("NEW") || couponUsersEN.getCouponType().equals("COMMON")){
                    couponUsersEN.setId(commonUtitls.createKey());
                    couponUsersEN.setCouponId(couponUsersEN.getCouponId());
                    couponUsersEN.setCouponType(couponUsersEN.getCouponType());
                    //优惠券的名称 类型 积分

                    couponUsersEN.setReceiveUid(couponUsersEN.getReceiveUid());
                    couponUsersEN.setGiveUserId(authUtils.AuthUser().getUserId());
                    couponUsersEN.setGiveUser(authUtils.AuthUser().getUserName());
                    couponUsersEN.setGiveDateTime(new Date());
                    couponUsersEN.setCreateTime(new Date());
                    couponUsersEN.setIntegralCodeUser(commonUtitls.createKey());//兑换券券码
                    couponUsersEN.setGiveType("微信分享链接获得");
                    couponUsersEN.setIsGive(1);

                    super.save(couponUsersEN);

                }
                /*else{
                    couponUsers.setGiveuserid(authUtils.AuthUser().getUserId());

                }*/
                //查无此券,两种可能 1 一条记录都没有 2只有COUPONID 没有receiveuid 也就是没有领取
                if (couponUsersEN.getCouponType() == "INTEGRAL"){

                    return Result.error("1");
                }

            }
        } catch (Exception ex) {
            throw ex;
        }
          return Result.success();
    }

    /**
     * 通过复制券码准备赠送给别人，此时先向COUPONUSERS表中插入一条记录   88889999
     *
     * @param  couponUsers
     * @return result
     */

    public Result copycode(CouponUsersEntity couponUsersEN) {
        try {
            QueryWrapper level = new QueryWrapper<>();

            //level.eq("integralcodeuser", couponUsersEN.getIntegralcodeuser());这个券码在未写入数据库中时是不停变动的，如果用了 会不停增加新数据

            level.eq("coupon_id", couponUsersEN.getCouponId());
            level.eq("receiveuid", couponUsersEN.getReceiveUid());
            level.eq("is_delete", 0);
            CouponUsersEntity couponusers = couponUsersDao.selectOne(level);

            if (couponusers != null) {
               if(couponusers.getIntegralCodeUser()==null){
                   couponusers.setIntegralCodeUser(couponUsersEN.getIntegralCodeUser());
                   this.updateById(couponusers);
               }
            }
            else{
                couponUsersEN.setId(commonUtitls.createKey());
                couponUsersEN.setTitle(couponUsersEN.getTitle());
                couponUsersEN.setCouponId(couponUsersEN.getCouponId());
                couponUsersEN.setCouponType(couponUsersEN.getCouponType());
                //优惠券的名称 类型 积分

                couponUsersEN.setReceiveUid(couponUsersEN.getReceiveUid());

                couponUsersEN.setCreateTime(new Date());
                //couponUsersEN.setIntegralcodeuser(couponUsersEN.getIntegralcodeuser());//兑换券券码



                super.save(couponUsersEN);
            }

        } catch (Exception ex) {
           throw ex;
        }
        return Result.success();
    }


    /**
     * 详情
     *
     * @return
     */
    @Override
    public CouponUsersEntity detail(String id) {
        return couponUsersDao.selectById(id);
    }


    public CouponUsersEntity detailbycouponID(String id) {//777788889999

        QueryWrapper level = new QueryWrapper<>();
        level.eq("coupon_id", id);
        //level.eq("id", couponUsers.getId());
        level.eq("is_delete", 0);
        level.eq("receiveuid", authUtils.AuthUser().getUserId());
        //CouponUsersEntity couponuser = couponUsersDao.selectOne(level);

        CouponUsersEntity couponuser = couponUsersDao.selectOne(level);

        return couponuser;

        /*if (StringUtils.isBlank(couponuser.getId())){

            return "";

        }else {

            return couponuser;
        }*/
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
            CouponUsersEntity removeEntity = couponUsersDao.selectById(id);
            removeEntity.setIsDelete(1);
            removeEntity.setUpdateTime(new Date());
//            removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
            couponUsersDao.updateById(removeEntity);
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
                CouponUsersEntity removeEntity = couponUsersDao.selectById(id);
                removeEntity.setIsDelete(1);
                removeEntity.setUpdateTime(new Date());
//                removeEntity.setUpdateBy(authUtils.AuthUser().getUserId());
                couponUsersDao.updateById(removeEntity);
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
            CouponUsersListener listener = new CouponUsersListener();
            EasyExcel.read(file.getInputStream(), CouponUsersEntity.class, listener).sheet().doRead();
            //获取数据
            List<CouponUsersEntity> uploadData = listener.getUploadData();

            // 非空验证

            uploadData.forEach(uploadItem -> {
                uploadItem.setId(commonUtitls.createKey());
//                uploadItem.setCreateBy(authUtils.AuthUser().getUserId());
                uploadItem.setCreateTime(new Date());
                couponUsersDao.insert(uploadItem);
            });
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
