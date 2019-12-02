package padone.common.model.User;

import java.sql.*;
import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;

public class RelationshipServer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public HashMap setRelationship(DataSource datasource, String userID1, String userID2, String identity)
	{
		HashMap relationship = new HashMap();
		Connection con = null;
		String result = new String();

		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();

			// String QRCode = QRCodeIconGeneratorHandler(account);
			if (isExistingUser(userID2, datasource))
			{
				/************************************
				 * 建立連結(開始)
				 *****************************************************/
				String insertdbSQL = null;

				if (identity.equals("病患"))
					insertdbSQL = "insert into patientrelationship(familyID, patientID)" + "values ('" + userID1
							+ "', '" + userID2 + "');";
				else if (identity.equals("醫生"))
					insertdbSQL = "insert into doctorrelationship(doctorID, secretaryID)" + "values ('" + userID1
							+ "', '" + userID2 + "');";

				int userSetting = st.executeUpdate(insertdbSQL);
				st.close();
				/************************************
				 * 建立連結(結束)
				 *****************************************************/

				if (userSetting > 0)
					result = "建立連結成功";
				else
					result = "建立連結失敗";
			} else
			{
				result = "沒有此使用者";
			}
		} catch (SQLException e)
		{
			System.out.println("familyID = " + userID1 + "\n"
							 + "patientID = " + userID2 + "\n"
							 + "identity = " + identity + "\n");
			System.out.println("RegisterServer registerAdd Exception :" + e.toString());
			e.printStackTrace();
		} finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				} catch (Exception ignore)
				{
				}
			}
		}
		relationship.put("result", result);
		return relationship;
	}

	private boolean isExistingUser(String userID, DataSource datasource)
	{
		Connection con = null;
		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select userID from patient where userID = '" + userID + "' ");

			while (rs.next())
			{
				return true; // true 代表已有此使用者
			}
			rs.close();// 關閉rs

			st.close();// 關閉st
		} catch (SQLException e)
		{
			System.out.println("ProfileSettingServer isExistingUser Exception :" + e.toString());
			e.printStackTrace();
		} finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				} catch (Exception ignore)
				{
				}
			}
		}
		return false;
	}
}