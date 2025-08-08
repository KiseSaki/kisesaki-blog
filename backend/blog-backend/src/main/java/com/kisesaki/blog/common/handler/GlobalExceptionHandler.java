package com.kisesaki.blog.common.handler;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class) @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleBusinessException(BusinessException e,
            HttpServletRequest request) {
        log.warn("业务异常: {} - {}", request.getRequestURI(), e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常处理 - @RequestBody 参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMessage);
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 参数校验异常处理 - @ModelAttribute 参数校验
     */
    @ExceptionHandler(BindException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBindException(BindException e) {
        String errorMessage = e.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定失败: {}", errorMessage);
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 参数校验异常处理 - @Validated 单个参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        log.warn("参数约束违反: {}", errorMessage);
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 缺少请求参数异常处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        String errorMessage = String.format("缺少必需的参数: %s", e.getParameterName());
        log.warn("缺少请求参数: {}", errorMessage);
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * 参数类型不匹配异常处理
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("参数类型不匹配: %s", e.getName());
        log.warn("参数类型不匹配: {}", errorMessage);
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), errorMessage);
    }

    /**
     * HTTP消息不可读异常处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.warn("HTTP消息不可读: {}", e.getMessage());
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), "请求参数格式错误");
    }

    /**
     * 不支持的媒体类型异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class) @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiResponse<Void> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        log.warn("不支持的媒体类型: {}", e.getContentType());
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), "不支持的媒体类型");
    }

    /**
     * 请求方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMethod());
        return ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 404异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("请求地址不存在: {}", e.getRequestURL());
        return ApiResponse.error(ErrorCode.NOT_FOUND);
    }

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthenticationException.class) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证失败: {}", e.getMessage());
        if (e instanceof BadCredentialsException) {
            return ApiResponse.error(ErrorCode.LOGIN_FAILED);
        }
        return ApiResponse.error(ErrorCode.UNAUTHORIZED);
    }

    /**
     * 权限不足异常处理
     */
    @ExceptionHandler(AccessDeniedException.class) @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return ApiResponse.error(ErrorCode.ACCESS_DENIED);
    }

    /**
     * 文件上传大小超限异常处理
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException e) {
        log.warn("文件上传大小超限: {}", e.getMessage());
        return ApiResponse.error(ErrorCode.FILE_SIZE_EXCEEDED);
    }

    /**
     * 空指针异常处理
     */
    @ExceptionHandler(NullPointerException.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleNullPointerException(NullPointerException e,
            HttpServletRequest request) {
        log.error("空指针异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 运行时异常处理
     */
    @ExceptionHandler(RuntimeException.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e,
            HttpServletRequest request) {
        log.error("运行时异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.error(ErrorCode.SYSTEM_ERROR);
    }
}
