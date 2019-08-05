package padone.common.controller.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.apache.tomcat.jdbc.pool.DataSource;

import padone.common.model.User.LoginServer;

@SuppressWarnings("serial")
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet
{
	public LoginServlet()
	{
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Gson gson = new Gson();
		// 連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		LoginServer login = new LoginServer();
		/*******************************************************************************************/
		String account = request.getParameter("account");
		String password = request.getParameter("Password");
		String identity = request.getParameter("identity");
		// String uuid = request.getParameter("uuid");

		// String userID = request.getParameter("userID");
		/*******************************************************************************************/
		/*******************************************************************************************/
		// 登入驗證
		response.getWriter().write(gson.toJson(login.userVerification(datasource, account, password, identity)));
	}
}