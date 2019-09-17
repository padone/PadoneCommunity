package padone.common.controller.Search;
import padone.common.model.Search.ReportListServer;

import com.google.gson.Gson;
import org.apache.tomcat.jdbc.pool.DataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ReportedArticleListServlet")
public class ReportedArticleListServlet extends HttpServlet {
    public ReportedArticleListServlet(){ super(); }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource)getServletContext().getAttribute("db");
        Gson gson = new Gson();

        response.getWriter().print(gson.toJson(ReportListServer.getReportedArticle(dataSource)));
    }
}
