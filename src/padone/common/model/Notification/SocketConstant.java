package padone.common.model.Notification;

import com.fasterxml.jackson.databind.ObjectMapper;

final class SocketConstant {
    static final String USER_ID = "userID";
    static final String TARGET_ID = "target";
    static final String MESSAGE = "message";
    static final String NOTICE_ID = "noticeID";
    static final String RECIPIENT_ID = "recipientID";
    static final String CONTENT = "content";
    static final String REQUEST = "request";
    static final String TIME = "time";
    static final String IF_CHECKED = "checked";
    static final String SENDER_ID = "senderID";
    static final String HTTP_SESSION = "httpSession";
    static final String ERROR = "instantiation error";
    static final ObjectMapper MAPPER= new ObjectMapper();

    private SocketConstant(){
        throw new IllegalStateException(ERROR);
    }
}
