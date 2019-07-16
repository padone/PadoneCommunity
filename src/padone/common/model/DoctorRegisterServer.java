package padone.common.model;

import java.sql.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.tomcat.jdbc.pool.DataSource;

import padone.encryption.base64.BBDPBase64;

public class DoctorRegisterServer
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public HashMap registerAdd(DataSource datasource, String account, String password, int gender, String name,
			String mail, String phone, String backUpPhone)
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
				 * 新增doctor(開始)
				 *****************************************************/
				String insertdbSQL = "insert into doctor(doctorID, QRCode, account, password, name, gender, mail, phone, backUpPhone, hospital, department, notification) "
						+ "select ifNULL(max(doctorID+0), 0)+1, '" + QRCode + "', '" + account + "','" + password + "','"
						+ name + "','" + gender + "','" + mail + "','" + phone + "','" + backUpPhone
						+ "', 'Nothing', 'Nothing', 'yes' FROM doctor";

				int userInsert = st.executeUpdate(insertdbSQL);
				st.close();
				/************************************
				 * 新增doctor(結束)
				 *****************************************************/

				if (userInsert > 0)
					result = "註冊成功";
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
			ResultSet rs = st.executeQuery("select account from doctor where account = '" + account + "' ");

			while (rs.next())
			{
				return true; // true 代表已有此使用者
			}
			rs.close();

			st.close();
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