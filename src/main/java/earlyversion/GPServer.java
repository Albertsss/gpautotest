package earlyversion;

import jsch.SSHCommandExecutor;

import java.util.Vector;

/**
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 14:36 2018/4/12
 * @Modified By:
 */
public class GPServer {

    private SSHCommandExecutor sshExecutor;
    private Vector<String> stdout;

    public static void main(String[] args) {
        new GPServer();
    }

    public GPServer(){
        sshExecutor = new SSHCommandExecutor(GPConfig.masterip, "root", GPConfig.rootpwd);
        serverReport();
    }

    public void exeShell(String shell){
        sshExecutor.execute(shell);
        stdout = sshExecutor.getStandardOutput();
        for (String str : stdout) {
            System.out.println(str);
//            earlyversion.GPConfig.list.add(str+"\n");
//            earlyversion.GPConfig.socket.onMessage(str,websocket.WebSocketTest.session);
        }
        stdout.clear();
    }

    public void serverReport(){
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println("|                      操作系统信息                       |");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println();
        System.out.println("----->>>---->>>  主机名: ");
//        earlyversion.GPConfig.list.add("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
//        earlyversion.GPConfig.list.add("|                      操作系统信息                       |");
//        earlyversion.GPConfig.list.add("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
//        earlyversion.GPConfig.list.add("\n");
//        earlyversion.GPConfig.list.add("----->>>---->>>  主机名: ");
        exeShell("hostname -s");
        System.out.println();
        System.out.println("----->>>---->>>  以太链路信息: ");
//        earlyversion.GPConfig.list.add("\n");
//        earlyversion.GPConfig.list.add("----->>>---->>>  以太链路信息: ");
        exeShell("ip link show");
        System.out.println();
        System.out.println("----->>>---->>>  IP地址信息: ");
        exeShell("ip addr show");
        System.out.println();
        System.out.println("----->>>---->>>  路由信息: ");
        exeShell("ip route show");
        System.out.println();
        System.out.println("----->>>---->>>  操作系统内核: ");
        exeShell("uname -a");
        System.out.println();
        System.out.println("----->>>---->>>  内存(MB): ");
        exeShell("free -m");
        System.out.println();
        System.out.println("----->>>---->>>  CPU: ");
        exeShell("lscpu");
        System.out.println();
        System.out.println("----->>>---->>>  块设备: ");
        exeShell("lsblk");
        System.out.println();
        /*System.out.println("----->>>---->>>  拓扑: ");
        exeShell("lstopo-no-graphics");
        System.out.println();
        System.out.println("----->>>---->>>  进程树: ");
        exeShell("pstree -a -A -c -l -n -p -u -U -Z");
        System.out.println();*/

        System.out.println("----->>>---->>>  操作系统配置文件 静态配置信息: ");
        System.out.println("----->>>---->>>  /etc/sysctl.conf ");
        exeShell("grep '^[a-z]' /etc/sysctl.conf");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/security/limits.conf ");
        exeShell("grep -v '^#' /etc/security/limits.conf|grep -v '^$'");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/security/limits.d/*.conf ");
        exeShell("for dir in `ls /etc/security/limits.d`; do echo '/etc/security/limits.d/$dir : '; grep -v '^#' /etc/security/limits.d/$dir|grep -v '^$'; done");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/sysconfig/iptables-config ");
        exeShell("cat /etc/sysconfig/iptables-config");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/fstab ");
        exeShell("cat /etc/fstab");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/rc.local ");
        exeShell("cat /etc/rc.local");
        System.out.println();
        System.out.println("----->>>---->>>  /etc/selinux/config ");
        exeShell("cat /etc/selinux/config");
        System.out.println();
        /*System.out.println("----->>>---->>>  /boot/grub/grub.conf ");
        exeShell("cat /boot/grub/grub.conf");
        System.out.println(); */
        System.out.println("----->>>---->>>  /var/spool/cron 用户cron配置 ");
        exeShell("for dir in `ls /var/spool/cron`; do echo '/var/spool/cron/$dir : '; cat /var/spool/cron/$dir; done");
        System.out.println();
        System.out.println("----->>>---->>>  chkconfig --list ");
        exeShell("chkconfig --list");
        System.out.println();
        System.out.println("----->>>---->>>  iptables -L -v -n -t filter 动态配置信息: ");
        exeShell("iptables -L -v -n -t filter");
        System.out.println();
        System.out.println("----->>>---->>>  iptables -L -v -n -t nat 动态配置信息: ");
        exeShell("iptables -L -v -n -t nat");
        System.out.println();
        System.out.println("----->>>---->>>  iptables -L -v -n -t mangle 动态配置信息: ");
        exeShell("iptables -L -v -n -t mangle");
        System.out.println();
        System.out.println("----->>>---->>>  iptables -L -v -n -t raw 动态配置信息: ");
        exeShell("iptables -L -v -n -t raw");
        System.out.println();
        System.out.println("----->>>---->>>  sysctl -a 动态配置信息: ");
        exeShell("sysctl -a");
        System.out.println();
        System.out.println("----->>>---->>>  mount 动态配置信息: ");
        exeShell("mount -l");
        System.out.println();
        System.out.println("----->>>---->>>  selinux 动态配置信息: ");
//        exeShell("getsebool");
        exeShell("sestatus");
        System.out.println();

        System.out.println("----->>>---->>>  建议禁用Transparent Huge Pages (THP): ");
        exeShell("cat /sys/kernel/mm/transparent_hugepage/enabled");
        exeShell("cat /sys/kernel/mm/transparent_hugepage/defrag");
//        exeShell("cat /sys/kernel/mm/redhat_transparent_hugepage/enabled");
//        exeShell("cat /sys/kernel/mm/redhat_transparent_hugepage/defrag");
        System.out.println();
        /*System.out.println("----->>>---->>>  硬盘SMART信息(需要root): ");
        exeShell("smartctl --scan|awk -F '#' '{print $1}' | while read i; do echo -e '\n\nDEVICE $i'; smartctl -a $i; done");
        System.out.println(); */
        System.out.println("----->>>---->>>  /var/log/boot.log ");
        exeShell("cat /var/log/boot.log");
        System.out.println();
        System.out.println("----->>>---->>>  /var/log/cron(需要root) ");
        exeShell("cat /var/log/cron");
        System.out.println();
        System.out.println("----->>>---->>>  /var/log/dmesg ");
        exeShell("cat /var/log/dmesg");
        System.out.println();
        System.out.println("----->>>---->>>  /var/log/messages(需要root) ");
        exeShell("tail -n 500 /var/log/messages");
        System.out.println();
        System.out.println("----->>>---->>>  /var/log/secure(需要root) ");
        exeShell("cat /var/log/secure");
        System.out.println();
        System.out.println("----->>>---->>>  /var/log/wtmp ");
        exeShell("who -a /var/log/wtmp");
        System.out.println();
    }


}
