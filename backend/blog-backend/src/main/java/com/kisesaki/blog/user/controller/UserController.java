package com.kisesaki.blog.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.file.FileUploadService;
import com.kisesaki.blog.user.dto.UserStatsDto;
import com.kisesaki.blog.user.dto.follow.UserFollowDto;
import com.kisesaki.blog.user.dto.info.UpdateProfileDto;
import com.kisesaki.blog.user.dto.info.UserInfoDto;
import com.kisesaki.blog.user.dto.info.UserProfileDto;
import com.kisesaki.blog.user.dto.setting.UserSettingsDto;
import com.kisesaki.blog.user.service.UserFollowService;
import com.kisesaki.blog.user.service.UserService;
import com.kisesaki.blog.user.service.UserSettingsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 用户控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;
    private final UserSettingsService userSettingsService;
    private final FileUploadService fileUploadService;
    private final UserFollowService userFollowService;

    @PostMapping("/getUserInfoByToken")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "根据token获取当前用户信息", description = "获取当前登录用户的详细信息")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoByToken(Authentication authentication) {
        String username = AuthUtils.getUsernameFromAuthentication(authentication);
        UserInfoDto userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据用户ID获取用户信息", description = "获取指定用户的详细信息")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        UserInfoDto userInfo = userService.getUserInfoById(id);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取用户信息", description = "根据用户名获取用户的详细信息")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfoByUsername(
            @Parameter(description = "用户名", required = true) @PathVariable String username) {
        UserInfoDto userInfo = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

    @GetMapping("/{id}/profile")
    @Operation(summary = "获取用户简要信息", description = "获取用户的简要信息用于展示")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        UserProfileDto userProfile = userService.getUserProfile(id);
        return ResponseEntity.ok(ApiResponse.success(userProfile));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新用户资料", description = "更新当前用户的个人资料")
    public ResponseEntity<ApiResponse<UserInfoDto>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileDto updateDto) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        UserInfoDto updatedUserInfo = userService.updateUserProfile(userId, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUserInfo));
    }

    @PostMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新用户头像", description = "更新当前用户的头像")
    public ResponseEntity<ApiResponse<UserInfoDto>> updateAvatar(
            Authentication authentication,
            @RequestParam("avatarUrl") String avatarUrl) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        UserInfoDto updatedUserInfo = userService.updateUserAvatar(userId, avatarUrl);
        return ResponseEntity.ok(ApiResponse.success(updatedUserInfo));
    }

    @PostMapping("/upload-avatar")
    @PreAuthorize("hasAuthority('FILE_UPLOAD')")
    @Operation(summary = "上传用户头像", description = "上传并更新当前用户的头像文件")
    public ResponseEntity<ApiResponse<UserInfoDto>> uploadAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);

            // 上传文件并获取URL
            String avatarUrl = fileUploadService.uploadFile(file, userId);

            // 更新用户头像
            UserInfoDto updatedUserInfo = userService.updateUserAvatar(userId, avatarUrl);
            return ResponseEntity.ok(ApiResponse.success(updatedUserInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("头像上传失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "获取用户统计信息", description = "获取用户的统计数据")
    public ResponseEntity<ApiResponse<UserStatsDto>> getUserStats(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        UserStatsDto userStats = userService.getUserStats(id);
        return ResponseEntity.ok(ApiResponse.success(userStats));
    }

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取用户设置", description = "获取当前用户的所有设置")
    public ResponseEntity<ApiResponse<Map<String, String>>> getUserSettings(Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        Map<String, String> settings = userSettingsService.getUserSettings(userId);
        return ResponseEntity.ok(ApiResponse.success(settings));
    }

    @PutMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "更新用户设置", description = "批量更新用户设置")
    public ResponseEntity<ApiResponse<String>> updateUserSettings(
            Authentication authentication,
            @Valid @RequestBody UserSettingsDto settingsDto) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        userSettingsService.setUserSettings(userId, settingsDto.getSettings());
        return ResponseEntity.ok(ApiResponse.success("设置更新成功"));
    }

    @GetMapping("/check/username")
    @Operation(summary = "检查用户名是否可用", description = "检查用户名是否已被使用")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailability(
            @Parameter(description = "用户名", required = true) @RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    @GetMapping("/check/email")
    @Operation(summary = "检查邮箱是否可用", description = "检查邮箱是否已被注册")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(
            @Parameter(description = "邮箱", required = true) @RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.success(available));
    }

    // ===== 关注相关接口 =====

    @PostMapping("/{id}/follow")
    @Operation(summary = "关注用户", description = "关注指定用户")
    public ResponseEntity<ApiResponse<String>> followUser(
            Authentication authentication,
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        Long currentUserId = AuthUtils.getUserIdFromAuthentication(authentication);
        boolean success = userFollowService.followUser(currentUserId, id);

        if (success) {
            return ResponseEntity.ok(ApiResponse.success("关注成功"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("关注失败"));
        }
    }

    @PostMapping("/{id}/unfollow")
    @Operation(summary = "取消关注用户", description = "取消关注指定用户")
    public ResponseEntity<ApiResponse<String>> unfollowUser(
            Authentication authentication,
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        Long currentUserId = AuthUtils.getUserIdFromAuthentication(authentication);
        boolean success = userFollowService.unfollowUser(currentUserId, id);

        if (success) {
            return ResponseEntity.ok(ApiResponse.success("取消关注成功"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("取消关注失败"));
        }
    }

    @GetMapping("/{id}/is-following")
    @Operation(summary = "检查是否已关注", description = "检查当前用户是否已关注指定用户")
    public ResponseEntity<ApiResponse<Boolean>> isFollowing(
            Authentication authentication,
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        Long currentUserId = AuthUtils.getUserIdFromAuthentication(authentication);
        boolean isFollowing = userFollowService.isFollowing(currentUserId, id);
        return ResponseEntity.ok(ApiResponse.success(isFollowing));
    }

    @GetMapping("/{id}/following")
    @Operation(summary = "获取用户关注列表", description = "获取指定用户关注的人列表")
    public ResponseEntity<ApiResponse<List<UserFollowDto>>> getFollowingList(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        List<UserFollowDto> followingList = userFollowService.getFollowingList(id);
        return ResponseEntity.ok(ApiResponse.success(followingList));
    }

    @GetMapping("/{id}/followers")
    @Operation(summary = "获取用户粉丝列表", description = "获取关注指定用户的人列表")
    public ResponseEntity<ApiResponse<List<UserFollowDto>>> getFollowersList(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        List<UserFollowDto> followersList = userFollowService.getFollowersList(id);
        return ResponseEntity.ok(ApiResponse.success(followersList));
    }

    @GetMapping("/{id}/follow-counts")
    @Operation(summary = "获取用户关注统计", description = "获取用户的关注数和粉丝数")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getFollowCounts(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        long followingCount = userFollowService.getFollowingCount(id);
        long followersCount = userFollowService.getFollowersCount(id);

        Map<String, Long> counts = Map.of(
                "followingCount", followingCount,
                "followersCount", followersCount);

        return ResponseEntity.ok(ApiResponse.success(counts));
    }

}
