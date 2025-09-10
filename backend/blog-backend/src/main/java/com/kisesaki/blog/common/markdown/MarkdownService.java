package com.kisesaki.blog.common.markdown;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.extern.slf4j.Slf4j;

/**
 * Markdown 转换服务
 * 负责将 Markdown 内容转换为 HTML，并提供相关的内容处理功能
 * 
 * @author KiseSaki
 */
@Component
@Slf4j
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer htmlRenderer;

    /**
     * 构造函数，初始化 Markdown 解析器和渲染器
     */
    public MarkdownService() {
        MutableDataSet options = new MutableDataSet();

        // 配置 Flexmark 扩展
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(), // 表格支持
                StrikethroughExtension.create(), // 删除线支持
                TaskListExtension.create(), // 任务列表支持
                GfmIssuesExtension.create(), // GitHub 风格的 issue 支持
                GfmUsersExtension.create(), // GitHub 风格的用户支持
                TocExtension.create() // 目录支持
        ));

        // 配置表格选项
        options.set(TablesExtension.COLUMN_SPANS, false)
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);

        // 配置任务列表
        options.set(TaskListExtension.ITEM_DONE_MARKER, "<span class=\"task-list-item-checkbox checked\">✓</span>")
                .set(TaskListExtension.ITEM_NOT_DONE_MARKER, "<span class=\"task-list-item-checkbox\">☐</span>");

        // 配置 HTML 渲染选项
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n")
                .set(HtmlRenderer.HARD_BREAK, "<br />\n")
                .set(HtmlRenderer.STRONG_EMPHASIS_STYLE_HTML_OPEN, "<strong>")
                .set(HtmlRenderer.STRONG_EMPHASIS_STYLE_HTML_CLOSE, "</strong>")
                .set(HtmlRenderer.EMPHASIS_STYLE_HTML_OPEN, "<em>")
                .set(HtmlRenderer.EMPHASIS_STYLE_HTML_CLOSE, "</em>");

        this.parser = Parser.builder(options).build();
        this.htmlRenderer = HtmlRenderer.builder(options).build();
    }

    /**
     * 将 Markdown 内容转换为 HTML
     *
     * @param markdownContent Markdown 内容
     * @return 转换后的 HTML 内容
     */
    public String convertToHtml(String markdownContent) {
        if (!StringUtils.hasText(markdownContent)) {
            return "";
        }

        try {
            Node document = parser.parse(markdownContent);
            String html = htmlRenderer.render(document);

            // 后处理：添加代码块语言类名
            html = enhanceCodeBlocks(html);

            // 后处理：优化图片标签
            html = enhanceImages(html);

            // 后处理：优化链接（添加外链属性）
            html = enhanceLinks(html);

            return html;
        } catch (Exception e) {
            log.error("Markdown 转换 HTML 失败: {}", e.getMessage(), e);
            // 转换失败时返回原始内容，用 <pre> 包裹
            return "<pre>" + escapeHtml(markdownContent) + "</pre>";
        }
    }

    /**
     * 从 Markdown 内容中提取纯文本（用于摘要生成）
     *
     * @param markdownContent Markdown 内容
     * @return 纯文本内容
     */
    public String extractPlainText(String markdownContent) {
        if (!StringUtils.hasText(markdownContent)) {
            return "";
        }

        try {
            // 移除 Markdown 语法，获取纯文本
            return markdownContent
                    .replaceAll("#+\\s*", "") // 移除标题标记
                    .replaceAll("\\*\\*(.+?)\\*\\*", "$1") // 移除粗体标记
                    .replaceAll("\\*(.+?)\\*", "$1") // 移除斜体标记
                    .replaceAll("~~(.+?)~~", "$1") // 移除删除线标记
                    .replaceAll("\\[(.+?)]\\(.+?\\)", "$1") // 移除链接，保留文本
                    .replaceAll("```[\\s\\S]*?```", "") // 移除代码块
                    .replaceAll("`(.+?)`", "$1") // 移除行内代码标记
                    .replaceAll("!\\[.*?]\\(.*?\\)", "") // 移除图片
                    .replaceAll("\\|.*?\\|", "") // 移除表格内容
                    .replaceAll("- \\[[ xX]]", "") // 移除任务列表标记
                    .replaceAll("\\n+", " ") // 将换行替换为空格
                    .replaceAll("\\s+", " ") // 合并多个空格
                    .trim();
        } catch (Exception e) {
            log.error("提取纯文本失败: {}", e.getMessage(), e);
            return markdownContent;
        }
    }

    /**
     * 估算 Markdown 内容的阅读时间（分钟）
     *
     * @param markdownContent Markdown 内容
     * @return 预估阅读时间（分钟）
     */
    public int estimateReadingTime(String markdownContent) {
        if (!StringUtils.hasText(markdownContent)) {
            return 0;
        }

        try {
            String plainText = extractPlainText(markdownContent);
            int wordCount = countWords(plainText);

            // 按照平均每分钟阅读 200 个中文字或 250 个英文单词计算
            // 这里简化处理，使用 220 作为平均值
            return Math.max(1, (int) Math.ceil(wordCount / 220.0));
        } catch (Exception e) {
            log.error("估算阅读时间失败: {}", e.getMessage(), e);
            return 1; // 默认返回1分钟
        }
    }

    /**
     * 计算 Markdown 内容的字数
     *
     * @param markdownContent Markdown 内容
     * @return 字数统计
     */
    public int countWords(String markdownContent) {
        if (!StringUtils.hasText(markdownContent)) {
            return 0;
        }

        try {
            String plainText = extractPlainText(markdownContent);

            // 中文字符统计
            int chineseCount = (int) plainText.chars()
                    .filter(ch -> Character.UnicodeScript.of(ch) == Character.UnicodeScript.HAN)
                    .count();

            // 英文单词统计
            String[] words = plainText.replaceAll("[\\u4e00-\\u9fff]", "").split("\\s+");
            int englishWords = (int) Arrays.stream(words)
                    .filter(word -> !word.trim().isEmpty())
                    .count();

            return chineseCount + englishWords;
        } catch (Exception e) {
            log.error("字数统计失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 从 Markdown 内容中生成摘要
     *
     * @param markdownContent Markdown 内容
     * @param maxLength       最大长度
     * @return 生成的摘要
     */
    public String generateExcerpt(String markdownContent, int maxLength) {
        if (!StringUtils.hasText(markdownContent)) {
            return "";
        }

        String plainText = extractPlainText(markdownContent);

        if (plainText.length() <= maxLength) {
            return plainText;
        }

        // 截取并在最后添加省略号
        return plainText.substring(0, maxLength) + "...";
    }

    /**
     * 增强代码块（添加语言类名和复制按钮）
     */
    private String enhanceCodeBlocks(String html) {
        Pattern codeBlockPattern = Pattern.compile("<pre><code class=\"language-([^\"]+)\">([\\s\\S]*?)</code></pre>");
        Matcher matcher = codeBlockPattern.matcher(html);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String language = matcher.group(1);
            String code = matcher.group(2);

            String enhanced = buildEnhancedCodeBlock(language, code);

            matcher.appendReplacement(result, Matcher.quoteReplacement(enhanced));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 增强图片标签（添加懒加载和响应式类名）
     */
    private String enhanceImages(String html) {
        Pattern imgPattern = Pattern.compile("<img([^>]+)>");
        Matcher matcher = imgPattern.matcher(html);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String imgTag = matcher.group(1);

            // 添加懒加载和响应式类名
            if (!imgTag.contains("loading=")) {
                imgTag += " loading=\"lazy\"";
            }
            if (!imgTag.contains("class=")) {
                imgTag += " class=\"img-responsive\"";
            } else {
                imgTag = imgTag.replaceAll("class=\"([^\"]*?)\"", "class=\"$1 img-responsive\"");
            }

            String enhanced = "<img" + imgTag + ">";
            matcher.appendReplacement(result, Matcher.quoteReplacement(enhanced));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 增强链接（为外部链接添加 target="_blank" 和 rel="noopener"）
     */
    private String enhanceLinks(String html) {
        Pattern linkPattern = Pattern.compile("<a href=\"(https?://[^\"]+)\"([^>]*?)>([\\s\\S]*?)</a>");
        Matcher matcher = linkPattern.matcher(html);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String href = matcher.group(1);
            String attributes = matcher.group(2);
            String content = matcher.group(3);

            // 为外部链接添加安全属性
            if (!attributes.contains("target=")) {
                attributes += " target=\"_blank\"";
            }
            if (!attributes.contains("rel=")) {
                attributes += " rel=\"noopener noreferrer\"";
            }

            String enhanced = String.format("<a href=\"%s\"%s>%s</a>", href, attributes, content);
            matcher.appendReplacement(result, Matcher.quoteReplacement(enhanced));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * HTML 转义
     */
    private String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * 构建增强的代码块 HTML 结构
     */
    private String buildEnhancedCodeBlock(String language, String code) {
        return String.format(
                "<div class=\"code-block-wrapper\">" +
                        "<div class=\"code-block-header\">" +
                        "<span class=\"code-language\">%s</span>" +
                        "<button class=\"copy-code-btn\" onclick=\"copyCode(this)\">复制</button>" +
                        "</div>" +
                        "<pre><code class=\"language-%s hljs\">%s</code></pre>" +
                        "</div>",
                language, language, code);
    }
}
