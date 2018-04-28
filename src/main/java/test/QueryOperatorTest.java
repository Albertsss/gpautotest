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
        addQuery();
        reduceQuery();
        multiplyQuery();
        divideQuery();
        remainderQuery();
        powerQuery();
        equalQuery();
        notEqualQuery();
        neQuery();
        greaterThanQuery();
        greaterOrEqualQuery();
        lessThanQuery();
        lessOrEqualQuery();
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
        String name = "postgres";
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

    void queryutil(String methodName,String psql,String expectValue){
        SqlConstant.socket.onMessage("methodName|"+methodName, WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        ResultSet rst = null;
        try {
            rst = QueryOperator.freeQuery(connection,psql);
            if (rst.next()) {
                assertEquals(expectValue,rst.getString(1));
            }
            rst.close();
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void addQuery(){
        queryutil("addQuery","select 3+2","5");
    }

    @Test
    void reduceQuery(){
        queryutil("reduceQuery","select 3-2","1");
    }

    @Test
    void multiplyQuery(){
        queryutil("multiplyQuery","select 3*2","6");
    }

    @Test
    void divideQuery(){
        queryutil("divideQuery","select 3/2","1");
    }

    @Test
    void remainderQuery(){
        queryutil("remainderQuery","select 3%2","1");
    }

    @Test
    void powerQuery(){
        queryutil("powerQuery","select 3^2","9");
    }

    @Test
    void equalQuery(){
        queryutil("equalQuery","select 3=2","false");
    }

    @Test
    void notEqualQuery(){
        queryutil("notEqualQuery","select 3!=2","true");
    }

    @Test
    void neQuery(){
        queryutil("neQuery","select 3<>2","true");
    }

    @Test
    void greaterThanQuery(){
        queryutil("greaterThanQuery","select 3>2","true");
    }

    @Test
    void greaterOrEqualQuery(){
        queryutil("greaterOrEqualQuery","select 3>=2","true");
    }

    @Test
    void lessThanQuery(){
        queryutil("lessThanQuery","select 3<2","false");
    }

    @Test
    void lessOrEqualQuery(){
        queryutil("lessOrEqualQuery","select 3<=2","false");
    }
}