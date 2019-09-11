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

        // TODO : search and add reported feedback

        return resultList;
    }

    public static ArrayList<ReportedArticle> getReportedArticle(DataSource dataSource){
        ArrayList<ReportedArticle> resultList = new ArrayList<>();
        Connection conn;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT articleID, title ";


        }catch (SQLException e){
            e.printStackTrace();
        }

        // TODO : search and add reported article

        return resultList;
    }

    public static ArrayList<ReportedUser> getReportedUserList(DataSource dataSource){
        ArrayList<ReportedUser> resultList = new ArrayList<>();
        Connection conn;
        ReportedUser temp;

        try{
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM reportUser";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp = new ReportedUser();
                temp.setValue(rs.getString("userID"), rs.getString("violatorID"), rs.getString("reason"), rs.getString("description"));
                resultList.add(temp);
            }

        }catch(SQLException e){
            e.printStackTrace();
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

        void setValue(String title, String articleID, String reporter, String authorID, String reason, String description, String message){
            super.setValue(title, articleID, reporter, authorID, reason, description);
            this.message = message;
        }
    }
}