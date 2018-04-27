package test;

import org.junit.jupiter.api.*;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String tablename = "test_table";
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public UpdateOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|UpdateOperatorTest", WebSocketTest.session);
        setUpTestTable();
        setUp();
        updateSingleRowData();
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
    void updateSingleRowData() {
        SqlConstant.socket.onMessage("methodName|updateSingleRowData", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String charColumnContent = "No.5 DBTestUseCaseObject.";
        long bigIntColumnContent = 50000;
        float numericColumnContent = 500;
        Timestamp timeColumnContent = new Timestamp(20180101);
        DBTestUseCaseObject testObj = new DBTestUseCaseObject(charColumnContent, bigIntColumnContent, numericColumnContent, timeColumnContent);
        try {
            UpdateOperator.updateData(connection, testObj, 5);
            String querySQL = "Select * from test_table where id = 5";
            ResultSet rst = QueryOperator.freeQuery(connection, querySQL);
            if (rst.next()) {
                assertTrue(testObj.getTimeColumn().equals(rst.getTimestamp("time_column")));
            } else {
                fail("Error Happened");
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}