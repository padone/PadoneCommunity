package padone.common.controller.Diary;
import padone.common.model.Diary.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class WritrFamilyDiaryServlet
 */
@WebServlet("/WriteFamilyDiaryServlet")
public class WriteFamilyDiaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteFamilyDiaryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");;
		
		String userID;
		String date;
		String description;
		String image;
		DiaryHandler writeFamilyDiary=new DiaryHandler();
		Gson gson = new Gson();	
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		userID=request.getParameter("userID");
		date=request.getParameter("date");
		description=request.getParameter("description");
		image=request.getParameter("picture");
		if (userID != null) {
			if (writeFamilyDiary.writeFamilyDiary(datasource, userID, date, image, description)) {
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
