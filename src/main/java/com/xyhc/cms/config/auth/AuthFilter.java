package com.xyhc.cms.config.auth;


import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/***************
 * token认证
 * @author xushuang
 * @time 2021-06-23
 */
@WebFilter(urlPatterns = { "*"}, filterName = "authFilter")
@Slf4j
public class AuthFilter implements Filter {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
                    "/taskInfo/all".toLowerCase(),
                    "/block/detail".toLowerCase(),
                    "/taskClassify/all".toLowerCase(),
                    "/notice/all".toLowerCase(),
                    "/vipOrder/all".toLowerCase(),
                    "/course/page".toLowerCase(),
                    "/shortcut/all".toLowerCase(),
                    "/activity/endActivity".toLowerCase(),
                    "/commodity/all".toLowerCase(),
                    "/video/all".toLowerCase(),
                    "/wx/responseUnifiedOrder".toLowerCase(),
                    "/user/register".toLowerCase(),
                    "/user/login".toLowerCase(),
                    "/user/sendVerifiCode".toLowerCase(),
                    "/user/verifiCode".toLowerCase(),
                    "/user/updateForgetPwd".toLowerCase(),
                    "/pay/dougongTaskPayNotify".toLowerCase(),
                    "/account/app/login".toLowerCase(),
                    "/wechat/getOpenId".toLowerCase(),
                    "/wechat/getTTOpenId".toLowerCase(),
                    "/timeLimit/detail".toLowerCase(),
                    "/sysBlock/indexBanner".toLowerCase(),
                    "/sysBlock/typeBanner".toLowerCase(),
//                    "/wechat/userInfoVip".toLowerCase(),
                    "/information/all".toLowerCase(),
                    "/wechat/infoByOpenId".toLowerCase(),
                    "/commodity/detail".toLowerCase()

            )));

    @Autowired
    private TokenUtils tokenUtil;

    /**
     * auth filter
     *
     * @param
     * @return
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        ResultInfo resultInfo = new ResultInfo();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        rep.setHeader("Access-Control-Allow-Credentials", "true");
        rep.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        rep.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Access-Control-Allow-Headers", "authtoken,uid,Origin, X-Requested-With, Content-Type, Accept");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        // 检查不需要 filter 的接口
        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
        AtomicBoolean allowedPath = new AtomicBoolean(false);
        ALLOWED_PATHS.forEach(item -> {
            if (path.toLowerCase().contains(item)) {
                allowedPath.set(true);
            }
        });
        String requestPath = req.getServletPath();
        if (allowedPath.get()) {
            chain.doFilter(requestWrapper, response);
        } else {
            RequestAttributes reqMethod = RequestContextHolder.getRequestAttributes();
            if (reqMethod != null) {
                resultInfo = valateToken(reqMethod, request, resultInfo, rep, requestWrapper);
            } else {
                resultInfo.setCode(HttpStatus.HTTP_UNAUTHORIZED);
                resultInfo.setMsg("认证失败");
            }
            if (resultInfo.getCode() == HttpStatus.HTTP_UNAUTHORIZED) {
                PrintWriter writer = null;
                try (OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(),
                        "UTF-8")) {
                    writer = new PrintWriter(osw, true);
                    String jsonStr = JSON.toJSONString(resultInfo);
                    writer.write(jsonStr);
                    writer.flush();
                    writer.close();
                    osw.close();
                } catch (UnsupportedEncodingException e) {
                    log.error("过滤器返回信息失败:" + e.getMessage(), e);
                } catch (IOException e) {
                    log.error("过滤器返回信息失败:" + e.getMessage(), e);
                }
                return;
            }

            if (resultInfo.getCode() == HttpStatus.HTTP_OK) {
                chain.doFilter(requestWrapper, response);
            }
        }

    }


    public ResultInfo valateToken(RequestAttributes reqMethod, ServletRequest request, ResultInfo resultInfo, HttpServletResponse rep, HeaderMapRequestWrapper requestWrapper) {

        // 接收 token
        HttpServletRequest requestFilter = ((ServletRequestAttributes) reqMethod)
                .getRequest();
        String accessToken = requestFilter.getHeader("authToken");

        String method = ((HttpServletRequest) request).getMethod();
        if ("OPTIONS".equals(method)) {
            rep.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resultInfo.setCode(HttpStatus.HTTP_NO_CONTENT);
            requestWrapper.addHeader("userCode", "");
            resultInfo.setMsg("认证成功");
        } else {

            if (null == accessToken || accessToken.isEmpty()) {
                resultInfo.setCode(HttpStatus.HTTP_UNAUTHORIZED);
                resultInfo.setMsg("认证失败");
            } else {
                accessToken = accessToken.replace("Bearer ", "").trim();
                // token 解析userCode\
                try {
                    AuthInfo authInfo = tokenUtil.volidateToken(accessToken);
                    if (authInfo!=null) {
                        resultInfo.setCode(HttpStatus.HTTP_OK);
                        requestWrapper.addHeader("userId", authInfo.getUserId());
                        requestWrapper.addHeader("userAccount", authInfo.getUserAccount());
                        requestWrapper.addHeader("userName", authInfo.getUserName());
                        requestWrapper.addHeader("vendorId", authInfo.getVendorId());
                        resultInfo.setMsg("认证成功");
                    } else {
                        resultInfo.setCode(HttpStatus.HTTP_UNAUTHORIZED);
                        resultInfo.setMsg("认证失败");
                    }
                } catch (Exception exception) {
                    resultInfo.setCode(HttpStatus.HTTP_UNAUTHORIZED);
                    resultInfo.setMsg(exception.getMessage());
                }

            }
        }

        return resultInfo;
    }


}
