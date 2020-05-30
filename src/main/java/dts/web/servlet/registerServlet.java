package dts.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dts.dao.UserDao;
import dts.dao.impl.UserDaoImpl;
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
import java.util.Objects;

@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResultInfo info = new ResultInfo();

        //verify checkCode first
        String checkCode = request.getParameter("check");
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        request.getSession().removeAttribute("CHECKCODE_SERVER"); //verify code is valid for only once
        if (checkcode_server==null || !checkcode_server.equalsIgnoreCase(checkCode)) {
            info.setFlag(false);
            info.setErrorMsg("VerifyCode is wrong!");
            String str = mapper.writeValueAsString(info);
            response.getWriter().write(str);
            return; //do nothing
        }

        //Encapsulate user obj
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println("ParameterUser"+user);

        //check username has not been taken
        UserDao dao_user = new UserDaoImpl();
        User userByUsername = dao_user.findUserByUsername(user.getUsername());
        if(userByUsername!=null) {
            info.setFlag(false);
            info.setErrorMsg("UserName has been taken!");
            String str = mapper.writeValueAsString(info);
            response.getWriter().write(str);
            return;
        }

        //call service to finish register
        UserService service = new UserServiceImpl();
        Boolean flag = service.register(user);

        //response
        info.setFlag(flag);
        if (!flag) {
            info.setErrorMsg("Registration failed!");
        }
        //return as json
        String result_json = mapper.writeValueAsString(info);
        System.out.println(result_json);
        response.getWriter().write(result_json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
