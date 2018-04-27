package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TableOperator {
    public static void createTable(Connection connection, String createTableSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(createTableSQL);
        stmt.close();
    }

    public static void truncateTable(Connection connection, String tableName) throws SQLException {
        String baseTruncateSQL = "TRUNCATE %s";
        String truncateSQL = String.format(baseTruncateSQL, tableName);
        Statement stmt = connection.createStatement();
        stmt.execute(truncateSQL);
        stmt.close();
    }

    public static void alterTable(Connection connection, String alterTableSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(alterTableSQL);
        stmt.close();
    }

    public static void dropTable(Connection connection, String tableName) {
        String baseDropSQL = "DROP TABLE IF EXISTS %s";
        String dropSQL = String.format(baseDropSQL, tableName);
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(dropSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
