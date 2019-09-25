package padone.common.controller.Diary;
import padone.common.model.Diary.DiaryHandler;
import padone.common.model.Diary.FamilyDiary;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class GetFamilyDiaryServlet
 */
@WebServlet("/GetFamilyDiaryServlet")
public class GetFamilyDiaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFamilyDiaryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID=request.getParameter("userID");
		String date=request.getParameter("date");
		response.setContentType("text/html;charset=UTF-8");
		Gson gson = new Gson();
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		DiaryHandler getFamilyDiary=new DiaryHandler();
		FamilyDiary diary=new FamilyDiary();
		diary=getFamilyDiary.getFamilyDiary(datasource, userID, date);
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
