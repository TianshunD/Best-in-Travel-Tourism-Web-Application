package dts.web.servlet;

import dts.service.UserService;
import dts.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class activeUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code!=null) {
            UserService service = new UserServiceImpl();
            Boolean flag = service.active(code);
            String msg = null;
            if (flag) { //success
                msg = "Success! Please <a href='login.html'>login</a>";
            } else {
                msg = "<div style='color:red'>Failed! Contact tianshund01@gmail for help!</div>";
            }
            response.getWriter().write(msg);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
