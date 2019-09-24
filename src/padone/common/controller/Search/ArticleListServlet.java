package padone.common.controller.Search;

import javax.servlet.ServletException;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import padone.common.model.Search.ArticleListServer;
import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;

@WebServlet("/ArticleListServlet")
public class ArticleListServlet extends HttpServlet {
    public ArticleListServlet(){ super(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        DataSource datasource = (DataSource)getServletContext().getAttribute("db");
        Gson gson;

        // get all article from database
        gson = new Gson();
        response.getWriter().print(gson.toJson(ArticleListServer.getAllArticle(datasource)));
    }
}
