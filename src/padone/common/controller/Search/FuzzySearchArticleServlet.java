package padone.common.controller.Search;

import padone.common.model.Search.FuzzySearchServer;
import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/FuzzySearchArticleServlet")
public class FuzzySearchArticleServlet extends HttpServlet {
    public FuzzySearchArticleServlet(){
        super();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
        Gson gson = new Gson();

        String searchMode = req.getParameter("mode");
        String fragment = req.getParameter("keyword");

        if(searchMode.equals("user")){
            resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaUserName(dataSource, fragment)));
        }else if(searchMode.equals("title")){
            resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaTitle(dataSource, fragment)));
        }else if(searchMode.equals("content")){
            resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaContent(dataSource, fragment)));
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("connection test\n");
/*
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");

        String sql = "SELECT userID as id FROM patient WHERE account='testaccoun'";
        try{
            ResultSet rs = dataSource.getConnection().createStatement().executeQuery(sql);
            resp.getWriter().print(rs.getString("id"));
        }catch (SQLException e){
            e.printStackTrace();
        }

        resp.getWriter().print("\nclose\n");*/
    }
}
