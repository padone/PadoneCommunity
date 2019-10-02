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

@WebServlet("/TrackingArticleListServlet")
public class TrackingArticleListServlet extends HttpServlet {
    public TrackingArticleListServlet(){
        super();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("application/json; charset=utf-8");

        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson = new Gson();
        String userID = req.getParameter("id");
        String tableName = req.getParameter("tableName");

        resp.getWriter().print(gson.toJson(TrackingTargetServer.getTrackingArticle(dataSource, userID, tableName)));
    }
}
