package padone.common.controller.User;

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

import padone.common.model.User.SwitchIdentityServer;;

@SuppressWarnings("serial")
@WebServlet("/SwitchIdentityServlet")

public class SwitchIdentityServlet extends HttpServlet
{
	public SwitchIdentityServlet()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().write("switch identity testing\n");

		DataSource dataSource = (DataSource) getServletContext().getAttribute("db");
		try
		{
			Connection conn = dataSource.getConnection();
			if(!conn.isClosed())
			{
				response.getWriter().write("connected");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select userID FROM patient WHERE account = 'A123456789'");
				PrintWriter out = response.getWriter();
				while(rs.next())
				{
					out.println("\n" + rs.getString("userID"));
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
		SwitchIdentityServer switchServer = new SwitchIdentityServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		String previous = request.getParameter("previous");
		String next = request.getParameter("next");
		/*******************************************************************************************/
		@SuppressWarnings("rawtypes")
		HashMap switchSet = new HashMap();
		/*******************************************************************************************/

		switchSet = switchServer.switchIdentity(datasource, userID, previous, next);

		System.out.println("在servlet中的switchSet: " + switchSet);

		response.getWriter().write(gson.toJson(switchSet));
	}
}