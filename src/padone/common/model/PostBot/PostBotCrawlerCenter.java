package padone.common.model.PostBot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PostBotCrawlerCenter
{
	synchronized public News setCrawler(DataSource datasource, String keyWord, String website, String frequency)
	{
		News crawlerNews = new News();
		
		switch(website)
		{
			case "東森新聞":
			{
				EbcArticleCrawler crawler = new EbcArticleCrawler();
				ArrayList<News> article_Ebc = crawler.search(keyWord);

				for(News s: article_Ebc)
				{
					if(isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						
						break;
					}
				}
				
				break;
			}
			
			case "ETToday":
			{
				ETTodayArticleCrawler crawler = new ETTodayArticleCrawler();
				ArrayList<News> article_ETToday = crawler.search(keyWord);

				for(News s: article_ETToday)
				{
					if(isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						
						break;
					}
				}

				break;
			}
			
			case "TVBS":
			{
				TvbsArticleCrawler crawler = new TvbsArticleCrawler();
				ArrayList<News> article_Tvbs = crawler.search(keyWord);
				
				for(News s: article_Tvbs)
				{
					if(isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						
						break;
					}
				}
				
				break;
			}
		}
		
		String newsArticle = crawlerNews.getArticle() + "\n" + 
							 "發文時間：" + crawlerNews.getSendTime() + "\n" + 
							 "原文網址：" + crawlerNews.getWebUrl();
		
		crawlerNews.setArticle(newsArticle);
		
		return crawlerNews;
	}
	
	public boolean isNotExistingTitle(String title, DataSource datasource)
	{
		Connection con = null;
		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select title from article where title = '" + title + "' ");

			while (rs.next())
			{
				return false; // false 代表已有此標題的文章
			}
			rs.close();// 關閉rs

			st.close();// 關閉st
		} catch (SQLException e)
		{
			System.out.println("PostBotCrawlerCenter isExistingTitle Exception :" + e.toString());
			e.printStackTrace();
		} finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				} catch (Exception ignore)
				{
				}
			}
		}
		return true;
	}
}