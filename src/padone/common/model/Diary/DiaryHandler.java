package padone.common.model.Diary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONArray;
import org.json.JSONException;

public class DiaryHandler {
	public boolean writePatientDiary(DataSource datasource, String userID, String date, String image,
			String patientDescription) {
		Connection con = null;
		JSONArray jImg;
		int imageNum = 0;
		PreparedStatement pStmt = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from patientdiary where patientID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			jImg = new JSONArray(image);
			imageNum = jImg.length();
			
			// 新增資料
			if (res.next() == false) {
				result = "insert into patientdiary(date,patientID,patientDescription)values('" + date + "','"
						+ userID + "','" + patientDescription + "')";
				st.executeUpdate(result);
				for (int i = 0; imageNum > i; i++) {
					String insertImageSql = "INSERT INTO picture(imageUrl, source, sourceID) VALUES (?, 'patientDiary', ?)";
					pStmt = con.prepareStatement(insertImageSql);
					pStmt.setString(1, jImg.getString(i));// set image
					pStmt.setString(2, userID+"-"+date);// set article id
					int insertImage = pStmt.executeUpdate();
					if (insertImage < 0) {
						return false;
					}
				}
			}
			// 修改已有的資料
			else {
				res.absolute(1);
				res.updateString("patientDescription", patientDescription);
				res.updateRow();
				st.executeUpdate("delete from picture where source='patientDiary' and sourceID = '"+userID+"-"+date+"'");
				for (int i = 0; imageNum > i; i++) {
					String insertImageSql = "INSERT INTO picture(imageUrl, source, sourceID) VALUES (?, 'patientDiary', ?)";
					pStmt = con.prepareStatement(insertImageSql);
					pStmt.setString(1, jImg.getString(i));// set image
					pStmt.setString(2, userID+"-"+date);// set article id
					int insertImage = pStmt.executeUpdate();
					if (insertImage < 0) {
						return false;
					}
				}
			}

			st.close();// 關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} catch (JSONException j){
			System.out.println("JSON parsing error");
			j.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return true;
	}

	public boolean writeFamilyDescription(DataSource datasource, String userID, String familyID, String date,
			String familyDescription) {
		Connection con = null;
		JSONArray jImg;
		int imageNum = 0;
		PreparedStatement pStmt = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from patientdiary where patientID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			// 新增資料
			if (res.next() == false) {
				result = "insert into patientdiary(date,patientID,familyID,familydescription)values('" + date + "','"
						+ userID +"','"+familyID+ "','" + familyDescription + "')";
				st.executeUpdate(result);
			}
			// 修改已有的資料
			else {
				res.absolute(1);
				res.updateString("familydescription", familyDescription);
				res.updateString("familyID", familyID);
				res.updateRow();
				
			}

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

	public PatientDiary getPatientDiary(DataSource datasource, String userID, String date) {
		Connection con = null;
		PatientDiary diary = new PatientDiary();
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();

			String result = "select * from patientdiary where patientID ='" + userID + "' and date ='" + date + "'";
			ResultSet rs = st.executeQuery(result);
			while (rs.next()) {
				String patientDescription = rs.getString("patientDescription");
				String familyDescription = rs.getString("familyDescription");
				String familyID = rs.getString("familyID");
				diary.PatientDiary(userID, familyID, date, familyDescription, patientDescription);
			}
			String cmd = "SELECT imageUrl as url FROM picture WHERE source = 'patientDiary' AND sourceID = '" +userID+"-"+date+ "'";
            rs = st.executeQuery(cmd);
            ArrayList<String> imgSet=new ArrayList();
            while(rs.next()){ imgSet.add(rs.getString("url")); }
            diary.setImageURL(imgSet);
			st.close();// 關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return diary;
	}

	public boolean writeFamilyDiary(DataSource datasource, String userID, String date, String image,
			String description) {
		Connection con = null;
		JSONArray jImg;
		int imageNum = 0;
		PreparedStatement pStmt = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from familydiary where familyID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			jImg = new JSONArray(image);
			imageNum = jImg.length();
			// 新增資料
			if (res.next() == false) {
				result = "insert into familydiary(date,familyID,description)values('" + date + "','" + userID
						+ "','" + description +  "')";
				st.executeUpdate(result);
				for (int i = 0; imageNum > i; i++) {
					String insertImageSql = "INSERT INTO picture(imageUrl, source, sourceID) VALUES (?, 'familyDiary', ?)";
					pStmt = con.prepareStatement(insertImageSql);
					pStmt.setString(1, jImg.getString(i));// set image
					pStmt.setString(2, userID+"-"+date);// set article id
					int insertImage = pStmt.executeUpdate();
					if (insertImage < 0) {
						return false;
					}
				}
			}
			// 修改已有的資料
			else {
				res.absolute(1);
				res.updateString("description", description);
				res.updateRow();
				for (int i = 0; imageNum > i; i++) {
					String insertImageSql = "INSERT INTO picture(imageUrl, source, sourceID) VALUES (?, 'familyDiary', ?)";
					pStmt = con.prepareStatement(insertImageSql);
					pStmt.setString(1, jImg.getString(i));// set image
					pStmt.setString(2, userID+"-"+date);// set article id
					int insertImage = pStmt.executeUpdate();
					if (insertImage < 0) {
						return false;
					}
				}
			}

			st.close();// 關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} catch (JSONException j){
			System.out.println("JSON parsing error");
			j.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return true;
	}

	public FamilyDiary getFamilyDiary(DataSource datasource, String userID, String date) {
		Connection con = null;
		FamilyDiary diary = new FamilyDiary();
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();

			String result = "select * from familydiary where familyID ='" + userID + "' and date ='" + date + "'";
			//System.out.println(result);
			ResultSet rs = st.executeQuery(result);
			while (rs.next()) {
				String familyDescription = rs.getString("description");
				String familyID = rs.getString("familyID");
				diary.FamilyDiary(familyID, date, familyDescription);
			}
			String cmd = "SELECT imageUrl as url FROM picture WHERE source = 'familyDiary' AND sourceID = '" +userID+"-"+date+ "'";
            rs = st.executeQuery(cmd);
            ArrayList<String> imgSet=new ArrayList();
            while(rs.next()){ imgSet.add(rs.getString("url")); }
            diary.setImageURL(imgSet);
			st.close();// 關閉st
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return diary;

	}
}
