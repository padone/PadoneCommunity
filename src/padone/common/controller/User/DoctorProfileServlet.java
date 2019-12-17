package padone.common.controller.User;

import java.io.IOException;
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

import padone.common.model.User.DoctorProfileServer;

/**
 * Servlet implementation class DoctorProfileServlet
 */
@WebServlet("/DoctorProfileServlet")
public class DoctorProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoctorProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
						"select * FROM doctor WHERE userID = '" + userID + "'");

				@SuppressWarnings("rawtypes")
				HashMap profileSet = new HashMap();

				while (rs.next())
				{
					String name = rs.getString("name");
					String gender = rs.getString("gender");
					String mail = rs.getString("mail");
					String department = rs.getString("introduction");
					String hospital=rs.getString("hospital");
					String phone=rs.getString("phone");
					String backupPhone=rs.getString("backupPhone");

					profileSet.put("name", name);
					profileSet.put("gender", gender);
					profileSet.put("mail", mail);
					profileSet.put("hospital", hospital);
					profileSet.put("department", department);
					profileSet.put("phone", phone);
					profileSet.put("backupPhone", backupPhone);

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
		DoctorProfileServer setting = new DoctorProfileServer();
		/*******************************************************************************************/
		String userID = request.getParameter("userID");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String mail = request.getParameter("mail");
		String hospital = request.getParameter("hospital");
		String department = request.getParameter("department");
		String phone = request.getParameter("phone");
		String backupPhone = request.getParameter("backupPhone");
		/*******************************************************************************************/
		@SuppressWarnings("rawtypes")
		HashMap profileSet = new HashMap();
		/*******************************************************************************************/

		profileSet = setting.profileSet(datasource, userID, name, gender, mail, hospital,backupPhone,department,phone);

		System.out.println("在servlet中的profileSet: " + profileSet);

		response.getWriter().write(gson.toJson(profileSet));
	}

}
