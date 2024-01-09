package com.xyhc.cms.config.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
@Component
public class AuthUtils {
    /**
     * 获取userCode
     *
     * @param
     * @return
     */
    public AuthInfo AuthUser() {
        try {

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
                        .getRequest();
                AuthInfo authInfo = new AuthInfo();
                authInfo.setUserId(request.getHeader("userId"));
                authInfo.setUserAccount(request.getHeader("userAccount"));
                authInfo.setUserName(request.getHeader("userName"));
                authInfo.setVendorId(request.getHeader("vendorId"));
                authInfo.setUid(request.getHeader("uid"));
                return authInfo;
            } else {
                throw new RuntimeException("token不合法 userCode");
            }
        } catch (Exception ex) {
            throw ex;
        }
    }


    public String authToken() {
        try {

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
                        .getRequest();
                return request.getHeader("authToken");
            } else {
                return "";
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}
