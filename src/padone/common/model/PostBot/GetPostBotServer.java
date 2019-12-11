package padone.common.model.PostBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;

public class GetPostBotServer
{
	synchronized public ArrayList<PostBot> getPostBot(DataSource datasource)
	{
		ArrayList<PostBot> postBotSet = new ArrayList<PostBot>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PostBot bot;

		try
		{
			con = datasource.getConnection();
			pstmt = con.prepareStatement("SELECT * FROM postbot");
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				bot = new PostBot();
				bot.setBotID(rs.getString("botID"));
				bot.setKeyword(rs.getString("keyword"));
				bot.setWebsite(rs.getString("website"));
				bot.setFrequency(rs.getString("frequency"));

				postBotSet.add(bot);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
		} catch (SQLException e)
		{
			System.out.println("GetPostBotServer getPostBot Exception :" + e.toString());
			e.printStackTrace();
		} finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				} catch (Exception ignore){}
			}
		}
		return postBotSet;
	}
}