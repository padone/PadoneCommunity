package padone.common.model.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ETTodayNewsCrawler extends Crawler
{	
	public ETTodayNewsCrawler()
	{
	}
	
	public Article search(String keyWord)
	{
		this.setKeyWord(keyWord);
		String url = new String("https://www.ettoday.net/news_search/doSearch.php?keywords=" + keyWord);

		getItem(url);

		return getArticle();
	}
	
	public void getItem(String url)
	{
		try
        {
            Document doc = Jsoup.connect(url)
                                .userAgent("\"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                                .validateTLSCertificates(false)
                                .get();
            Elements list = doc.getElementsByClass("box_2").select("h2 > a");

            setArticle(new Article());
            getArticle().setTitle(list.get(0).text());
            getArticle().setWebUrl(list.get(0).attr("abs:href"));
            getInside(list.get(0).attr("abs:href"));

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
								.validateTLSCertificates(false)
								.get();
		
			Elements info = doc.getElementsByClass("story").select("p, div > div");
			
			int length = info.size();
			String article = "";
			for(int i = 0; i < length; i++)
			{
				if(info.get(i).text().startsWith("▲") || info.get(i).text().startsWith("▼") || info.get(i).text().startsWith("（圖／") || info.get(i).text().startsWith("►"))
				{
					continue;
				}
				else
				{
					article += info.get(i).text() + "\n";
				}
			}
			if(article.startsWith("\n"))
				article = article.replaceFirst("\n", "");
			getArticle().setArticle(article);
			
			Elements date = doc.getElementsByClass("date");
			getArticle().setSendTime(date.text());
			
			Elements img = doc.getElementsByClass("story").select("p > img");
			int k = 0;
			for(Element image: img)
			{
				if(k == 0)
				{
					getArticle().setHeadPhotoUrl(image.attr("src"));
					getArticle().setHeadPhotoDescription(image.attr("alt"));
				}
				else
				{
					getArticle().setPhotoUrl(image.attr("src"));
					getArticle().setPhotoDescription(image.attr("alt"));
				}
				
				k++;
			}
		}
		catch (IOException e)
		{
			System.out.println("Error!! " + e);
			e.printStackTrace();
		}
	}
}