package padone.common.controller.Search;

import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Search.TrackingTargetServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/FamilyTrackingArticleListServlet")
public class FamilyTrackingArticleListServlet extends HttpServlet {
    public FamilyTrackingArticleListServlet(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson = new Gson();
        String userID = req.getParameter("id");
        String tableName = req.getParameter("tableName");

        if(tableName.contains("track"))
            resp.getWriter().print(gson.toJson(TrackingTargetServer.getTrackingArticle(dataSource, userID, tableName)));
        else if(tableName.contains("suggest"))
            resp.getWriter().print(gson.toJson(TrackingTargetServer.getMySuggestArticle(dataSource, userID)));
    }
}
