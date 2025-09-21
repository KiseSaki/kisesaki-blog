package com.kisesaki.blog.content.interaction.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.kisesaki.blog.content.interaction.entity.Likes.TargetType;

/**
 * PostgreSQL target_type 枚举类型处理器
 * 用于处理 Java 枚举类型与 PostgreSQL 枚举类型之间的转换
 */
@MappedTypes(TargetType.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class TargetTypeHandler extends BaseTypeHandler<TargetType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, TargetType parameter, JdbcType jdbcType)
            throws SQLException {
        // 将 Java 枚举转换为 PostgreSQL 枚举类型
        ps.setObject(i, parameter.name(), Types.OTHER);
    }

    @Override
    public TargetType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : TargetType.valueOf(value);
    }

    @Override
    public TargetType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : TargetType.valueOf(value);
    }

    @Override
    public TargetType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : TargetType.valueOf(value);
    }
}