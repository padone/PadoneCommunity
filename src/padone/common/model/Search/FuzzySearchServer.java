package padone.common.model.Search;

import padone.common.model.Article.Article;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Search.ArticleListServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FuzzySearchServer {
    public static ArrayList<Article> singleList;
    public static ArrayList<ArrayList<Article>> searchArticleViaUserName(DataSource dataSource, String fragment){
        ArrayList<ArrayList<Article>> result = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        // step :
        // fuzzy search user name -> get user id -> gather all article from user id list
        // search patient                        -> use article list server

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT userID as id FROM patient WHERE name like '%" + fragment + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                singleList = ArticleListServer.getAuthorArticle(dataSource, rs.getString("id"));
                result.add(singleList);
            }
            rs.close();
        }catch (SQLException e){
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

        return result;
    }

    public static ArrayList<Article> searchArticleViaTitle(DataSource dataSource, String fragment, String userID){
        singleList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;
        // step :
        // fuzzy search title -> get article id -> gather article information
        // search article                       -> use article list server

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT articleID as id FROM article WHERE title like '%" + fragment + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = ArticleListServer.getSpecificArticle(dataSource, rs.getString("id"), userID).get(0);
                singleList.add(temp);
            }
            rs.close();
        }catch (SQLException e){
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
        return singleList;
    }

    public static ArrayList<Article> searchArticleViaContent(DataSource dataSource, String fragment, String userID){
        singleList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;
        // step :
        // fuzzy search content aka description -> get article id -> gather article information
        // search article                                         -> use article list server

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT authorID as id FROM article WHERE description like '%" + fragment + "%'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = ArticleListServer.getSpecificArticle(dataSource, fragment, userID).get(0);
                singleList.add(temp);
            }
            rs.close();
        }catch (SQLException e){
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
        return  singleList;
    }
}
