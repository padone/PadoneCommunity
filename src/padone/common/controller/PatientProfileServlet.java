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

import padone.common.model.PatientProfileServer;

@SuppressWarnings("serial")
@WebServlet("/PatientProfileServlet")

public class PatientProfileServlet extends HttpServlet
{
	public PatientProfileServlet()
	{
		super();
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setContentType("text/html");
		
		DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
		try
		{
			Connection conn = dataSource.getConnection();
			if (!conn.isClosed())
			{
				Gson gson = new Gson();
				Statement stmt = conn.createStatement();
				String userID = request.getParameter("userID");
				//String userID = "1000";
				ResultSet rs = stmt.executeQuery(
						"select name, birthday, mail, introduction FROM patient WHERE userID = '" + userID + "'");

				@SuppressWarnings("rawtypes")
				HashMap profileSet = new HashMap();

				while (rs.next())
				{
					String name = rs.getString("name");
					String birthday = rs.getString("birthday");
					String mail = rs.getString("mail");
					String introduction = rs.getString("introduction");

					profileSet.put("name", name);
					profileSet.put("birthday", birthday);
					profileSet.put("mail", mail);
					profileSet.put("introduction", introduction);

					System.out.println("在servlet中的profileSet: " + profileSet);

					response.getWriter().write(gson.toJson(profileSet));
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
		PatientProfileServer setting = new PatientProfileServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		String name = request.getParameter("name");
		String birthday = request.getParameter("birthday");
		String mail = request.getParameter("mail");
		String introduction = request.getParameter("introduction");
		/*******************************************************************************************/
		@SuppressWarnings("rawtypes")
		HashMap profileSet = new HashMap();
		/*******************************************************************************************/

		profileSet = setting.profileSet(datasource, userID, name, birthday, mail, introduction);

		System.out.println("在servlet中的profileSet: " + profileSet);

		response.getWriter().write(gson.toJson(profileSet));
	}
}