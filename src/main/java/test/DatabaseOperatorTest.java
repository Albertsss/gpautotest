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
 * DatabaseOperator test class. Two query statements
 * have been specified to query database name and owner
 * through system catalogs. Connections will be obtained
 * before each test and return to connection pool after
 * test.
 * DatabaseOperator测试类。使用两个系统表查询语句来查询数据库名称
 * 及所有者。连接再每个测试之前被获取，测试结束后释放（放回连接池）。
 */
public class DatabaseOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String dbName = "test_db";
    private String baseDBNameSQL = "SELECT d.datname as dbname FROM pg_catalog.pg_database d WHERE d.datname = '%s'";
    private String baseDBOwnerSQL = "SELECT d.datname as dbname, pg_catalog.pg_get_userbyid(d.datdba) as owner FROM pg_catalog.pg_database d WHERE d.datname = '%s' ORDER BY 1";
    private String verifyNameSQL =  String.format(baseDBNameSQL, dbName);
    private String verifyOwnerSQL =  String.format(baseDBOwnerSQL, dbName);

    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间


    public DatabaseOperatorTest() {
    }

    public void startTest() {
        SqlConstant.socket.onMessage("className|DatabaseOperatorTest", WebSocketTest.session);
        setUp();
        createDatabase();
        alterDatabaseName();
        alterDatabaseOwner();
        dropDatabase();
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
     * Test createDatabase() method.
     * Another way to verify the creation of a database is
     * using a new connection that connects to the newly created
     * database directly. If the connection object is not null,
     * the database was created successfully. By doing so, we use
     * less database methods. The method presented below verifies the
     * exist of the new database by searching through the system catalog.
     * 测试createDatabase()方法。
     * 另一个验证数据库是否创建成功的方法是通过新建一个数据库连接。
     * 如果连接变量不为空，数据库存在。这样做，能用到更少的数据库操作，
     * 降低耦合度。以下的方法通过查询系统表来验证。
     *
     * @throws SQLException
     */
    @Test
    void createDatabase() {
        SqlConstant.socket.onMessage("methodName|createDatabase", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            DatabaseOperator.createDatabase(connection, dbName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            rst.next();
            assertEquals(dbName, rst.getString("dbname"));
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DatabaseOperator.dropDatabase(connection, dbName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * Test alterDatabaseName() method.
     * In finally clause, there are two drop statements.
     * By doing so, we can prevent unexpected break of
     * this test.
     * 测试alterDatabaseName()方法。
     * 再finally中执行了两个DROP操作，目的是为了防止意外
     * 导致测试中断，测试数据库不能被正确删除。
     *
     * @throws SQLException
     */
    @Test
    void alterDatabaseName() {
        SqlConstant.socket.onMessage("methodName|alterDatabaseName", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newName = "new_db";
        try {
            DatabaseOperator.createDatabase(connection, dbName);
            DatabaseOperator.alterDatabaseName(connection, dbName, newName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            if (rst.next()) {
                assertEquals(newName, rst.getString("dbname"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DatabaseOperator.dropDatabase(connection, dbName);
            DatabaseOperator.dropDatabase(connection, newName);
        }
        TestResultUtil.returnTestResult(startTime,status);

    }

    /**
     * Test of alterDatabaseOwner() method. The owner,
     * gpadmin, is the default user of Greenplum.
     * 测试alterDatabaseOwner()方法。gpadmin是Greenplum
     * 的默认用户。
     *
     * @throws SQLException
     */
    @Test
    void alterDatabaseOwner() {
        SqlConstant.socket.onMessage("methodName|alterDatabaseOwner", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newOwner = "gpadmin";
        try {
            DatabaseOperator.createDatabase(connection, dbName);
            DatabaseOperator.alterDatabaseOwner(connection, dbName, newOwner);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyOwnerSQL);
            if (rst.next()) {
                assertEquals(newOwner, rst.getString("owner"));
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DatabaseOperator.dropDatabase(connection, dbName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * Test dropDatabase() method. Verify the target database's
     * existence via system catalog.
     * 测试dropDatabase()方法。通过系统表检查数据库是否存在。
     *
     * @throws SQLException
     */
    @Test
    void dropDatabase() {
        SqlConstant.socket.onMessage("methodName|dropDatabase", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            DatabaseOperator.createDatabase(connection, dbName);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            rst.next();
            assertEquals(dbName, rst.getString("dbname"));

            DatabaseOperator.dropDatabase(connection, dbName);
            rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            assertFalse(rst.next());
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DatabaseOperator.dropDatabase(connection, dbName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }
}