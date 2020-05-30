package dts.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dts.domain.PageBean;
import dts.domain.Route;
import dts.domain.User;
import dts.service.FavoriteService;
import dts.service.RouteService;
import dts.service.impl.FavoriteServiceImpl;
import dts.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();
    private FavoriteService fav_service = new FavoriteServiceImpl();

    /**
     * List route info by pagination
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void routeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid_str = request.getParameter("cid");
        //System.out.println(cid_str);
        String currentPage_str = request.getParameter("currentPage");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        int pageSize = 10;

        int cid = 0;
        if (cid_str!=null && cid_str.length()!=0 && !"null".equals(cid_str)) {
            cid = Integer.parseInt(cid_str);
        }
        //System.out.println("cid=" + cid);

        int currentPage = 1;
        if (currentPage_str!=null && currentPage_str.length()!=0) {
            currentPage = Integer.parseInt(currentPage_str)>0 ? Integer.parseInt(currentPage_str) : 1;
        }
        //System.out.println("currentPage=" + currentPage);

        PageBean<Route> pb = service.pageList(5,currentPage,pageSize,rname);
        String str = mapper.writeValueAsString(pb); //https://www.sojson.com/ json txt online parse tool
        response.getWriter().write(str);
    }

    /**
     * find detailed info of a travel route by id
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void routeDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = service.routeDetail(rid);
        response.getWriter().write(mapper.writeValueAsString(route));
    }

    /**
     * check whether this route has been added as favorite by login user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user==null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }
        boolean flag = fav_service.isFavorite(rid, uid);
        response.getWriter().write(mapper.writeValueAsString(flag));
    }

    /**
     * login user can add this route as favorite
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user==null) {
            return;
        } else {
            uid = user.getUid();
        }
        fav_service.addFavorite(rid,uid);


    }

}
