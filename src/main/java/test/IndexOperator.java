package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class IndexOperator {

    /**
     * @param connection
     * @param indexName
     * @param tableName
     * @param columnName
     * @throws SQLException
     */
    public static void createIndex(Connection connection,String indexName,String tableName,String columnName) throws SQLException{
        String baseCreateIndexSQL = "CREATE INDEX %s ON %s(%s)";
        String createIndexSQL = String.format(baseCreateIndexSQL,indexName,tableName,columnName);
        Statement stmt = connection.createStatement();
        stmt.execute(createIndexSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param indexName
     * @throws SQLException
     */
    public static void reindexIndex(Connection connection,String indexName) throws SQLException{
        String baseReindexIndexSQL = "REINDEX INDEX %s";
        String reindexIndexSQL = String.format(baseReindexIndexSQL,indexName);
        Statement stmt = connection.createStatement();
        stmt.execute(reindexIndexSQL);
        stmt.close();
    }

    /**
     * @param connection
     * @param indexName
     * @throws SQLException
     */
    public static void dropIndex(Connection connection,String indexName) {
        String baseDropIndexSQL = "DROP INDEX %s";
        String dropIndexSQL = String.format(baseDropIndexSQL,indexName);
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(dropIndexSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
