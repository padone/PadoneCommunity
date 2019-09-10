package padone.common.controller.Search;

import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Search.ArticleListServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/HospitalArticleListServlet")
public class HospitalArticleListServlet extends HttpServlet {
    public HospitalArticleListServlet(){
        super();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson;

        String position = request.getParameter("hospital");

        gson = new Gson();
        response.getWriter().print(gson.toJson(ArticleListServer.getHospitalArticle(dataSource, position)));
    }
}
