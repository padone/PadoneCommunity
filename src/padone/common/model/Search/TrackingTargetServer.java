package padone.common.model.Search;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Article.Article;

import java.sql.*;
import java.util.ArrayList;

public class TrackingTargetServer {

    public static ArrayList<String> getTrackingUser(DataSource dataSource, String userID){
        ArrayList<String> resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT target FROM trackTarget WHERE follower = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){ resultList.add(rs.getString("target")); }
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                try{
                    conn.close();
                }catch (SQLException ignored){}
            }
            if(stmt != null){
                try{
                    stmt.close();
                }catch (SQLException ignored){}
            }
        }

        return resultList;
    }

    public static ArrayList<Article> getTrackingArticle(DataSource dataSource, String userID, String tableName){
        ArrayList<Article> resultList = new ArrayList<>();
        Article temp;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = dataSource.getConnection();
            if(tableName.contains("track"))
                pstmt = conn.prepareStatement("SELECT articleID FROM trackArticle WHERE userID = ?");
            if(tableName.contains("suggest"))
                pstmt = conn.prepareStatement("SELECT articleID FROM suggestArticle WHERE userID = ?");

            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                //temp = ArticleListServer.getSpecArticle(dataSource, rs.getString("articleID"));
                //temp = ArticleListServer.getSpecificArticle(dataSource, rs.getString("articleID"), userID).get(0);
                //resultList.add(temp);
                resultList.addAll(ArticleListServer.getSpecificArticle(dataSource, rs.getString("articleID"), userID));
            }
            rs.close();
        } catch(SQLException e){
            e.printStackTrace();
        } catch(ArrayIndexOutOfBoundsException a) {
            System.out.println("Array index out of bounds !");
            a.printStackTrace();
        }finally {
            if(conn != null){
                try{
                    conn.close();
                }catch (SQLException ignored){}
            }
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException ignored){}
            }
        }

        return resultList;
    }
}
