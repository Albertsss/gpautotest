package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class ViewOperator {

    /**
     * @param connection
     * @param viewName
     * @throws SQLException
     */
    public static void createView(Connection connection, String viewName) throws SQLException {
        String baseCreateViewSQL = "CREATE VIEW %s as select * from test_table";
        String createViewSQL = String.format(baseCreateViewSQL,viewName);
        Statement stmt = connection.createStatement();
        stmt.execute(createViewSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param oldViewName
     * @param newViewName
     * @throws SQLException
     */
    public static void alterView(Connection connection,String oldViewName,String newViewName) throws SQLException {
        String baseAlterViewSQL = "ALTER VIEW %s RENAME TO %s;";
        String alterViewSQL = String.format(baseAlterViewSQL, oldViewName, newViewName);
        Statement stmt = connection.createStatement();
        stmt.execute(alterViewSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param viewName
     * @throws SQLException
     */
    public static void dropView(Connection connection,String viewName) {
        String baseDropViewSQL = "DROP VIEW %s";
        String dropViewSQL = String.format(baseDropViewSQL, viewName);
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(dropViewSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
