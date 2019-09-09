package padone.common.controller.Article;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.Article.ArticleHandler;


@WebServlet("/WriteArticleServlet")
public class WriteArticleServlet extends HttpServlet {
	String title="";
	String author="";
	String department="";
	String description="";
	String Image="";
	String tag = "";
	int tagNumber;
	public WriteArticleServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	
    	Gson gson = new Gson();	
		//連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		ArticleHandler writeAreicle=new ArticleHandler();
		/*******************************/
		Date date= new Date();
		String articleID="";
		if(date.getMonth()+1>10&&date.getDate()>10)articleID=date.getYear()+""+(date.getMonth()+1)+""+date.getDate()+set();
		if(date.getMonth()+1<10&&date.getDate()>10)articleID=date.getYear()+"0"+(date.getMonth()+1)+""+date.getDate()+set();
		if(date.getMonth()+1>10&&date.getDate()<10)articleID=date.getYear()+""+(date.getMonth()+1)+"0"+date.getDate()+set();
		if(date.getMonth()+1<10&&date.getDate()<10)articleID=date.getYear()+"0"+(date.getMonth()+1)+"0"+date.getDate()+set();
		title=request.getParameter("title");
    	author=request.getParameter("author");
    	department=request.getParameter("department");
    	description=request.getParameter("description");
    	Image=request.getParameter("Image");
    	tag=request.getParameter("tag");
    	if(writeAreicle.newArticle(datasource, articleID, title, author, date, department, description, Image,tag)) {
    		response.getWriter().write(gson.toJson(true));
    	}
    	else {
    		response.getWriter().write(gson.toJson(false));
    	}
    	
        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.getWriter().println("HI");
		DataSource d = (DataSource) getServletContext().getAttribute("db");
		/*response.getWriter().println(title);
		response.getWriter().println(author);
		response.getWriter().println(department);
		response.getWriter().println(description);
		response.getWriter().println(Image);*/
		try {
			response.getWriter().println("test1");
			Connection conn = d.getConnection();
			response.getWriter().println("test");
			if(!conn.isClosed()){
				response.getWriter().write("connected");
				// print random sql statement result to check connection
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT patientID FROM healthtracking WHERE healthTrackingID = 1");
				PrintWriter out = response.getWriter();
				while(rs.next()){
					out.println("\n" + rs.getString("patientID"));
				}
				response.getWriter().write("connection success");
			}else{
				response.getWriter().write("connection failed");
			}
		} catch (SQLException e) {
			response.getWriter().println("error");
			e.printStackTrace();
		}
	}
	public String set() {
		char[] p = {
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'0','1','2','3','4','5','6','7','8','9'
				};
		Random ran = new Random();
		return p[ran.nextInt(62)]+""+p[ran.nextInt(62)]+""+p[ran.nextInt(62)];
	
	}
}
