package utils;

import earlyversion.GPConfig;
import websocket.WebSocketTest;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 */
public class ExceptionUtil {

    public static void exceptionPrint(Exception e){
        e.printStackTrace();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String msg=sw.toString();
        SqlConstant.socket.onMessage("errorMsg|"+msg, WebSocketTest.session);
    }
}
