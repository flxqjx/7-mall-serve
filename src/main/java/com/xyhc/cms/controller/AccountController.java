package com.xyhc.cms.controller;

import com.xyhc.cms.common.Result;
import com.xyhc.cms.config.auth.AuthUtils;
import com.xyhc.cms.service.WechatService;
import com.xyhc.cms.utils.SendMsgUtils;
import com.xyhc.cms.utils.WechatUtils;
import com.xyhc.cms.vo.common.WxJsapiSignatureVo;
import com.xyhc.cms.vo.system.AppLoginDto;
import com.xyhc.cms.vo.system.LoginRepDto;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * 登录
 *
 * @Author apollo
 */
@RestController
public class AccountController {


    @Autowired
    WechatService wechatService;

    @Autowired
    AuthUtils authUtils;


    @Autowired
    SendMsgUtils sendMsgUtils;

    @Resource
    WechatUtils wechatUtils;

    @Resource
    WxMpService wxMpService;


    /**
     * 注销
     *
     * @param
     * @return
     */
    @PostMapping("/account/logout")
    public Result logout() {
        try {
            return Result.success();
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 登录
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/getOpenId")
    public Result getOpenId(@RequestParam String code) throws Exception {
        try {
            return Result.success(wechatService.getOpenId(code));
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }


    /**
     * 登录
     *
     * @param
     * @return
     */
    @PostMapping("/account/app/login")
    public Result appLogin(@RequestBody AppLoginDto loginDto) throws Exception {
        try {

            LoginRepDto repDto = wechatService.appLogin(loginDto);
            if (repDto.getUserId().equals("-1")) {
                throw new Exception("登录失败");
            }
            if (repDto.getUserId().equals("-2")) {
                throw new Exception("账号冻结");
            }
            return Result.success(repDto);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    /**
     * 登录
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/userInfo")
    public Result userInfo() throws Exception {
        try {
            return Result.success(wechatService.userInfo());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    /**
     * 登录
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/mineInfo")
    public Result mineInfo() throws Exception {
        try {
            return Result.success(wechatService.mineInfo());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    /**
     * 小程序账号单条查询
     *
     * @param
     */
    @GetMapping("/wechat/wechatByVipNo")
    public Result wechatByVipNo(String vipNo) throws Exception {
        try {
            return Result.success(wechatService.wechatByVipNo(vipNo));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    /**
     * 小程序账号单条查询
     *
     * @param
     */
    @GetMapping("/wechat/wechatByScene")
    public Result wechatByScene(String vipNo) throws Exception {
        try {
            return Result.success(wechatService.wechatByScene(vipNo));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/infoByOpenId")
    public Result infoByOpenId(String openid) throws Exception {
        try {
            return Result.success(wechatService.infoByOpenId(openid));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 公众号登录
     *
     * @param
     * @return
     */
    @GetMapping("/account/app/wechatLogin")
    public Result wechatLogin(@RequestParam String code, String openid) throws Exception {
        try {
            LoginRepDto repDto = wechatService.wechatLogin(code, openid);
            if (repDto.getUserId().equals("-1")) {
                throw new Exception("登录失败");
            }
            if (repDto.getUserId().equals("-2")) {
                throw new Exception("账号冻结");
            }
            return Result.success(repDto);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    /**
     * 获取公众号认证
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/configSignature")
    public Result configSignature(@RequestParam("localUrl") String localUrl) throws WxErrorException, WxErrorException, UnsupportedEncodingException {
        localUrl = URLDecoder.decode(localUrl, "UTF-8");
        String currentUrl = localUrl;
        currentUrl = currentUrl.split("＃")[0];
        WxJsapiSignatureVo wxJsapiSignatureVo = new WxJsapiSignatureVo();
        WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(currentUrl);
//        WxJsapiSignatureVo wxJsapiSignatureVo = wechatUtils.createTicketSign(currentUrl);
        BeanUtils.copyProperties(jsapiSignature, wxJsapiSignatureVo);
        System.out.println(JSONObject.fromObject(wxJsapiSignatureVo));
        return Result.success(wxJsapiSignatureVo);
    }

    /**
     * 解密用户手机号
     *
     * @return RestResponse
     */
    @RequestMapping("/wechat/enPhoneNumber")
    public Result enPhoneNumber(@RequestParam String sessionKey, String ivData, String encryptedData) {
        try {
            return Result.success(wechatService.enPhoneNumber(sessionKey, ivData, encryptedData));
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

    /**
     * 获取 sessionKey
     *
     * @param
     * @return
     */
    @GetMapping("/wechat/sessionKey")
    public Result sessionKey(@RequestParam String code) throws Exception {
        try {
            return Result.success(wechatService.sessionKey(code));
        } catch (Exception ex) {
            return Result.error(ex.getMessage());
        }
    }

}

