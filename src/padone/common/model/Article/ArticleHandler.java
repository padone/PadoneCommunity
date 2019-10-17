package padone.common.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.json.JSONArray;
import org.json.JSONException;

public class ArticleHandler {
	public boolean newArticle(DataSource datasource, String title, String authorID, String department, String description, String image, String tag, String hospital) {
		Connection con = null;
		JSONArray jImg, jTag;
		int imageNum = 0, tagNum = 0;
		PreparedStatement pStmt = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			Date date = new Date();
			Object param = new java.sql.Timestamp(date.getTime());
			int articleID = 0;
			String sql = "select max(articleID+0) from article";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				articleID = (rs.getInt(1)) + 1;
				System.out.println(articleID);
			}

			jImg = new JSONArray(image);
			jTag = new JSONArray(tag);
			imageNum = jImg.length();
			tagNum = jTag.length();

			String insertSql = "insert into article(articleID,title,authorID,department,description,hospital,posttime,lastupdatetime, image)value('"
					+ articleID + "','" + title + "','" + authorID + "','" + department + "','" + description + "','"
					+ hospital + "','" + param + "','" + param + "', " + imageNum + ")";
			int insertSet = st.executeUpdate(insertSql);
			for (int i = 0; imageNum > i; i++) {
				String insertImageSql = "INSERT INTO picture(imageUrl, source, sourceID) VALUES (?, 'article', ?)";
				pStmt = con.prepareStatement(insertImageSql);
				pStmt.setString(1, jImg.getString(i));// set image
				pStmt.setString(2, Integer.toString(articleID));// set article id
				int insertImage = pStmt.executeUpdate();
				if (insertImage < 0) {
					st.executeUpdate("delete from article where articleID= '" + articleID + "'");
					return false;
				}
			}
			for (int i = 0; tagNum > i; i++) {
				String insertTagSql = "INSERT INTO tag(articleID, tagName) VALUES (?, ?)";
				pStmt = con.prepareStatement(insertTagSql);
				pStmt.setString(1, Integer.toString(articleID));// set article id
				pStmt.setString(2, jTag.getString(i));// set tag
				int insertTag = pStmt.executeUpdate();
				if (insertTag < 0) {
					st.executeUpdate("delete from article where articleID= '" + articleID + "'");
					st.executeUpdate("delete from picture where sourceID= '" + articleID + "' and source ='article'");
					return false;
				}
			}

			st.close();// 關閉st
			if(pStmt != null)
				pStmt.close();
			if (insertSet < 0)
				return false;
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} catch (JSONException j){
			System.out.println("JSON parsing error");
			j.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception ignore) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean deleteArticle(DataSource datasource, String articleID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sqlArticle = "delete from article where articleID = '" + articleID + "'";
			String sqlImage = "delete from picture where source = 'article' and sourceID = '" + articleID + "'";
			String deleteTrack="delete from trackarticle where articleID = '"+articleID+"'";
      
			System.out.println(sqlArticle);
			st.executeUpdate(sqlArticle);
			st.executeUpdate(deleteTrack);
			st.executeUpdate(sqlImage);
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
			return false;
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
					return false;
				}
			return true;
		}

	}

	public boolean editArticle(DataSource datasource, String title, String articleID, String department,
			String description, String[] image, String tag, String hospital){
		Connection con = null;
		try {
			con = datasource.getConnection();
			int length = 0;
			if (image != null)length = image.length;
			Date date = new Date();
			Timestamp param = new java.sql.Timestamp(date.getTime());
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String result = "";
			/* 查詢是否已有紀錄 */
			String search = "select * from article where articleID ='" + articleID + "'";
			ResultSet res = st.executeQuery(search);
			res.absolute(1);
			res.updateString("title",title );
			res.updateString("description",description );
			res.updateString("department",department );
			res.updateInt("image",length );
			res.updateString("tag",tag );
			res.updateString("hospital",hospital );
			res.updateTimestamp("lastupdatetime", param);
			res.updateRow();

			String del="delete from picture where source = 'article' and sourceID = '"+articleID+"'";
			st.executeUpdate(del);
			for (int i = 0; length > i; i++) {
				String insertImageSql = "insert into picture(imageURL,source,sourceID)value('" + image[i]
						+ "','article','" + articleID + "')";
				int insertimage = st.executeUpdate(insertImageSql);
				if (insertimage < 0) {
					return false;
				}
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
}
