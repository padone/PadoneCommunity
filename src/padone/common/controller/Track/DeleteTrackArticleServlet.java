package padone.common.controller.Track;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.Track.TrackHandler;

/**
 * Servlet implementation class DeleteTrackArticle
 */
@WebServlet("/DeleteTrackArticleServlet")
public class DeleteTrackArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteTrackArticleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		Gson gson=new Gson();
		TrackHandler track=new TrackHandler();
		PrintWriter out = response.getWriter();
		String articleID;
		String userID;
		String tableName;
		
		articleID=request.getParameter("articleID");
		userID=request.getParameter("userID");
		tableName=request.getParameter("tableName");
		if(articleID!=null) {
			if (track.deleteTrackArticle(datasource, articleID, userID,tableName)) {
				out.write(gson.toJson(true));
			} else {
				out.write(gson.toJson(false));
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
