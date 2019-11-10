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

@WebServlet("/FuzzySearchUser")
public class FuzzySearchUser extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json; charset=utf-8");
        DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
        Gson gson = new Gson();
        String fragment = request.getParameter("keyword");
        // search patient
        response.getWriter().print(gson.toJson(FuzzySearchServer.searchPatient(dataSource, fragment)));
        // search doctor
        // TODO : add search doctor
    }
}
