package utils;

import websocket.WebSocketTest;

/**
 * 常量类
 */
public class SqlConstant {
    public static final String foreText="psql---->";
    public static String masterIP = "192.168.60.129";       //master主机ip，默认192.168.60.129
    public static String gpPort = "5432";         //gp端口，默认5432
    public static String gpUser = "gpadmin";         //gp登录名，默认gpadmin
    public static String rootPwd = "linewell@123";        //master主机root密码，默认linewell@123
    public static String gpPwd = "linewell@minielectric";          //gp密码，默认linewell@minielectric
    public static String gpadminPwd = "gpadmin";     //master主机gpadmin密码，默认gpadmin
    public static WebSocketTest socket = new WebSocketTest();

}
