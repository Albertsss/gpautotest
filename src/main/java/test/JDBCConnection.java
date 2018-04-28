package test;

import utils.SqlConstant;

import java.sql.*;

/**
 * This class is used to generate and obtain database connection.
 * To specify the host, modify url.
 * 用于获取数据库连接。要修改连接的HOST，修改url。
 */
class JDBCConnection
{
    private String url = "jdbc:pivotal:greenplum://"+ SqlConstant.masterIP+":"+SqlConstant.gpPort+";DatabaseName=";
    private String defaultDB = "postgres";
    private String user = SqlConstant.gpUser;
    private String pwd = SqlConstant.gpPwd;
    private Connection connection;

    static {
        try {
            Class.forName("com.pivotal.jdbc.GreenplumDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Default constructor.
     * 默认构造器。
     *
     * @throws SQLException
     */
    public JDBCConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url + defaultDB, user, pwd);
    }

    /**
     * Constructor that can specify a target database.
     * 重载构造器，可以指定目标数据库。
     *
     * @param dbName
     * @throws SQLException
     */
    public JDBCConnection(String dbName) throws SQLException {
        this.connection = DriverManager.getConnection(url + dbName, user, pwd);
    }

    /**
     * Haven't been used so far.
     * 尚未使用。
     *
     * @param dbName
     * @param user
     * @param pwd
     * @throws SQLException
     */
    public JDBCConnection(String dbName, String user, String pwd) throws SQLException {
        this.connection = DriverManager.getConnection(url + dbName, user, pwd);
    }

    /**
     * Method to verify version of the Greenplum database Driver in use.
     * Can change the return value to the driver version for further usage.
     * 用于验证当前数据库驱动版本。可以修改返回值以便更广泛的用途。
     *
     * @param connection The connection to inspect.
     * @throws SQLException
     */
    public static void printDriverVersion(Connection connection) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("JDBC driver version is " + meta.getDriverVersion());
    }

    /**
     * Method to get connection.
     * 用于获取连接。
     *
     * @return Get a connection according to the current settings.
     */
    public Connection getConnection() {
        return this.connection;
    }
}
