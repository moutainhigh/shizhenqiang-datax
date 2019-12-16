package com.alibaba.datax.plugin.rdbms.util;

import java.sql.Connection;

/**
 * Date: 15/3/16 下午3:12
 */
public class JdbcConnectionFactory implements ConnectionFactory {

    private DataBaseType dataBaseType;

    private String jdbcUrl;

    private String userName;

    private String password;

    public JdbcConnectionFactory(DataBaseType dataBaseType, String jdbcUrl, String userName, String password) {
        this.dataBaseType = dataBaseType;
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnecttion() {
        return DBUtil.getConnection(dataBaseType, jdbcUrl, userName, password);
    }

    public Connection getConnecttionWithoutRetry() {
        return DBUtil.getConnectionWithoutRetry(dataBaseType, jdbcUrl, userName, password);
    }

    public String getConnectionInfo() {
        return "jdbcUrl:" + jdbcUrl;
    }
}
