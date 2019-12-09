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
            //String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content, hospital, image FROM article as a INNER JOIN patient as p ON a.authorID=p.userID ORDER BY posttime DESC";
            String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, a.department as category, description as content, a.hospital, image FROM article as a INNER JOIN patient as p ON a.authorID=p.userID UNION SELECT articleID as aId, title, d.name as author, authorID, DATE(posttime) as post_time, a.department as category, description as content, a.hospital, image FROM article as a INNER JOIN doctor as d ON a.authorID=d.doctorID ORDER BY post_time DESC";
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

            //SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN doctor as d ON a.authorID=d.doctorID) as n WHERE n.articleID = '" + articleID + "'
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

    public static ArrayList<LightArticle> getLightAuthorArticleList(DataSource dataSource, String authorID){
        ArrayList<LightArticle> result = new ArrayList<>();
        LightArticle temp = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet nrs = null;
        String name = "sb";

        try{
            conn = dataSource.getConnection();
            if(amiadoctor(authorID))
                pstmt = conn.prepareStatement("SELECT d.name as n FROM doctor as d WHERE d.doctorID = ?");
            else
                pstmt = conn.prepareStatement("SELECT p.name as n FROM patient as p WHERE p.userID = ?");
            pstmt.setString(1, authorID);
            nrs = pstmt.executeQuery();
            if(nrs != null && nrs.next())
                name = nrs.getString("n");

            pstmt = conn.prepareStatement("SELECT title, description as des, posttime as pt FROM article WHERE authorID = ?");
            pstmt.setString(1, authorID);
            rs = pstmt.executeQuery();

            while(rs != null && rs.next()){
                temp = new LightArticle(authorID, name, rs.getString("title"), rs.getString("des"), rs.getString("pt"));
                result.add(temp);
            }

            if(rs != null) rs.close();
            if(nrs != null) nrs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(pstmt != null)
                try{ pstmt.close(); }catch (SQLException ignored){}
            if(conn != null)
                try{ conn.close(); }catch (SQLException ignored){}
        }
        return result;
    }

    public static ArrayList<Article> getDepartmentArticle(DataSource dataSource, String typ){
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

    public static ArrayList<LightArticle> getLightDepartmentArticle(DataSource dataSource, String department){
        ArrayList<LightArticle> result = new ArrayList<>();
        LightArticle temp = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT articleID as aID, p.name as authorName, title, description as content, posttime as postTime FROM article as a INNER JOIN patient as p ON p.userID = a.authorID and a.department = ? UNION SELECT articleID as aID, d.name as authorName, title, description as content, posttime as postTime FROM article as a INNER JOIN doctor as d ON d.doctorID = a.authorID and a.department = ?");
            pstmt.setString(1, department);
            pstmt.setString(2, department);
            rs = pstmt.executeQuery();

            while (rs.next()){
                temp = new LightArticle(rs.getString("aID"), rs.getString("authorName"), rs.getString("title"), rs.getString("content"), rs.getString("postTime"));
                result.add(temp);
            }

            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(pstmt != null)
                try{ pstmt.close(); }catch (SQLException ignored){}
            if(conn != null)
                try{ conn.close(); }catch (SQLException ignored){}
        }
        return result;
    }

    public static ArrayList<Article> getSpecificArticle(DataSource dataSource, String articleID, String userID){
        resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement gStmt = null;
        ResultSet grs = null, trs = null;
        ResultSet frs = null, drs = null;
        Article temp = new Article();
        String sql;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            /*
            if(amiadoctor(userID))
                //sql = "SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN doctor as d ON a.authorID=d.doctorID) as n WHERE n.articleID = '" + articleID + "'";
                sql = "SELECT a.*, d.name as author FROM article as a INNER JOIN doctor as d ON a.authorID = d.doctorID and a.articleID = ?";
            else
                //sql = "SELECT n.title, n.name as author, n.authorID, n.department, DATE(n.posttime) as post_time, n.description as content, n.hospital, n.image FROM (SELECT * FROM article as a INNER JOIN patient as p ON a.authorID=p.userID) as n WHERE n.articleID = '" + articleID + "'";
                sql = "SELECT a.*, p.name as author FROM article as a INNER JOIN patient as p ON a.authorID = p.userID and a.articleID = ?";
            */
            gStmt = conn.prepareStatement("SELECT a.*, d.name as author FROM article as a INNER JOIN doctor as d ON a.authorID = d.doctorID and a.articleID = ? UNION SELECT a.*, p.name as author FROM article as a INNER JOIN patient as p ON a.authorID = p.userID and a.articleID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, articleID);
            //ResultSet rs = stmt.executeQuery(sql);
            ResultSet rs = gStmt.executeQuery();

            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT userID) as num FROM great WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            grs = gStmt.executeQuery();

            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT articleID) as num FROM trackArticle WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            trs = gStmt.executeQuery();

            //gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT articleID) as num FROM suggestArticle INNER JOIN (SELECT familyID FROM patientrelationship WHERE patientID = ?) as p ON userID = p.familyID and articleID = ?");
            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT articleID) as num FROM suggestArticle WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            frs = gStmt.executeQuery();

            gStmt = conn.prepareStatement("SELECT COUNT(DISTINCT articleID) as num FROM recommend WHERE articleID = ? AND userID = ?");
            gStmt.setString(1, articleID);
            gStmt.setString(2, userID);
            drs = gStmt.executeQuery();

            if(grs.next()){
                if (grs.getInt("num") > 0) temp.setIfEvaluted(true);
                else temp.setIfEvaluted(false);
            }
            if(trs.next()){
                if (trs.getInt("num") > 0) temp.setIfTracked(true);
                else temp.setIfTracked(false);
            }
            if(frs.next()){
                if (frs.getInt("num") > 0) temp.setIfSuggestedByFamily(true);
                else temp.setIfSuggestedByFamily(false);
            }
            if(drs.next()){
                if (drs.getInt("num") > 0) temp.setIfSuggestedByDoctor(true);
                else temp.setIfSuggestedByDoctor(false);
            }
            if(rs.next()){
                temp.setArticleID(articleID);
                temp.setTitle(rs.getString("title"));
                temp.setAuthor(rs.getString("author"));
                temp.setAuthorID(rs.getString("authorID"));
                temp.setDepartment(rs.getString("department"));
                temp.setPostTime(rs.getString("posttime"));
                temp.setDescription(rs.getString("description"));
                temp.setHospital(rs.getString("hospital"));
                temp.setImage(rs.getInt("image"));
                resultList.add(temp);
            }
            rs.close();

            grs.close();
            trs.close();
            frs.close();
            drs.close();
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

    public static LightArticle getSpecLightArticle(DataSource dataSource, String articleID){
        LightArticle result = new LightArticle();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet nrs = null;
        boolean identity = false;
        String aid = "";

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT authorID as aid, title, description as des, posttime as pt FROM article WHERE articleID = ?");
            pstmt.setString(1, articleID);
            rs = pstmt.executeQuery();

            if(rs != null && rs.next()){
                result = new LightArticle(articleID, "name", rs.getString("title"), rs.getString("des"), rs.getString("pt"));
                aid = rs.getString("aid");
                identity = amiadoctor(aid);
            }

            if(identity)
                pstmt = conn.prepareStatement("SELECT d.name as n FROM doctor as d WHERE d.doctorID = ?");
            else
                pstmt = conn.prepareStatement("SELECT p.name as n FROM patient as p WHERE p.userID = ?");
            pstmt.setString(1, aid);
            nrs = pstmt.executeQuery();

            if(nrs != null && nrs.next())
                result.setAuthorName(nrs.getString("n"));

            if(rs != null) rs.close();
            if(nrs != null) nrs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                try{ conn.close(); }catch (SQLException ignored){}
            }
            if(pstmt != null){
                try{ pstmt.close(); }catch (SQLException ignored){}
            }
        }
        return result;
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

    public static ArrayList<LightArticle> getHospitalArticle(DataSource dataSource, String position){
        ArrayList<LightArticle> list = new ArrayList<>();
        resultList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LightArticle temp;

        try{
            conn = dataSource.getConnection();
            //String sql = "SELECT articleID as aId, title, p.name as author, authorID, DATE(posttime) as post_time, department as category, description as content, image FROM article as a INNER JOIN patient as p WHERE a.hospital = '" + position + "' and a.authorID=p.userID ORDER BY posttime DESC";
            pstmt = conn.prepareStatement("SELECT articleID as aID, p.name as authorName, title, description as content, posttime as postTime FROM article as a INNER JOIN patient as p ON p.userID = a.authorID and a.hospital = ? UNION SELECT articleID as aID, d.name as authorName, title, description as content, posttime as postTime FROM article as a INNER JOIN doctor as d ON d.doctorID = a.authorID and a.hospital = ?");
            pstmt.setString(1, position);
            pstmt.setString(2, position);
            rs = pstmt.executeQuery();

            while(rs.next()){
                temp = new LightArticle(rs.getString("aID"), rs.getString("authorName"), rs.getString("title"), rs.getString("content"), rs.getString("postTime"));
                list.add(temp);
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
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException ignored){}
            }
        }
        return list;
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

    public static class LightArticle{
        String articleID = "init";
        String authorName = "init";
        String title = "init";
        String description = "init";
        String postTime = "initT";

        private void setAuthorName(String authorName){
            this.authorName = authorName;
        }

        LightArticle(String articleID, String authorName, String title, String description, String postTime){
            this.articleID = articleID;
            this.authorName = authorName;
            this.title = title;
            this.description = description;
            this.postTime = postTime;
        }
        LightArticle(){}
    }
}
