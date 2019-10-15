package padone.common.model.Search;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


public class ReportListServer {

    public static ArrayList<ReportedFeedBack> getReportedFeedback(DataSource dataSource){
        ArrayList<ReportedFeedBack> resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ReportedFeedBack temp;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reportFeedback";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new ReportedFeedBack();
                temp.setValue(rs.getString("feedbackID"), rs.getString("articleID"), rs.getString("userID"), rs.getString("authorID"), rs.getString("reason"), rs.getString("description"), rs.getString("message"));
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

        return resultList;
    }

    public static ArrayList<ReportedArticle> getReportedArticle(DataSource dataSource){
        ArrayList<ReportedArticle> resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ReportedArticle temp;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reportArticle";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new ReportedArticle();
                temp.setValue(rs.getString("title"), rs.getString("articleID"), rs.getString("userID"), rs.getString("authorID"), rs.getString("reason"), rs.getString("description"));
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

        return resultList;
    }

    public static ArrayList<ReportedUser> getReportedUserList(DataSource dataSource){
        ArrayList<ReportedUser> resultList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ReportedUser temp;

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reportUser";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new ReportedUser();
                temp.setValue(rs.getString("userID"), rs.getString("violatorID"), rs.getString("reason"), rs.getString("description"));
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

        return resultList;
    }

    static class ReportedUser {
        String reporter;
        String authorID;
        String reason;
        String description;

        void setValue(String reporter, String authorID, String reason, String description){
            this.reporter = reporter;
            this.authorID = authorID;
            this.reason =  reason;
            this.description = description;
        }
    }

    static class ReportedArticle extends ReportedUser {
        String title;
        String articleID;

        void setValue(String title, String articleID, String reporter, String authorID, String reason, String description){
            super.setValue(reporter, authorID, reason, description);
            this.title = title;
            this.articleID = articleID;
        }
    }

    static class ReportedFeedBack extends ReportedArticle{
        String message;
        // use title from reportedArticle as feedbackID

        void setValue(String feedbackID, String articleID, String reporter, String authorID, String reason, String description, String message){
            super.setValue(feedbackID, articleID, reporter, authorID, reason, description);
            this.message = message;
        }
    }
}