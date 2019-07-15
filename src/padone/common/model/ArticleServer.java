package padone.common.model;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.fabric.xmlrpc.base.Array;
import org.apache.tomcat.jdbc.pool.DataSource;

public class ArticleServer {
    private static ArrayList<Article> resultList;

    /** get all article, sort by update time **/
    public static ArrayList<Article> getAllArticle(DataSource datasource){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;


        try{
            conn = datasource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, author, DATE(posttime) as post_time, department as category, description as content FROM article ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDepartment(rs.getString("category"));
                temp.setDescription(rs.getString("content"));
                resultList.add(temp);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<Article> getAuthorArticle(DataSource datasource, String name){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;

        try{
            conn = datasource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, DATE(posttime) as post_time, department as category, description as content FROM article WHERE author = '"+ name + "' ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setAuthor(name);
                temp.setTitle(rs.getString("title"));
                temp.setDepartment(rs.getString("category"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                resultList.add(temp);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<Article> getCategoryArticle(DataSource dataSource, String typ){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, author, DATE(posttime) as post_time, description as content FROM article WHERE department = '" + typ + "' ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setDepartment(typ);
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                resultList.add(temp);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<Article> getSpecificArticle(DataSource dataSource, String id){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp = new Article();

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT title, author, department, DATE(posttime) as post_time, description as content FROM article WHERE articleID = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                temp.setArticleID(id);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                resultList.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    protected static Article getSpecArticle(DataSource dataSource, String id){
        Article temp = new Article();
        Connection conn;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT title, author, department, DATE(posttime) as post_time, description as content FROM article WHERE articleID = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                temp.setArticleID(id);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return temp;
    }

    public static ArrayList<Article> getSingleTagArticle(DataSource dataSource, String tag){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;
        String id;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            /** find article id by tag **/
            String sql = "SELECT articleID as aId FROM tag WHERE tagName = '" + tag + "'";
            ResultSet rs = stmt.executeQuery(sql);

            /** find target article by using article id **/
            while(rs.next()){
                id = rs.getString("aId");
                temp = getSpecArticle(dataSource, id);
                resultList.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }
}
