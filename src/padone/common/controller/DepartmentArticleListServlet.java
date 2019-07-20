package padone.common.controller;

import com.google.gson.Gson;
import padone.common.model.ArticleServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DepartmentArticleListServlet extends HttpServlet {
    public DepartmentArticleListServlet(){ super(); }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson;

        String department = request.getParameter("department");

        gson = new Gson();
        response.getWriter().print(gson.toJson(ArticleServer.getCategoryArticle(dataSource, department)));
    }
}
