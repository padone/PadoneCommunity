package padone.common.model.Notification;

import com.google.gson.JsonParser;
import org.json.JSONArray;

import javax.sql.DataSource;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class NoticeManager {
    private static final java.util.concurrent.locks.Lock Lock = new ReentrantLock();
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    private NoticeManager(){
        throw new IllegalStateException(SocketConstant.ERROR);
    }

    public static boolean init(final Session session){
        boolean result = false;
        try{
            Lock.lock();

            result = !SESSIONS.contains(session) && !SESSIONS.stream()
                    .filter(e -> ((String)e.getUserProperties().get(SocketConstant.USER_ID)).equals((String)session.getUserProperties().get(SocketConstant.USER_ID))).findFirst().isPresent() && SESSIONS.add(session);
        }finally {
            Lock.unlock();
        }
        return result;
    }

    public static void send(final Session origin, final Message message){

        SESSIONS.stream().filter(session -> session.equals(origin))
                .forEach(session -> {
                    try{
                        session.getBasicRemote().sendObject(message);
                    }catch (IOException | EncodeException e){
                        e.printStackTrace();
                    }
                });
    }

    public static void sendText(final Session origin, final String msg){
        SESSIONS.stream().filter(session -> session.equals(origin))
                .forEach(session -> {
                    try{
                        session.getBasicRemote().sendText(msg);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                });
    }

    public static void sendOtherUser(DataSource dataSource, Session origin, final Message message){
        // store notice in database
        Message msg;
        int noticeID = 999;
        int updateRow;
        Date date = new Date();
        Object time = new java.sql.Timestamp(date.getTime());
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT max(noticeID) as nid FROM notification");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                noticeID = rs.getInt("nid") + 1;
            }
            pstmt = conn.prepareStatement("INSERT INTO notification(noticeID, recipientID, request, content, time, checked, senderID) VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, noticeID);
            pstmt.setString(2, message.getRecipientID());
            pstmt.setString(3, message.getRequest());
            pstmt.setString(4, message.getContent());
            pstmt.setString(5, time.toString());
            pstmt.setBoolean(6, false);
            pstmt.setString(7, message.getRecipientID());
            updateRow = pstmt.executeUpdate();
            msg = new Message(noticeID, message.getRecipientID(), message.getRequest(), message.getContent(), time.toString(), false, origin.getUserProperties().get(SocketConstant.USER_ID).toString());

            if(updateRow < 0){ throw new RuntimeException("notification update failed"); }
            conn.close();
            pstmt.close();
            rs.close();
            SESSIONS.stream().filter(session -> session.getUserProperties().get(SocketConstant.USER_ID).toString().equals(message.getRecipientID()))
                    .forEach(session -> {
                        try{
                            session.getBasicRemote().sendObject(msg);
                        }catch (IOException | EncodeException e){
                            e.getStackTrace();
                        }
                    });
        }catch (SQLException e) {
            e.printStackTrace();
        }
        // send
        //result = !SESSIONS.contains(session) && !SESSIONS.stream()
        //                    .filter(e -> ((String)e.getUserProperties().get(SocketConstant.USER_ID)).equals((String)session.getUserProperties().get(SocketConstant.USER_ID))).findFirst().isPresent() && SESSIONS.add(session);
    }

    public static void checkoutNotice(final Session session, DataSource dataSource){
        ArrayList<Message> list;
        Message msg;

        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM notification WHERE recipientID = ? and checked = ?");
            pstmt.setString(1, session.getUserProperties().get(SocketConstant.USER_ID).toString());
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                SESSIONS.stream().filter(s -> s.equals(session))
                        .forEach(s -> {
                            try{
                                s.getBasicRemote().sendObject(new Message(rs.getInt("noticeID"), rs.getString("recipientID"), rs.getString("request"), rs.getString("content"), rs.getString("time"), rs.getBoolean("checked"), rs.getString("senderID")));
                            }catch (SQLException | EncodeException | IOException e){
                                e.printStackTrace();
                            }
                        });
            }
            conn.close();
            pstmt.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean remove(final Session session){
        System.out.println("remove session for " + session.getUserProperties().get(SocketConstant.USER_ID));
        return SESSIONS.remove(session);
    }

    public static void close(final Session session, final CloseReason.CloseCodes closecodes, final String message){
        try{
            session.close(new CloseReason(closecodes, message));
        }catch (IOException e){
            throw new RuntimeException("Unable to close session", e);
        }
    }
}
