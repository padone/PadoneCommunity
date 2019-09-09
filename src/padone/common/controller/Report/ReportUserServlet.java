package padone.common.controller.Report;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jdbc.pool.DataSource;

import padone.common.model.Report.ReportHandler;

/**
 * Servlet implementation class ReportUserServlet
 */
@WebServlet("/ReportUserServlet")
public class ReportUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String violatorID=request.getParameter("violatorID");
		String userID=request.getParameter("userID");
		String reason=request.getParameter("reason");
		String description=request.getParameter("descripton");
		DataSource datasource = (DataSource) getServletContext().getAttribute("db");
		ReportHandler report=new ReportHandler();
		if(userID!=null) {
			
			response.getWriter().println(report.ReportUser(datasource, violatorID, userID, reason, description));
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
