package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Schema operation implementation. Four operations,
 * create\alter name\alter owner\drop, have been implemented.
 * No return value so far. And nly users with corresponding
 * privileges can do these operations. PrepareStatement
 * does not allow users to assign value to database name,
 * schema name and something similar for security reasons.
 * Schema操作类。
 */
public class SchemaOperator {

    /**
     * @param connection
     * @param schemaName
     * @throws SQLException
     */
    public static void createSchema(Connection connection, String schemaName) throws SQLException {
        String baseCreateSchemaSQL = "CREATE SCHEMA %s";
        String createSchemaSQL = String.format(baseCreateSchemaSQL, schemaName);
        Statement stmt = connection.createStatement();
        stmt.execute(createSchemaSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param oldSchemaName
     * @param newSchemaName
     * @throws SQLException
     */
    public static void alterSchemaName(Connection connection, String oldSchemaName, String newSchemaName) throws SQLException {
        String baseAlterSchemaNameSQL = "ALTER SCHEMA %s RENAME TO %s";
        String alterSchemaNameSQL = String.format(baseAlterSchemaNameSQL, oldSchemaName, newSchemaName);
        Statement stmt = connection.createStatement();
        stmt.execute(alterSchemaNameSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param schemaName
     * @param schemaOwner
     * @throws SQLException
     */
    public static void alterSchemaOwner(Connection connection, String schemaName, String schemaOwner) throws SQLException {
        String baseAlterSchemaOwnerSQL = "ALTER SCHEMA %s OWNER TO %s";
        String alterSchemaOwnerSQL = String.format(baseAlterSchemaOwnerSQL, schemaName, schemaOwner);
        Statement stmt = connection.createStatement();
        stmt.execute(alterSchemaOwnerSQL);
        stmt.close();
    }

    /**
     * CASCADE and RESTRICT key words are not tested.
     * They should be tested with and without privileges to access the target database.
     * We also don't verify if we could drop a schema without ownership.
     */
    public static void dropSchema(Connection connection, String schemaName) {
        String baseDropSchemaSQL = "DROP SCHEMA IF EXISTS %s";
        String dropSchemaSQL = String.format(baseDropSchemaSQL, schemaName);
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(dropSchemaSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
