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

    public static ArrayList<ArticleListServer.LightArticle> getTrackingArticle(DataSource dataSource, String userID, String tableName){
        ArrayList<ArticleListServer.LightArticle> resultList = new ArrayList<>();
        Article temp;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT articleID FROM trackArticle WHERE userID = ?");
            pstmt.setString(1, userID);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                //temp = ArticleListServer.getSpecArticle(dataSource, rs.getString("articleID"));
                //temp = ArticleListServer.getSpecificArticle(dataSource, rs.getString("articleID"), userID).get(0);
                //resultList.add(temp);
                //resultList.addAll(ArticleListServer.getSpecificArticle(dataSource, rs.getString("articleID"), userID));
                resultList.add(ArticleListServer.getSpecLightArticle(dataSource, rs.getString("articleID")));
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

    public static ArrayList<ArticleListServer.LightArticle> getSuggestArticleFromOther(DataSource dataSource, String userID){
        ArrayList<ArticleListServer.LightArticle> list = new ArrayList<>();
        ArticleListServer.LightArticle temp;
        ArrayList<String> idList = new ArrayList<>();
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try{
            conn = dataSource.getConnection();
            // get family data
            pstmt = conn.prepareStatement("SELECT a.*, p.name as authorName FROM article as a INNER JOIN (SELECT s.articleID as aID FROM patientrelationship as p INNER JOIN suggestArticle as s ON s.userID = p.familyID and p.patientID = ?) as sp ON articleID = sp.aID INNER JOIN patient as p ON p.userID = a.authorID UNION SELECT a.*, d.name as authorName FROM article as a INNER JOIN (SELECT s.articleID as aID FROM patientrelationship as p INNER JOIN suggestArticle as s ON s.userID = p.familyID and p.patientID = ?) as sp ON articleID = sp.aID INNER JOIN doctor as d ON d.doctorID = a.authorID");

            // sql command :
            //SELECT articleID, p.name as authorName, title, description as content, posttime as postTime
            //FROM article as a
            //INNER JOIN
            //  (SELECT s.articleID as aID
            //   FROM patientrelationship as p
            //   INNER JOIN suggestArticle as s
            //    ON s.userID = p.familyID and p.userID = ?) as sp
            // ON articleID = sp.aID
            // INNER JOIN patient as p
            //  ON p.userID = a.authorID
            //UNION
            //SELECT articleID, d.name as authorName, title, description as content, posttime as postTime
            //FROM article as a
            //INNER JOIN
            //  (SELECT s.articleID as aID
            //   FROM patientrelationship as p
            //   INNER JOIN suggestArticle as s
            //    ON s.userID = p.familyID and p.userID = ?) as sp
            // ON articleID = sp.aID
            // INNER JOIN doctor as d
            //  ON d.doctorID = a.authorID
            //
            // note that union will auto distinct data row

            pstmt.setString(1, userID);
            pstmt.setString(2, userID);
            rs = pstmt.executeQuery();
            if(rs.next()){
                temp = new ArticleListServer.LightArticle(rs.getString("articleID"), rs.getString("authorName"), rs.getString("title"), rs.getString("description"), rs.getString("posttime"));
                list.add(temp);
            }
            conn.close();
            pstmt.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
