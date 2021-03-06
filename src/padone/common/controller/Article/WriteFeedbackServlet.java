package padone.common.controller.Article;
import padone.common.model.Article.*;

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
 * Servlet implementation class WriteFeedbackServlet
 */
@WebServlet("/WriteFeedbackServlet")
public class WriteFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteFeedbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String author=null;
		String message;
		String articleID;
		response.setContentType("text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	FeedbackHandler writefeedback=new FeedbackHandler();
    	Gson gson = new Gson();	
    	author=request.getParameter("authorID");
    	message=request.getParameter("message");
    	articleID=request.getParameter("articleID");
		//連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		if(author!=null) {
			ArticleHandler writeAreicle=new ArticleHandler();
			if(writefeedback.newFeedback(datasource, author, articleID, message)) {
				response.getWriter().write(gson.toJson(true));
			}
			else {
				response.getWriter().write(gson.toJson(false));
			}
		}
	}

}
