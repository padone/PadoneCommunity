package padone.common.model.PostBot;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PostBotSetter
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	synchronized public HashMap crawlerSet(DataSource datasource, String keyword, String website, String frequency)
	{
		HashMap crawlerSet = new HashMap();
		Connection con = null;
		String result = new String();

		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();

			/************************************
			 * 新增PostBot(開始)
			 *****************************************************/
			String insertdbSQL = "insert into postbot" + "(botID, keyword, website, frequency)"
					+ "select ifNULL(max(botID+0), 0)+, '" + keyword + "', '" + website + "', '" + frequency
					+ "' FROM postbot";

			int postBotInsert = st.executeUpdate(insertdbSQL);
			st.close();
			/************************************
			 * 新增PostBot(結束)
			 *****************************************************/
			if (postBotInsert > 0)
				result = "新增機器人成功";
			else
				result = "新增機器人失敗";

		} catch (SQLException e)
		{
			System.out.println("PostBotSetter postBotAdd Exception :" + e.toString());
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
		crawlerSet.put("result", result);
		return crawlerSet;
	}
}