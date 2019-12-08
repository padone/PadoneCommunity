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

import padone.common.model.User.PatientGetFamilyServer;
import padone.common.model.User.PatientsFamily;

@SuppressWarnings("serial")
@WebServlet("/PatientGetFamilyServlet")

public class PatientGetFamilyServlet extends HttpServlet
{
	public PatientGetFamilyServlet()
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
		PatientGetFamilyServer server = new PatientGetFamilyServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		/*******************************************************************************************/
		ArrayList<PatientsFamily> familySet = new ArrayList<PatientsFamily>();
		/*******************************************************************************************/

		familySet = server.getFamily(datasource, userID);

		System.out.println("在servlet中的profileSet: " + familySet);

		response.getWriter().write(gson.toJson(familySet));
	}
}