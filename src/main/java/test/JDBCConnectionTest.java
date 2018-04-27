package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ExceptionUtil;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class used to test JDBCConnection class.
 * Within @BeforeEach and @AfterEach, we don't initialize
 * the connection so that users can specify tests more freely
 * in the future.
 * JDBCConnection测试类。@BeforeEach 和 @AfterEach暂时未空。
 * 连接在测试内才进行初始化，以便之后调整。
 */
public class JDBCConnectionTest {
    Connection connection;
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    @Test
    public void startTest(){
        SqlConstant.socket.onMessage("className|JDBCConnectionTest", WebSocketTest.session);
//        printDriverVersion();
        getConnection();
        testSingleParameterConstructor();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Test if the printDriverVersion() works properly
     * via comparing the System output with the given
     * pattern (specified by methodOutputStartsWith).
     * The variable, methodOutputStartWith, could be
     * specified accordingly.
     * Also, the connection object could be specified
     * by need.
     * 测试printDriverVersion()是否能正确验证驱动版本。
     * 该方法通过比较给定的字符串和系统输出来判断结果。
     * methodOutputStartWith和连接对象都可以重新指定。
     *
     * @throws SQLException
     */
    @Test
    void printDriverVersion() {
        try {
            final ByteArrayOutputStream systemOutputContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(systemOutputContent));
            connection = new JDBCConnection().getConnection();
            JDBCConnection.printDriverVersion(connection);
            String methodOutputStartsWith = "JDBC driver version is";
            assertTrue(systemOutputContent.toString().startsWith(methodOutputStartsWith));
            System.setOut(System.out);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initialize a new database connection and verify
     * the method by checking if the connection variable
     * is null.
     * 初始化新的连接并通过变量是否为NULL来判断方法是否有效。
     *
     * @throws SQLException
     */
    @Test
    void getConnection() {
        SqlConstant.socket.onMessage("methodName|getConnection", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            connection = new JDBCConnection().getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * Verify the single parameter constructor. The postgres database
     * is the default created database of PostgreSQL.
     *
     * @throws SQLException
     */
    @Test
    void testSingleParameterConstructor() {
        SqlConstant.socket.onMessage("methodName|testSingleParameterConstructor", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String dbNmae = "postgres";
        try {
            connection = new JDBCConnection(dbNmae).getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}