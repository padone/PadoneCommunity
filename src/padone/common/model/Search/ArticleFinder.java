package padone.common.model.Search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;

import padone.common.model.Article.Article;

public class ArticleFinder {
	public boolean ArticleFinder(DataSource datasource,String articleID,Article a) {
		
		
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs=st.executeQuery("select * from article where articleID='"+articleID+"'");
			while(rs.next()) {
				String title=""+rs.getString("title");
				System.out.println("title:"+title);
				a.setTitle(title);
				String department=""+rs.getString("department");
				System.out.println("department:"+department);
				a.setDepartment(department);
				String description=""+rs.getString("description");
				System.out.println("description:"+description);
				a.setDescription(description);
				String postTime=""+rs.getString("postTime");
				System.out.println("postTime:"+postTime);
				a.setPostTime(postTime);
				String lastUpdateTime=""+rs.getString("lastUpdateTime");
				System.out.println("lastUpdateTime:"+lastUpdateTime);
				a.setLastUpdateTime(lastUpdateTime);
				int image= rs.getInt("image");
				System.out.println("image:"+image);
				a.setImage(image);
				String tag=""+rs.getString("tag");
				System.out.println("tag:"+tag);
				a.setTag(tag);
				String author=""+rs.getString("author");
				System.out.println("author:"+author);	
				a.setAuthor(author);
			    a.setArticleID(articleID);			
				
				
				
				//a.tostring();
				return true;
			}
			return false;
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
	}
}
