package earlyversion;

import com.jcraft.jsch.*;
import jsch.SSHCommandExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

//import org.junit.Test;

/**
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 16:25 2018/4/12
 * @Modified By:
 */
public class GPService {

    private static final String t="psql---->>";
    private Session sshSession;

    public static void main(String[] args) {
        new GPService().gpfdistTest();
    }

//    public earlyversion.GPService(){
//        gpfdistTest();
//    }

    /*public void gpgp(){
        File file=new File("ebike_record.txt");
        if(file.exists()){
            System.out.println("hello");
        }else{
            System.out.println("bad");
        }

        System.out.println("项目服务器脚本文件路径:"+System.getProperty("user.dir"));
        //类加载根路径
        String classPath = this.getClass().getResource("/").getPath();
        System.out.println("类加载根路径:"+classPath);
        //类所在工程根路径
        String proClassPath = this.getClass().getResource("").getPath();
        System.out.println("类所在工程根路径:"+proClassPath);
        //项目服务器脚本文件路径
        File directory = new File("");// 参数为空
        try {
            String proRootPath = directory.getCanonicalPath();
            System.out.println("项目服务器脚本文件路径"+proRootPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //项目服务器脚本文件路径
        String proPath = System.getProperty("user.dir");
        System.out.println("项目服务器脚本文件路径"+proPath);
    }*/

    /**
     * 测试用gpfdist导入数据
     */
//    @Test
    public void gpfdistTest(){
        //开启gpfdist服务
        SSHCommandExecutor sshExecutor = new SSHCommandExecutor(GPConfig.masterip, "gpadmin", GPConfig.gpadminpwd);
        sshExecutor.execute("ps -ef|grep gpfdist");
        Vector<String> stdout = sshExecutor.getStandardOutput();
        int end;
        String pro;
        for (String str : stdout) {
            System.out.println(str);
            if(str.indexOf("-p 8088")!=-1){
                end=str.indexOf(" ",9);
                pro=str.substring(9,end);
                sshExecutor.execute("kill -9 "+pro);
            }
        }
        stdout.clear();
        sshExecutor.execute("gpfdist -d /home/gpadmin -p 8088 >/home/gpadmin/gpfdist.out 2>&1 &");
        stdout = sshExecutor.getStandardOutput();
        for (String str : stdout) {
            System.out.println(str);
        }
        stdout.clear();
        sshExecutor.execute("ls");
        //通过sftp上传文件
        ChannelSftp sftp=connect(GPConfig.masterip,22,"root", GPConfig.rootpwd);
        upload("/home/gpadmin","src/ebike_record.txt",sftp);
        //建外部表测试数据的导入
        importData();
        //通过可写外部表测试数据的导出
        outputData();
        try {
            sftp.cd("/home/gpadmin");
            sftp.rm("ebike_record.txt");
            sftp.disconnect();
            sshSession.disconnect();
        } catch (SftpException e) {
            e.printStackTrace();
        }


    }

