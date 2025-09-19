package com.kisesaki.blog.content.comment.dto;

import java.time.OffsetDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "评论列表响应数据")
public class CommentListResponse {

    @Schema(description = "评论ID", example = "1")
    private Long id;

    @Schema(description = "所属文章ID", example = "10")
    private Long postId;

    @Schema(description = "评论用户ID", example = "5")
    private Long userId;

    @Schema(description = "父评论ID（null 表示顶级评论）", example = "null")
    private Long parentId;

    @Schema(description = "回复目标评论ID（@ 某条评论）", example = "2")
    private Long replyToId;

    @Schema(description = "评论原始内容")
    private String content;

    @Schema(description = "渲染后的 HTML 内容")
    private String htmlContent;

    @Schema(description = "点赞数", example = "12")
    private Integer likeCount;

    @Schema(description = "踩数", example = "0")
    private Integer dislikeCount;

    @Schema(description = "直接回复数量", example = "3")
    private Integer replyCount;

    @Schema(description = "嵌套层级（0 表示顶级）", example = "0")
    private Integer level;

    @Schema(description = "评论路径，例如 '1.3.5' 用于快速定位层级关系")
    private String path;

    @Schema(description = "评论状态（PENDING/APPROVED/REJECTED/SPAM）")
    private String status;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isPinned;

    @Schema(description = "是否为文章作者回复", example = "false")
    private Boolean isAuthorReply;

    @Schema(description = "最后编辑时间（如果为 null 表示未编辑）")
    private OffsetDateTime editedAt;

    @Schema(description = "评论者信息（仅包含公开的基础信息）")
    private CommentUserDto user;

    @Schema(description = "当前用户对该评论的交互状态（仅在用户已登录时返回）")
    private CommentUserInteractionDto currentUserInteraction;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;

    @Schema(description = "子回复列表（用于嵌套显示，默认只返回前几层）")
    private List<CommentListResponse> replies;

    @Schema(description = "是否还有更多子回复（用于分页加载）", example = "false")
    private Boolean hasMoreReplies;

}
