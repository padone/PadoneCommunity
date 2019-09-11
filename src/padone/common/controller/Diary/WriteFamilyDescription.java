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
 * Servlet implementation class WriteFamilyDescription
 */
@WebServlet("/WriteFamilyDescription")
public class WriteFamilyDescription extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WriteFamilyDescription() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		Gson gson=new Gson();
		String userID=request.getParameter("userID");
		String date=request.getParameter("date");
		String familyID=request.getParameter("familyID");
		String familyDescription=request.getParameter("familyDescription");
		DiaryHandler writeFmilyDescription=new DiaryHandler();
		if (userID != null) {
			if (writeFmilyDescription.writeFamilyDescription(datasource, userID, familyID, date, familyDescription)) {
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
