package padone.common.controller;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;
import com.google.gson.Gson;

import padone.common.model.PatientSettingServer;

@SuppressWarnings("serial")
@WebServlet("/PatientSettingServlet")

public class PatientSettingServlet extends HttpServlet
{
	public PatientSettingServlet()
	{
		super();
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
		try
		{
			Connection conn = dataSource.getConnection();
			if (!conn.isClosed())
			{
				Gson gson = new Gson();
				Statement stmt = conn.createStatement();
				String userID = request.getParameter("userID");
				//String userID = "1003";
				ResultSet rs = stmt.executeQuery(
						"select track, birthday, mail, introduction, global, familyOnly, followers, myFamily, collaborative, notification FROM patientpermission WHERE userID = '"
								+ userID + "'");

				@SuppressWarnings("rawtypes")
				HashMap patientSet = new HashMap();

				while (rs.next())
				{	
					String track = rs.getString("track");
					String birthday = rs.getString("birthday");
					String mail = rs.getString("mail");
					String introduction = rs.getString("introduction");
					String global = rs.getString("global");
					String familyOnly = rs.getString("familyOnly");
					String followers = rs.getString("followers");
					String myFamily = rs.getString("myFamily");
					String collaborative = rs.getString("collaborative");
					String notification = rs.getString("notification");

					patientSet.put("track", track);
					patientSet.put("birthday", birthday);
					patientSet.put("mail", mail);
					patientSet.put("introduction", introduction);
					patientSet.put("global", global);
					patientSet.put("familyOnly", familyOnly);
					patientSet.put("followers", followers);
					patientSet.put("myFamily", myFamily);
					patientSet.put("collaborative", collaborative);
					patientSet.put("notification", notification);

					System.out.println("在servlet中的profileSet: " + patientSet);

					response.getWriter().write(gson.toJson(patientSet));
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Gson gson = new Gson();
		// 連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		PatientSettingServer setting = new PatientSettingServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		String track = request.getParameter("track");
		String birthday = request.getParameter("birthday");
		String mail = request.getParameter("mail");
		String introduction = request.getParameter("introduction");
		String global = request.getParameter("global");
		String familyOnly = request.getParameter("familyOnly");
		String followers = request.getParameter("followers");
		String myFamily = request.getParameter("myFamily");
		String collaborative = request.getParameter("collaborative");
		String notification = request.getParameter("notification");
		/*******************************************************************************************/
		@SuppressWarnings("rawtypes")
		HashMap patientSet = new HashMap();
		/*******************************************************************************************/

		patientSet = setting.patientSet(datasource, userID, track, birthday, mail, introduction, global, familyOnly,
				followers, myFamily, collaborative, notification);

		System.out.println("在servlet中的profileSet: " + patientSet);

		response.getWriter().write(gson.toJson(patientSet));
	}
}