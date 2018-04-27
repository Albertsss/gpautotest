package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TableOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String tablename = "test_table";
    private String createTableSQL = TestDataSetBuilder.createTableSQL;
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public TableOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|TableOperatorTest", WebSocketTest.session);
        setUp();
        createTable();
        truncateTable();
        alterTableName();
        alterTableColumn();
        dropTable();
        tearDown();
    }

    @BeforeEach
    void setUp() {
        connection = pool.getConnection();
    }

    @AfterEach
    void tearDown() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createTable() {
        SqlConstant.socket.onMessage("methodName|createTable", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            TableOperator.createTable(connection, createTableSQL);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rst = dbMetaData.getTables(null, null, tablename, null);
            assertTrue(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            TableOperator.dropTable(connection, tablename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void truncateTable() {
        SqlConstant.socket.onMessage("methodName|truncateTable", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            TableOperator.createTable(connection, createTableSQL);
            TableOperator.truncateTable(connection, tablename);
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            TableOperator.dropTable(connection, tablename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void alterTableName() {
        SqlConstant.socket.onMessage("methodName|alterTableName", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newTableName = "new_table_name";
        String baseAlterTableNameSQL = "ALTER TABLE %s RENAME TO %s";
        String alterTableNameSQL = String.format(baseAlterTableNameSQL, tablename, newTableName);
        try {
            TableOperator.createTable(connection, createTableSQL);
            TableOperator.alterTable(connection, alterTableNameSQL);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rst = dbMetaData.getTables(null, null, newTableName, null);
            assertTrue(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            TableOperator.dropTable(connection, tablename);
            TableOperator.dropTable(connection, newTableName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void alterTableColumn() {
        SqlConstant.socket.onMessage("methodName|alterTableColumn", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newColumnIdName = "identifier";
        String baseAlterTableColumnNameSQL = "ALTER TABLE %s RENAME COLUMN id TO %s";
        String alterTableColumnNameSQL = String.format(baseAlterTableColumnNameSQL, tablename, newColumnIdName);
        try {
            TableOperator.createTable(connection, createTableSQL);
            TableOperator.alterTable(connection, alterTableColumnNameSQL);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rst = dbMetaData.getColumns(null, null, tablename, newColumnIdName);
            assertTrue(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            TableOperator.dropTable(connection, tablename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void dropTable() {
        SqlConstant.socket.onMessage("methodName|dropTable", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            TableOperator.createTable(connection, createTableSQL);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rst = dbMetaData.getTables(null, null, tablename, null);
            if (rst.next()) {
                TableOperator.dropTable(connection, tablename);
                dbMetaData = connection.getMetaData();
                rst = dbMetaData.getTables(null, null, tablename, null);
                assertFalse(rst.next());
            } else {
                fail("Might fail to create table");
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            TableOperator.dropTable(connection, tablename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}