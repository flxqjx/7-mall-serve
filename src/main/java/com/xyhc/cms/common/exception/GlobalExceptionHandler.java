package com.xyhc.cms.common.exception;

import com.xyhc.cms.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, BusinessException e){
        System.err.println("未知异常！原因是:"+e.getErrorMessage());
        return Result.error(e.getErrorCode(),e.getErrorMessage());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        System.err.println("未知异常！原因是:"+e);
        return Result.error(CommonEnum.NULLException);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        System.err.println("未知异常！原因是:"+e);
        return Result.error(CommonEnum.INTERNAL_SERVER_ERROR.getResultCode(),e.getMessage());
    }
}