package padone.common.model.PostBot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.jdbc.pool.DataSource;

@WebListener
public class PostBotListener implements ServletContextListener
{
	private ScheduledExecutorService scheduler;
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext sc = sce.getServletContext();
		DataSource datasource = (DataSource) sc.getAttribute("db");
		
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new PostBotCreate(1, datasource), 0, 30, TimeUnit.SECONDS);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		scheduler.shutdownNow();
	}
}