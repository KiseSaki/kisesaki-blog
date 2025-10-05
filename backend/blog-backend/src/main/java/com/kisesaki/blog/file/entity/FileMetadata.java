package com.kisesaki.blog.file.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

/**
 * 文件元数据实体，对应表 file_metadata
 *
 * @author KiseSaki
 */
@Data
@Builder
@TableName("file_metadata")
public class FileMetadata {
    /** 媒体资源唯一ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文件相对访问路径 (如 /files/hash/uuid.jpg) */
    private String url;

    /** 原始文件名 */
    private String filename;

    /** 文件存储路径 */
    private String filePath;

    /** 文件MIME类型 */
    private String fileType;

    /** 文件大小 (字节) */
    private Long fileSize;

    /** 图片宽度 (仅图片类型) */
    private Integer width;

    /** 图片高度 (仅图片类型) */
    private Integer height;

    /** 替代文本 */
    private String altText;

    /** 文件描述 */
    private String description;

    /** 上传者ID */
    private Long uploadedBy;

    /** 使用次数 */
    private Integer usageCount;

    /** 上传时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}
