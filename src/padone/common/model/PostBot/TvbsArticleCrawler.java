package padone.common.model.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TvbsArticleCrawler extends Crawler
{ 
	public TvbsArticleCrawler()
	{
	}
	
	public ArrayList<News> search(String keyWord)
	{
		setKeyWord(keyWord);
		clearArticleList();
		String url = new String("https://news.tvbs.com.tw/news/searchresult/news/?search_text=" + getKeyWord());

		getItem(url);
		
		return getArticleList();
	}

	public void getItem(String url)
	{
		try
        {
            Document doc = Jsoup.connect(url)
                                .userAgent("\"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                                .get();
            Elements list = doc.getElementsByClass("search_list_div").select("ul > li");
            
            int listLength = list.size();

          	for(int i = 0; i < listLength; i++)
            {
	            setArticle(new News());
	            getArticle().setTitle(list.get(i).getElementsByClass("search_list_txt").get(0).text());
	            getArticle().setWebUrl(list.get(i).select("a").get(0).attr("abs:href"));
	            getArticle().setSendTime(list.get(i).getElementsByClass("icon_time").get(0).text());
	            
	            getInside(getArticle().getWebUrl());
	            //System.out.println(getArticle().toString());
	            getArticleList().add(getArticle());
            }
        }
		catch (IOException e)
		{
			System.out.println("Error!!" + e);
			e.printStackTrace();
		}
	}
	
	public void getInside(String url)
	{
		try
		{
			Document doc = Jsoup.connect(url)
								.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
								.get();
		
			Elements info = doc.getElementsByClass("h7 margin_b20");
			String article = info.text();
			String[] articleSplit = article.split("最HOT話題在這");
			getArticle().setArticle(articleSplit[0]);
			
			Elements img = doc.getElementsByClass("img margin_b20").select("img");	
			getArticle().setPhotoUrl(img.attr("src"));
		}
		catch (IOException e)
		{
			System.out.println("Error!! " + e);
			e.printStackTrace();
		}
	}
}