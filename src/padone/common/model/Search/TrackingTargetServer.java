package padone.common.model.Search;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Article.Article;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TrackingTargetServer {

    public static ArrayList<String> getTrackingUser(DataSource dataSource, String userID){
        ArrayList<String> resultList = new ArrayList<>();
        Connection conn;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT target FROM trackTarget WHERE follower = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                resultList.add(rs.getString("target"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;
    }

    public static ArrayList<Article> getTrackingArticle(DataSource dataSource, String userID, String tableName){
        ArrayList<Article> resultList = new ArrayList<>();
        Article temp;
        Connection conn;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID FROM " + tableName + " WHERE userID = '" + userID + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = ArticleListServer.getSpecArticle(dataSource, rs.getString("articleID"));
                resultList.add(temp);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        return resultList;
    }
}
