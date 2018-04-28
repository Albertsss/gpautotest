package test;

import org.junit.jupiter.api.*;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 *
 */
public class ViewOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String viewName = "test_table_view";
    private String newViewName = "test_table_view_new";
    private String baseVerifyVSQL = "select * from pg_views where viewname='%s'";
    private String verifyViewSQL = String.format(baseVerifyVSQL, viewName);
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间
//    private String baseCountColumnSQL = "select count(*) from information_schema.columns where table_name='%s'";
//    private String CountColumnSQL = String.format(baseCountColumnSQL, viewName);

    public ViewOperatorTest(){}

    public void startTest(){
        SqlConstant.socket.onMessage("className|ViewOperatorTest", WebSocketTest.session);
        setUpTestTable();
        setUp();
        createView();
        tearDown();
        setUp();
        alterView();
        tearDown();
        setUp();
        dropView();
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
    void setUp(){ connection = pool.getConnection();}

    @AfterEach
    void tearDown(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void createView(){
        SqlConstant.socket.onMessage("methodName|createView", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            ViewOperator.createView(connection,viewName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyViewSQL);
            if (rst.next()) {
                assertEquals(viewName, rst.getString("viewname"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            ViewOperator.dropView(connection,viewName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void alterView(){
        SqlConstant.socket.onMessage("methodName|alterView", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            ViewOperator.createView(connection,viewName);
            ViewOperator.alterView(connection,viewName,newViewName);
            ResultSet rst = QueryOperator.freeQuery(connection, String.format(baseVerifyVSQL, newViewName));
            if(rst.next()){
                assertEquals(newViewName, rst.getString("viewname"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            ViewOperator.dropView(connection,newViewName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void dropView(){
        SqlConstant.socket.onMessage("methodName|dropView", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            ViewOperator.createView(connection,viewName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyViewSQL);
            if (rst.next()) {
                System.out.println(rst.getString("viewname"));
                assertEquals(viewName, rst.getString("viewname"));
            }
            ViewOperator.dropView(connection,viewName);
            rst = QueryOperator.freeQuery(connection, verifyViewSQL);
            assertFalse(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}
