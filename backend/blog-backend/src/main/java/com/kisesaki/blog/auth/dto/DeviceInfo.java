package com.kisesaki.blog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备信息数据传输对象
 * 
 * 用于封装设备指纹相关的信息，包括：
 * 1. 设备唯一标识符（指纹）
 * 2. 可读的设备信息描述
 * 3. 客户端和服务端的指纹特征
 * 4. 指纹生成类型标识
 * 
 * 该类主要用于设备识别、安全验证和用户体验提升。
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceInfo {

    /**
     * 设备指纹ID - 设备的唯一标识符
     */
    private String deviceId;

    /**
     * 可读的设备信息 - 用于向用户展示的设备描述
     */
    private String deviceInfo;

    /**
     * 客户端原始指纹 - 客户端传递的原始设备指纹
     */
    private String clientFingerprint;

    /**
     * 服务端特征 - 服务端提取的设备特征信息
     */
    private String serverFeatures;

    /**
     * 指纹类型 - 标识指纹的生成方式
     * 可能的值：CLIENT_ENHANCED（客户端增强）、SERVER_GENERATED（服务端生成）
     */
    private String fingerprintType;

    /**
     * 自定义 toString 方法，避免暴露完整的设备指纹
     */
    @Override
    public String toString() {
        return String.format("DeviceInfo{deviceId='%s...', deviceInfo='%s', type='%s'}",
                deviceId != null ? deviceId.substring(0, Math.min(8, deviceId.length())) : "null",
                deviceInfo, fingerprintType);
    }
}
