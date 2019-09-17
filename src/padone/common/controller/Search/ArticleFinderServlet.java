package padone.common.controller.Search;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

import padone.common.model.Article.Article;
import padone.common.model.Search.ArticleFinder;


@WebServlet("/ArticleFinderServlet")
public class ArticleFinderServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
      
       Article article=new Article();
    public ArticleFinderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//PrintWriter out = response.getWriter();
		 String articleID="";
		Gson gson=new Gson();
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		//response.setContentType("application/json");
	
		articleID= request.getParameter("articleID");
		System.out.println(articleID);
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		ArticleFinder af=new ArticleFinder();
		if(af.ArticleFinder(datasource, articleID, article)) {
			String data = gson.toJson(article);
			System.out.println(data);
			response.getWriter().println(data);
			
		}
		else{
			response.getWriter().println(false);
		}
		response.getWriter().println("");
		response.getWriter().flush();
		response.getWriter().close();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.setContentType("text/html;charset=UTF-8");
		//PrintWriter out = response.getWriter();
		//out.flush();
		
    	//PrintWriter out = response.getWriter();
   
		
		
		
	}

}
