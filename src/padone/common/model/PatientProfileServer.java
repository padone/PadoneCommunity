package padone.common.model;

import java.sql.*;
import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PatientProfileServer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public HashMap profileSet(DataSource datasource, String userID, String name, String birthday,
			String mail, String introduction)
	{
		HashMap setting = new HashMap();
		Connection con = null;
		String result = new String();

		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();

			// String QRCode = QRCodeIconGeneratorHandler(account);
			if (isExistingUser(userID, datasource))
			{
				/************************************
				 * 修改patient(開始)
				 *****************************************************/

				String insertdbSQL = "UPDATE patient SET name = '" + name + "', birthday = '" + birthday + "', mail = '"
						+ mail + "', introduction = '" + introduction + "' WHERE userID = '" + userID + "'";

				int userSetting = st.executeUpdate(insertdbSQL);
				st.close();
				/************************************
				 * 修改patient(結束)
				 *****************************************************/

				if (userSetting > 0)
					result = "修改成功";
				else
					result = "修改失敗";
			}
			else
			{
				result = "沒有此使用者";
			}
		}
		catch (SQLException e)
		{
			System.out.println("RegisterServer registerAdd Exception :" + e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (Exception ignore)
				{
				}
			}
		}
		setting.put("result", result);
		return setting;
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
		}
		catch (SQLException e)
		{
			System.out.println("ProfileSettingServer isExistingUser Exception :" + e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				}
				catch (Exception ignore)
				{
				}
			}
		}
		return false;
	}
}