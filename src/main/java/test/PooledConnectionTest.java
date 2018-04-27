package test;

import org.junit.jupiter.api.Test;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify PooledConnection class.
 * Connection and connection pool should be released
 * after every test. But we haven't implement this
 * due to the lack of garbage collection understanding.
 * PooledConnection的测试类。理论上，数据库连接池和连接在每个
 * 测试结束后都应该被释放。因为个人原因暂时没有实现。
 */
public class PooledConnectionTest {
    private PooledConnection connectionPool;
    private Connection connection;
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public void startTest(){
        SqlConstant.socket.onMessage("className|PooledConnectionTest", WebSocketTest.session);
        getConnectionPool();
        getConnection();
    }
    /**
     * Will be null if the connection pool is not
     * initialized properly.
     * 如果获取失败，连接池对象将为NULL。
     */
    @Test
    void getConnectionPool() {
        SqlConstant.socket.onMessage("methodName|getConnectionPool", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        connectionPool = PooledConnection.getConnectionPool();
        assertNotNull(connectionPool);
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * 通过连接池单例对象，获取连接。
     * @throws SQLException
     */
    @Test
    void getConnection() {
        SqlConstant.socket.onMessage("methodName|getConnection", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            connectionPool = PooledConnection.getConnectionPool();
            connection = connectionPool.getConnection();
            assertNotNull(connection);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                status = "failed";
                ExceptionUtil.exceptionPrint(e);
            }
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * Wait for implementation in the future.
     * 暂未实现。
     */
    @Test
    void inspectFinalizeMethod() {
    }
}