    /**
     * gpfdist数据导入
     */
//    @Test
    public void importData(){
        String sql="CREATE TABLE ebike_record (" +
                "ID varchar(32)  NOT NULL DEFAULT '' ," +   //--主键
                "EBIKE_ID varchar(50)  DEFAULT '' ," +  //--电动车ID
                "CREATE_TIME timestamp DEFAULT NULL ," +    //--预约时间
                "ADDRESS varchar(300)  DEFAULT '' ," +  //--备案地址
                "PS_ID varchar(32)  DEFAULT '' ," + //--备案派出所ID
                "TAG_NO varchar(50)  DEFAULT '' ," +    //--标签ID
                "OWNER_ID varchar(50)  DEFAULT '' ," +  //--车主ID
                "USER_ID varchar(50)  DEFAULT '' ," +   //--用户ID
                "SUBMMITER_ID varchar(50)  DEFAULT '' ," +  //--预约提交人ID
                "PIC_ID varchar(500)  DEFAULT '' ," +   //--车辆图片ID
                "ATTACHMENTS_TYPE varchar(500)  DEFAULT '' ," + //--附件类型,多值，以","分割
                "ATTACHMENTS varchar(500)  DEFAULT '' ," +  //--附件列表
                "CARD_ID varchar(50)  DEFAULT '' ," +   //--车主证件号
                "REALNAME varchar(50)  DEFAULT '' ," +  //--真实姓名
                "OWNER_ADDRESS varchar(300)  DEFAULT '' ," +    //--地址
                "CARD_FRONT_PIC varchar(200)  DEFAULT '' ," +   //--持证件正面照（URL）
                "CARD_BACK_PIC varchar(200)  DEFAULT '' ," +    //--证件反面照（URL）
                "CARD_VALID_YMD timestamp DEFAULT NULL ," + //--到期时间
                "CARD_TYPE int DEFAULT NULL ," +    //--身份证类型（1-大陆身份证、2-香港身份证、3-台湾身份证、4-澳门身份证）
                "PHONE varchar(20)  DEFAULT '' ," + //--车主联系电话
                "POSTCODE varchar(20)  DEFAULT '' ," +  //--邮政编码
                "TEMP_RESIDENCE_NO varchar(200)  DEFAULT '' ," +    //--暂住证编号
                "PROVINCE varchar(50)  DEFAULT '' ," +  //--所属省份
                "CITY varchar(50)  DEFAULT '' ," +  //--所属城市
                "COUNTY varchar(50)  DEFAULT '' ," +    //--所属县区
                "NATIVE_CITY int DEFAULT 1 ," + //--是否本市，0--不是，1--是
                "RECORD_TYPE int DEFAULT 0 ," + //--备案类型（0--预约，1--非预约）
                "APPOINTMENT_NO varchar(50)  DEFAULT '' ," +    //--预约编号
                "STATUS int DEFAULT 0 ," +  //--备案状态：0-未备案，1-已备案，2-退回
                "RECORD_TIME timestamp DEFAULT NULL ," +    //--备案时间
                "EBIKE_STATUS int DEFAULT 0 ," +    //--车辆状态：0-新销售车辆，1-已上牌车辆
                "PLATE_NO varchar(150)  DEFAULT '' ," + //--车牌号
                "DELETABLE int DEFAULT 0 ," +   //--是否删除
                "POLICE_ID varchar(50)  DEFAULT '' ," + //--备案民警标识
                "AGENT_ID varchar(50)  DEFAULT '' ," +  //--代理人标识
                "SITE_ID varchar(32)  DEFAULT '' ," +   //--站点标识
                "IMAGE_UPLOAD_ID varchar(32)  DEFAULT '' ," +   //--图片上传记录标识
                "INSURANCE_ORDER_ID varchar(32)  DEFAULT '' ," +    //--保险订单标识
                "BIND_USER_ID varchar(32)  DEFAULT '' ," +  //--绑定的操作用户标识
                "BIND_TIME timestamp DEFAULT NULL ," +  //--绑定时间
                "RESIDENCE_ID varchar(32)  DEFAULT '' ," +  //--街道标识
                "COMMUNITY_ID varchar(32)  DEFAULT '' " +   //--社区标识
                ")" +
                "with(appendonly=true,orientation=column,compresslevel=5)" +
                "DISTRIBUTED BY (ID);";
        System.out.println(t+"建表ebike_record");
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            String gpfdistURL="gpfdist://"+ GPConfig.masterip+":8088/ebike_record.txt";
            sql="create external table ebike_record_ext (" +
                    "ID varchar(32)  ," +
                    "EBIKE_ID varchar(50)  ," +
                    "CREATE_TIME timestamp ," +
                    "ADDRESS varchar(300)  ," +
                    "PS_ID varchar(32)  ," +
                    "TAG_NO varchar(50)  ," +
                    "OWNER_ID varchar(50)  ," +
                    "USER_ID varchar(50)  ," +
                    "SUBMMITER_ID varchar(50)  ," +
                    "PIC_ID varchar(500)  ," +
                    "ATTACHMENTS_TYPE varchar(500)  ," +
                    "ATTACHMENTS varchar(500)  ," +
                    "CARD_ID varchar(50)  ," +
                    "REALNAME varchar(50)  ," +
                    "OWNER_ADDRESS varchar(300)  ," +
                    "CARD_FRONT_PIC varchar(200)  ," +
                    "CARD_BACK_PIC varchar(200)  ," +
                    "CARD_VALID_YMD timestamp ," +
                    "CARD_TYPE int ," +
                    "PHONE varchar(20)  ," +
                    "POSTCODE varchar(20)  ," +
                    "TEMP_RESIDENCE_NO varchar(200)  ," +
                    "PROVINCE varchar(50)  ," +
                    "CITY varchar(50)  ," +
                    "COUNTY varchar(50)  ," +
                    "NATIVE_CITY int ," +
                    "RECORD_TYPE int ," +
                    "APPOINTMENT_NO varchar(50)  ," +
                    "STATUS int ," +
                    "RECORD_TIME timestamp ," +
                    "EBIKE_STATUS int ," +
                    "PLATE_NO varchar(150)  ," +
                    "DELETABLE int ," +
                    "POLICE_ID varchar(50)  ," +
                    "AGENT_ID varchar(50)  ," +
                    "SITE_ID varchar(32)  ," +
                    "IMAGE_UPLOAD_ID varchar(32)  ," +
                    "INSURANCE_ORDER_ID varchar(32)  ," +
                    "BIND_USER_ID varchar(32)  ," +
                    "BIND_TIME timestamp ," +
                    "RESIDENCE_ID varchar(32)  ," +
                    "COMMUNITY_ID varchar(32)   " +
                    ") location ('"+gpfdistURL+"') " +
                    "format 'TEXT' (delimiter as '|' null as '');";
            System.out.println(t+"建外部表ebike_record_ext");
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into ebike_record select * from ebike_record_ext";
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="select count(distinct id) from ebike_record";
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                int num=Integer.parseInt(rs.getString(1));
//                System.out.println(num);
                if(num>0){
                    System.out.println(t+"数据导入成功");
                }else{
                    System.out.println(t+"数据导入失败");
                }
            }
            sql="drop external table ebike_record_ext";
            System.out.println(t+"删除外部表ebike_record_ext");
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }


    }

    /**
     * gpfdist数据导出
     */
