package com.kisesaki.blog.file.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.file.entity.FileMetadata;

@Mapper
public interface FileMetadataMapper extends BaseMapper<FileMetadata> {
    // 可以在这里添加自定义查询方法，如果需要
}
