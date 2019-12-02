package padone.common.controller.User;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.User.FamilyGetPatientServer;
import padone.common.model.User.FamilysPatient;

@SuppressWarnings("serial")
@WebServlet("/FamilyGetPatientServlet")

public class FamilyGetPatientServlet extends HttpServlet
{
	public FamilyGetPatientServlet()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Gson gson = new Gson();
		// 連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		FamilyGetPatientServer server = new FamilyGetPatientServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		/*******************************************************************************************/
		ArrayList<FamilysPatient> patientSet = new ArrayList<FamilysPatient>();
		/*******************************************************************************************/

		patientSet = server.getPatient(datasource, userID);

		System.out.println("在servlet中的profileSet: " + patientSet);

		response.getWriter().write(gson.toJson(patientSet));
	}
}