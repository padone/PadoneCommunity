package padone.common.model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.tomcat.jdbc.pool.DataSource;


public class FeedbackHandler {

	public boolean newFeedback(DataSource datasource,String authorID,String articleID,String message) {
			Connection con = null;
			try {
				con = datasource.getConnection();
				Statement st = con.createStatement();
				Date date= new Date();
				
				Object param = new java.sql.Timestamp(date.getTime());
				System.out.println(param);
				 int insert = st.executeUpdate("insert into feedback(feedbackID,articleID,authorID,updatetime,message) select ifNULL(max(feedbackID+0),0)+1,'" + articleID + "','" + authorID + "','"+ param + "','" + message + "' FROM feedback");
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
			String author="";
			ResultSet rs=st.executeQuery("select * from feedback where feedbackID='"+feedbackID+"'");
			while(rs.next()){
				author=""+rs.getString("author");
			}
			if(userID.equals(author)) {
				String sql="delete from feedback where feedbackID='"+feedbackID+"'";
				st.executeUpdate(sql);
				return true;
			}
			else {
				return false;
			}
				
			
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
    public ArrayList<Feedback> getFeedback(DataSource datasource,String articleID){
    	Connection con = null;
    	ArrayList<Feedback> result=new ArrayList();
    	try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs=st.executeQuery("select feedbackID, f.authorID as authorID, f.updateTime as updatetime, f.message as message, p.name as authorName from feedback as f LEFT JOIN patient as p ON articleID = '" + articleID + "' and p.userID = f.authorID");
			//select * from feedback where articleID='"+articleID+"'
			while(rs.next()) {
				Feedback temp=new Feedback();
				String author=rs.getString("authorID");
				String id=rs.getString("feedbackID");
				String updateTime=rs.getString("updateTime");
				String message=rs.getString("message");
				String authorName = rs.getString("authorName");
				temp.feedback(articleID, id, authorName, author, message, updateTime);
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
