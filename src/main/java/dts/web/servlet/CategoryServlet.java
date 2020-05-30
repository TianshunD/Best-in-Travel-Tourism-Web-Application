package dts.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dts.domain.Category;
import dts.domain.ResultInfo;
import dts.service.CategoryService;
import dts.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private ResultInfo info = new ResultInfo();
    private ObjectMapper mapper = new ObjectMapper();
    private CategoryService service = new CategoryServiceImpl();

    /**
     * find all categories
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> all = service.findAll();
        response.getWriter().write(mapper.writeValueAsString(all));
    }

    protected void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category ca = new Category();
        if (cid!=null && cid.length()!=0) {
            ca = service.find(Integer.parseInt(cid));
        }
        response.getWriter().write(mapper.writeValueAsString(ca));
    }
}
