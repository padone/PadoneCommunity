package padone.common.controller.Article;
import padone.common.model.Article.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetFeedbackServlet
 */
@WebServlet("/GetFeedbackServlet")
public class GetFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFeedbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setContentType("application/json");
		FeedbackHandler feedbackHandler=new FeedbackHandler();
		String articleID=request.getParameter("articleID");
		
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		ArrayList<Feedback> feedback=feedbackHandler.getFeedback(datasource, articleID);
		Gson gson=new Gson();
		PrintWriter out = response.getWriter();
		String result=gson.toJson(feedback);
		out.println(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
