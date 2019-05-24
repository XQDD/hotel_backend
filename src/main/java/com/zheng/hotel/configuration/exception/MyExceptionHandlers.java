package com.zheng.hotel.configuration.exception;

import com.zheng.hotel.dto.Result;
import com.zheng.hotel.repository.PermissionLongRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//同意错误处理类
@RestControllerAdvice
@RestController
@Slf4j
public class MyExceptionHandlers extends AbstractErrorController {


    private final ErrorAttributes errorAttributes;
    private final ServerProperties serverProperties;
    private final HttpServletRequest request;
    private final PermissionLongRepository permissionRepository;

    public MyExceptionHandlers(ErrorAttributes errorAttributes, ServerProperties serverProperties, HttpServletRequest request, PermissionLongRepository permissionRepository) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
        this.serverProperties = serverProperties;
        this.request = request;
        this.permissionRepository = permissionRepository;
    }


    /**
     * 方法校验错误
     *
     * @param e 错误
     * @return 错误结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Map<String, String>>> handleApiConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(c -> errors.put(StreamSupport.stream(c.getPropertyPath().spliterator(), false).skip(1).map(Path.Node::getName).collect(Collectors.joining(".")), c.getMessage()));
        return Result.badRequest(errors);
    }

    //普通参数校验错误,json参数校验错误
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Result<Map<String, String>>> validExceptionHandler(Exception e) {
        BindingResult result;
        if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();
        } else {
            result = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach((fe) -> errors.put(fe.getField(), fe.getDefaultMessage()));
        return Result.badRequest(errors);
    }


    @ExceptionHandler({
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<Result> formatError(HttpMessageNotReadableException e) {
        log.warn("请求url：【" + request.getRequestURL() + "】，客户端错误：", e);
        return Result.badRequest("参数格式有误");
    }


    //参数转化null错误
    @ExceptionHandler({IllegalStateException.class})
    public Object numberError(IllegalStateException e) {
        if (e.getMessage().contains("is present but cannot be translated into a null")) {
            return ResponseEntity.badRequest().body(Collections.singletonMap(e.getMessage().split("'")[1], "不能为空"));
        }
        return ResponseEntity.badRequest().body(getErrors(e));
    }


    //权限错误
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity authorizationException(AuthorizationException e) {
        var permission = StringUtils.substringBetween(e.getMessage(),"[", "]");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error("操作失败，无"+permissionRepository.findNameByPermission(permission)+"权限"));
    }


    @RequestMapping(value = "error", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status.is5xxServerError()) {
            Throwable error = errorAttributes.getError(new ServletWebRequest(request) {
            });
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器发生未知错误：" + getErrors(error));
        } else {
            return ResponseEntity.status(status).body(getErrorAttributes(request, isIncludeStackTrace(request)));
        }
    }


    @Override
    public String getErrorPath() {
        return "error";
    }

    private String getErrors(Throwable throwable) {
        List<String> errors = new ArrayList<>();
        while (throwable != null) {
            errors.add(throwable.getMessage());
            throwable = throwable.getCause();
        }
        return String.join("--------------->", errors);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request the source request
     * @return if the stacktrace attribute should be included
     */
    private boolean isIncludeStackTrace(HttpServletRequest request) {
        IncludeStacktrace include = serverProperties.getError().getIncludeStacktrace();
        if (include == IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }


    //业务异常（视为客户端错误）
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity numberError(BusinessException e) {
        return e.getResponseEntity();
    }

}


