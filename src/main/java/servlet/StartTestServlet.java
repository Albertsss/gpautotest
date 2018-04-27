package servlet;


import com.google.gson.Gson;
import earlyversion.GPConfig;
import earlyversion.GPTest;
import utils.SqlConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 开始测试的servlet
 */
public class StartTestServlet extends HttpServlet{
    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if(action.equals("setParameter")){
            SqlConstant.masterIP = req.getParameter("masterIP");
            SqlConstant.gpPort = req.getParameter("gpPort");
            SqlConstant.gpUser = req.getParameter("gpUser");
            SqlConstant.rootPwd = req.getParameter("rootPwd");
            SqlConstant.gpPwd = req.getParameter("gpPwd");
            SqlConstant.gpadminPwd = req.getParameter("gpadminPwd");
        }
    }
}
