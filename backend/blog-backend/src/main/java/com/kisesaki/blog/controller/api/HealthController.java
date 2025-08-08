package com.kisesaki.blog.controller.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.utils.ResultUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 健康检查控制器
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Tag(name = "健康检查", description = "系统状态检查相关接口") @RestController @RequestMapping("/health")
public class HealthController {

    /**
     * 健康检查
     */
    @Operation(summary = "健康检查", description = "检查系统是否正常运行") @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> data = Map.of("status", "UP", "timestamp", LocalDateTime.now(),
                "application", "KiseSaki Blog Backend", "version", "1.0.0", "environment",
                "development");
        return ResultUtils.success("系统运行正常", data);
    }

    /**
     * 简单ping测试
     */
    @Operation(summary = "Ping测试", description = "简单的连通性测试") @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ResultUtils.success("pong");
    }
}
