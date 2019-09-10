package padone.common.model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.tomcat.jdbc.pool.DataSource;


public class FeedbackHandler {

	public boolean newFeedback(DataSource datasource,String userID,String articleID,String message) {
			Connection con = null;
			try {
				con = datasource.getConnection();
				Statement st = con.createStatement();
				Date date= new Date();
				
				Object param = new java.sql.Timestamp(date.getTime());
				System.out.println(param);
				 int insert = st.executeUpdate("insert into feedback(feedbackID,articleID,authorID,updatetime,message) select ifNULL(max(feedbackID+0),0)+1,'" + articleID + "','" + userID + "','"+ param + "','" + message + "' FROM feedback");
				//int insert = st.executeUpdate("insert into feedback"+"(feedbackID,articleID,author,updatetime,message))select ifNULL(max(feedbackID+0),0)+1,'"+ articleID + "','"+ author + "','" + param + "','" + message+"' FROM feedback");	  
			    st.close();//關閉st
			} catch (SQLException e) {
				System.out.println("Exception :" + e.toString());
				e.printStackTrace();
			}finally {
			      if (con!=null) try {con.close();}catch (Exception ignore) {}
			}
			return true;
	}
	
	public boolean deleteFeedback(DataSource datasource,String feedbackID,String userID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sql="delete from feedback where feedbackID='"+feedbackID+"'"+"and authorID='"+userID+"'";
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return false;
	}
    public ArrayList<Feedback> getFeedback(DataSource datasource,String articleID){
    	Connection con = null;
    	ArrayList<Feedback> result=new ArrayList();
    	try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs=st.executeQuery("select * from feedback where articleID='"+articleID+"'");
			while(rs.next()) {
				Feedback temp=new Feedback();
				String authorID=rs.getString("authorID");
				String id=rs.getString("feedbackID");
				String updateTime=rs.getString("updateTime");
				String message=rs.getString("message");
				temp.feedback(articleID, id, author,authorID, message, updateTime);
				result.add(temp);
				
				
			}
				
			
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return result;
    	
    }
}
