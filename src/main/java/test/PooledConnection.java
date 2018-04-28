package test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import utils.SqlConstant;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton implementation to use database connection pool.
 * 使用数据库连接池的单例实现。
 */
public class PooledConnection {
    private static  PooledConnection instance;
    private static ComboPooledDataSource comboPooledDataSource;

    private String url = "jdbc:pivotal:greenplum://"+ SqlConstant.masterIP+":"+SqlConstant.gpPort+";DatabaseName=postgres";
    private String user = SqlConstant.gpUser;
    private String pwd = SqlConstant.gpPwd;

    static {
        try {
            Class.forName("com.pivotal.jdbc.GreenplumDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
            e.printStackTrace();
        }
    }

    /**
     * Constructor. Since we are not going to test the c3p0
     * library we don't provide methods to modify c3p0 parameters.
     * This job should only be done by those who have privileges
     * to manage the database.
     * 构造器。没有提供配置C3P0库的方法。修改连接配置应该由有管理数据库权限的
     * 人来执行。
     *
     * @throws PropertyVetoException
     */
    private PooledConnection() throws PropertyVetoException {

        comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass("com.pivotal.jdbc.GreenplumDriver");
        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(pwd);
        comboPooledDataSource.setMaxPoolSize(40);
        comboPooledDataSource.setMinPoolSize(2);
        comboPooledDataSource.setInitialPoolSize(5);
        comboPooledDataSource.setAcquireIncrement(5);
        //
        comboPooledDataSource.setIdleConnectionTestPeriod(60);
        //
        comboPooledDataSource.setMaxIdleTime(3600);
        //
        comboPooledDataSource.setMaxStatements(200);
        //
        comboPooledDataSource.setAcquireRetryAttempts(30);
        //
        comboPooledDataSource.setAcquireRetryDelay(1000);
        comboPooledDataSource.setAutoCommitOnClose(true);
        //
        comboPooledDataSource.setBreakAfterAcquireFailure(true);
    }

    /**
     * The instance will be initialized when getConnectionPool()
     * is called and the instance is NULL, otherwise it will
     * directly return the existing instance.
     * 当getConnectionPool()被调用且instance为NULL时才会初始化连接池。
     * 否则直接返回现有的instance。
     *
     * @return
     */
    public static PooledConnection getConnectionPool() {
        if (instance == null) {
            try {
                instance = new PooledConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Get synchronized connection to prevent dead lock.
     * 获取同步连接，防止死锁。
     *
     * @return
     */
    public synchronized Connection getConnection() {
        try {
            return comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tell jvm to release connections (the comboPooledDataSource)
     * explicitly.
     * 通知JVM释放链接，进行GC。
     * 
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        DataSources.destroy(comboPooledDataSource);
        super.finalize();
    }
}
