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

/**
 * Servlet implementation class EditArticleServlet
 */
@WebServlet("/EditArticleServlet")
public class EditArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditArticleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String articleID=request.getParameter("articleID");
		String title=request.getParameter("title");
		String author=request.getParameter("author");
		String department=request.getParameter("deppartment");
		String description=request.getParameter("description");
		String Image=request.getParameter("Image");
		String tag = request.getParameter("tag");
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		Gson gson=new Gson();
		ArticleHandler writeAreicle=new ArticleHandler();
		Date date= new Date();
		if(writeAreicle.editArticle(datasource, articleID, title,  tag, date, department, description, Image,tag)) {
    		response.getWriter().write(gson.toJson(true));
    	}
    	else {
    		response.getWriter().write(gson.toJson(false));
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
