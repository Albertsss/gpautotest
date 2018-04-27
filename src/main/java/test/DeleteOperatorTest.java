package test;

import org.junit.jupiter.api.*;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String tablename = "test_table";
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public DeleteOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|DeleteOperatorTest", WebSocketTest.session);
        setUpTestTable();
        setUp();
        deleteData();
        tearDown();
        setUp();
        deleteRowData();
        tearDown();
        tearDownTestTable();
    }

    @BeforeAll
    static void setUpTestTable() {
        try {
            TestDataSetBuilder.prepareTestDataSet(true);
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
    void deleteData() {
        SqlConstant.socket.onMessage("methodName|deleteData", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        int id = 5;
        try {
            DeleteOperator.deleteData(connection, id);
            String verifySQL = "SELECT * FROM test_table WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(verifySQL);
            pstmt.setInt(1, id);
            ResultSet rst = pstmt.executeQuery();
            assertFalse(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void deleteRowData() {
        SqlConstant.socket.onMessage("methodName|deleteRowData", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        TestResultUtil.returnTestResult(startTime,status);
    }
}