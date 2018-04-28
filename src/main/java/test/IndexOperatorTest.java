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
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 15:06 2018/4/28
 * @Modified By:
 */
public class IndexOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String indexName = "test_table_id_index";
    private String tableName = "test_table";
    private String columnName = "id";
    private String baseVerifyVSQL = "select * from pg_indexes where tablename='%s' and indexname='%s'";
    private String verifyIndexSQL = String.format(baseVerifyVSQL,tableName,indexName);
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    public IndexOperatorTest(){}

    public void startTest(){
        SqlConstant.socket.onMessage("className|IndexOperatorTest", WebSocketTest.session);
        setUpTestTable();
        setUp();
        createIndex();
        tearDown();
        setUp();
        reindexIndex();
        tearDown();
        setUp();
        dropIndex();
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
    void createIndex(){
        SqlConstant.socket.onMessage("methodName|createIndex", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            IndexOperator.createIndex(connection,indexName,tableName,columnName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyIndexSQL);
            if (rst.next()) {
                assertEquals(indexName, rst.getString("indexname"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            IndexOperator.dropIndex(connection,indexName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * 重置索引的效果还没检测
     */
    @Test
    void reindexIndex(){
        SqlConstant.socket.onMessage("methodName|reindexIndex", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            IndexOperator.createIndex(connection,indexName,tableName,columnName);
            IndexOperator.reindexIndex(connection,indexName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyIndexSQL);
            if (rst.next()) {
                assertEquals(indexName, rst.getString("indexname"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            IndexOperator.dropIndex(connection,indexName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void dropIndex(){
        SqlConstant.socket.onMessage("methodName|dropIndex", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            IndexOperator.createIndex(connection,indexName,tableName,columnName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyIndexSQL);
            if (rst.next()) {
                assertEquals(indexName, rst.getString("indexname"));
            }
            IndexOperator.dropIndex(connection,indexName);
            rst = QueryOperator.freeQuery(connection, verifyIndexSQL);
            assertFalse(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}
