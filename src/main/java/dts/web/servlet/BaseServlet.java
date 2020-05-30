package dts.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get method name
        String requestURI = req.getRequestURI();
        //System.out.println("request URI: " + requestURI); // /travel/user/add
        String methodNM = requestURI.substring(requestURI.lastIndexOf('/') + 1);
        System.out.println(methodNM); //add

        //get method
        System.out.println(this); //dts.web.servlet.UserServlet@6eb0215e
        try {
            //getDeclaredMethod  ignore accessibility
            Method method = this.getClass().getDeclaredMethod(methodNM, HttpServletRequest.class, HttpServletResponse.class);
            System.out.println(method);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}
