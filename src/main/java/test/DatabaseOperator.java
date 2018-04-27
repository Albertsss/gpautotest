package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Operations to modify databases. Only create database,
 * drop database, alter database name and owner operations
 * have been implemented. No return value so far.
 * Only users with corresponding privileges can
 * do these operations.
 * 数据库级操作。只有简单实现创建、删除数据库，更改数据库名称和数据库所有者。
 * 目前实现的方法没有返回值。可以修改以便未来使用。
 */
public class DatabaseOperator {

    /**
     * Create database (without any parameter).
     * The implementation of ParameterStatement does not
     * allow users to modify parameters of database name
     * (schema or something similar) via set methods.
     * Thus we code the SQL statements in plain text.
     * 数据库创建操作，没有额外的参数。
     * 由于PostgreSQL (包括Greenplum)的ParameterStatement创建、删除
     * 的实现不允许通过SET函数来修改数据库名称等变量，只能写死语句再执行。
     * 用户需要有相应的权限。
     *
     * @param connection
     * @param dbName
     * @throws SQLException
     */
    public static void createDatabase(Connection connection, String dbName) throws SQLException {
        String baseCreateDBSQL = "CREATE DATABASE %s";
        String createDBSQL = String.format(baseCreateDBSQL, dbName);
        Statement stmt = connection.createStatement();
        boolean rst = stmt.execute(createDBSQL);
        stmt.close();
    }

    /**
     * Modify database name.
     * 修改数据库名称。
     *
     * @param connection
     * @param oldDBName
     * @param newDBName
     * @throws SQLException
     */
    public static void alterDatabaseName(Connection connection, String oldDBName, String newDBName) {
        String baseAlterDBSQL = "ALTER DATABASE %s RENAME TO %s";
        String alterDBSQL = String.format(baseAlterDBSQL, oldDBName, newDBName);
        Statement stmt;
        try {
            stmt = connection.createStatement();
            boolean rst = stmt.execute(alterDBSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modify database owner.
     * 修改数据库所有者。
     *
     * @param connection
     * @param dbName
     * @param newOnwer
     * @throws SQLException
     */
    public static void alterDatabaseOwner(Connection connection, String dbName, String newOnwer) {
        String baseAlterDBSQL = "ALTER DATABASE %s OWNER TO %s";
        String alterDBSQL = String.format(baseAlterDBSQL, dbName, newOnwer);
        Statement stmt;
        try {
            stmt = connection.createStatement();
            boolean rst = stmt.execute(alterDBSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an existing database.
     * 删除一个存在的数据库。
     *
     * @param connection
     * @param dbName
     * @throws SQLException
     */
    public static void dropDatabase(Connection connection, String dbName) {
        //Bad syntax since this has to suffer SQL injection risk
        String baseDropDBSql = "DROP DATABASE IF EXISTS %s";
        String dropDBSQL = String.format(baseDropDBSql, dbName);
        Statement stmt;
        try {
            stmt = connection.createStatement();
            boolean rst = stmt.execute(dropDBSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
