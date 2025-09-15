package com.kisesaki.blog.content.tag;

import org.springframework.stereotype.Service;

import com.kisesaki.blog.content.tag.mapper.TagsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 标签服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagsMapper tagsMapper;
}
