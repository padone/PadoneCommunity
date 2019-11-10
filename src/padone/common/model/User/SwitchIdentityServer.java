package padone.common.model.User;

import java.sql.*;
import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;

public class SwitchIdentityServer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public HashMap switchIdentity(DataSource datasource, String userID, String previous, String next)
	{
		HashMap switchID = new HashMap();
		Connection con = null;
		String result = new String();
		String reason = new String(); 

		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			
			if (isExistingUser(userID, datasource))
			{
				switch(next)
				{
				case "病患":
					{
						result = "成功";
						reason = "成功找到病患身分";
						break;
					}
				case "家屬":
					{
						ResultSet rs = st.executeQuery("select userID from patientrelationship where userID = '" + userID + "' ");
						int check = 0;
						while(rs.next())
						{
							check = 1;
							result = "成功";
							reason = "成功找到家屬身分";
						}
						
						if(check == 0)
						{
							result = "失敗";
							reason = "沒有找到家屬關係";
						}
						rs.close();// 關閉rs
						break;
					}
				case "助理":
					{
						ResultSet rs = st.executeQuery("select userID from doctorrelationship where userID = '" + userID + "' ");
						int check = 0;
						while(rs.next())
						{
							check = 1;
							result = "成功";
							reason = "成功找到助理身分";
						}
						
						if(check == 0)
						{
							result = "失敗";
							reason = "沒有找到助理關係";
						}
						rs.close();// 關閉rs
						break;
					}
				default:
					{
						result = "失敗";
						reason = "沒有找到身分";
						break;
					}
				}
			}
			else
			{
				result = "失敗";
				reason = "沒有此使用者";
			}
			
			st.close();// 關閉st
		}
		catch (SQLException e)
		{
			System.out.println("SwitchIdentityServer switchIdentity Exception :" + e.toString());
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
		switchID.put("result", result);
		switchID.put("reason", reason);
		return switchID;
	}

	// 失敗!使用者帳號已註冊過!//已關資料庫

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