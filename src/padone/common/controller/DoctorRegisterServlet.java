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

import padone.common.model.DoctorRegisterServer;

@SuppressWarnings("serial")
@WebServlet("/DoctorRegisterServlet")
public class DoctorRegisterServlet extends HttpServlet
{
	public DoctorRegisterServlet()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().write("doctor connection testing\n");

		DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
		try
		{
			Connection conn = dataSource.getConnection();
			if (!conn.isClosed())
			{
				response.getWriter().write("connected");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select patientID FROM healthtracking WHERE healthTrackingID = 1");
				PrintWriter out = response.getWriter();
				while (rs.next())
				{
					out.println("\n" + rs.getString("patientID"));
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
		DoctorRegisterServer register = new DoctorRegisterServer();
		/*******************************************************************************************/
		String account = request.getParameter("account");
		String password = request.getParameter("Password");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");
		String backUpPhone = request.getParameter("backUpPhone");
		/*******************************************************************************************/
		@SuppressWarnings("rawtypes")
		HashMap registerAdd = new HashMap(); // 新增紀錄結果
		int genderNum = 0;
		/*******************************************************************************************/
		if (gender.equals("男"))
			genderNum = 1;
		else if (gender.equals("女"))
			genderNum = 2;
		else
			genderNum = 3;

		registerAdd = register.registerAdd(datasource, account, password, genderNum, name, mail, phone, backUpPhone);

		System.out.println("在servlet中的registerAdd: " + registerAdd);

		response.getWriter().write(gson.toJson(registerAdd));
	}
}