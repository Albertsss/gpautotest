package earlyversion;

import websocket.WebSocketTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 16:32 2018/4/18
 * @Modified By:
 */
public class GPConfig {

    public static String masterip = "192.168.60.129";       //master主机ip，默认192.168.60.129
    public static String gpport = "5432";         //gp端口，默认5432
    public static String gpuser = "gpadmin";         //gp登录名，默认gpadmin
    public static String rootpwd = "linewell@123";        //master主机root密码，默认linewell@123
    public static String gppwd = "linewell@minielectric";          //gp密码，默认linewell@minielectric
    public static String gpadminpwd = "gpadmin";     //master主机gpadmin密码，默认gpadmin
    public static List<String[]> list = new ArrayList<String[]>();  //测试结果集合
    public static WebSocketTest socket = new WebSocketTest();


}
