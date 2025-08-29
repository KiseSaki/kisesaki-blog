package com.kisesaki.blog.notification.event;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.kisesaki.blog.notification.dto.request.EmailSendRequestDto;
import com.kisesaki.blog.notification.dto.response.EmailSendResponseDto;
import com.kisesaki.blog.notification.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件事件监听器
 * <p>
 * 负责监听并处理系统中发布的所有 {@link EmailEvent}。
 * 这个类集成了异步处理、失败重试和条件分发等核心功能，确保邮件发送流程的健壮性和高效性。
 *
 * @author KiseSaki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    /**
     * 监听所有邮件事件并进行处理的主方法。
     * <p>
     * <b>注解解释:</b>
     * <ul>
     * <li>{@code @Async("emailTaskExecutor")}:
     * 核心注解，使此方法在名为 "emailTaskExecutor" 的独立线程池中异步执行。
     * 这可以防止邮件发送的耗时操作阻塞主业务线程（如HTTP请求处理线程），从而提高系统吞吐量和响应速度。
     * </li>
     * <li>{@code @EventListener}:
     * Spring事件监听注解，声明此方法用于接收并处理 {@link EmailEvent} 类型的事件。
     * </li>
     * <li>{@code @Order(1)}:
     * 定义监听器的执行顺序，数字越小优先级越高。
     * </li>
     * <li>{@code @Retryable}:
     * 来自 Spring Retry 的注解，提供强大的失败自动重试功能。
     * - {@code maxAttempts = 3}: 包括首次执行在内，最多尝试3次。
     * - {@code backoff = @Backoff(delay = 2000, multiplier = 2)}:
     * 重试策略。第一次重试前等待2秒，第二次等待4秒（2*2），以指数形式递增，避免在服务故障时频繁冲击。
     * <b>注意:</b> 要触发重试，方法内部必须向上抛出异常。
     * </li>
     * </ul>
     *
     * @param event 邮件事件对象
     */
    @Async("emailTaskExecutor")
    @EventListener
    @Order(1)
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void handleEmailEvent(EmailEvent event) {
        try {
            log.info("开始处理邮件事件: {}", event.getEventDescription());

            // 1. 验证事件数据的有效性
            if (!validateEmailEvent(event)) {
                log.warn("邮件事件验证失败，跳过处理: {}", event.getEventDescription());
                return; // 不抛出异常，因为这是数据问题，重试也无用
            }

            // 2. 将事件对象转换为邮件服务所需的请求对象
            EmailSendRequestDto emailRequest = buildEmailSendRequest(event);

            // 3. 根据事件的优先级和异步标志决定发送方式
            if (event.isAsync() && event.getPriority() >= 3) {
                // 对于低优先级事件，调用异步发送接口，请求会立即返回
                EmailSendResponseDto response = new EmailSendResponseDto();
                emailService.sendEmailAsync(emailRequest, response);
                log.info("异步邮件发送请求已提交: type={}, to={}",
                        event.getEmailType().getDescription(), event.getToEmail());
            } else {
                // 对于高优先级或强制同步的事件，调用同步发送接口，会阻塞直到发送完成或失败
                emailService.sendEmail(emailRequest);
                log.info("同步邮件发送完成: type={}, to={}",
                        event.getEmailType().getDescription(), event.getToEmail());
            }

        } catch (Exception e) {
            log.error("处理邮件事件失败: {} - {}", event.getEventDescription(), e.getMessage(), e);
            event.incrementRetryCount();

            if (event.canRetry()) {
                log.info("邮件事件将由 @Retryable 机制自动重试: {} (已尝试次数: {})",
                        event.getEventDescription(), event.getRetryCount());
            } else {
                log.error("邮件事件重试次数已达上限，放弃处理: {}", event.getEventDescription());
                // 在这里可以加入失败回调逻辑，例如记录到失败队列、通知管理员等
            }

            throw e; // 必须重新抛出异常，才能触发 @Retryable 的重试机制
        }
    }

    /**
     * 条件监听器：专门处理高优先级邮件事件（同步处理）。
     * <p>
     * <b>注解解释:</b>
     * <ul>
     * <li>{@code @EventListener(condition = "#event.priority <= 2")}:
     * 使用 Spring Expression Language (SpEL) 设置监听条件。
     * 此方法只会在发布的 EmailEvent 的 `priority` 属性小于等于2时被触发。
     * 这是一种非常优雅的事件路由方式。
     * </li>
     * <li>{@code @Order(0)}:
     * 设置最高优先级，确保此监听器在其他通用监听器之前执行。
     * </li>
     * </ul>
     *
     * @param event 高优先级的邮件事件对象
     */
    @EventListener(condition = "#event.priority <= 2")
    @Order(0)
    public void handleHighPriorityEmailEvent(EmailEvent event) {
        // 此处省略了异步和重试注解，意味着它默认是同步执行且失败不自动重试
        try {
            log.info("开始同步处理高优先级邮件事件: {}", event.getEventDescription());
            if (!validateEmailEvent(event)) {
                log.warn("高优先级邮件事件验证失败: {}", event.getEventDescription());
                return;
            }
            EmailSendRequestDto emailRequest = buildEmailSendRequest(event);
            emailRequest.setAsync(false); // 强制同步发送

            emailService.sendEmail(emailRequest);
            log.info("高优先级邮件发送完成: type={}, to={}",
                    event.getEmailType().getDescription(), event.getToEmail());

        } catch (Exception e) {
            log.error("处理高优先级邮件事件失败: {} - {}", event.getEventDescription(), e.getMessage(), e);
            // 高优先级邮件失败通常需要立即告警或记录，而不是依赖自动重试
        }
    }

    /**
     * 条件监听器：专门对“认证类”邮件进行预处理或记录。
     * <p>
     * condition = "#event.emailType.authEmail" 是一个示例，假设 EmailType 枚举中有一个
     * isAuthEmail() 或 authEmail 字段。
     * 这种方式可以将特定业务类型的通用逻辑（如审计日志）聚合处理。
     *
     * @param event 认证类的邮件事件对象
     */
    @EventListener(condition = "#event.emailType.isAuthEmail()") // 假设EmailType有isAuthEmail方法
    @Order(1)
    public void handleAuthEmailEvent(EmailEvent event) {
        log.info("识别到认证类邮件事件，进行特殊处理: type={}, to={}",
                event.getEmailType().getDescription(), event.getToEmail());
        // 此处可以添加认证邮件的特殊处理逻辑，比如：
        // 1. 强制记录每一次认证邮件的发送日志到数据库
        // 2. 更新用户的状态，例如 "邮箱待验证"
    }

    private boolean validateEmailEvent(EmailEvent event) {
        // ... (省略实现)
        return true;
    }

    /**
     * 构建邮件发送请求对象
     * 
     * @param event 邮件事件对象
     * @return 邮件发送请求对象
     */
    private EmailSendRequestDto buildEmailSendRequest(EmailEvent event) {
        return EmailSendRequestDto.builder()
                .toEmail(event.getToEmail())
                .subject(event.getSubject())
                .emailType(event.getEmailType())
                .templateVariables(event.getTemplateVariables())
                .priority(event.getPriority())
                .async(event.isAsync())
                .businessId(event.getBusinessId())
                .businessType(event.getBusinessType())
                .build();
    }
}