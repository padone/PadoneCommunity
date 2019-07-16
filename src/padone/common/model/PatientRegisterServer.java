package padone.common.model;

import java.sql.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.tomcat.jdbc.pool.DataSource;

import padone.encryption.base64.BBDPBase64;

public class PatientRegisterServer
{
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	synchronized public HashMap registerAdd(DataSource datasource, String account, String password, int gender,
			String name, String mail)
	{
		HashMap register = new HashMap();
		Connection con = null;
		String result = new String();

		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();

			if (isInvalidUsername(account, datasource))
			{
				result = "已有此使用者";
			}
			// 都沒有錯誤才新增註冊資料進入資料庫
			else
			{
				String QRCode = QRCodeIconGeneratorHandler(account);
				/************************************
				 * 新增patient(開始)
				 *****************************************************/
				String insertdbSQL = "insert into patient"
						+ "(userID, QRCode, account, password, name, gender, mail, birthday, notification)"
						+ "select ifNULL(max(userID+0), 0)+1, '" + QRCode + "', '" + account + "', '" + password + "', '"
						+ name + "', " + gender + ", '" + mail + "', '2015-01-01', 'yes' FROM patient";

				int userInsert = st.executeUpdate(insertdbSQL);
				st.close();
				/************************************
				 * 新增patient(結束)
				 *****************************************************/
				/************************************
				 * 新增patient權限(開始)
				 *****************************************************/
				con = datasource.getConnection();
			    st = con.createStatement();
			    insertdbSQL = "insert into patientpermission"
						+ "(userID, track, birthday, mail, introduction, global, familyOnly, followers, myFamily, collaborative, notification)"
						+ "select max(userID+0), '1', '1', '1', '1', '1', '1', '1', '1', '1', '1' FROM patient";

				int userPermission = st.executeUpdate(insertdbSQL);
				st.close();//關閉st
				/************************************
				 * 新增patient權限(結束)
				 *****************************************************/
				if (userInsert > 0 && userPermission > 0)
					result = "註冊成功";
				else
					result = "註冊失敗";
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
		register.put("result", result);
		return register;
	}

	// 失敗!使用者帳號已註冊過!//已關資料庫

	private boolean isInvalidUsername(String account, DataSource datasource)
	{
		Connection con = null;
		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select account from patient where account = '" + account + "' ");

			while (rs.next())
			{
				return true; // true 代表已有此使用者
			}
			rs.close();// 關閉rs

			st.close();// 關閉st
		}
		catch (SQLException e)
		{
			System.out.println("RegisterServer isInvalidUsername Exception :" + e.toString());
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

	// QRcode產生
	private String QRCodeIconGeneratorHandler(String account)
	{
		String QRCode = "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl="
				+ BBDPBase64.encode(account) + "&choe=UTF-8";
		return QRCode;
	}
}