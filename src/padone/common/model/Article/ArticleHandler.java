package PadoneArticleMod;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.tomcat.jdbc.pool.DataSource;

public class ArticleHandler {
	public boolean newArticle(DataSource datasource, String articleID, String title, String author, Date date,
			String department,String description,String image,String tag) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			Object param = new java.sql.Timestamp(date.getTime());
			System.out.println("HI");
			String result="insert into article"+"(articleID,title,author,department,description,posttime,lastupdatetime,image,tag)values('" 
				    + articleID + "','" + title + "','"+ author + "','" + department + "','" + description + "','"+ param + "','" + param + "','"+image+"','"+tag+"' )";
			System.out.println(result);
			int articleInsert = st.executeUpdate(result);
			
			String resultII="insert into tag"+"(articleID,tagName)values('" + articleID + "','" + tag + "' )";
			System.out.println(resultII);
			int tagInsert = st.executeUpdate(resultII);//
		   /* int insert = st.executeUpdate("insert into patientinstruction"
		    		+ "(patientInstructionID,doctorID,symptom,type,date,title,content,editDate) "
		    		+ "select ifNULL(max(patientInstructionID+0),0)+1,'" 
		    		+ doctorID + "','" + symptom + "','"+ type + "','" + date + "','" + title + "','"+ html + "','" + date +
		    		"' FROM patientinstruction");*/
		    
		    st.close();//關閉st
			if(tagInsert > 0&&articleInsert>0){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
	public boolean newtag(DataSource datasource, String articleID, int tagNumber, ArrayList<String> tags) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			
			for(int i=0;i<tagNumber;i++) {
				int insert = st.executeUpdate("insert into tag(articleID,tagName)" 
			+ articleID + "','" + tags.get(i) + "' FROM newtag");
				st.close();//關閉st
				if(insert <= 0) return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
	public boolean deleteArticle(DataSource datasource,String articleID,String userID) {
		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			String author="";
			ResultSet rs=st.executeQuery("select * from article where articleID='"+articleID+"'");
			while(rs.next()){
				author=""+rs.getString("author");
			}
			if(userID.equals(author)) {
				String sql="delete from article where articleID='"+articleID+"'";
				st.executeUpdate(sql);
				return true;
			}
			else {
				return false;
			}
				
			
		} catch (SQLException e) {
			System.out.println("PatientInstructionServer newInstruction Exception :" + e.toString());
			e.printStackTrace();
		}finally {
		      if (con!=null) try {con.close();}catch (Exception ignore) {}
		}
		return true;
	}
	

}
