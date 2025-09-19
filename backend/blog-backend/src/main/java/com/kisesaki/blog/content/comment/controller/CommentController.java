package com.kisesaki.blog.content.comment.controller;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/comments")
@Tag(name = "评论", description = "评论相关接口")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 获取评论列表
     * 
     */
    @GetMapping("/{postId}/comments")
    public ApiResponse<PageResponse<CommentListResponse>> getCommentList(@PathVariable Long postId,
            @Valid CommentListParams params) {
        PageResponse<CommentListResponse> pageResponse = commentService.getCommentList(postId, params);
        return ResultUtils.success(pageResponse);
    }
}
