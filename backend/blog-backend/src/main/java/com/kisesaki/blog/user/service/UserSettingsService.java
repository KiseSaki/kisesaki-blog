package com.kisesaki.blog.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kisesaki.blog.user.entity.UserSettings;
import com.kisesaki.blog.user.mapper.UserSettingsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户设置服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSettingsService {

    private final UserSettingsMapper userSettingsMapper;

    /**
     * 获取用户的所有设置
     * 
     * @param userId 用户ID
     * @return 设置键值对映射
     */
    public Map<String, String> getUserSettings(Long userId) {
        log.debug("获取用户 {} 的所有设置", userId);
        List<UserSettings> settingsList = userSettingsMapper.findAllByUserId(userId);

        Map<String, String> settingsMap = new HashMap<>();
        for (UserSettings settings : settingsList) {
            if (settings.getSettingKey() != null) {
                settingsMap.put(settings.getSettingKey(), settings.getSettingValue());
            }
        }

        log.debug("用户 {} 的设置数量: {}", userId, settingsMap.size());
        return settingsMap;
    }

    /**
     * 获取用户的特定设置值
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 设置值，如果不存在返回null
     */
    public String getUserSetting(Long userId, String settingKey) {
        log.debug("获取用户 {} 的设置 {}", userId, settingKey);
        return userSettingsMapper.findByUserIdAndKey(userId, settingKey)
                .map(UserSettings::getSettingValue)
                .orElse(null);
    }

    /**
     * 设置用户的单个配置项
     * 
     * @param userId       用户ID
     * @param settingKey   设置键
     * @param settingValue 设置值
     */
    public void setUserSetting(Long userId, String settingKey, String settingValue) {
        log.debug("设置用户 {} 的配置 {} = {}", userId, settingKey, settingValue);

        var existingSetting = userSettingsMapper.findByUserIdAndKey(userId, settingKey);

        if (existingSetting.isPresent()) {
            // 更新现有设置
            UserSettings settings = existingSetting.get();
            settings.setSettingValue(settingValue);
            userSettingsMapper.updateById(settings);
            log.debug("更新用户 {} 的设置 {}", userId, settingKey);
        } else {
            // 创建新设置
            UserSettings newSettings = new UserSettings();
            newSettings.setUserId(userId);
            newSettings.setSettingKey(settingKey);
            newSettings.setSettingValue(settingValue);
            userSettingsMapper.insert(newSettings);
            log.debug("创建用户 {} 的新设置 {}", userId, settingKey);
        }
    }

    /**
     * 批量设置用户配置
     * 
     * @param userId   用户ID
     * @param settings 设置键值对映射
     */
    public void setUserSettings(Long userId, Map<String, String> settings) {
        log.debug("批量设置用户 {} 的配置，数量: {}", userId, settings.size());

        for (Map.Entry<String, String> entry : settings.entrySet()) {
            setUserSetting(userId, entry.getKey(), entry.getValue());
        }

        log.info("完成用户 {} 的批量设置更新", userId);
    }

    /**
     * 删除用户的特定设置
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 是否删除成功
     */
    public boolean deleteUserSetting(Long userId, String settingKey) {
        log.debug("删除用户 {} 的设置 {}", userId, settingKey);

        int deletedCount = userSettingsMapper.deleteByUserIdAndKey(userId, settingKey);
        boolean success = deletedCount > 0;

        if (success) {
            log.debug("成功删除用户 {} 的设置 {}", userId, settingKey);
        } else {
            log.debug("用户 {} 的设置 {} 不存在，无需删除", userId, settingKey);
        }

        return success;
    }

    /**
     * 删除用户的所有设置
     * 
     * @param userId 用户ID
     * @return 删除的设置数量
     */
    public int deleteAllUserSettings(Long userId) {
        log.debug("删除用户 {} 的所有设置", userId);

        int deletedCount = userSettingsMapper.deleteAllByUserId(userId);
        log.info("删除用户 {} 的设置数量: {}", userId, deletedCount);

        return deletedCount;
    }

    /**
     * 检查用户设置是否存在
     * 
     * @param userId     用户ID
     * @param settingKey 设置键
     * @return 是否存在
     */
    public boolean hasUserSetting(Long userId, String settingKey) {
        return userSettingsMapper.existsByUserIdAndKey(userId, settingKey);
    }

    /**
     * 获取用户设置的数量
     * 
     * @param userId 用户ID
     * @return 设置数量
     */
    public int getUserSettingsCount(Long userId) {
        return userSettingsMapper.findAllByUserId(userId).size();
    }
}
