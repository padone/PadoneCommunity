package padone.common.controller.Diary;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.Diary.DiaryHandler;
import padone.common.model.Diary.PatientDiary;

/**
 * Servlet implementation class GetPatientDiaryServlet
 */
@WebServlet("/GetPatientDiaryServlet")
public class GetPatientDiaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPatientDiaryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setContentType("text/html;charset=UTF-8");
		Gson gson = new Gson();
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		DiaryHandler getPatientDiary=new DiaryHandler();
		String userID=request.getParameter("userID");
		String date=request.getParameter("date");
		PatientDiary diary=new PatientDiary();
		diary=getPatientDiary.getPatientDiary(datasource, userID, date);
		String res=gson.toJson(diary);
		response.getWriter().println(res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
