package padone.common.controller;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import padone.common.model.ArticleServer;
import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONArray;

@WebServlet("/ArticleListServlet")
public class ArticleListServlet extends HttpServlet {
    public ArticleListServlet(){ super(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        DataSource datasource = (DataSource)getServletContext().getAttribute("db");
        Gson gson;
        //super.doGet(req, resp);

        // get all article from database
        gson = new Gson();
        response.getWriter().print(gson.toJson(ArticleServer.getAllArticle(datasource)));
    }
}
