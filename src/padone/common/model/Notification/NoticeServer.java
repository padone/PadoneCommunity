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
                NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "server open socket for " + userID));
                System.out.println("id: " + userID);
                System.out.println("test id: " + session.getUserProperties().get(SocketConstant.USER_ID));
            }else{
                throw new RegistFailedException("user id already taken, try another");
            }
        }
    }

    @OnClose
    public void close(final Session session){
        if(NoticeManager.remove(session)){
            System.out.println("Socket closed for "+ session.getUserProperties().get(SocketConstant.USER_ID));
            NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "<< socket closed by server"));
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

        } catch (Exception e){
            e.printStackTrace();
        }

        if(msg.equals("patientTest")){
            NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), "string verify success"));
        }else{
            NoticeManager.send(session, new Message((String) session.getUserProperties().get(SocketConstant.USER_ID), msg.toString()));
            System.out.println(name);
        }

        // TODO: check if invitation already send
        // TODO: send invitation
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
