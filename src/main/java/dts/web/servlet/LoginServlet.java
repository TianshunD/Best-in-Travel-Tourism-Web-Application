package dts.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dts.domain.ResultInfo;
import dts.domain.User;
import dts.service.UserService;
import dts.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        //verify checkCode first
        String checkCode = request.getParameter("check");
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER"); //verify code is valid for only once
        if (checkcode_server==null || !checkcode_server.equalsIgnoreCase(checkCode)) {
            info.setFlag(false);
            info.setErrorMsg("VerifyCode is wrong!");
            String str = mapper.writeValueAsString(info);
            response.getWriter().write(str);
            return; //do nothing after this
        }

        //then try finding user
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        UserService service = new UserServiceImpl();
        User u = service.login(user);

        if (u==null) {
            info.setFlag(false);
            info.setErrorMsg("Wrong username or password!");
        } else if (!"Y".equals(u.getStatus())) {
            info.setFlag(false);
            info.setErrorMsg("Please active first!");
        }
        if (u!=null && "Y".equals(u.getStatus())) {
            info.setFlag(true);
            request.getSession().setAttribute("user",u);
        }

        String s = mapper.writeValueAsString(info);
        System.out.println(s);
        response.getWriter().write(s);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
