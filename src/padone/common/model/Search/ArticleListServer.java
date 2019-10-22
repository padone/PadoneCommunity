package padone.common.model.Search;

import java.sql.*;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Article.Article;
import padone.common.model.Article.GreatHandler;

public class ArticleListServer {
    private static ArrayList<Article> resultList;

    /** get all article, sort by update time **/
    public static ArrayList<Article> getAllArticle(DataSource datasource){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;

        try{
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content, hospital, image FROM article as a INNER JOIN patient as p ON a.authorID=p.userID ORDER BY posttime DESC";
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
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
            }
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
        setImage(resultList, datasource);
        getGreatCount(resultList, datasource);
        setTag(resultList, datasource);
        return resultList;
    }

    public static ArrayList<Article> getAuthorArticle(DataSource datasource, String authorID){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;

        try{
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, p.name as author, title, DATE(posttime) as post_time, department as category, description as content, hospital, image FROM article as a INNER JOIN patient as p ON p.userID = '"+ authorID + "' and a.authorID = '" + authorID + "' ORDER BY posttime DESC";
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
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
            }
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
        setImage(resultList, datasource);
        getGreatCount(resultList, datasource);
        setTag(resultList, datasource);
        return resultList;
    }

    public static ArrayList<Article> getCategoryArticle(DataSource dataSource, String typ){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT n.articleID as aId, n.title, n.name as author, n.authorID, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN patinet as p ON a.authorID=p.userID) as n WHERE n.department='" + typ + "'ORDER BY posttime DESC";
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
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
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
        setImage(resultList, dataSource);
        getGreatCount(resultList, dataSource);
        setTag(resultList, dataSource);
        return resultList;
    }

    public static ArrayList<Article> getSpecificArticle(DataSource dataSource, String articleID, String userID){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement gStmt = null;
        ResultSet grs = null, trs = null;
        Article temp = new Article();
        String sql;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            if(amiadoctor(userID))
                sql = "SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN doctor as d ON a.authorID=d.doctorID) as n WHERE n.articleID = '" + articleID + "'";
            else
                sql = "SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN patient as p ON a.authorID=p.userID) as n WHERE n.articleID = '" + articleID + "'";
            ResultSet rs = stmt.executeQuery(sql);
            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT userID) as num FROM great WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            grs = gStmt.executeQuery();
            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT articleID) as num FROM trackArticle WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            trs = gStmt.executeQuery();

            if(grs.next()){
                if (grs.getInt("num") > 0) temp.setIfEvaluted(true);
                else temp.setIfEvaluted(false);
            }
            if(trs.next()){
                if (trs.getInt("num") > 0) temp.setIfTracked(true);
                else temp.setIfTracked(false);
            }
            if(rs.next()){
                temp.setArticleID(articleID);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("post_time"));
                temp.setDescription(rs.getString("content"));
                temp.setHospital(rs.getString("hospital"));
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
            }
            rs.close();

            grs.close();
            trs.close();
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
            if(gStmt != null){
                try{
                    gStmt.close();
                }catch (SQLException ignored){}
            }
        }
        setImage(resultList, dataSource);
        getGreatCount(resultList, dataSource);
        setTag(resultList, dataSource);
        return resultList;
    }

    // lack of image information, great information and tag information
    // be careful using
    protected static Article getSpecArticle(DataSource dataSource, String id){
        Article temp = new Article();
        Connection conn = null;
        Statement stmt = null;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN patient as p ON a.authorID=p.userID) as n WHERE n.articleID = '" + id + "'";
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
                temp.setImage(rs.getInt("image"));
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
        return temp;
    }

    /*
    public static ArrayList<Article> getSingleTagArticle(DataSource dataSource, String tag){
        resultList = new ArrayList<>();
        Connection conn;
        Article temp;
        String id;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID as aId FROM tag WHERE tagName = '" + tag + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                id = rs.getString("aId");
                temp = getSpecArticle(dataSource, id);
                resultList.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        setImage(resultList, dataSource);
        return resultList;
    }
    */

    public static ArrayList<Article> getHospitalArticle(DataSource dataSource, String position){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Article temp;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content, image FROM article as a INNER JOIN patient as p WHERE a.hospital = '" + position + "' and a.authorID=p.userID ORDER BY posttime DESC";
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
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
            }
            rs.close();
        }catch(SQLException e) {
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
        setImage(resultList, dataSource);
        getGreatCount(resultList, dataSource);
        setTag(resultList, dataSource);
        return resultList;
    }

    private static void setImage(ArrayList<Article> list, DataSource dataSource){
        int length = list.size();
        ArrayList<String> imgSet;
        Connection conn = null;
        Statement stmt = null;
        String cmd;
        ResultSet rs;
        Article temp;

        for(int i=0; i<length; i++){
            temp = list.get(i);
            if(temp.getImageNum() <= 0){ continue; }
            imgSet = new ArrayList<>();
            try{
                conn = dataSource.getConnection();
                stmt = conn.createStatement();
                cmd = "SELECT imageUrl as url FROM picture WHERE source = 'article' AND sourceID = '" + temp.getArticleID() + "'";
                rs = stmt.executeQuery(cmd);
                while(rs.next()){ imgSet.add(rs.getString("url")); }
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
            temp.setImageURL(imgSet);
            list.set(i, temp);
        }
    }

    private static void getGreatCount(ArrayList<Article> list, DataSource dataSource){
        int length = list.size();
        Connection conn = null;
        Statement stmt = null;
        String cmd;
        ResultSet rs;
        Article temp;

        for(int i=0; i<length; i++){
            temp = list.get(i);
            try{
                conn = dataSource.getConnection();
                stmt = conn.createStatement();
                cmd = "SELECT COUNT(DISTINCT userID) as num FROM great WHERE articleID = '" + temp.getArticleID() + "'";
                rs = stmt.executeQuery(cmd);

                if(rs.next()) temp.setGreat(rs.getInt("num"));
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
            list.set(i, temp);
        }
    }

    private static void setTag(ArrayList<Article> list, DataSource dataSource){
        int length = list.size();
        Connection conn = null;
        Statement stmt = null;
        String cmd;
        ResultSet rs;
        Article temp;
        ArrayList<String> tag;

        for(int i=0; i< length; i++){
            temp = list.get(i);
            tag = new ArrayList<>();
            try{
                conn = dataSource.getConnection();
                stmt = conn.createStatement();
                cmd = "SELECT tagName as t FROM tag WHERE articleID = '" + temp.getArticleID() + "'";
                rs = stmt.executeQuery(cmd);

                while(rs.next()){ tag.add(rs.getString("t")); }
                rs.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                if(conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ignored){}
                }
                if(stmt != null){
                    try{
                        stmt.close();
                    }catch (SQLException ignored){}
                }
            }
            temp.setTag(tag);
            list.set(i, temp);
        }
    }

    private static boolean amiadoctor(String id){
        return id.contains("d");
    }
}
