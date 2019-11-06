package padone.common.model.Search;

import padone.common.model.Article.Article;

import org.apache.tomcat.jdbc.pool.DataSource;
import padone.common.model.Search.ArticleListServer;

import java.sql.*;
import java.util.ArrayList;

// TODO: add fragment fix, change to prepared statement, fix sql statement, check if article search work fine
public class FuzzySearchServer {
    public static ArrayList<Article> singleList;
    public static ArrayList<ArticleListServer.LightArticle> searchArticleViaUserName(DataSource dataSource, String fragment){
        //ArrayList<ArrayList<Article>> result = new ArrayList<>();
        ArrayList<ArticleListServer.LightArticle> list = new ArrayList<>();
        String frag = specHandle(fragment);
        Connection conn = null;
        PreparedStatement pstmt = null;
        // step :
        // fuzzy search user name -> get user id -> gather all article from user id list
        // search patient                        -> use article list server

        try{
            conn = dataSource.getConnection();
            //stmt = conn.createStatement();
            //String sql = "SELECT userID as id FROM patient WHERE name like '%" + fragment + "%'";
            pstmt = conn.prepareStatement("SELECT p.userID as id FROM patient as p WHERE p.name LIKE ? ESCAPE !");
            pstmt.setString(1, "%" + frag + "%");
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                //singleList = ArticleListServer.getAuthorArticle(dataSource, rs.getString("id"));
                //result.add(singleList);
                list.addAll(ArticleListServer.getLightAuthorArticleList(dataSource, rs.getString("id")));
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
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException ignored){}
            }
        }

        return list;
    }

    public static ArrayList<ArticleListServer.LightArticle> searchArticleViaTitle(DataSource dataSource, String fragment, String userID){
        //singleList = new ArrayList<>();
        ArrayList<ArticleListServer.LightArticle> list = new ArrayList<>();
        String frag = specHandle(fragment);
        Connection conn = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        Article temp;
        // step :
        // fuzzy search title -> get article id -> gather article information
        // search article                       -> use article list server

        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            pstmt = conn.prepareStatement("SELECT articleID as id FROM article WHERE title LIKE ? ESCAPE !");
            //String sql = "SELECT articleID as id FROM article WHERE title like '%" + fragment + "%'";
            //ResultSet rs = stmt.executeQuery(sql);
            pstmt.setString(1, "%" + frag + "%");
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                //temp = ArticleListServer.getSpecificArticle(dataSource, rs.getString("id"), userID).get(0);
                //singleList.add(temp);
                list.add(ArticleListServer.getSpecLightArticle(dataSource, rs.getString("id")));
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
        return list;
    }

    public static ArrayList<ArticleListServer.LightArticle> searchArticleViaContent(DataSource dataSource, String fragment, String userID){
        singleList = new ArrayList<>();
        ArrayList<ArticleListServer.LightArticle> list = new ArrayList<>();
        String frag = specHandle(fragment);
        Connection conn = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        Article temp;
        // step :
        // fuzzy search content aka description -> get article id -> gather article information
        // search article                                         -> use article list server

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT authorID as id FROM article WHERE description like ? ESCAPE !");
            pstmt.setString(1, "%" + frag + "%");
            //stmt = conn.createStatement();
            //String sql = "SELECT authorID as id FROM article WHERE description like '%" + fragment + "%'";
            //ResultSet rs = stmt.executeQuery(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                //temp = ArticleListServer.getSpecificArticle(dataSource, fragment, userID).get(0);
                //singleList.add(temp);
                list.add(ArticleListServer.getSpecLightArticle(dataSource, rs.getString("id")));
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
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException ignored){}
            }
        }
        return list;
    }

    public static ArrayList<ArticleListServer.LightArticle> searchArticleBySingleTag(DataSource dataSource, String fragment){
        // search tag in tag table -> get article id -> return light article content
        ArrayList<ArticleListServer.LightArticle> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT articleID as id FROM tag WHERE tagName like ? ESCAPE !");
            pstmt.setString(1, "%" + specHandle(fragment) + "%");
            rs = pstmt.executeQuery();
            if(rs.next()){
                list.add(ArticleListServer.getSpecLightArticle(dataSource, rs.getString("id")));
            }

            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn != null){ try{ conn.close(); }catch (SQLException ignored){} }
            if(pstmt != null){ try{ pstmt.close(); }catch (SQLException ignored){} }
        }
        return list;
    }

    public static ArrayList<UserInfo> searchPatient(DataSource dataSource, String fragment){
        // only support search by name now
        ArrayList<UserInfo> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        UserInfo temp;
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("SELECT p.name as n, p.userID as id, p.gender as g, p.introduction as intro FROM patient as p WHERE p.name like ? ESCAPE !");
            pstmt.setString(1, "%" + specHandle(fragment) + "%");
            rs = pstmt.executeQuery();
            while(rs.next()){
                temp = new UserInfo(rs.getString("n"), rs.getString("id"), "gender", rs.getString("intro"));
                switch(rs.getInt("g")){
                    case 0:
                        temp.setGender("male");
                        break;
                    case 1:
                        temp.setGender("female");
                        break;
                    default:
                        temp.setGender("helicopter");
                        break;
                }
                list.add(temp);
            }
            rs.close();
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
        return null;
    }

    public static String searchDoctor(DataSource dataSource, String fragment){
        // TODO : search doctor user via id or name, notice return type
        return null;
    }

    protected static String specHandle(String input){
        String out = input.replace("!", "!!")
                          .replace("%", "!%")
                          .replace("_", "!_")
                          .replace("[", "![");
        return out;
    }

    static class UserInfo{
        String name = "username";
        String id = "myID";
        String gender = "helicopter";
        String intro = "nothing";

        public void setGender(String gender){
            this.gender = gender;
        }

        UserInfo(){}
        UserInfo(String name, String id, String gender, String intro){
            this.name = name;
            this.id = id;
            this.gender = gender;
            this.intro = intro;
        }
    }
}
