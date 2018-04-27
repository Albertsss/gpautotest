package test;

import java.sql.*;

public class UpdateOperator {
    public static void updateData(Connection connection, DBTestUseCaseObject testObj, int id) throws SQLException {
        String updateSQL = "UPDATE test_table SET time_column = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateSQL);
        pstmt.setTimestamp(1, testObj.getTimeColumn());
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static void updateRowData(Connection connection, String updateSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(updateSQL);
        stmt.close();
    }
}
