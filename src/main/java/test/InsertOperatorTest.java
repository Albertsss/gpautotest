package test;

import org.junit.jupiter.api.*;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class InsertOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String tablename = "test_table";
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public InsertOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|InsertOperatorTest", WebSocketTest.session);
        setUpTestTable();
        setUp();
        insertData();
        tearDown();
        setUp();
        insertRawData();
        tearDown();
        tearDownTestTable();
    }

    @BeforeAll
    static void setUpTestTable() {
        try {
            TestDataSetBuilder.prepareTestDataSet(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownTestTable() {
        Connection connection = null;
        try {
            connection = new JDBCConnection().getConnection();
            TableOperator.dropTable(connection, "test_table");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
        connection = pool.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertData() {
        SqlConstant.socket.onMessage("methodName|insertData", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        DBTestUseCaseObject testObj = new DBTestUseCaseObject();
        try {
            InsertOperator.insertData(connection, tablename, testObj);
            String querySQL = "Select * from test_table";
            ResultSet rst = QueryOperator.freeQuery(connection, querySQL);
            assertTrue(rst.next() && 1 == rst.getInt(1));
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void insertRawData() {
        SqlConstant.socket.onMessage("methodName|insertRawData", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        TestResultUtil.returnTestResult(startTime,status);
    }
}