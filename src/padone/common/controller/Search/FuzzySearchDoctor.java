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

@WebServlet("/FuzzySearchDoctor")
public class FuzzySearchDoctor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
        Gson gson = new Gson();
        String fragment = req.getParameter("keyword");

        resp.getWriter().print(gson.toJson(FuzzySearchServer.searchDoctor(dataSource, fragment)));
    }
}
