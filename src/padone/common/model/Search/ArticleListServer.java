package padone.common.model.Search;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Article.Article;

public class ArticleListServer {
    private static ArrayList<Article> resultList;

    /** get all article, sort by update time **/
    public static ArrayList<Article> getAllArticle(DataSource datasource){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;


        try{
            conn = datasource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content, hospital FROM article as a LEFT JOIN paitent as p ON a.authorID=p.userID ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDepartment(rs.getString("category"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
                resultList.add(temp);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<Article> getAuthorArticle(DataSource datasource, String authorID){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;

        try{
            conn = datasource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, p.name as author, title, DATE(posttime) as post_time, department as category, description as content, hospital FROM article as a LEFT JOIN patient as p ON p.userID = '"+ authorID + "' ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(authorID);
                temp.setTitle(rs.getString("title"));
                temp.setDepartment(rs.getString("category"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
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
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, description as content, hospital FROM article as a LEFT JOIN patient as p WHERE a.department = '" + typ + "' ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setDepartment(typ);
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
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
            String sql = "SELECT title, p.name as author, authorID, department, DATE(posttime) as post_time, description as content, hospital FROM article as a LEFT JOIN patient as p WHERE a.articleID = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                temp.setArticleID(id);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
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
            String sql = "SELECT title, p.name as author, authorID, department, DATE(posttime) as post_time, description as content, hospital FROM article as a LEFT JOIN patient as p WHERE a.articleID = '" + id + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                temp.setArticleID(id);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
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

    public static ArrayList<Article> getHospitalArticle(DataSource dataSource, String position){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content FROM article as a LEFT JOIN patient as p WHERE a.hospital = '" + position + "' ORDER BY posttime DESC";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new Article();
                temp.setArticleID(rs.getString("aId"));
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDepartment(rs.getString("category"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(position);
                resultList.add(temp);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
