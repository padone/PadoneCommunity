package padone.common.controller.Article;
import padone.common.model.Article.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.gson.Gson;

/**
 * Servlet implementation class DeleteArticleServlet
 */
@WebServlet("/DeleteArticleServlet")
public class DeleteArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteArticleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String articleID=null;
		response.setContentType("text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	Gson gson = new Gson();	
    	articleID=request.getParameter("articleID");
    	ArticleHandler deleteArticle=new ArticleHandler();
		//連接資料庫
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		if (articleID != null) {
			if (deleteArticle.deleteArticle(datasource, articleID)) {
				response.getWriter().write(gson.toJson(true));
			} else {
				response.getWriter().write(gson.toJson(false));
			}
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
