package test;

import java.sql.*;

public class DeleteOperator {
    public static void deleteData(Connection connection, int id) throws SQLException {
        String deleteSQL = "DELETE FROM test_table WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }
    public static void deleteRowData(Connection connection, String deleteSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(deleteSQL);
        stmt.close();
    }
}
