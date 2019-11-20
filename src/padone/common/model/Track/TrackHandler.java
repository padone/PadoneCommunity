package padone.common.model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

public class TrackHandler {
	public boolean newTrackArticle(DataSource datasource, String articleID, String userID,String tableName) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement st = null;
		String cmd = "";
		ResultSet rs = null;
		int updateLine;
		try {
			con = datasource.getConnection();
			//st = con.createStatement();
			cmd = "SELECT * FROM " + tableName + " WHERE articleID = ? and userID = ?";
			pstmt= con.prepareStatement(cmd);
			pstmt.setString(1, articleID);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();

			if(rs.next()){
				// tracked -> delete
				cmd = "DELETE FROM " + tableName + " WHERE articleID = ? and userID = ?";
			}else{
				// not tracked -> add
				cmd = "INSERT INTO " + tableName + " (articleID, userID) values (?, ?)";
			}
			pstmt = con.prepareStatement(cmd);
			pstmt.setString(1, articleID);
			pstmt.setString(2, userID);
			updateLine = pstmt.executeUpdate();
			/*
			String check="select * from "+tableName+" where articleID = '"+articleID+"' and userID = '"+userID+"'";
			System.out.println(check);
			rs =st.executeQuery(check);
			if(rs.next())return true;
			String result="insert into "+tableName+"(articleID,userID)values('" 
			+ articleID +  "','"+userID+"' )";
			System.out.println(result);
			int articleInsert = st.executeUpdate(result);
			*/
			
		    rs.close();
		    //st.close();//關閉st
			if(updateLine>0){
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
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		      if(pstmt != null){
		      	try {
		      		pstmt.close();
				}catch (Exception ignored){}
			  }
		}
		
	}
    public boolean deleteTrackArticle(DataSource datasource, String articleID, String userID,String tableName) {
    	Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sql="delete from "+tableName+" where articleID='"+articleID+"' and  userID='"+userID+"'";
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
			String result="insert into tracktarget(target,follow)values('" //SQL刪除
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
