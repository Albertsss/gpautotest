package test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

public class TestDataSetBuilder {
    public static final String createTableSQL = "CREATE TABLE test_table (\n" +
            "    id              SERIAL,\n" +
            "    char_column     VARCHAR(32) DEFAULT 'no content' NOT NULL,\n" +
            "    bigint_column   BIGINT NOT NULL,\n" +
            "    numeric_column  NUMERIC(15,8) NOT NULL,\n" +
            "    time_column     TIMESTAMP WITHOUT TIME ZONE DEFAULT current_timestamp,\n" +
            "    PRIMARY KEY (id))";
    public static void prepareTestDataSet(boolean hasDataSet) throws SQLException {
        Connection connection = new JDBCConnection().getConnection();
        TableOperator.createTable(connection, createTableSQL);
        if (hasDataSet) {
            HashSet<DBTestUseCaseObject> objectSet = DBTestUseCaseObject.getTestObjectSet();
            objectSet.forEach(obj -> {
                try {
                    InsertOperator.insertData(connection, "test_table", obj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        connection.close();
    }
}
