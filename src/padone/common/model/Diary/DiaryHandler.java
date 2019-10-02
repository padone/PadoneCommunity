package padone.common.model.Diary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

public class DiaryHandler {
	public boolean writePatientDiary(DataSource datasource, String userID, String date, String image,
			String patientDescription) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from patientdiary where patientID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			// 新增資料
			if (res.next() == false) {
				result = "insert into patientdiary(date,patientID,patientDescription,photo)values('" + date + "','"
						+ userID + "','" + patientDescription + "','" + image + "')";
				st.executeUpdate(result);
			}
			// 修改已有的資料
			else {
				res.absolute(1);
				res.updateString("patientDescription", patientDescription);
				res.updateString("photo", image);
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

	public boolean writeFamilyDescription(DataSource datasource, String userID, String familyID, String date,
			String familyDescription) {
		Connection con = null;
		try {

			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from patientdiary where patientID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			res.absolute(1);
			res.updateString("familyDescription", familyDescription);
			res.updateRow();
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
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from familydiary where familyID ='" + userID + "' and date = '" + date + "'";
			ResultSet res = st.executeQuery(search);
			// 新增資料
			if (res.next() == false) {
				result = "insert into familydiary(date,familyID,description,photo)values('" + date + "','" + userID
						+ "','" + description + "','" + image + "')";
				st.executeUpdate(result);
			}
			// 修改已有的資料
			else {
				res.absolute(1);
				res.updateString("description", description);
				res.updateString("photo", image);
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
				String picture = rs.getString("photo");
				diary.FamilyDiary(familyID, date, familyDescription, picture);
			}
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
