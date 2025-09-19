package com.kisesaki.blog.config;

import java.time.OffsetDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kisesaki.blog.content.comment.entity.CommentReactions.ReactionType;
import com.kisesaki.blog.content.comment.entity.Comments.CommentStatus;
import com.kisesaki.blog.content.comment.handler.CommentReactionTypeHandler;
import com.kisesaki.blog.content.comment.handler.CommentStatusTypeHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * MybatisPlus 配置类
 *
 * 作用：
 * - 注册 MyBatis-Plus 全局拦截器（MybatisPlusInterceptor）为 Spring Bean。
 * - 在拦截器中添加分页插件（适配 PostgreSQL）、乐观锁插件和防攻击插件，用于全局启用常见功能：
 * 1. PaginationInnerInterceptor：支持数据库分页（此处指定为 DbType.POSTGRE_SQL）。
 * 2. OptimisticLockerInnerInterceptor：支持乐观锁注解（@Version）实现并发控制。
 * 3. BlockAttackInnerInterceptor：防止恶意的全表更新删除操作。
 * 4. MetaObjectHandler：自动填充字段（创建时间、更新时间等）。
 * 5. 注册自定义类型处理器：处理 PostgreSQL 枚举类型与 Java 枚举类型的转换。
 *
 * 备注：如需增加更多插件（如性能分析等），可在此统一注册。
 */
@Configuration
@MapperScan("com.kisesaki.blog.**.mapper")
@Slf4j
public class MybatisPlusConfig {

    /**
     * 注册自定义类型处理器
     * 通过 ApplicationListener 监听 ContextRefreshedEvent，在容器初始化完成后注册类型处理器
     */
    @Bean
    public ApplicationListener<ContextRefreshedEvent> typeHandlerRegistrar() {
        return event -> {
            try {
                SqlSessionFactory sqlSessionFactory = event.getApplicationContext().getBean(SqlSessionFactory.class);
                TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

                // 注册评论状态类型处理器
                typeHandlerRegistry.register(CommentStatus.class, CommentStatusTypeHandler.class);

                // 注册评论反应类型处理器
                typeHandlerRegistry.register(ReactionType.class, CommentReactionTypeHandler.class);

                log.info("Custom type handlers registered: CommentStatusTypeHandler, CommentReactionTypeHandler");
            } catch (Exception e) {
                log.error("Failed to register custom type handlers", e);
            }
        };
    }

    /**
     * 注册 MybatisPlusInterceptor Bean
     *
     * 返回值：MybatisPlusInterceptor（包含多个 inner interceptor）
     *
     * 说明：
     * - PaginationInnerInterceptor：负责拦截并处理分页查询，必须指定目标数据库类型以正确生成分页 SQL。
     * - OptimisticLockerInnerInterceptor：启用乐观锁支持，处理基于 @Version 注解的并发更新逻辑。
     * - BlockAttackInnerInterceptor：防止恶意的全表更新删除操作，提高系统安全性。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加分页拦截器，指定 PostgreSQL 以正确生成分页 SQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

        // 添加乐观锁拦截器，支持 @Version 注解的并发控制
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 添加防攻击拦截器，防止恶意的全表更新删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        log.info("MybatisPlus interceptor configured with pagination, optimistic lock and block attack protection");
        return interceptor;
    }

    /**
     * 自动填充字段处理器
     * 
     * 用于自动填充创建时间、更新时间、创建人、更新人等公共字段
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                // 自动填充创建时间和更新时间
                this.strictInsertFill(metaObject, "createdAt", OffsetDateTime.class, OffsetDateTime.now());
                this.strictInsertFill(metaObject, "updatedAt", OffsetDateTime.class, OffsetDateTime.now());
                log.debug("Auto fill createdAt and updatedAt for insert operation");
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 自动填充更新时间
                this.strictUpdateFill(metaObject, "updatedAt", OffsetDateTime.class, OffsetDateTime.now());
                log.debug("Auto fill updatedAt for update operation");
            }
        };
    }
}