package padone.common.model.PostBot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PostBotCrawlerCenter
{
	synchronized public News setCrawler(DataSource datasource, String botID)
	{
		News crawlerNews = new News();
		PostBot postBot = getPostBot(datasource, botID);

		String keyword = postBot.getKeyword();
		String website = postBot.getWebsite();
		String frequency = postBot.getFrequency();

		switch (website)
		{
		case "東森新聞":
		{
			EbcArticleCrawler crawler = new EbcArticleCrawler();
			ArrayList<News> article_Ebc = crawler.search(keyword);

			for (News s : article_Ebc)
			{
				try
				{
					if (isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews = new News();
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						crawlerNews.setSourceUrl(s.getSourceUrl());
						crawlerNews.setKeyword(keyword);

						break;
					}
				} catch (Exception e)
				{
					System.out.println("PostBotCrawlerCenter Ebc Exception :" + e.toString());
					continue;
				}
			}

			break;
		}

		case "ETToday":
		{
			ETTodayArticleCrawler crawler = new ETTodayArticleCrawler();
			ArrayList<News> article_ETToday = crawler.search(keyword);

			for (News s : article_ETToday)
			{
				try
				{
					if (isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews = new News();
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						crawlerNews.setSourceUrl(s.getSourceUrl());
						crawlerNews.setKeyword(keyword);

						break;
					}
				} catch (Exception e)
				{
					System.out.println("PostBotCrawlerCenter ETToday Exception :" + e.toString());
					continue;
				}
			}

			break;
		}

		case "TVBS":
		{
			TvbsArticleCrawler crawler = new TvbsArticleCrawler();
			ArrayList<News> article_Tvbs = crawler.search(keyword);

			for (News s : article_Tvbs)
			{
				try
				{
					if (isNotExistingTitle(s.getTitle(), datasource))
					{
						crawlerNews = new News();
						crawlerNews.setTitle(s.getTitle());
						crawlerNews.setSendTime(s.getSendTime());
						crawlerNews.setWebUrl(s.getWebUrl());
						crawlerNews.setArticle(s.getArticle());
						crawlerNews.setAllPhotoUrl(s.getPhotoUrl());
						crawlerNews.setSourceUrl(s.getSourceUrl());
						crawlerNews.setKeyword(keyword);

						break;
					}
				} catch (Exception e)
				{
					System.out.println("PostBotCrawlerCenter TVBS Exception :" + e.toString());
					continue;
				}
			}

			break;
		}
		}

		String newsArticle = crawlerNews.getArticle() + "\r\n" + 
							 "發文時間：" + crawlerNews.getSendTime() + "\r\n" +
							 "原文網址：" + crawlerNews.getWebUrl() + "\r\n" + 
							 "搜尋網址：" + crawlerNews.getSourceUrl();

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

	public PostBot getPostBot(DataSource datasource, String botID)
	{
		PostBot postBot = new PostBot();

		Connection con = null;
		try
		{
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from postbot where botID = '" + botID + "' ");

			while (rs.next())
			{
				postBot.setBotID(botID);
				postBot.setKeyword(rs.getString("keyword"));
				postBot.setWebsite(rs.getString("website"));
				postBot.setFrequency(rs.getString("frequency"));
			}
			rs.close();// 關閉rs

			st.close();// 關閉st
		} catch (SQLException e)
		{
			System.out.println("PostBotCrawlerCenter getPostBot Exception :" + e.toString());
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

		return postBot;
	}
}