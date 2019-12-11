package padone.common.controller.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.PostBot.GetPostBotServer;
import padone.common.model.PostBot.PostBot;

@SuppressWarnings("serial")
@WebServlet("/GetPostBotServlet")

public class GetPostBotServlet extends HttpServlet
{
	public GetPostBotServlet()
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
		GetPostBotServer server = new GetPostBotServer();
		ArrayList<PostBot> postBotSet = new ArrayList<PostBot>();
		/*******************************************************************************************/

		postBotSet = server.getPostBot(datasource);

		System.out.println("在servlet中的profileSet: " + postBotSet);

		response.getWriter().write(gson.toJson(postBotSet));
	}
}