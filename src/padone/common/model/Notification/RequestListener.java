package padone.common.model.Notification;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        System.out.println("request adder triggered");
        // add httpSession in every request
        ((HttpServletRequest)sre.getServletRequest()).getSession();
    }

    public RequestListener(){}

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }
}
