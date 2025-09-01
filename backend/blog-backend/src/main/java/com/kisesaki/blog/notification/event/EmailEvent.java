package com.kisesaki.blog.notification.event;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.kisesaki.blog.notification.enums.EmailType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件事件基类
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = false) // 仅比较当前类的属性，不考虑父类
public class EmailEvent extends ApplicationEvent {

        // 邮件类型
        private EmailType emailType;
        // 模板变量
        private Map<String, Object> templateVariables;

        // 接收人邮箱
        private String toEmail;
        // 接收人邮箱ID
        private Long userId;
        // 接收人昵称
        private String userDisplayName;
        // 邮件主题
        private String subject;

        // 触发时间
        private LocalDateTime triggerTime;
        // 是否异步发送
        private boolean async;
        // 重试次数
        private int retryCount;
        // 优先级 (1-5, 1最高)
        private int priority;

        /**
         * 日志用
         * 比如在密码重置场景下，代码里将 businessId 设置为 userId.toString()。
         * 你只需要拿到用户的ID（比如 123），就可以在日志系统中精确搜索 businessType="PASSWORD_RESET",
         * businessId="123"。
         * 这样能立刻筛选出所有与该用户密码重置相关的邮件事件日志，快速定位问题是“事件没发布”、“发送失败”还是“被邮件服务商拦截”。
         */
        // 业务标识符
        private String businessId;
        // 业务类型
        private String businessType;

        /**
         * 基础构造函数
         *
         * @param source source 事件源对象，通常是发布事件的服务实例 (`this`)。
         */
        public EmailEvent(Object source) {
                super(source);
                this.triggerTime = LocalDateTime.now();
                this.async = true; // 默认异步发送
                this.retryCount = 0; // 默认不重试
                this.priority = 3; // 默认优先级为中等
        }

        /**
         * 全参数构造函数，用于创建高度自定义的邮件事件。
         */
        public EmailEvent(Object source, EmailType emailType, String toEmail, Long userId, String userDisplayName,
                        Map<String, Object> templateVariables, String subject, String businessType,
                        String businessId, int priority) {
                super(source);
                this.emailType = emailType;
                this.toEmail = toEmail;
                this.userId = userId;
                this.userDisplayName = userDisplayName;
                this.templateVariables = templateVariables;
                this.subject = subject;
                this.businessType = businessType;
                this.businessId = businessId;
                this.priority = priority;
                this.triggerTime = LocalDateTime.now();
                this.async = true; // 默认异步
                this.retryCount = 0;
        }

        /**
         * 增加重试次数。
         * 当一次发送尝试失败后，由重试逻辑调用此方法。
         */
        public void incrementRetryCount() {
                this.retryCount++;
        }

        /**
         * 判断是否可以继续重试。
         *
         * @return 如果当前重试次数小于最大限制（例如3次），则返回true。
         */
        public boolean canRetry() {
                return this.retryCount < 3; // 最多重试3次
        }

        /**
         * 获取事件的简要描述字符串。
         * 主要用于日志输出，方便快速了解事件的核心信息。
         *
         * @return 格式化的事件描述字符串。
         */
        public String getEventDescription() {
                return String.format("EmailEvent[type=%s, to=%s, userId=%s, businessType=%s, priority=%d]",
                                emailType != null ? emailType.getDescription() : "UNKNOWN",
                                toEmail,
                                userId,
                                businessType,
                                priority);
        }
}
