package com.kisesaki.blog.content.tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 标签控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/tags")
@Tag(name = "Tag", description = "标签相关接口")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
}
