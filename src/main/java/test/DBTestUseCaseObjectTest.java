package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SqlConstant;
import utils.TestResultUtil;
import websocket.WebSocketTest;

import java.sql.Timestamp;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class DBTestUseCaseObjectTest {
    private DBTestUseCaseObject testObj = new DBTestUseCaseObject();
    private String status = "passed";   //方法执行的状态
    private long startTime; //方法开始的时间

    @Test
    public void startTest(){
        SqlConstant.socket.onMessage("className|DBTestUseCaseObjectTest", WebSocketTest.session);
        setUp();
        setToDefault();
        setUp();
        testOverrideConstructor();
        setUp();
        setCharColumn();
        setUp();
        setBigIntColumn();
        setUp();
        setNumericColumn();
        setUp();
        setTimeColumn();
        setUp();
        getCharColumn();
        setUp();
        getBigIntColumn();
        setUp();
        getNumericColumn();
        setUp();
        getTimeColumn();
        setUp();
        getTestObjectSet();
    }

    @BeforeEach
    void setUp() {
        testObj.setToDefault();
    }

    @Test
    void setToDefault() {
        SqlConstant.socket.onMessage("methodName|setToDefault", WebSocketTest.session);
        startTime = System.nanoTime();
        testObj.setCharColumn("test string");
        testObj.setBigIntColumn(12134567);
        testObj.setNumericColumn(10);
        testObj.setTimeColumn(new Timestamp(19941028));
        testObj.setToDefault();
        assertTrue("".equals(testObj.getCharColumn())
                && (long) 0 == testObj.getBigIntColumn()
                && (float) 0 == testObj.getNumericColumn()
                && new Timestamp(19000000).equals(testObj.getTimeColumn()));
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void testOverrideConstructor() {
        SqlConstant.socket.onMessage("methodName|testOverrideConstructor", WebSocketTest.session);
        startTime = System.nanoTime();
        String charColumn = "test string";
        long bigIntColumn = 12134567;
        float numericColumn = 10;
        Timestamp timeColumn = new Timestamp(19941028);
        testObj = new DBTestUseCaseObject(charColumn, bigIntColumn, numericColumn, timeColumn);
        assertTrue(charColumn.equals(testObj.getCharColumn())
                    && bigIntColumn == testObj.getBigIntColumn()
                    && numericColumn == testObj.getNumericColumn()
                    && timeColumn.equals(testObj.getTimeColumn()));
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void setCharColumn() {
        SqlConstant.socket.onMessage("methodName|setCharColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        String charColumn = "test string";
        testObj.setCharColumn(charColumn);
        assertEquals(charColumn, testObj.getCharColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void setBigIntColumn() {
        SqlConstant.socket.onMessage("methodName|setBigIntColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        long bigIntColumn = 12134567;
        testObj.setBigIntColumn(bigIntColumn);
        assertEquals(bigIntColumn, testObj.getBigIntColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void setNumericColumn() {
        SqlConstant.socket.onMessage("methodName|setNumericColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        float numericColumn = (float) 10.00;
        testObj.setNumericColumn(numericColumn);
        assertEquals(numericColumn, testObj.getNumericColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void setTimeColumn() {
        SqlConstant.socket.onMessage("methodName|setTimeColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        Timestamp timeColumn = new Timestamp(19941028);
        testObj.setTimeColumn(timeColumn);
        assertTrue(timeColumn.equals(testObj.getTimeColumn()));
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void getCharColumn() {
        SqlConstant.socket.onMessage("methodName|getCharColumn", WebSocketTest.session);
        startTime = System.nanoTime();
//        assertEquals("",);
        System.out.println(testObj.getCharColumn());
        assertEquals("", testObj.getCharColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void getBigIntColumn() {
        SqlConstant.socket.onMessage("methodName|getBigIntColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        assertEquals(0, testObj.getBigIntColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void getNumericColumn() {
        SqlConstant.socket.onMessage("methodName|getNumericColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        assertEquals(0.0, testObj.getNumericColumn());
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void getTimeColumn() {
        SqlConstant.socket.onMessage("methodName|getTimeColumn", WebSocketTest.session);
        startTime = System.nanoTime();
        assertTrue(new Timestamp(19000000).equals(testObj.getTimeColumn()));
        TestResultUtil.returnTestResult(startTime,status);
    }

    @Test
    void getTestObjectSet() {
        SqlConstant.socket.onMessage("methodName|getTestObjectSet", WebSocketTest.session);
        startTime = System.nanoTime();
        HashSet<DBTestUseCaseObject> dbTestUseCaseObjectHashSet = DBTestUseCaseObject.getTestObjectSet();
//        dbTestUseCaseObjectHashSet.forEach(description -> System.out.println("It's the " + description.getCharColumn()));
        assertEquals(10, dbTestUseCaseObjectHashSet.size());
        TestResultUtil.returnTestResult(startTime,status);
    }
}