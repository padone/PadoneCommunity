package padone.common.model.User;

import java.sql.*;
import java.util.HashMap;
import org.apache.tomcat.jdbc.pool.DataSource;

public class LoginServer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap userVerification(DataSource datasource, String account, String password, String identity)
	{
		String result = "";
		String s, p, ID = null, name = null;
		Connection con = null;
		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs;
			if(identity.equals("doctor"))
				rs = st.executeQuery("select doctorID, account, password, name from doctor");
			else if(identity.equals("manager"))
				rs = st.executeQuery("select managerID, account, password, name from manager");
			else
				rs = st.executeQuery("select userID, account, password, name from patient");
			
			while (rs.next())
			{
				s = rs.getString("account"); 
				p = rs.getString("password");
				if (account.equals(s) == true)
				{// true 代表已有此使用者
					if (password.equals(p) == true)
					{
						result = "登入成功";// true 代表此使用者密碼正確
						if(identity.equals("doctor"))
						{
							ID = rs.getString("doctorID");
							name = rs.getString("name");
						}
						else if(identity.equals("manager"))
						{
							ID = rs.getString("managerID");
							name = rs.getString("name");
						}
						else
						{
							ID = rs.getString("userID");
							name = rs.getString("name");
						}
						break;
					}
					else
					{
						result = "密碼錯誤";// false 代表此使用者密碼不正確
						break;
					}
				}
				else
				{
					result = "沒有此使用者";// false 代表沒有此使用者
				}
			}
			rs.close();
			st.close();
		}
		catch (SQLException e)
		{
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
		HashMap loginResult = new HashMap();
		loginResult.put("result", result);
		loginResult.put("userID", ID);
		loginResult.put("name", name);
		loginResult.put("identity", identity);
		return loginResult;
	}

	// 判斷登入
	// 此用戶已在其他裝置登入
	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) public HashMap
	 * judgeLogin(DataSource datasource, String userID, String uuid) { Connection
	 * con = null; HashMap judgeLogin = new HashMap();
	 * 
	 * try { con = datasource.getConnection(); Statement st = con.createStatement();
	 * ResultSet rs = st.executeQuery("select uuid from user where userID = " +
	 * userID);
	 * 
	 * while (rs.next()) { if(rs.wasNull()){ System.out.println("uuid : null");
	 * }else{ System.out.println("uuid : " + rs.getString("uuid")); }
	 * 
	 * if(rs.wasNull() || uuid.equals(rs.getString("uuid"))){
	 * judgeLogin.put("result", true); } else{ judgeLogin.put("result", false); } }
	 * rs.close();//關閉rs st.close();//關閉st } catch (SQLException e) {
	 * System.out.println("LoginVerification judgeLogin Exception :" +
	 * e.toString()); e.printStackTrace(); }finally { if (con!=null) try
	 * {con.close();}catch (Exception ignore) {} } return judgeLogin; }
	 * 
	 * // 更新uuid public boolean updateUUID(DataSource datasource, String userID,
	 * String uuid) { Connection con = null; try { con = datasource.getConnection();
	 * Statement st = con.createStatement();
	 * 
	 * int update =
	 * st.executeUpdate("update user set uuid = '"+uuid+"' where userID = '"
	 * +userID+"' "); st.close();//關閉st
	 * 
	 * if(update > 0) return true; else return false; } catch (SQLException e) {
	 * System.out.println("LoginVerification updateUUID Exception :" +
	 * e.toString()); e.printStackTrace(); }finally { if (con!=null) try
	 * {con.close();}catch (Exception ignore) {} } return true; }
	 */
}