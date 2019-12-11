package padone.common.model.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EbcArticleCrawler extends Crawler
{
	public EbcArticleCrawler()
	{
	}
	
	public ArrayList<News> search(String keyWord)
	{
		this.setKeyWord(keyWord);
		clearArticleList();
		String url = new String("https://news.ebc.net.tw/Search/Result?type=keyword&value=" + getKeyWord());
		
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
			
            Elements list = doc.getElementsByClass("style1 white-box");
            
            int listLength = list.size();
            //int listLength = 1;

          	for(int i = 0; i < listLength; i++)
            {
	            setArticle(new News());
	            getArticle().setTitle(list.get(i).select("a > div > div > span").get(0).text());
	            getArticle().setWebUrl(list.get(i).select("a").get(0).attr("abs:href"));
	            getArticle().setSourceUrl(url);
	            getArticle().setSendTime(list.get(i).select("a > div > span").get(0).text());
	            
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
		
			Elements info = doc.getElementsByClass("raw-style").select("p, strong");
			int length = info.size();
			String article = "";
			for(int i = 0; i < length; i++)
			{
				if(info.get(i).text().startsWith("▲") || info.get(i).text().startsWith("▼") || info.get(i).text().startsWith("★") || info.get(i).text().startsWith("【") || info.get(i).text().startsWith("\n") || info.get(i).text().startsWith("（") || info.get(i).text().startsWith("●"));
				else
					article += info.get(i).text() + "\n";
			}
			
			getArticle().setArticle(article);
			
			Elements headImage = doc.getElementsByClass("news-cover").select("img");
			getArticle().setPhotoUrl(headImage.attr("src"));
			
			Elements img = info.select("img");
			for(Element image: img)
			{
				getArticle().setPhotoUrl(image.attr("src"));
			}
		}
		catch (IOException e)
		{
			System.out.println("Error!! " + e);
			e.printStackTrace();
		}
	}
}