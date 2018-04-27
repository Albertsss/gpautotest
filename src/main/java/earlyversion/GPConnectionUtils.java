package earlyversion;

import java.sql.*;

public class GPConnectionUtils {

    public static Connection conn = null;
//    public static String url="jdbc:pivotal:greenplum://192.168.60.129:5432;DatabaseName=postgres";
    public static String url="jdbc:pivotal:greenplum://"+GPConfig.masterip+":"+GPConfig.gpport+";DatabaseName=postgres";

    public static Connection getConn()
    {
        if(conn == null)
        {
            try {
                //Class.forName("org.postgresql.Driver");
                Class.forName("com.pivotal.jdbc.GreenplumDriver");
                //String url = "jdbc:postgresql://192.168.60.129:5432/linewell";
//                conn = DriverManager.getConnection(url, "gpadmin", "linewell@minielectric");
                conn = DriverManager.getConnection(url, GPConfig.gpuser, GPConfig.gppwd);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }


    public static void close(ResultSet rs, PreparedStatement ps){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}