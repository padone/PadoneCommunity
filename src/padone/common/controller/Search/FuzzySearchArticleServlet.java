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

@WebServlet("/FuzzySearchArticleServlet")
public class FuzzySearchArticleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
        Gson gson = new Gson();

        String searchMode = req.getParameter("mode");
        String fragment = req.getParameter("keyword");
        String userID = req.getParameter("userID");

        switch (searchMode) {
            case "user":
                resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaUserName(dataSource, fragment)));
                break;
            case "title":
                resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaTitle(dataSource, fragment, userID)));
                break;
            case "content":
                resp.getWriter().print(gson.toJson(FuzzySearchServer.searchArticleViaContent(dataSource, fragment, userID)));
                break;
        }
    }
}
