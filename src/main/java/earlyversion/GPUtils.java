package earlyversion;

import websocket.WebSocketTest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 14:08 2018/4/9
 * @Modified By:
 */
public class GPUtils {

    public static void printText(String text){
        try {
            int a=text.getBytes("gbk").length;
            String words="++                              ++";
            StringBuilder sb=new StringBuilder(words);
            sb.replace((34-a)/2,(34+a)/2,text);
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println(sb);
            System.out.println("++++++++++++++++++++++++++++++++++");
//            earlyversion.GPConfig.socket.onMessage("\n",websocket.WebSocketTest.session);
//            earlyversion.GPConfig.socket.onMessage("++++++++++++++++++++++++++++++++++",websocket.WebSocketTest.session);
//            earlyversion.GPConfig.socket.onMessage(sb.toString(),websocket.WebSocketTest.session);
//            earlyversion.GPConfig.socket.onMessage("++++++++++++++++++++++++++++++++++",websocket.WebSocketTest.session);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }

    public static String selectEmployees(String sql, Connection con, PreparedStatement ps, ResultSet rs){
        String success="passed";
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("id")+"--"+rs.getString("name")+"--"+
                        rs.getString("age")+"--"+rs.getString("address")+"--"+rs.getString("salary"));
                GPConfig.socket.onMessage("psql|"+rs.getString("id")+"--"+rs.getString("name")+"--"+
                        rs.getString("age")+"--"+rs.getString("address")+"--"+rs.getString("salary"), WebSocketTest.session);
            }
        } catch (SQLException e) {
            success="failed";
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String msg=sw.toString();
            GPConfig.socket.onMessage("errorMsg|"+msg,WebSocketTest.session);
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
        return success;
    }

    public static String printJoin(String sql, Connection con, PreparedStatement ps, ResultSet rs){
        String success="passed";
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("id")+"--"+rs.getString("name")+"--"+
                rs.getString("dept"));
//                earlyversion.GPConfig.socket.onMessage(rs.getString("id")+"--"+rs.getString("name")+"--"+
//                        rs.getString("dept"),websocket.WebSocketTest.session);
            }
        } catch (SQLException e) {
            success="failed";
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String msg=sw.toString();
            GPConfig.socket.onMessage("errorMsg|"+msg,WebSocketTest.session);
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
        return success;
    }


}
