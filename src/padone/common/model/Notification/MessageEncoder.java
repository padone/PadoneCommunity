package padone.common.model.Notification;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public final class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public String encode(final Message message) throws EncodeException {
        try{
            return SocketConstant.MAPPER.writeValueAsString(message);
        }catch (JsonProcessingException e){
            throw new EncodeException(message, "unable to encode message", e);
        }
    }

    @Override
    public void destroy() {
    }
}
