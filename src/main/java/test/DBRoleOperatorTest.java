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

public class DBRoleOperatorTest {
    protected PooledConnection pool = PooledConnection.getConnectionPool();
    protected Connection connection;
    private String rolename = "test_role";
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    private String baseCreateRoleSQL = "CREATE ROLE %s WITH LOGIN";
    private String createRoleSQL = String.format(baseCreateRoleSQL, rolename);
    private String baseVerifyRoleSQL = "SELECT rolname FROM pg_roles WHERE rolname='%s'";

    public DBRoleOperatorTest() {
    }

    public void startTest(){
        SqlConstant.socket.onMessage("className|DBRoleOperatorTest", WebSocketTest.session);
        setUp();
        createRole();
        alterRoleName();
        dropRole();
        grantPrivilege();
        revokePrivilege();
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
     * We don't try to verify this through a new connection
     * because the access privilege has to be modified
     * in pg_hba.conf before logging in the account.
     *
     * @throws SQLException
     */
    @Test
    void createRole() {
        SqlConstant.socket.onMessage("methodName|createRole", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String verifyNameSQL = String.format(baseVerifyRoleSQL, rolename);
        try {
            DBRoleOperator.createRole(connection, createRoleSQL);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            if (rst.next()) {
                assertEquals(rolename, rst.getString("rolname"));
            }else {
                fail("failed to create role");
            }
//            Connection conn = (Connection) new JDBCConnection("fen", rolename, "");
//            assertNotNull(conn);
//            conn.close();
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DBRoleOperator.dropRole(connection, rolename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void alterRoleName() {
        SqlConstant.socket.onMessage("methodName|alterRoleName", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String newRoleName = "new_role";
        String verifyNameSQL = String.format(baseVerifyRoleSQL, rolename);
        String baseAlterRoleSQL = "ALTER ROLE %s RENAME TO %s";
        String alterRoleSQL = String.format(baseAlterRoleSQL, rolename, newRoleName);
        try {
            DBRoleOperator.createRole(connection, createRoleSQL);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            rst.next();
            assertEquals(rolename, rst.getString("rolname"));

            DBRoleOperator.alterRole(connection, alterRoleSQL);
            verifyNameSQL = String.format(baseVerifyRoleSQL, newRoleName);
            rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            rst.next();
            assertEquals(newRoleName, rst.getString("rolname"));
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DBRoleOperator.dropRole(connection, rolename);
            DBRoleOperator.dropRole(connection, newRoleName);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     * Some privileges will prevent roles from being dropped.
     * JDBC won't tell details about it. Check it through SQL
     * client and we could find something like:
     * ERROR:  role "test_role" cannot be dropped because some objects depend on it
     * DETAIL:  access to database fen
     * Therefore, we should revoke some of a role's privileges
     * before dropping it.
     *
     * @throws SQLException
     */
    @Test
    void dropRole() {
        SqlConstant.socket.onMessage("methodName|dropRole", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        String verifyNameSQL = String.format(baseVerifyRoleSQL, rolename);
        try {
            DBRoleOperator.createRole(connection, createRoleSQL);
            ResultSet rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            rst.next();
            assertEquals(rolename, rst.getString("rolname"));

            DBRoleOperator.dropRole(connection, rolename);
            rst = QueryOperator.freeQuery(connection, verifyNameSQL);
            assertFalse(rst.next());
            System.out.println("arrived");
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DBRoleOperator.dropRole(connection, rolename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    private String currentDB = "postgres";
    private String baseGrantSQL = "GRANT ALL PRIVILEGES ON DATABASE %s TO %s";
    private String grantSQL = String.format(baseGrantSQL, currentDB, rolename);
    private String baseRevokeSQL = "REVOKE ALL PRIVILEGES ON DATABASE %s FROM %s";
    private String revokeSQL = String.format(baseRevokeSQL, currentDB, rolename);
    private String baseGettingPrivilegeSQL = "select datacl from pg_database where datname='%s'";
    private String gettingPrivilegeSQL = String.format(baseGettingPrivilegeSQL, currentDB);

    /**
     * We verify the granted privilege via checking it
     * in the "datacl" column of pg_database system catalog.
     * If the target database contains the access privilege
     * of the target tole, a substring in a certain format
     * will be matched. The substring within contains() function
     * should be specified according to the role and privileges
     * to be inspected.
     *
     * @throws SQLException
     */
    @Test
    void grantPrivilege() {
        SqlConstant.socket.onMessage("methodName|grantPrivilege", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            DBRoleOperator.createRole(connection, createRoleSQL);
            DBRoleOperator.grantPrivilege(connection, grantSQL);
            ResultSet rst = QueryOperator.freeQuery(connection, gettingPrivilegeSQL);
            rst.next();
            assertTrue(rst.getString("datacl").contains(rolename + "=CTc/gpadmin"));
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DBRoleOperator.revokePrivilege(connection, revokeSQL);
            DBRoleOperator.dropRole(connection, rolename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    /**
     *  Slightly overlapped with grantPrivilege() test.
     *  Will modify this in the future.
     *
     * @throws SQLException
     */
    @Test
    void revokePrivilege() {
        SqlConstant.socket.onMessage("methodName|revokePrivilege", WebSocketTest.session);
        status = "passed";
        startTime = System.nanoTime();
        try {
            DBRoleOperator.createRole(connection, createRoleSQL);
            DBRoleOperator.grantPrivilege(connection, grantSQL);
            ResultSet rst = QueryOperator.freeQuery(connection, gettingPrivilegeSQL);
            if (rst.next() && rst.getString("datacl").contains("test_role=CTc/gpadmin")) {
                DBRoleOperator.revokePrivilege(connection, revokeSQL);
                rst = QueryOperator.freeQuery(connection, gettingPrivilegeSQL);
                rst.next();
                assertFalse(rst.getString("datacl").contains("test_role=CTc/gpadmin"));
            } else {
                fail("Might failed to get database access privileges.");
            }
        } catch (SQLException e) {
            status = "failed";
            ExceptionUtil.exceptionPrint(e);
        } finally {
            DBRoleOperator.revokePrivilege(connection, revokeSQL);
            DBRoleOperator.dropRole(connection, rolename);
        }
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void setRole() {
    }
}