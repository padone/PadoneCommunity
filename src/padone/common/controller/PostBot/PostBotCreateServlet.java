package padone.common.controller.PostBot;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.Article.ArticleHandler;
import padone.common.model.PostBot.News;
import padone.common.model.PostBot.PostBotCrawlerCenter;

@WebServlet("/PostBotCreateServlet")
public class PostBotCreateServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public PostBotCreateServlet() 
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
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		PostBotCrawlerCenter crawler = new PostBotCrawlerCenter();
		ArticleHandler handler = new ArticleHandler();
		Boolean newArticle = false;
		/*******************************************************************************************/
		String botID = request.getParameter("botID");
		/*******************************************************************************************/
		News crawlerNews = new News();
		/*******************************************************************************************/

		crawlerNews = crawler.setCrawler(datasource, botID);
		newArticle = handler.newArticle(datasource, crawlerNews.getTitle(), "6684", "其他或健康運動資訊", crawlerNews.getArticle(),
				crawlerNews.getArrayPhotoUrl(), "[" + crawlerNews.getKeyword() + "]", "無");

		System.out.println("在servlet中的crawlerNews: " + crawlerNews);
		System.out.println("在servlet中的crawlerNews的搜尋網址: " + crawlerNews.getSourceUrl());
		System.out.println("在servlet中的crawlerNews的圖片: " + crawlerNews.getArrayPhotoUrl());
		System.out.println("在servlet中的newArticle: " + newArticle);

		response.getWriter().write(gson.toJson(newArticle));
	}
}