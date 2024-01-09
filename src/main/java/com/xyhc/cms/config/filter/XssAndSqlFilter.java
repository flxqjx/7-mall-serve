package com.xyhc.cms.config.filter;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.xyhc.cms.config.auth.ResultInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

//import org.apache.commons.lang3.StringUtils;

public class XssAndSqlFilter implements Filter {

    private Object JSONResponseUtil;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = "GET";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = this.getBodyString(xssRequest.getReader());
            if (StringUtils.isNotBlank(param)) {
                if (xssRequest.checkXSSAndSql(param)) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    ResultInfo resultInfo = new ResultInfo();
                    resultInfo.setCode(HttpStatus.HTTP_BAD_GATEWAY);
                    resultInfo.setMsg("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!");
                    String jsonStr = JSON.toJSONString(resultInfo);
                    out.write(jsonStr);
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setCode(HttpStatus.HTTP_BAD_GATEWAY);
            resultInfo.setMsg("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!");
            String jsonStr = JSON.toJSONString(resultInfo);
            out.write(jsonStr);
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    // 获取request请求body中参数
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;

    }

}