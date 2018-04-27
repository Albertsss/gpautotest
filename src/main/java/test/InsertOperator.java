package test;

import java.sql.*;
import java.util.Arrays;

public class InsertOperator {
    public static void insertData(Connection connection, String tableName, DBTestUseCaseObject testObj) throws SQLException {
//        String insertSQL = "INSERT INTO %s (char_column, bigint_column, numeric_column, time_column) VALUES ('%s', '%s', '%s', '%s')";
        String insertSQL = "INSERT INTO test_table (char_column, bigint_column, numeric_column, time_column) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(insertSQL);
//        pstmt.setString(1, tableName);
        pstmt.setString(1, testObj.getCharColumn());
        pstmt.setLong(2, testObj.getBigIntColumn());
        pstmt.setFloat(3, testObj.getNumericColumn());
        pstmt.setTimestamp(4, testObj.getTimeColumn());
        pstmt.executeUpdate();
        pstmt.close();
//        insertSQL = String.format(insertSQL, tableName, testObj.getCharColumn(),  testObj.getBigIntColumn(), testObj.getNumericColumn(), testObj.getTimeColumn());
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate(insertSQL);
//        stmt.close();
    }

    public static void insertRowData(Connection connection, String insertSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(insertSQL);
        stmt.close();
    }
}
