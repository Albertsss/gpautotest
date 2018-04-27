import earlyversion.GPConfig;
import earlyversion.GPConnectionUtils;
import earlyversion.GPUtils;
import websocket.WebSocketTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.*;

/**
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 8:48 2018/4/20
 * @Modified By:
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GPAutoTest {
    private static final String t="psql---->>";

    public static void main(String[] args) throws Exception {
        GPAutoTest autoTest=new GPAutoTest();
        autoTest.t01_createDB();
        autoTest.t02_createSchema();
        autoTest.t03_createTable();
        autoTest.t04_tableTest();
        autoTest.t05_curdTest();
        autoTest.t06_dropTable();
        autoTest.t07_orderByTest();
        autoTest.t08_groupByTest();
        autoTest.t09_havingTest();
        autoTest.t10_andTest();
        autoTest.t11_orTest();
        autoTest.t12_notTest();
        autoTest.t13_likeTest();
        autoTest.t14_inTest();
        autoTest.t15_notInTest();
        autoTest.t16_betweenTest();
        autoTest.t17_updateData();
        autoTest.t18_innerJoinTest();
        autoTest.t19_leftJoinTest();
        autoTest.t20_rightJoinTest();
        autoTest.t21_fullJoinTest();
        autoTest.t22_crossJoinTest();
        autoTest.t23_viewTest();
        autoTest.t24_functionTest();
        autoTest.t25_triggerSequenceTest();
        autoTest.t26_maxAsTest();
        autoTest.t27_limitTest();
        autoTest.t28_indexTest();
        autoTest.t29_dateTest();
        autoTest.t30_collectionTest();
        autoTest.t31_alterTest();
        autoTest.t32_transactionTest();
        autoTest.t33_subQueryTest();
        autoTest.t34_serialTest();
        autoTest.t35_authorityTest();
        autoTest.t36_operatorTest();
        autoTest.t39_dropSchema();
        autoTest.t40_dropDB();
    }

    @Test
    public void t01_createDB() throws Exception {
        GPUtils.printText("测试创建修改数据库");
        String sql="create database testdd";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
//        earlyversion.GPConfig.list.add(t+sql+"\n");
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            boolean ifsuc=ps.execute();
            if(!ifsuc){
                sql="alter database testdd rename to testdb";
                System.out.println(t+sql);
                GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
                ps=con.prepareStatement(sql);
                ps.execute();
                GPConnectionUtils.url="jdbc:pivotal:greenplum://"+ GPConfig.masterip+":"+ GPConfig.gpport+";DatabaseName=testdb";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
            GPConnectionUtils.conn=null;
        }
    }

    @Test
    public void t02_createSchema() throws Exception {
        GPConfig.socket.onMessage("hh2", WebSocketTest.session);
        GPUtils.printText("测试创建修改架构");
        String sql="create schema demo";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="alter schema demo rename to demolw";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t03_createTable() throws Exception {
        GPUtils.printText("测试建表");
        String sql="create table emp(eno int primary key not null,ename varchar(30),eage int)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create table employees(id int primary key not null,name text not null,age int not null,address " +
                    "varchar(50),salary real)";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t04_tableTest() throws Exception {
        GPUtils.printText("测试表约束、数据类型");
        String sql="create table test(" +
                "a smallint not null," +
                "b integer unique," +
                "c bigint check(c>0), " +
                "d decimal(15,8)," +
                "e numeric(15,8)," +
                "f real," +
                "g text," +
                "h timestamp," +
                "i interval," +
                "j date," +
                "k time," +
                "l boolean," +
                "eno int references emp(eno)" +
                ");";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            boolean a=ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t05_curdTest() throws Exception {
        GPUtils.printText("测试增删改查");
        Connection con= GPConnectionUtils.getConn();
        String sql="insert into employees(id,name,age,address,salary) values (1, 'Maxsu', 25, '海口市人民大道2880号', 109990.00 )," +
                "(2, 'minsu', 25, '广州中山大道', 125000.00 ),(3, '李洋', 21, '北京市朝阳区', 185000.00)," +
                "(4, 'Manisha', 24, 'Mumbai', 65000.00),(5, 'Larry', 21, 'Paris', 85000.00)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        PreparedStatement ps=null;
        ResultSet rs=null;
        boolean ifsuc;
        try {
            ps=con.prepareStatement(sql);
            ifsuc=ps.execute();
            if(!ifsuc){
                System.out.println("新增成功");
                GPConfig.socket.onMessage("新增成功", WebSocketTest.session);
            }
            sql="select * from employees order by id";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
//            System.out.println("查询结果:");
            while (rs.next()){
                System.out.println(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"));
                GPConfig.socket.onMessage(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"), WebSocketTest.session);
            }
            sql="update employees set age=29,salary=9800 where id=1";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            if(ps.executeUpdate()>0){
                System.out.println("更新成功");
                GPConfig.socket.onMessage("更新成功", WebSocketTest.session);
            }
            sql="select * from employees order by id";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"));
                GPConfig.socket.onMessage(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"), WebSocketTest.session);
            }
            sql="delete from employees where id=1";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ifsuc=ps.execute();
            if(!ifsuc){
                System.out.println("删除成功");
                GPConfig.socket.onMessage("删除成功", WebSocketTest.session);
            }
            sql="select * from employees order by id";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"));
                GPConfig.socket.onMessage(rs.getString("id")+"--"+rs.getString("name")+"--"+rs.getString("age")+"--"+
                        rs.getString("address")+"--"+rs.getString("salary"), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t06_dropTable() throws Exception {
        GPUtils.printText("测试删表");
        String sql="drop table test";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="drop table emp";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t07_orderByTest() throws Exception {
        GPUtils.printText("测试order by");
        String sql="select * from employees order by name desc";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t08_groupByTest() throws Exception {
        GPUtils.printText("测试group by");
        String sql="select name,sum(salary) sum from employees group by name";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name")+"--"+rs.getString("sum"));
                GPConfig.socket.onMessage(rs.getString("name")+"--"+rs.getString("sum"), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t09_havingTest() throws Exception {
        GPUtils.printText("测试having子句");
        String sql="select name from employees group by name having count(name)<2";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name"));
                GPConfig.socket.onMessage(rs.getString("name"), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t10_andTest() throws Exception {
        GPUtils.printText("测试and");
        String sql="select * from employees where salary>120000 and id<4";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);

    }

    @Test
    public void t11_orTest() throws Exception {
        GPUtils.printText("测试or");
        String sql="select * from employees where name='李洋' or address='Mumbai'";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t12_notTest() throws Exception {
        GPUtils.printText("测试not条件");
        String sql="select * from employees where address is not null";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t13_likeTest() throws Exception {
        GPUtils.printText("测试like条件");
        String sql="select * from employees where name like 'Ma%'";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t14_inTest() throws Exception {
        GPUtils.printText("测试in条件");
        String sql="select * from employees where age in (21,24)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t15_notInTest() throws Exception {
        GPUtils.printText("测试not in条件");
        String sql="select * from employees where age not in (21,24)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t16_betweenTest() throws Exception {
        GPUtils.printText("测试between条件");
        String sql="select * from employees where age between 23 and 26";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t17_updateData() throws Exception {
        GPUtils.printText("更新数据");
        String sql="truncate employees";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into employees(id,name,age,address,salary) values (1,'Maxsu',25,'海口市人民大道2880号',109990)," +
                    "(2,'Minsu',25,'广州中山大道',125000),(3,'李洋',21,'北京市朝阳区',185000)," +
                    "(4,'Manisha',24,'Mumbai',65000),(5,'Larry',21,'Paris',85000)," +
                    "(7,'Minsu',21,'Delhi',135000),(8,'Manisha',19,'Noida',125000)";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create table department(id int,dept text,fac_id int)";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into department values(1,'IT', 1),(2,'Engineering', 2),(3,'HR', 7)";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t18_innerJoinTest() throws Exception {
        GPUtils.printText("测试内连接");
        String sql="select employees.id,employees.name,department.dept from employees inner join department " +
                "on employees.id=department.id";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.printJoin(sql,con,ps,rs);
    }

    @Test
    public void t19_leftJoinTest() throws Exception {
        GPUtils.printText("测试左外连接");
        String sql="select employees.id,employees.name,department.dept from employees left outer join department " +
                "on employees.id=department.id";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.printJoin(sql,con,ps,rs);
    }

    @Test
    public void t20_rightJoinTest() throws Exception {
        GPUtils.printText("测试右外连接");
        String sql="select employees.id,employees.name,department.dept from employees right outer join department " +
                "on employees.id=department.id";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.printJoin(sql,con,ps,rs);
    }

    @Test
    public void t21_fullJoinTest() throws Exception {
        GPUtils.printText("测试全外连接");
        String sql="select employees.id,employees.name,department.dept from employees full outer join department " +
                "on employees.id=department.id";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.printJoin(sql,con,ps,rs);
    }

    @Test
    public void t22_crossJoinTest() throws Exception {
        GPUtils.printText("测试跨连接");
        String sql="select name,dept from employees cross join department";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name")+"--"+rs.getString("dept"));
                GPConfig.socket.onMessage(rs.getString("name")+"--"+rs.getString("dept"), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t23_viewTest() throws Exception {
        GPUtils.printText("测试视图");
        String sql="create view current_employees as select name,id,salary from employees";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select * from current_employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("name")+"--"+rs.getString("id")+"--"+
                        rs.getString("salary"));
                GPConfig.socket.onMessage(rs.getString("name")+"--"+rs.getString("id")+"--"+
                        rs.getString("salary"), WebSocketTest.session);
            }
            sql="drop view current_employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t24_functionTest() throws Exception {
        GPUtils.printText("测试函数");
        String sql="create or replace function totalRecords() " +
                "returns integer as $total$ " +
                "declare " +
                "   total integer;" +
                "begin " +
                "   select count(*) into total from employees;" +
                "   return total;" +
                "end;" +
                "$total$ language plpgsql;";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select totalRecords()";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="drop function totalrecords()";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t25_triggerSequenceTest() throws Exception {
        GPUtils.printText("测试触发器和序列");
        String sql="create sequence shipments_ship_id_seq minvalue 1";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="alter sequence shipments_ship_id_seq restart with 1";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create table shipments(id integer NOT NULL PRIMARY KEY,customer_id integer, isbn text, ship_date timestamp)";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create or replace function insert_id() returns trigger as $$ " +
                    "declare " +
                    "   seq_id integer; " + //声明一个变量，存储新的序列值
                    "begin " +
                    "   select into seq_id nextval('shipments_ship_id_seq');" + //获取新序列值
                    "   NEW.id=seq_id;" +   //赋值给记录
                    "   return NEW;" +  //返回修改后的记录
                    "end;" +
                    "$$ LANGUAGE plpgsql volatile"; //指定使用 PL/PGSQL 作为脚本语言
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create trigger insert_ship_id before insert on shipments " +
                    "for each row execute procedure insert_id()";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into shipments (customer_id, isbn, ship_date) values (221, '0394800753', 'now'),(321, '0394800555', 'now')";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select * from shipments";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3), WebSocketTest.session);
            }
            sql="drop trigger insert_ship_id on shipments"; //测试删除触发器
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="drop sequence shipments_ship_id_seq";  //测试删除序列
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t26_maxAsTest() throws Exception {
        GPUtils.printText("测试max和as别名");
        String sql="select name,max(salary) as package from employees group by name";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2), WebSocketTest.session);
            }
            sql="select min(salary) from employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="select sum(salary) from employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="select avg(salary) from employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t27_limitTest() throws Exception {
        GPUtils.printText("测试limit和offset");
        String sql="select * from employees order by id limit 3 offset 2";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+
                        rs.getString(3)+"--"+rs.getString(4)+"--"+
                        rs.getString(5));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2)+"--"+
                        rs.getString(3)+"--"+rs.getString(4)+"--"+
                        rs.getString(5), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t28_indexTest() throws Exception {
        GPUtils.printText("测试索引");
        String sql="create index employees_id_idx on employees(id)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select * from employees where id=4";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+
                        rs.getString(3)+"--"+rs.getString(4));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2)+"--"+
                        rs.getString(3)+"--"+rs.getString(4), WebSocketTest.session);
            }
            sql="reindex index employees_id_idx";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="drop index employees_id_idx";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t29_dateTest() throws Exception {
        GPUtils.printText("测试日期和时间函数");
        String sql="select age(timestamp '2017-01-26', timestamp '1951-08-15')";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="SELECT CURRENT_TIME";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="SELECT CURRENT_DATE";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
            sql="SELECT CURRENT_TIMESTAMP";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1));
                GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t30_collectionTest() throws Exception {
        GPUtils.printText("测试集合(union、intersect、except)");
        String sql="create table tbl_test1(a int,b varchar(10),c varchar(5))";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="create table tbl_test2(e int,f varchar(10),g varchar(32))";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into tbl_test1(a,b,c) values (1,'HA','12'),(2,'ha','543')";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into tbl_test2(e,f,g) values (1,'HA','dh'),(3,'hk','76sskjhk')";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select a,b from tbl_test1 union select e,f from tbl_test2";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2), WebSocketTest.session);
            }
            sql="select a,b from tbl_test1 intersect select e,f from tbl_test2";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2), WebSocketTest.session);
            }
            sql="select a,b from tbl_test1 except select e,f from tbl_test2";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2), WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t31_alterTest() throws Exception {
        GPUtils.printText("测试修改表");
        String sql="select * from employees";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3)+"--"+
                        rs.getString(4)+"--"+rs.getString(5));
            }
            sql="alter table employees add gender char(1) default '男'";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select * from employees";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3)+"--"+
                        rs.getString(4)+"--"+rs.getString(5)+"--"+rs.getString(6));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2)+"--"+rs.getString(3)+"--"+
                        rs.getString(4)+"--"+rs.getString(5)+"--"+rs.getString(6), WebSocketTest.session);
            }
            sql="alter table employees drop gender";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t32_transactionTest() throws Exception {
        GPUtils.printText("测试事务");
        String sql="update employees set age=30 where id=1";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            con.setAutoCommit(false);
            ps=con.prepareStatement(sql);
            ps.execute();
            Savepoint point=con.setSavepoint("upd");
            con.rollback();
            System.out.println("select * from employees");
            GPConfig.socket.onMessage("select * from employees", WebSocketTest.session);
            GPUtils.selectEmployees("select * from employees",con,ps,rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t33_subQueryTest() throws Exception {
        GPUtils.printText("测试子查询");
        String sql="select * from employees where id in (select id from department)";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        GPUtils.selectEmployees(sql,con,ps,rs);
    }

    @Test
    public void t34_serialTest() throws Exception {
        GPUtils.printText("测试real自动增长");
        String sql="create table tinfo(id serial primary key,name varchar(20))";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into tinfo(name) values('t1'),('t2'),('t3')";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select * from tinfo";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(1)+"--"+rs.getString(2));
                GPConfig.socket.onMessage(rs.getString(1)+"--"+rs.getString(2), WebSocketTest.session);
            }
            sql="drop table tinfo";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    @Test
    public void t35_authorityTest() throws Exception {
        GPUtils.printText("测试权限控制");
        String sql="create user initest with password 'initest'";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="grant all on employees to initest";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="revoke all on employees from initest";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="drop user initest";
            System.out.println(t+sql);
            GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t36_operatorTest() throws Exception {
        GPUtils.printText("测试权限控制");
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        operatorUtil("select 2+3;",con,ps,rs);
        operatorUtil("select 2-3;",con,ps,rs);
        operatorUtil("select 2*3;",con,ps,rs);
        operatorUtil("select 2/3;",con,ps,rs);
        operatorUtil("select 2%3;",con,ps,rs);
        operatorUtil("select 2^3;",con,ps,rs);
        operatorUtil("select 10=20;",con,ps,rs);
        operatorUtil("select 10!=20;",con,ps,rs);
        operatorUtil("select 10<>20;",con,ps,rs);
        operatorUtil("select 10<20;",con,ps,rs);
        operatorUtil("select 10>20;",con,ps,rs);
        operatorUtil("select 10>=20;",con,ps,rs);
        operatorUtil("select 10<=20;",con,ps,rs);
    }

    public void operatorUtil(String sql,Connection con,PreparedStatement ps,ResultSet rs){
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        try {
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            rs.next();
            System.out.println(rs.getString(1));
            GPConfig.socket.onMessage(rs.getString(1), WebSocketTest.session);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }
    }

    /*@Test
    public void t37_serverReport() throws Exception{
        earlyversion.GPServer gpServer=new earlyversion.GPServer();
    }

    @Test
    public void t38_gpfdistTest() throws Exception{
        new earlyversion.GPService().gpfdistTest();
    }*/

    @Test
    public void t39_dropSchema() throws Exception {
        GPUtils.printText("测试删除架构");
        String sql="drop schema demolw";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

    @Test
    public void t40_dropDB() throws Exception {
        if(GPConnectionUtils.conn!=null){
            try {
                GPConnectionUtils.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        GPConnectionUtils.conn=null;
        GPConnectionUtils.url="jdbc:pivotal:greenplum://"+ GPConfig.masterip+":"+ GPConfig.gpport+";DatabaseName=postgres";
        GPUtils.printText("测试删除数据库");
        String sql="drop database testdb";
        System.out.println(t+sql);
        GPConfig.socket.onMessage(t+sql, WebSocketTest.session);
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        try {
            ps=con.prepareStatement(sql);
            boolean ifsuc=ps.execute();
            if(!ifsuc){
                System.out.println("删除数据库testdb成功");
                GPConfig.socket.onMessage("删除数据库testdb成功", WebSocketTest.session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(null,ps);
        }
    }

}