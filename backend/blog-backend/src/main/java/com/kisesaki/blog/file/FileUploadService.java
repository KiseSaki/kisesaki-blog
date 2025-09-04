package com.kisesaki.blog.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kisesaki.blog.common.util.Utils;
import com.kisesaki.blog.file.config.FileUploadProperties;
import com.kisesaki.blog.file.entity.FileMetadata;
import com.kisesaki.blog.file.mapper.FileMetadataMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传服务
 *
 * @author KiseSaki
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final FileUploadProperties fileUploadProperties;
    private final FileMetadataMapper fileMetadataMapper;
    private final Utils utils;
    private final Tika tika = new Tika();

    // 允许的文件类型
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp");

    /**
     * 上传文件
     * 
     * @param file   上传的文件
     * @param userId 上传用户ID
     * @return 文件访问URL
     * @throws IOException 如果上传或保存文件时发生错误
     */
    @Transactional(rollbackOn = Exception.class)
    public String uploadFile(MultipartFile file, Long userId) throws IOException {
        // 验证文件类型
        validateFileType(file);

        // 生成文件名称
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // 构建文件路径
        String hashPath = utils.getHashedDirectoryByUserId(userId);
        Path uploadDir = Paths.get(fileUploadProperties.getPath(), hashPath);
        // resolve作用：将文件名与目录路径结合，形成完整的文件路径
        Path filePath = uploadDir.resolve(uniqueFilename);

        // 创建目录
        Files.createDirectories(uploadDir);

        // 构造相对路径（用于URL）
        String relativePath = Paths.get(hashPath, uniqueFilename).toString().replace("\\", "/");

        // 构造完整的访问URL
        String accessUrl = fileUploadProperties.getBaseUrl() + "/" + relativePath;

        try (InputStream inputStream = file.getInputStream()) {
            // 保存文件
            // copy: 将输入流的数据复制到目标路径
            // REPLACE_EXISTING表示如果目标文件已存在则替换它
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("文件上传失败: {}", originalFilename, e);
            throw new IOException("文件上传失败", e);
        }

        try {
            FileMetadata metadata = buildMetadata(file, userId, filePath.toString(), accessUrl);
            fileMetadataMapper.insert(metadata);
        } catch (DataAccessException e) {
            // 如果数据库操作失败，删除已上传的文件
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ex) {
                log.error("无法删除文件: {}", filePath, ex);
            }
            log.error("保存文件元数据失败: {}", originalFilename, e);
            throw new IOException("保存文件元数据失败", e);
        }

        return accessUrl;
    }

    /**
     * 构建文件元数据
     *
     * @param file     上传的文件
     * @param userId   用户ID
     * @param filePath 文件路径
     * @param url      文件URL
     * @return 文件元数据
     */
    private FileMetadata buildMetadata(MultipartFile file, Long userId, String filePath, String url)
            throws IOException {
        String filename = file.getOriginalFilename();
        String fileType = tika.detect(file.getInputStream());
        long fileSize = file.getSize();
        Integer width = null;
        Integer height = null;

        // 如果是图片类型，尝试获取宽度和高度
        if (fileType.startsWith("image/")) {
            try {
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(file.getInputStream());
                if (img != null) {
                    width = img.getWidth();
                    height = img.getHeight();
                }
            } catch (IOException e) {
                log.warn("无法获取图片尺寸: {}", filename, e);
            }
        }

        return FileMetadata.builder()
                .url(url)
                .filename(filename)
                .filePath(filePath)
                .fileType(fileType)
                .fileSize(fileSize)
                .width(width)
                .height(height)
                .uploadedBy(userId)
                .usageCount(0)
                .build();
    }

    /**
     * 验证文件类型
     *
     * @param multipartFile 上传的文件
     * @throws IOException 如果读取文件时发生错误
     */
    private void validateFileType(MultipartFile multipartFile) throws IOException {
        String detectedType = tika.detect(multipartFile.getInputStream());
        if (!ALLOWED_FILE_TYPES.contains(detectedType)) {
            throw new IllegalArgumentException("不支持的文件类型: " + detectedType);
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension found
        }
        return filename.substring(lastDotIndex);
    }

}
