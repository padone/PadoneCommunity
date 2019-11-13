package padone.common.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import org.apache.tomcat.jdbc.pool.DataSource;

public class FeedbackHandler {

	public boolean newFeedback(DataSource datasource, String authorID, String articleID, String message) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			Date date = new Date();

			Object param = new java.sql.Timestamp(date.getTime());
			System.out.println(param);
			int insert = st.executeUpdate(
					"insert into feedback(feedbackID,articleID,authorID,updatetime,message) select ifNULL(max(feedbackID+0),0)+1,'"
							+ articleID + "','" + authorID + "','" + param + "','" + message + "' FROM feedback");

			st.close();// 關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return true;
	}

	public boolean deleteFeedback(DataSource datasource, String feedbackID, String userID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String author = "";

			String sql = "delete from feedback where feedbackID='" + feedbackID + "' and authorID = '" + userID + "'";
			st.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return true;
	}

	public ArrayList<Feedback> getFeedback(DataSource datasource, String articleID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Feedback> result = new ArrayList();
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			/*
			ResultSet rs = st.executeQuery(
					"SELECT feedbackID, f.authorID as authorID, f.updateTime as updatetime, f.message as message, p.name as authorName FROM (SELECT * FROM feedback as t1 WHERE t1.articleID = '"
							+ articleID + "') as f INNER JOIN patient as p ON p.userID = f.authorID");
							*/
			pstmt = con.prepareStatement("SELECT f.*, p.name as authorName FROM (SELECT * FROM feedback WHERE articleID = ?) as f INNER JOIN patient as p ON p.userID = f.authorID UNION SELECT f.*, d.name as authorName FROM (SELECT * FROM feedback WHERE articleID = ?) as f INNER JOIN doctor as d ON d.doctorID = f.authorID ORDER BY feedbackID ASC");
			pstmt.setString(1, articleID);
			pstmt.setString(2, articleID);
			rs = pstmt.executeQuery();

			// union sql example:
			//select ds.*, u1.name as name from dataset as ds inner join u1 on u1.id = ds.id
			//UNION
			//SELECT ds.*, u2.name as name from dataset as ds inner join u2 on u2.id = ds.id
			//ORDER BY `uid` ASC

			while (rs.next()) {
				Feedback temp = new Feedback();
				String author = rs.getString("authorID");
				String id = rs.getString("feedbackID");
				String updateTime = rs.getString("updateTime");
				String message = rs.getString("message");
				String authorName = rs.getString("authorName");
				temp.feedback(articleID, id, authorName, author, message, updateTime);
				result.add(temp);
			}

		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return result;

	}

	
}
