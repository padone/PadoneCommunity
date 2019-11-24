package padone.common.model.Notification;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public Message decode(String s) throws DecodeException {
        try{
            return SocketConstant.MAPPER.readValue(s, Message.class);
        }catch (IOException e){
            throw new DecodeException(s, "unable to decode text to message", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        //return s.contains(SocketConstant.USER_ID) && s.contains(SocketConstant.MESSAGE);
        return s.contains(SocketConstant.NOTICE_ID) && s.contains(SocketConstant.RECIPIENT_ID) && s.contains(SocketConstant.REQUEST) && s.contains(SocketConstant.CONTENT) && s.contains(SocketConstant.TIME) && s.contains(SocketConstant.IF_CHECKED) && s.contains(SocketConstant.SENDER_ID);
    }

    @Override
    public void destroy() {
    }
}
