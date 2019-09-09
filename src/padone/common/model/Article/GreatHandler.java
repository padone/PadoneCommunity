package padone.common.model.Article;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.tomcat.jdbc.pool.DataSource;

public class GreatHandler {
	public boolean great(DataSource datasource,String userID,String articleID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
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
		}
		return true;
	}
	public boolean notGreat(DataSource datasource,String userID,String articleID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String result="delete from great where articleiD = "+articleID+" and userID = "+userID;
			System.out.println(result);
			int delete = st.executeUpdate(result);
			
			
		    
		    st.close();//關閉st
			if(delete>0){
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
	public int countingGreat(DataSource datasource,String articleID) {
		Connection con = null;
		int num=0;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String result="select from great where articleiD = articleID";
			System.out.println(result);
			ResultSet rs = st.executeQuery(result);
			
			while(rs.next()){
				num++;
			}
		    st.close();//關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return num;
	}
	
}
