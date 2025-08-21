package com.kisesaki.blog.common.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.common.enums.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 * 统一处理系统中的各种异常，返回标准的错误响应格式
 * 
 * @author KiseSaki
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     * 
     * @param e       业务异常对象
     * @param request HTTP请求对象
     * @return 包含错误信息的API响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.warn("业务异常: {} - {}", e.getErrorCode().getCode(), e.getMessage(), e);
        logRequestInfo(request, e);

        ApiResponse<Void> response = ResultUtils.error(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(getHttpStatus(e.getErrorCode())).body(response);
    }

    /**
     * 处理参数校验异常 - @Valid 注解校验失败
     * 
     * @param e       方法参数校验异常对象
     * @param request HTTP请求对象
     * @return 包含校验错误详情的API响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.warn("参数校验异常: {}", e.getMessage());
        logRequestInfo(request, e);

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        String message = "参数校验失败: " + errors.values().stream()
                .collect(Collectors.joining(", "));

        ApiResponse<Map<String, String>> response = ResultUtils.error(ErrorCode.VALIDATION_FAILED, message, errors);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理绑定异常 - @ModelAttribute 校验失败
     * 
     * @param e       绑定异常对象
     * @param request HTTP请求对象
     * @return 包含绑定错误详情的API响应
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(
            BindException e, HttpServletRequest request) {
        logger.warn("绑定异常: {}", e.getMessage());
        logRequestInfo(request, e);

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        });

        String message = "参数绑定失败: " + errors.values().stream()
                .collect(Collectors.joining(", "));

        ApiResponse<Map<String, String>> response = ResultUtils.error(ErrorCode.VALIDATION_FAILED, message, errors);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理约束校验异常 - @Validated 校验失败
     * 
     * @param e       约束校验异常对象
     * @param request HTTP请求对象
     * @return 包含约束校验错误详情的API响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        logger.warn("约束校验异常: {}", e.getMessage());
        logRequestInfo(request, e);

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errors.put(fieldName, violation.getMessage());
        }

        String message = "参数校验失败: " + errors.values().stream()
                .collect(Collectors.joining(", "));

        ApiResponse<Map<String, String>> response = ResultUtils.error(ErrorCode.VALIDATION_FAILED, message, errors);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理Spring Security权限不足异常
     * 
     * @param e       权限拒绝异常对象
     * @param request HTTP请求对象
     * @return 包含权限错误信息的API响应，HTTP状态码403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        logger.warn("权限不足异常: {}", e.getMessage());
        logRequestInfo(request, e);

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 处理认证异常
     * 根据异常类型返回不同的错误码：
     * - BadCredentialsException: 登录失败
     * - InsufficientAuthenticationException: 未授权
     * 
     * @param e       认证异常对象
     * @param request HTTP请求对象
     * @return 包含认证错误信息的API响应，HTTP状态码401
     */
    @ExceptionHandler({ BadCredentialsException.class, InsufficientAuthenticationException.class })
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            Exception e, HttpServletRequest request) {
        logger.warn("认证异常: {}", e.getMessage());
        logRequestInfo(request, e);

        ErrorCode errorCode = e instanceof BadCredentialsException
                ? ErrorCode.LOGIN_FAILED
                : ErrorCode.UNAUTHORIZED;

        ApiResponse<Void> response = ResultUtils.error(errorCode);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 处理404异常 - 接口不存在
     * 
     * @param e       404异常对象
     * @param request HTTP请求对象
     * @return 包含404错误信息的API响应，HTTP状态码404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {
        logger.warn("404异常: {} {}", e.getHttpMethod(), e.getRequestURL());
        logRequestInfo(request, e);

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.NOT_FOUND,
                "请求的接口不存在: " + e.getHttpMethod() + " " + e.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理405异常 - 请求方法不支持
     * 
     * @param e       请求方法不支持异常对象
     * @param request HTTP请求对象
     * @return 包含405错误信息和支持方法列表的API响应，HTTP状态码405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        logger.warn("405异常: 不支持的请求方法 {}", e.getMethod());
        logRequestInfo(request, e);

        String supportedMethods = String.join(", ", e.getSupportedMethods());
        String message = String.format("不支持的请求方法 %s，支持的方法: %s", e.getMethod(), supportedMethods);

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.METHOD_NOT_ALLOWED, message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * 处理请求体解析异常
     * 通常由JSON格式错误或Content-Type不匹配引起
     * 
     * @param e       HTTP消息不可读异常对象
     * @param request HTTP请求对象
     * @return 包含请求体解析错误信息的API响应，HTTP状态码400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.warn("请求体解析异常: {}", e.getMessage());
        logRequestInfo(request, e);

        String message = "请求体格式错误，请检查JSON格式是否正确";
        ApiResponse<Void> response = ResultUtils.error(ErrorCode.PARAM_ERROR, message);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理参数类型不匹配异常
     * 当URL路径参数或请求参数类型转换失败时触发
     * 
     * @param e       方法参数类型不匹配异常对象
     * @param request HTTP请求对象
     * @return 包含参数类型错误信息的API响应，HTTP状态码400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        logger.warn("参数类型不匹配异常: {}", e.getMessage());
        logRequestInfo(request, e);

        String message = String.format("参数 '%s' 类型错误，期望类型: %s",
                e.getName(), e.getRequiredType().getSimpleName());

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.PARAM_ERROR, message);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理文件上传大小超限异常
     * 
     * @param e       文件上传大小超限异常对象
     * @param request HTTP请求对象
     * @return 包含文件大小错误信息的API响应，HTTP状态码413
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        logger.warn("文件上传大小超限: {}", e.getMessage());
        logRequestInfo(request, e);

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.FILE_TOO_LARGE);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    /**
     * 处理运行时异常
     * 作为兜底异常处理器，避免暴露内部异常信息给客户端
     * 
     * @param e       运行时异常对象
     * @param request HTTP请求对象
     * @return 包含系统错误信息的API响应，HTTP状态码500
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException e, HttpServletRequest request) {
        logger.error("运行时异常: {}", e.getMessage(), e);
        logRequestInfo(request, e);

        // 避免暴露内部异常信息给客户端
        ApiResponse<Void> response = ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 处理其他未知异常
     * 最终的异常处理器，处理所有未被其他处理器捕获的异常
     * 
     * @param e       异常对象
     * @param request HTTP请求对象
     * @return 包含系统错误信息的API响应，HTTP状态码500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e, HttpServletRequest request) {
        logger.error("未知异常: {}", e.getMessage(), e);
        logRequestInfo(request, e);

        ApiResponse<Void> response = ResultUtils.error(ErrorCode.SYSTEM_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 根据错误码获取对应的HTTP状态码
     * 
     * @param errorCode 业务错误码
     * @return 对应的HTTP状态码
     */
    private HttpStatus getHttpStatus(ErrorCode errorCode) {
        int code = errorCode.getCode();
        if (code >= 400 && code < 500) {
            return HttpStatus.valueOf(code >= 404 && code <= 405 ? code : 400);
        } else if (code >= 500) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

    /**
     * 记录请求信息
     * 在DEBUG模式下记录详细的请求信息，便于问题排查
     * 
     * @param request HTTP请求对象
     * @param e       异常对象
     */
    private void logRequestInfo(HttpServletRequest request, Exception e) {
        if (logger.isDebugEnabled()) {
            logger.debug("异常请求信息 - Method: {}, URI: {}, RemoteAddr: {}, UserAgent: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    getRemoteAddr(request),
                    request.getHeader("User-Agent"));
        }
    }

    /**
     * 获取真实IP地址
     * 优先从代理头部获取真实IP，适用于负载均衡和反向代理场景
     * 
     * @param request HTTP请求对象
     * @return 客户端真实IP地址
     */
    private String getRemoteAddr(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}