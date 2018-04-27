package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBRoleOperator {
    public static void createRole(Connection connection, String createRoleSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(createRoleSQL);
        stmt.close();
    }

    public static void alterRole(Connection connection, String alterRoleSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(alterRoleSQL);
        stmt.close();
    }

    public static void dropRole(Connection connection, String rolename) {
        String baseDropRoleSQL = "DROP ROLE IF EXISTS %s";
        String dropRoleSQL = String.format(baseDropRoleSQL, rolename);
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(dropRoleSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void grantPrivilege(Connection connection, String grantSQL) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(grantSQL);
        stmt.close();
    }

    public static void revokePrivilege(Connection connection, String revokeSQL) {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(revokeSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Not going to test at this stage.
     */
    public static void setRole() {}
}
