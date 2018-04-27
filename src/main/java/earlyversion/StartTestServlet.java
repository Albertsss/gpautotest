package earlyversion;


import com.google.gson.Gson;

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
 * @Author: KeHongwei
 * @Description:
 * @Date: Created in 17:49 2018/4/17
 * @Modified By:
 */
public class StartTestServlet extends HttpServlet{
    @Override
    public void init() throws ServletException {
//        try {
//            Class.forName("com.pivotal.jdbc.GreenplumDriver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        if(action.equals("setconfig")){
            String masterip = req.getParameter("masterip");
            String gpport = req.getParameter("gpport");
            String gpuser = req.getParameter("gpuser");
            String rootpwd = req.getParameter("rootpwd");
            String gppwd = req.getParameter("gppwd");
            String gpadminpwd = req.getParameter("gpadminpwd");
            GPConfig.masterip=masterip;
            GPConfig.gpport=gpport;
            GPConfig.gpuser=gpuser;
            GPConfig.rootpwd=rootpwd;
            GPConfig.gppwd=gppwd;
            GPConfig.gpadminpwd=gpadminpwd;
        }

        if(action.equals("start")){
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            Gson gson=new Gson();
            List<String> list=new ArrayList<String>();
            list.add("createDB");list.add("createSchema");list.add("dropSchema");list.add("createTable");
            list.add("tableTest");list.add("dropTable");list.add("curdTest");list.add("orderByTest");
            list.add("groupByTest");list.add("havingTest");list.add("andTest");list.add("orTest");
            list.add("notTest");list.add("likeTest");list.add("inTest");list.add("notInTest");
            list.add("betweenTest");list.add("updateData");list.add("innerJoinTest");list.add("leftJoinTest");
            list.add("rightJoinTest");list.add("fullJoinTest");list.add("crossJoinTest");list.add("viewTest");
            list.add("functionTest");list.add("triggerSequenceTest");list.add("maxAsTest");list.add("limitTest");
            list.add("indexTest");list.add("dateTest");list.add("collectionTest");list.add("alterTest");
            list.add("transactionTest");list.add("subQueryTest");list.add("serialTest");list.add("authorityTest");
            list.add("operatorTest");
            String info=gson.toJson(list);
            System.out.println(info);
            out.write(info);
            out.flush();
            out.close();
            new GPTest().startTest();
        }

//        session.setAttribute("list",earlyversion.GPConfig.list);
//        resp.sendRedirect("jsps/report-early.jsp");
//        new earlyversion.GPTest().startTest();

        /*RequestDispatcher dispatcher = req.getRequestDispatcher("jsps/index.jsp");
        dispatcher .forward(req, resp);*/


    }
}
