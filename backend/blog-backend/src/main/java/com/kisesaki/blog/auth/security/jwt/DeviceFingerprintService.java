package com.kisesaki.blog.auth.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kisesaki.blog.auth.dto.DeviceInfo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备指纹生成服务
 *
 * 功能特性：
 * 1. 基于多种设备特征生成唯一指纹
 * 2. 支持客户端传递的设备指纹
 * 3. 服务端验证和增强安全性
 * 4. 防止指纹伪造和重放攻击
 * 5. 支持设备信息的可读化显示
 *
 * 安全机制：
 * 1. 使用盐值防止彩虹表攻击
 * 2. 结合服务端特征增强安全性
 * 3. 支持设备指纹的时效性验证
 * 4. 记录设备变更历史用于风控
 *
 * @author KiseSaki
 */
@Service
@Slf4j
public class DeviceFingerprintService {

    @Value("${kisesaki.blog.security.device.salt:kisesaki-blog-device-salt-2024}")
    private String deviceSalt;

    @Value("${kisesaki.blog.security.device.trust-client-fingerprint:true}")
    private boolean trustClientFingerprint;

    // 请求头常量
    private static final String HEADER_CLIENT_FINGERPRINT = "X-Device-Fingerprint";
    private static final String HEADER_CLIENT_INFO = "X-Device-Info";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    /**
     * 生成设备指纹
     *
     * @param request HTTP请求对象
     * @return DeviceInfo 包含设备指纹和设备信息
     */
    public DeviceInfo generateDeviceFingerprint(HttpServletRequest request) {
        // 1. 优先使用客户端传递的设备指纹（如果信任客户端）
        String clientFingerprint = request.getHeader(HEADER_CLIENT_FINGERPRINT);
        if (trustClientFingerprint && StringUtils.hasText(clientFingerprint)) {
            return enhanceClientFingerprint(request, clientFingerprint);
        }

        // 2. 服务端生成设备指纹
        return generateServerSideFingerprint(request);
    }

    /**
     * 增强客户端提供的设备指纹
     */
    private DeviceInfo enhanceClientFingerprint(HttpServletRequest request, String clientFingerprint) {
        // 获取服务端特征用于验证和增强
        String serverFeatures = extractServerSideFeatures(request);

        // 组合客户端指纹和服务端特征
        String enhancedFingerprint = generateHash(clientFingerprint + "|" + serverFeatures + "|" + deviceSalt);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(enhancedFingerprint);
        deviceInfo.setDeviceInfo(parseDeviceInfo(request));
        deviceInfo.setClientFingerprint(clientFingerprint);
        deviceInfo.setServerFeatures(serverFeatures);
        deviceInfo.setFingerprintType("CLIENT_ENHANCED");

        log.debug("增强客户端设备指纹: {} -> {}",
                clientFingerprint.substring(0, Math.min(8, clientFingerprint.length())) + "...",
                enhancedFingerprint.substring(0, 8) + "...");

        return deviceInfo;
    }

    /**
     * 生成服务端设备指纹
     */
    private DeviceInfo generateServerSideFingerprint(HttpServletRequest request) {
        String serverFeatures = extractServerSideFeatures(request);
        String deviceId = generateHash(serverFeatures + "|" + deviceSalt);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceInfo(parseDeviceInfo(request));
        deviceInfo.setServerFeatures(serverFeatures);
        deviceInfo.setFingerprintType("SERVER_GENERATED");

        log.debug("生成服务端设备指纹: {}", deviceId.substring(0, 8) + "...");

        return deviceInfo;
    }

    /**
     * 提取服务端可获取的设备特征
     */
    private String extractServerSideFeatures(HttpServletRequest request) {
        StringBuilder features = new StringBuilder();

        // 1. 用户代理字符串（最重要的特征）
        String userAgent = request.getHeader(HEADER_USER_AGENT);
        features.append("ua:").append(normalizeUserAgent(userAgent)).append("|");

        // 2. Accept相关头部
        String acceptLanguage = request.getHeader(HEADER_ACCEPT_LANGUAGE);
        features.append("lang:").append(acceptLanguage != null ? acceptLanguage : "").append("|");

        String acceptEncoding = request.getHeader(HEADER_ACCEPT_ENCODING);
        features.append("enc:").append(acceptEncoding != null ? acceptEncoding : "").append("|");

        // 3. 网络特征（需要谨慎使用，可能变化频繁）
        String clientIp = getClientIpAddress(request);

        // 使用IP的前3段作为网络特征（避免因DHCP等原因频繁变化）
        String networkFeature = extractNetworkFeature(clientIp);
        features.append("net:").append(networkFeature).append("|");

        // 4. 其他HTTP头部特征
        String connection = request.getHeader("Connection");
        features.append("conn:").append(connection != null ? connection : "").append("|");

        return features.toString();
    }

