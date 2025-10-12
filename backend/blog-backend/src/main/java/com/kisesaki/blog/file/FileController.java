package com.kisesaki.blog.file;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kisesaki.blog.auth.security.user.CustomUserPrincipal;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Tag(name = "File", description = "文件相关接口")
class FileController {
    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('FILE_UPLOAD')")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        if (file.isEmpty()) {
            return ResultUtils.error("上传的文件不能为空");
        }
        try {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();
            String fileUrl = fileUploadService.uploadFile(file, userId);

            return ResultUtils.success("上传文件成功",fileUrl);
        } catch (Exception e) {
            return ResultUtils.error(500,  e.getMessage());
        }
    }
}
