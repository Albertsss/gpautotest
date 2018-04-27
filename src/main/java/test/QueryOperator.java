package test;

import utils.SqlConstant;
import websocket.WebSocketTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryOperator {
    public static ResultSet freeQuery(Connection connection, String querySQL) throws SQLException {
        System.out.println(querySQL);
        SqlConstant.socket.onMessage("psql|"+SqlConstant.foreText+querySQL, WebSocketTest.session);
        Statement stmt = connection.createStatement();
        ResultSet rst = stmt.executeQuery(querySQL);
        return rst;
    }
}
