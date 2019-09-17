package padone.common.model.Report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ReportHandler {
	public boolean ReportUser(DataSource datasource,String violatorID,String userID,String reason ,String description) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			 String result="";
			 
			result="insert into reportuser(violatorID,userID,reason,description)values('"+violatorID+"','"+userID+"','"+reason+"','"+description+"')";
			st.executeUpdate(result);
			 
			 st.close();//關閉st		    
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
		
	}
	public boolean ReportArticle(DataSource datasource,String articleID,String author,String title,String userID,String reason ,String description) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			 String result="";
			 
			result="insert into reportarticle(articleID,title,author,userID,reason,description)values('"+articleID+"','"+title+"','"+author+"','"+userID+"','"+reason+"','"+description+"')";
			st.executeUpdate(result);
			 
			 st.close();//關閉st		    
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
		
	}
	public boolean ReportFeedback(DataSource datasource,String articleID,String feedbackID,String author,String message,String userID,String reason ,String description) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			 String result="";
			 
			result="insert into reportfeedback(articleID,feedbackID,author,message,userID,reason,description)values('"+articleID+"','"+feedbackID+"','"+author+"','"+message+"','"+userID+"','"+reason+"','"+description+"')";
			st.executeUpdate(result);
			 
			 st.close();//關閉st		    
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
		
	}
}
