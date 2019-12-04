package padone.common.model.Notification;

import javax.sql.DataSource;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;
import com.google.gson.Gson;

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

    public static void sendAll(DataSource dataSource, Session origin, final Message message){
        ArrayList<String> list = new ArrayList<>();
        Map<String, Message> noticeList = new HashMap<>();
        Message msg;
        Date date = new Date();
        Object time = new java.sql.Timestamp(date.getTime());
        int updateRow = 0;
        int noticeID = 5555;

        try{
            Lock.lock();
            // store all notice in db
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT max(noticeID) as nid FROM notification");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                noticeID = rs.getInt("nid") + 1;
            }
            pstmt = conn.prepareStatement("SELECT userID as id FROM patient UNION SELECT doctorID as id FROM patient");
            rs = pstmt.executeQuery();
            while(rs.next()){
                list.add(rs.getString("id"));
            }
            pstmt = conn.prepareStatement("INSERT INTO notification(noticeID, recipientID, request, content, time, checked, senderID) VALUES (?, ?, ?, ?, ?, ?, ?)");
            for(String id : list){
                pstmt.setInt(1, noticeID++);
                pstmt.setString(2, id);
                pstmt.setString(3, message.getRequest());
                pstmt.setString(4, message.getContent());
                pstmt.setString(5, time.toString());
                pstmt.setBoolean(6, false);
                pstmt.setString(7, message.getSenderID());
                updateRow = pstmt.executeUpdate();
                msg = new Message(noticeID, id, message.getRequest(), message.getContent(), time.toString(), false, message.getSenderID());
                noticeList.put(id, msg);
            }

            for (Session s : SESSIONS) {
                if (!s.equals(origin)) {
                    try {
                        //session.getBasicRemote().sendObject(new Message(noticeID.noticeID, session.getUserProperties().get(SocketConstant.USER_ID).toString(), message.getRequest(), message.getContent(), time.toString(), false, message.getSenderID()));
                        s.getBasicRemote().sendObject(noticeList.get(s.getUserProperties().get(SocketConstant.USER_ID).toString()));
                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            Lock.unlock();
        }
        // TODO : checkout table LOCK issue when multiple user are writing notification
    }

    public static void checkoutNotice(final Session session, DataSource dataSource){
        ArrayList<Message> list = new ArrayList<>();
        Message msg;
        Date date = new Date();
        Object time = new java.sql.Timestamp(date.getTime());
        Gson gson = new Gson();

        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM notification WHERE recipientID = ? and checked = ?");
            pstmt.setString(1, session.getUserProperties().get(SocketConstant.USER_ID).toString());
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();

            // add all message in one message structure
            while(rs.next()){
                msg = new Message(rs.getInt("noticeID"), session.getUserProperties().get(SocketConstant.USER_ID).toString(), rs.getString("request"), rs.getString("content"), rs.getString("time"), false, rs.getString("senderID"));
                list.add(msg);
            }
            msg = new Message(0, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "unread_message", gson.toJson(list), time.toString(), false,"system");

            for (Session s : SESSIONS) {
                if (s.equals(session)) {
                    try {
                        //s.getBasicRemote().sendObject(new Message(rs.getInt("noticeID"), rs.getString("recipientID"), rs.getString("request"), rs.getString("content"), rs.getString("time"), rs.getBoolean("checked"), rs.getString("senderID")));
                        s.getBasicRemote().sendObject(msg);
                    } catch (EncodeException | IOException e) {
                        e.printStackTrace();
                    }
                }
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
