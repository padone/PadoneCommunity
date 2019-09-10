package padone.common.model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ArticleHandler {
	public boolean newArticle(DataSource datasource, String articleID, String title, String authorID, Date date,
			String department,String description,String image,String tag,String hospital) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			Object param = new java.sql.Timestamp(date.getTime());
			System.out.println("HI");
			String result="insert into article(articleID,title,authorID,department,description,posttime,lastupdatetime,image,tag,hospital)values('" 
				    + articleID + "','" + title + "','"+ authorID + "','" + department + "','" + description + "','"+ param + "','" + param + "','"+image+"','"+tag+"','"+hospital+"' )";
			System.out.println(result);
			int articleInsert = st.executeUpdate(result);
			
			String resultII="insert into tag"+"(articleID,tagName)values('" + articleID + "','" + tag + "' )";
			System.out.println(resultII);
			int tagInsert = st.executeUpdate(resultII);//
		   
		    
		    st.close();//關閉st
			if(tagInsert > 0&&articleInsert>0){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}

	public boolean deleteArticle(DataSource datasource,String articleID,String userID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String author="";
			String sql="delete from article where articleID='"+articleID+"' and authorID='"+userID+"'";
			st.executeUpdate(sql);
			return true;
			
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
	public boolean editArticle(DataSource datasource,String articleID, String title, String author, Date date,
			String department,String description,String image,String tag) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			Object param = new java.sql.Timestamp(date.getTime());
			System.out.println("HI");
			
		    
		    st.close();//關閉st
			if(true){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}

}
