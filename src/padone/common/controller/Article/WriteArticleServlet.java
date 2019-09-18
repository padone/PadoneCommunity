package padone.common.controller.Article;
import padone.common.model.Article.*;

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


@WebServlet("/WriteArticleServlet")
public class WriteArticleServlet extends HttpServlet {
	String title="";
	String author="";
	String department="";
	String description="";
	String[] imageURL;
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
		ArticleHandler writeArticle=new ArticleHandler();
		/*******************************/
		
		String articleID="";
	
		title=request.getParameter("title");
    	author=request.getParameter("userID");
    	department=request.getParameter("department");
    	description=request.getParameter("description");
    	imageURL=request.getParameterValues("image");
    	String hospital=request.getParameter("hospital");
    	tag=request.getParameter("tag");
    	if(writeArticle.newArticle(datasource, title, author, department, description, imageURL, tag, hospital)) {
    		response.getWriter().write(gson.toJson(true));
    	}
    	else {
    		response.getWriter().write(gson.toJson(false));
    	}
    	
        
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
		DataSource d = (DataSource) getServletContext().getAttribute("db");
		
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
}
