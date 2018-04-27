package utils;

import websocket.WebSocketTest;

/**
 * 返回测试相关信息的工具类
 */
public class TestResultUtil {

    private static long endTime;   //方法结束的时间
    private static double mTime;   //方法所用时间
    private static String time;    //方法所用时间（格式化后）

    public static void returnTestResult(long startTime,String status){
        endTime = System.nanoTime();
        mTime=(endTime-startTime)/1000000000.0;
        time=String.format("%.3f", mTime);
        SqlConstant.socket.onMessage("methodTime|"+time, WebSocketTest.session);
        SqlConstant.socket.onMessage("methodStatus|"+status, WebSocketTest.session);
    }
}
