package padone.common.controller.Search;

import com.google.gson.Gson;
import padone.common.model.Search.ArticleListServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.IOException;

@WebServlet("/SpecificArticleListServlet")
public class SpecificArticleListServlet extends HttpServlet {
    public SpecificArticleListServlet(){ super(); }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson = new Gson();
        String articleID = request.getParameter("articleID");
        String userID = request.getParameter("userID");

        response.getWriter().print(gson.toJson(ArticleListServer.getSpecificArticle(dataSource, articleID, userID)));
    }
}
