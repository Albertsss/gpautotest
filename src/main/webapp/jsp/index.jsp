<%--
  Created by IntelliJ IDEA.
  User: Dell
  Date: 2018/4/17
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>gp-autotest</title>
    <%--标签页图标--%>
    <link href='/image/gp.ico' rel='shortcut icon'>
    <!-- 引入 Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <style>
        .container{
            margin-top: 100px;
        }
        #myform{
            width:80%;
            height: 400px;
            margin-left: 10%;
            margin-top: 20px;
        }
        .control-label{
            width:200px;
            text-align: center;
            font-family: "微软雅黑";
            font-weight: bold;
          /*  border: 1px solid black;*/
        }
        .rightdiv{
            padding-left: 20px;
        }
    </style>
</head>
<script>
    function startTest() {
        $.ajax({
            type:"post",
            datatype:"json",
            url:"/startTest?action=setParameter",
            data:$('#myForm').serialize(),
            success:function (res) {
            }
        });
        window.open('jsp/report.jsp','_blank');
    }

</script>


<body>


<div class="container">
    <strong style="margin-left: 11%;color: #007bff;font-size: 18px;">测试参数配置:</strong>
    <form id="myForm" method="post" class="form-inline" role="form"  align="center">
        <div class="form-group">
            <label for="masterIP" class="col-sm-2 control-label">master主机IP</label>
            <div class="col-md-4">
                <input class="form-control" type="text" id="masterIP" name="masterIP" value="192.168.60.129"/>
            </div>
            <label for="gpPort" class="col-sm-2 control-label">gp端口</label>
            <div class="col-md-4 rightdiv">
                <input class="form-control" type="text" id="gpPort" name="gpPort" value="5432"/>
            </div>
        </div>

        <div class="form-group">
            <label for="gpUser" class="col-sm-2 control-label">gp登录名</label>
            <div class="col-md-4">
                <input class="form-control" type="text" id="gpUser" name="gpUser" value="gpadmin"/>
            </div>
            <label for="rootPwd" class="col-sm-2 control-label">master主机root密码</label>
            <div class="col-md-4 rightdiv">
                <input class="form-control" type="password" id="rootPwd" name="rootPwd" value="linewell@123"/>
            </div>
        </div>

        <div class="form-group">
            <label for="gpPwd" class="col-sm-2 control-label">gp密码</label>
            <div class="col-md-4">
                <input class="form-control" type="password" id="gpPwd" name="gpPwd" value="linewell@minielectric"/>
            </div>
            <label for="gpadminPwd" class="col-sm-2 control-label">master主机gpadmin密码</label>
            <div class="col-md-4 rightdiv">
                <input class="form-control" type="password" id="gpadminPwd" name="gpadminPwd" value="gpadmin"/>
            </div>
        </div>

        <button class="btn btn-primary" onclick="startTest()" style="margin-left: 45%;margin-top: 30px;">开始测试</button>

    </form>

</div>
</body>
</html>
