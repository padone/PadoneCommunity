package padone.common.model.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

// TODO : fix image resource error
public class ArticleHandler {
	public boolean newArticle(DataSource datasource,  String title, String authorID, 
			String department,String description,String[] image,String tag,String hospital) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			Date date= new Date();
			Object param = new java.sql.Timestamp(date.getTime());
			int articleID=0;
			String sql="select max(articleID+0) from article";
			ResultSet rs=st.executeQuery(sql);
			while(rs.next()) {	
				articleID=(rs.getInt(1))+1;
				System.out.println(articleID);
			}

			int length=0;
			if(image!=null)length=image.length;
			
			String insertsql="insert into article(articleID,title,authorID,department,description,hospital,posttime,lastupdatetime, image)value('"
		    +articleID+"','"+title+"','"+authorID+"','"+department+"','"+description+"','"+hospital+"','"+param+"','"+param+"', " + length +")";
      
		    int insertset=st.executeUpdate(insertsql);
		    for(int i=0;length>i;i++) {
		    	String insertImageSql="insert into picture(imageURL,source,sourceID)value('"+image[i]+"','article','"+articleID+"')";
		    	int insertimage=st.executeUpdate(insertImageSql);
		    	if(insertimage<0) {
		    		st.executeUpdate("delete from article where articleID= '"+articleID+"'");
		    		return false;
		    	}
		    }
		     st.close();//關閉st
		    if(insertset<0)return false;
			return true;
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {
		    	  return false;
		      }
		}
		
	}

	public boolean deleteArticle(DataSource datasource,String articleID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String sqlarticle="delete from article where articleID = '"+articleID+"'";
			String sqlimage="delete from picture where source = 'article' and sourceID = '"+articleID+"'";
			System.out.println(sqlarticle);
			st.executeUpdate(sqlarticle);
			st.executeUpdate(sqlimage);
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
			return false;
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {return false;}
		      return true;
		}
		
	}
	

}
