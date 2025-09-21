package com.kisesaki.blog.content.comment.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.kisesaki.blog.content.comment.entity.CommentReports.ReportStatus;

/**
 * PostgreSQL report_status 枚举类型处理器
 * 用于处理 Java 枚举类型与 PostgreSQL 枚举类型之间的转换
 */
@MappedTypes(ReportStatus.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class CommentReportStatusTypeHandler extends BaseTypeHandler<ReportStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ReportStatus parameter, JdbcType jdbcType)
            throws SQLException {
        // 将 Java 枚举转换为 PostgreSQL 枚举类型
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public ReportStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : ReportStatus.valueOf(value.toUpperCase());
    }

    @Override
    public ReportStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : ReportStatus.valueOf(value.toUpperCase());
    }

    @Override
    public ReportStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : ReportStatus.valueOf(value.toUpperCase());
    }
}