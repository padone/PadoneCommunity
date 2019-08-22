package padone.common.controller.Search;

import com.google.gson.Gson;
import padone.common.model.Search.ArticleListServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.tomcat.jdbc.pool.DataSource;

@WebServlet("/AuthorArticleListServlet")
public class AuthorArticleListServlet extends HttpServlet {
    public AuthorArticleListServlet(){ super(); }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson;

        String authorID = request.getParameter("id");

        gson = new Gson();
        response.getWriter().print(gson.toJson(ArticleListServer.getAuthorArticle(dataSource, authorID)));
    }
}
