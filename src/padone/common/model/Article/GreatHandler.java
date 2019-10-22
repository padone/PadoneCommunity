package padone.common.model.Article;
import java.sql.*;

import org.apache.tomcat.jdbc.pool.DataSource;

public class GreatHandler {
	public boolean great(DataSource datasource,String userID,String articleID) {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		String cmd = "";
		int result = 0;
		/*
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String check="select * from great where articleID = '"+articleID+"' and userID = '"+userID+"'";
			ResultSet rs =st.executeQuery(check);
			if(rs.next()) {
				String del="delete from great where articleID ='"+articleID+"' and userID = '"+userID+"'";
				st.executeUpdate(del);
				st.close();
				System.out.println(del);
				return true;
			}
			String result="insert into great"+"(articleID,userID)values('" 
				    + articleID + "','" +  userID+"' )";
			System.out.println(result);
			int insert = st.executeUpdate(result);
			
			
		    
		    st.close();//關閉st
			if(insert>0){
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
		}*/
		try{
		    if(ifEvaluated(datasource, articleID, userID)){
                cmd = "DELETE FROM great WHERE articleID = ? AND userID = ?";
            }else{
		        cmd = "INSERt INTO great(articleID, userID) VALUES (?, ?)"; }
            conn = datasource.getConnection();
            pstmt = conn.prepareStatement(cmd);
            pstmt.setString(1 , articleID);
            pstmt.setString(2, userID);
            result = pstmt.executeUpdate();
        }catch (SQLException e){
		    e.printStackTrace();
        }finally {
		    if(conn != null){
		        try{ conn.close(); } catch (SQLException ignored){}
            }
            if(pstmt != null){
                try{ pstmt.close(); } catch (SQLException ignored){}
            }
        }
        if(result > 0)
            return true;
        else
            return false;
	}
	public boolean checkGreat(DataSource datasource,String userID,String articleID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String result="select from great where articleiD = "+articleID+" and userID = "+userID;
			System.out.println(result);
			ResultSet rs = st.executeQuery(result);
			while(rs.next()){
				if(rs.getString("articleID")==null)return false;
				else return true;
			}
		    st.close();//關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}

	public boolean ifEvaluated(DataSource dataSource, String articleID, String userID){
		Connection conn = null;
		PreparedStatement pstmt = null;
		String cmd = null;
		ResultSet rs = null;
		int result = -1;
		try{
			conn = dataSource.getConnection();
			cmd = "SELECT COUNT(DISTINCT userID) as num FROM great WHERE articleID = ? AND userID = ?";
			pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, articleID);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = rs.getInt("num");
			}
			rs.close();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			if(conn != null){
				try{ conn.close(); } catch (SQLException ignored){}
			}
			if(pstmt != null){
				try{ pstmt.close(); } catch (SQLException ignored){}
			}
		}
		if(result > 0)
			return true;
		else
			return false;
	}

	public int getGreatCount(DataSource dataSource, String id){
		Connection conn = null;
		Statement stmt = null;
		String cmd;
		ResultSet rs = null;
		int num = 0;
		try{
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			cmd = "SELECT COUNT(DISTINCT userID) as num FROM great WHERE articleID = '" + id + "'";
			rs = stmt.executeQuery(cmd);
			if(rs.next()){
				num = rs.getInt("num");
			}
			rs.close();

		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try { conn.close(); } catch (SQLException ignored) {}
			}
			if(stmt != null){
				try { stmt.close(); } catch (SQLException ignored) {}
			}
		}
		return num;
	}
	
}
