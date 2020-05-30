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

@WebServlet("/user/*") // /user/add...
public class UserServlet extends BaseServlet {
    private String checkCode;
    private String checkcode_server;
    private ResultInfo info = new ResultInfo();
    private ObjectMapper mapper = new ObjectMapper();
    private UserService service = new UserServiceImpl();

    /**
     * For user register
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //verify checkCode first
        checkCode = request.getParameter("check");
        checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
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

    /**
     * For user login
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //verify checkCode first
        checkCode = request.getParameter("check");
        checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
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

    /**
     * Find login user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        String s = new ObjectMapper().writeValueAsString(user);
        response.getWriter().write(s);
    }

    /**
     * exit login user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("user");
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * alert user to activate account first
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void activateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code!=null) {
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
}
