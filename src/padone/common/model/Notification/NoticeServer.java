package padone.common.model.Notification;

import org.apache.tomcat.jdbc.pool.DataSource;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

@ServerEndpoint(value = "/notice/{userID}", configurator = HttpSessionConfigurator.class, encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public final class NoticeServer {
    private EndpointConfig config;
    private DataSource db;
    private Session session;
    private HttpSession httpSession;

    @OnOpen
    public void open(@PathParam(SocketConstant.USER_ID) final String userID, final Session session, EndpointConfig config){
        // test context
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        db = (DataSource)httpSession.getServletContext().getAttribute("db");
        if(db != null){
            System.out.println(db);
            this.session = session;
            this.httpSession = httpSession;
        }

        if(Objects.isNull(userID) || userID.isEmpty()){
            // check if user info received
            throw new RegistFailedException("User id required");
        }else{
            // store user info
            session.getUserProperties().put(SocketConstant.USER_ID, userID);
            if(NoticeManager.init(session)){
                // notice user socket is open
                //NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "server open socket for " + userID));
                //NoticeManager.sendText(session, "server open socket for " + userID);
                NoticeManager.send(session, new Message(154, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "init result", "success", "now", true, null));
                System.out.println("id: " + userID);
                NoticeManager.checkoutNotice(session, db);
            }else{
                throw new RegistFailedException("user id already taken, try another");
            }
        }
    }

    @OnClose
    public void close(final Session session){
        if(NoticeManager.remove(session)){
            System.out.println("Socket closed for "+ session.getUserProperties().get(SocketConstant.USER_ID));
            //NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "<< socket closed by server"));
            NoticeManager.sendText(session, "<< socket closed by server");
            System.out.println("leave onClose function");
        }
    }

    @OnMessage
    public void onMessage(final Message msg, final Session session){
        // get dataSource
        //System.out.println(httpSession.getServletContext().getAttribute("db"));
        System.out.println(msg);
        String name = "init";

        try{
            //System.out.println(db);

            Connection conn = db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM patient WHERE userID = ?");
            pstmt.setString(1, "6667");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                name = rs.getString("name");
                //System.out.println(name);
            }rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e){
            e.printStackTrace();
        }


        /*
        if(msg.getRequest().equals("messageTest")){
            //NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "string verify success"));
            //NoticeManager.sendText(session, "string verify success");
            NoticeManager.send(session, new Message(200, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "onMessage testing", "success", "now", true, null));
        }else{
            //NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), msg.toString()));
            //NoticeManager.sendText(session, msg.toString());
            NoticeManager.send(session, new Message(404, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "onMessage testing", name, "now", true, null));
            System.out.println(name);
        }
        */
        switch (msg.getRequest()){
            case "getUnRead":
                NoticeManager.checkoutNotice(session, db);
                break;
            case "noticeAll":
                break;
            case "noticeUser":
                NoticeManager.sendOtherUser(db, session, msg);
                break;
            case "messageTest":
                NoticeManager.send(session, new Message(200, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "onMessage testing", "success", "now", true, null));
                break;
            case "databaseTest":
                NoticeManager.send(session, new Message(200, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "onMessage testing", name, "now", true, null));
                break;
            default:
                //NoticeManager.send(session, new Message(100, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "switch failed", name, "now", true, null));
                NoticeManager.send(session, new Message(404, session.getUserProperties().get(SocketConstant.USER_ID).toString(), "unavailable request", "unknown request pattern", "now", true, "system"));
                break;
        }
        // TODO: add user send notice to another user
        // TODO: add system send notice to specific user
        // TODO: add system send notice to all users
        // TODO: add get all notice
    }

    @OnError
    public void onError(final Session session, final Throwable throwable){
        throwable.printStackTrace();
        System.out.println("onError triggered");
        if(throwable instanceof RegistFailedException){
            System.out.println("error code: " + CloseReason.CloseCodes.VIOLATED_POLICY.getCode());
            System.out.println("message: " + throwable.getMessage());
            NoticeManager.close(session, CloseReason.CloseCodes.VIOLATED_POLICY, throwable.getMessage());
        }
    }

    private static final class RegistFailedException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        public RegistFailedException(final String msg){
            super(msg);
        }
    }
}
