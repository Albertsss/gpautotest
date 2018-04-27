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

public class QueryOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public QueryOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|QueryOperatorTest", WebSocketTest.session);
        setUp();
        freeQuery();
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
    void freeQuery() {
        SqlConstant.socket.onMessage("methodName|freeQuery", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String name = "fen";
        String verifyNameSQL =  String.format("SELECT d.datname as dbname FROM pg_catalog.pg_database d WHERE d.datname = '%s'", name);
        ResultSet rst = null;
        try {
            rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            if (rst.next()) {
                assertEquals(rst.getString("dbname"), name);
            }
            rst.close();
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}