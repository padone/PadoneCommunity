package padone.common.model.PostBot;

import org.apache.tomcat.jdbc.pool.DataSource;

import padone.common.model.Article.ArticleHandler;

public class PostBotCreate implements Runnable
{
	String botID;
	DataSource datasource;
	
	public PostBotCreate()
	{
		super();
	}
	
	public PostBotCreate(int botID, DataSource datasource)
	{
		this.botID = Integer.toString(botID);
		this.datasource = datasource;
	}
	
	private void createCrawler(String botID, DataSource datasource)
	{
		PostBotCrawlerCenter crawler = new PostBotCrawlerCenter();
		ArticleHandler handler = new ArticleHandler();
		Boolean newArticle = false;
		News crawlerNews = new News();

		crawlerNews = crawler.setCrawler(datasource, botID);
		newArticle = handler.newArticle(datasource, crawlerNews.getTitle(), "6684", "其他或健康運動資訊", crawlerNews.getArticle(),
				crawlerNews.getArrayPhotoUrl(), "[" + crawlerNews.getKeyword() + "]", "無");

		System.out.println("在servlet中的crawlerNews: " + crawlerNews);
		System.out.println("在servlet中的newArticle: " + newArticle);
	}
	
	@Override
	public void run()
	{
		createCrawler(botID, datasource);
	}
}