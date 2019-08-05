package PadoneArticleMod;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class DeleteFeedback
 */
@WebServlet("/DeleteFeedbackServlet")
public class DeleteFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFeedbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String feedbackID=null;
		String userID=null;
		response.setContentType("text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	Gson gson = new Gson();	
    	feedbackID=request.getParameter("feedbackID");
    	userID=request.getParameter("userID");
    	FeedbackHandler deleteFeedback=new FeedbackHandler();
		//連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		if (feedbackID != null) {
			if (deleteFeedback.deleteFeedback(datasource, feedbackID, userID)) {
				response.getWriter().write(gson.toJson(true));
			} else {
				response.getWriter().write(gson.toJson(false));
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
