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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("application/json; charset=utf-8");
        DataSource datasource = (DataSource)getServletContext().getAttribute("db");

        String option = request.getParameter("pattern");
        String target;
        Gson gson;


        if(option.equals("all")){
            // get all article
            gson = new Gson();
            response.getWriter().print(gson.toJson(ArticleServer.getAllArticle(datasource)));
        }else if(option.equals("author")){
            gson = new Gson();
            target = request.getParameter("name");
            response.getWriter().print(gson.toJson(ArticleServer.getAuthorArticle(datasource, target)));
            // get author article
        }else if(option.equals("department")){
            gson = new Gson();
            target = request.getParameter("department");
            response.getWriter().print(gson.toJson(ArticleServer.getCategoryArticle(datasource, target)));
            // get department article
        }else if(option.equals("articleID")){
            gson = new Gson();
            target = request.getParameter("id");
            response.getWriter().print(gson.toJson(ArticleServer.getSpecificArticle(datasource, target)));
            // get specific article
        }else if(option.equals("singleTag")){
            gson = new Gson();
            target = request.getParameter("tag");
            response.getWriter().print(gson.toJson(ArticleServer.getSingleTagArticle(datasource, target)));
        }
    }
}