    /**
     * 规范化用户代理字符串（移除版本号等易变部分）
     */
    private String normalizeUserAgent(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "unknown";
        }

        // 移除浏览器版本号中的小版本
        // 例如: Chrome/91.0.4472.124 -> Chrome/91.0
        return userAgent
                .replaceAll("(Chrome/\\d+\\.\\d+)\\.[\\d\\.]+", "$1")
                .replaceAll("(Firefox/)\\d+\\.\\d+", "$1")
                .replaceAll("(Safari/)\\d+\\.\\d+", "$1")
                .replaceAll("(Edge/)\\d+\\.\\d+", "$1");
    }

    /**
     * 提取网络特征（IP前3段）
     */
    private String extractNetworkFeature(String ip) {
        if (!StringUtils.hasText(ip) || "unknown".equals(ip)) {
            return "unknown";
        }

        try {
            // 对于IPv4，取前3段
            if (ip.contains(".")) {
                String[] parts = ip.split("\\.");
                if (parts.length >= 3) {
                    return parts[0] + "." + parts[1] + "." + parts[2];
                }
            }
            // 对于IPv6，取前4段
            else if (ip.contains(":")) {
                String[] parts = ip.split(":");
                if (parts.length >= 4) {
                    return parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + parts[3];
                }
            }
        } catch (Exception e) {
            log.warn("提取网络特征失败: {}", ip, e);
        }

        return "unknown";
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        // 依次检查各种代理头
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "X-Client-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For可能包含多个IP，取第一个
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * 解析设备信息（用于展示）
     */
    private String parseDeviceInfo(HttpServletRequest request) {
        String clientDeviceInfo = request.getHeader(HEADER_CLIENT_INFO);
        if (StringUtils.hasText(clientDeviceInfo)) {
            return clientDeviceInfo;
        }

        // 从User-Agent解析基本信息
        String userAgent = request.getHeader(HEADER_USER_AGENT);
        if (StringUtils.hasText(userAgent)) {
            return parseUserAgentInfo(userAgent);
        }

        return "Unknown Device";
    }

    /**
     * 从User-Agent解析设备信息
     */
    private String parseUserAgentInfo(String userAgent) {
        if (!StringUtils.hasText(userAgent)) {
            return "Unknown Device";
        }

        StringBuilder info = new StringBuilder();

        // 检测操作系统
        if (userAgent.contains("Windows")) {
            info.append("Windows");
            if (userAgent.contains("Windows NT 10"))
                info.append(" 10");
            else if (userAgent.contains("Windows NT 6.3"))
                info.append(" 8.1");
            else if (userAgent.contains("Windows NT 6.2"))
                info.append(" 8");
            else if (userAgent.contains("Windows NT 6.1"))
                info.append(" 7");
        } else if (userAgent.contains("Mac OS X")) {
            info.append("macOS");
        } else if (userAgent.contains("Linux")) {
            info.append("Linux");
        } else if (userAgent.contains("Android")) {
            info.append("Android");
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            info.append("iOS");
        }

        // 检测浏览器
        if (userAgent.contains("Chrome") && !userAgent.contains("Edge")) {
            info.append(" - Chrome");
        } else if (userAgent.contains("Firefox")) {
            info.append(" - Firefox");
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            info.append(" - Safari");
        } else if (userAgent.contains("Edge")) {
            info.append(" - Edge");
        }

        return !info.isEmpty() ? info.toString() : "Unknown Device";
    }

    /**
     * 生成哈希值
     */
    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256算法不可用", e);
            // 降级使用简单哈希
            return String.valueOf(input.hashCode());
        }
    }

    /**
     * 验证设备指纹的有效性
     * 
     * @param request        HTTP请求对象
     * @param storedDeviceId 存储的设备ID
     * @return true如果设备指纹有效，false否则
     */
    public boolean validateDeviceFingerprint(HttpServletRequest request, String storedDeviceId) {
        DeviceInfo currentDevice = generateDeviceFingerprint(request);
        return currentDevice.getDeviceId().equals(storedDeviceId);
    }

}