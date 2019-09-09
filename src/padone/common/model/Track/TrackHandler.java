package padone.common.model.Track;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

public class TrackHandler {
	public boolean newTrackArticle(DataSource datasource, String articleID, String userID,String title) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			
			System.out.println("HI");
			String result="insert into trackarticle"+"(articleID,title,userID)values('" 
			+ articleID + "','" + title + "','"+userID+"' )";
			System.out.println(result);
			int articleInsert = st.executeUpdate(result);
			
			
		   
		    st.close();//關閉st
			if(articleInsert>0){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {
		    	  return false;
		      }
		}
		
	}
    public boolean deleteTrackArticle(DataSource datasource, String articleID, String userID) {
    	Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sql="delete from trackarticle where articleID='"+articleID+"' and  userID='"+userID+"'";
			System.out.println(sql);
			st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
    
    public boolean newTrackUser(DataSource datasource, String target, String follow) {
    	Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			
			System.out.println("HI");
			String result="insert into tracktarget"+"(target,follow)values('" 
			+ target + "','"+follow+"' )";
			System.out.println(result);
			int targetInsert = st.executeUpdate(result);
			
			
		   
		    st.close();//關閉st
			if(targetInsert>0){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {
		    	  return false;
		      }
		}
    }
    public boolean deleteTrackUser(DataSource datasource, String target, String follow) {
    	return true;
    }

}
