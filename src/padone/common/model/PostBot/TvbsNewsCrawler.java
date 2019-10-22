package padone.common.model.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TvbsNewsCrawler extends Crawler
{ 
	public TvbsNewsCrawler()
	{
	}
	
	public Article search(String keyWord)
	{
		this.setKeyWord(keyWord);
		String url = new String("https://news.tvbs.com.tw/news/searchresult/news/1/?search_text=" + keyWord);

		getItem(url);
		
		return getArticle();
	}

	public void getItem(String url)
	{
		try
        {
            Document doc = Jsoup.connect(url)
                                .userAgent("\"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                                .get();
            Elements list = doc.getElementsByClass("search_list_txt");
            Elements web = doc.getElementsByClass("search_list_box");
            Elements time = doc.getElementsByClass("icon_time");

            int length = list.size();

            for(int i = 0; i < length; i++)
            {
                setArticle(new Article());
                getArticle().setTitle(list.get(i).text());
                getArticle().setWebUrl(web.get(i).attr("abs:href"));
                getInside(web.get(i).attr("abs:href"));
                getArticle().setSendTime(time.get(i).text());
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
			
			Elements img = doc.getElementsByClass("img margin_b20").select("div > img");	
			getArticle().setHeadPhotoUrl(img.attr("src"));
		}
		catch (IOException e)
		{
			System.out.println("Error!! " + e);
			e.printStackTrace();
		}
	}
}