package org.alvin.home.v3.code.exception;


import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.beans.RestApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * <p>
 * 规范：流程跳转尽量避免使用抛 BizException 来做控制。
 *
 * @author daoshenzzg@163.com
 * @date 2019-09-06 18:02
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public RestApiResult handleBizException(BizException ex) {
        log.error(ex.getMessage(), ex);
        RestApiResult.RestApiResultBuilder result = RestApiResult.builder().code(ex.getCode());
        if (Strings.isNullOrEmpty(ex.getErrMsg())) {
            result.errmsg(ex.getErrMsg());
        }
        return result.build();
    }

    /**
     * 参数绑定错误
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public RestApiResult handleBindException(BindException ex) {
        log.error(ex.getMessage(), ex);
        String msg = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(";"));
        return RestApiResult.builder().code(500).errmsg(msg).build();
    }

    /**
     * 参数校验错误
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ValidationException.class)
    public RestApiResult handleValidationException(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return RestApiResult.builder().code(500).errmsg(ex.getCause().getMessage()).build();
    }

    /**
     * 字段校验不通过异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        String msg = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(";"));
        return RestApiResult.builder().code(500).errmsg(msg).build();
    }

    /**
     * Controller参数绑定错误
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestApiResult handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage(), ex);
        return RestApiResult.builder().code(500).errmsg(ex.getCause().getMessage()).build();
    }

    /**
     * 处理方法不支持异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public RestApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return RestApiResult.builder().code(500).errmsg("服务器错误").build();
    }

    /**
     * 其他未知异常
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public RestApiResult handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return RestApiResult.builder().code(500).errmsg("服务器错误").build();
    }


}