package com.kisesaki.blog.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限操作类型枚举
 * 
 * @author KiseSaki
 */
@Getter
@AllArgsConstructor
public enum PermissionAction {
    VIEW("view", "查看"),
    CREATE("create", "创建"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除"),
    MANAGE("manage", "管理"),
    PUBLISH("publish", "发布"),
    BAN("ban", "封禁"),
    UNBAN("unban", "解封"),
    ASSIGN("assign", "分配"),
    MODERATE("moderate", "审核"),
    UPLOAD("upload", "上传"),
    SEND("send", "发送"),
    MONITOR("monitor", "监控"),
    BACKUP("backup", "备份"),
    RESTORE("restore", "恢复"),
    EXPORT("export", "导出"),
    ACTION("action", "处理"),
    OWN("own", "自己的"),
    ALL("all", "所有");

    private final String code;
    private final String description;
}