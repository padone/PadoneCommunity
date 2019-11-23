package padone.common.model.Notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Message {

    /*
    @JsonProperty("userID")
    private final String userID;
    @JsonProperty("message")
    private final String message;
    */
    @JsonProperty("noticeID")
    private final int noticeID;
    @JsonProperty("recipientID")
    private final String recipientID;
    @JsonProperty("request")
    private final String request;
    @JsonProperty("content")
    private final String content;
    @JsonProperty("time")
    private final String time;
    @JsonProperty("senderID")
    private String senderID = "";

    /*
    @JsonCreator
    public Message(@JsonProperty("userID") final String userID, @JsonProperty("message") final String message){
        Objects.requireNonNull(userID);
        Objects.requireNonNull(message);

        this.userID = userID;
        this.message = message;
    }

    public String getUserID(){
        return this.userID;
    }

    public String getMessage() {
        return this.message;
    }
    */

    @JsonCreator
    public Message(@JsonProperty("noticeID") final int noticeID, @JsonProperty("recipientID") final String recipientID, @JsonProperty("request") final String request, @JsonProperty("content") final String content, @JsonProperty("time") final String time, @JsonProperty("senderID") final String senderID){
        Objects.requireNonNull(noticeID);
        Objects.requireNonNull(recipientID);
        Objects.requireNonNull(request);
        Objects.requireNonNull(content);
        //Objects.requireNonNull(time); // TODO: check if get from client or server endpoint

        this.noticeID = noticeID;
        this.recipientID = recipientID;
        this.request = request;
        this.content = content;
        this.time = time;
        this.senderID = senderID;
    }

    public int getNoticeID() {
        return noticeID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public String getRequest() {
        return request;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getSenderID() {
        return senderID;
    }

    /*
    @Override
    public String toString() {
        String msg = "content: {";
        msg = msg.concat("'userID': '" + userID);
        msg = msg.concat("', 'message': '" + message + "'}");

        return msg;
    }
    */

    @Override
    public String toString() {
        String msg = "content: {";
        msg = msg.concat("'noticeID': " + noticeID);
        msg = msg.concat("', 'recipientID': '" + recipientID);
        msg = msg.concat("', 'request': '" + request);
        msg = msg.concat("', 'content': '" + content);
        msg = msg.concat("', 'time': '" + time);
        msg = msg.concat("', 'senderID': '" + senderID + "'}");

        return msg;
    }
}

/*
    design of notification data table
{
    "noticeID": "",      as title, the id
    "recipientID": "",   id of the guy who will get this notice
    "request": "",       what is this purpose
    "content": "",       detail
    "time": "",          when did this notification send
    "senderID": ""       optional, another guy's id if need to send notification from user a to b
}
 */