//    @Test
    public void outputData(){
        String gpfdistURL="gpfdist://"+ GPConfig.masterip+":8088/ebike_record_out.txt";
        String sql="create writable external table ebike_record_out_ext (" +
                "ID varchar(32)  ," +
                "EBIKE_ID varchar(50)  ," +
                "CREATE_TIME timestamp ," +
                "ADDRESS varchar(300)  ," +
                "PS_ID varchar(32)  ," +
                "TAG_NO varchar(50)  ," +
                "OWNER_ID varchar(50)  ," +
                "USER_ID varchar(50)  ," +
                "SUBMMITER_ID varchar(50)  ," +
                "PIC_ID varchar(500)  ," +
                "ATTACHMENTS_TYPE varchar(500)  ," +
                "ATTACHMENTS varchar(500)  ," +
                "CARD_ID varchar(50)  ," +
                "REALNAME varchar(50)  ," +
                "OWNER_ADDRESS varchar(300)  ," +
                "CARD_FRONT_PIC varchar(200)  ," +
                "CARD_BACK_PIC varchar(200)  ," +
                "CARD_VALID_YMD timestamp ," +
                "CARD_TYPE int ," +
                "PHONE varchar(20)  ," +
                "POSTCODE varchar(20)  ," +
                "TEMP_RESIDENCE_NO varchar(200)  ," +
                "PROVINCE varchar(50)  ," +
                "CITY varchar(50)  ," +
                "COUNTY varchar(50)  ," +
                "NATIVE_CITY int ," +
                "RECORD_TYPE int ," +
                "APPOINTMENT_NO varchar(50)  ," +
                "STATUS int ," +
                "RECORD_TIME timestamp ," +
                "EBIKE_STATUS int ," +
                "PLATE_NO varchar(150)  ," +
                "DELETABLE int ," +
                "POLICE_ID varchar(50)  ," +
                "AGENT_ID varchar(50)  ," +
                "SITE_ID varchar(32)  ," +
                "IMAGE_UPLOAD_ID varchar(32)  ," +
                "INSURANCE_ORDER_ID varchar(32)  ," +
                "BIND_USER_ID varchar(32)  ," +
                "BIND_TIME timestamp ," +
                "RESIDENCE_ID varchar(32)  ," +
                "COMMUNITY_ID varchar(32)   " +
                ") location ('"+gpfdistURL+"') " +
                "format 'TEXT' (delimiter as '|' null as '');";
        System.out.println(t+"建可写外部表ebike_record_out_ext");
        Connection con= GPConnectionUtils.getConn();
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            ps=con.prepareStatement(sql);
            ps.execute();
            sql="insert into ebike_record_out_ext select * from ebike_record";
            System.out.println(t+"gpfdist数据导出");
            ps=con.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            GPConnectionUtils.close(rs,ps);
        }


    }


    /**
     * 连接sftp服务器
     *
     * @param host  主机
     * @param port  端口
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
//            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            // sshSession.disconnect();
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory 上传的目录
     * @param uploadFile    要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
