package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class SchemaOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String schemaName = "test_schema";
    private String baseVerifySchemaSQL = "select * from information_schema.schemata where schema_name='%s'";
    private String verifySchemaSQL = String.format(baseVerifySchemaSQL, schemaName);
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public SchemaOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|SchemaOperatorTest", WebSocketTest.session);
        setUp();
        createSchema();
        alterSchemaName();
        alterSchemaOwner();
        dropSchema();
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

    /**
     * @throws SQLException
     */
    @Test
    void createSchema() {
        SqlConstant.socket.onMessage("methodName|createSchema", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            SchemaOperator.createSchema(connection, schemaName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            if (rst.next()) {
                assertEquals(schemaName, rst.getString("schema_name"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            SchemaOperator.dropSchema(connection, schemaName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * @throws SQLException
     */
    @Test
    void alterSchemaName() {
        SqlConstant.socket.onMessage("methodName|alterSchemaName", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newName = "new_schema";
        try {
            SchemaOperator.createSchema(connection, schemaName);
            SchemaOperator.alterSchemaName(connection, schemaName, newName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            if (rst.next()) {
                assertEquals(newName, rst.getString("schema_name"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            SchemaOperator.dropSchema(connection, newName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * @throws SQLException
     */
    @Test
    void alterSchemaOwner() {
        SqlConstant.socket.onMessage("methodName|alterSchemaOwner", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newOnwer = "gpadmin";
        try {
            SchemaOperator.createSchema(connection, schemaName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            if (rst.next()) {
                assertNotEquals(newOnwer, rst.getString("schema_owner"));
            }
            SchemaOperator.alterSchemaOwner(connection, schemaName, newOnwer);
            rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            if (rst.next()) {
                assertEquals(newOnwer, rst.getString("schema_owner"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            SchemaOperator.dropSchema(connection, schemaName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * @throws SQLException
     */
    @Test
    void dropSchema() {
        SqlConstant.socket.onMessage("methodName|dropSchema", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            SchemaOperator.createSchema(connection, schemaName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            if (rst.next()) {
                assertEquals(schemaName, rst.getString("schema_name"));
            }
            SchemaOperator.dropSchema(connection, schemaName);
            rst = QueryOperator.freeQuery(connection, verifySchemaSQL);
            assertFalse(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            SchemaOperator.dropSchema(connection, schemaName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}