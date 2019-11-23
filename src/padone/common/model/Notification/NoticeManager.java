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

    public static void close(final Session session, final CloseReason.CloseCodes closecodes, final String message){
        try{
            session.close(new CloseReason(closecodes, message));
        }catch (IOException e){
            throw new RuntimeException("Unable to close session", e);
        }
    }

    public static boolean remove(final Session session){
        System.out.println("remove session for " + session.getUserProperties().get(SocketConstant.USER_ID));
        return SESSIONS.remove(session);
    }
}
