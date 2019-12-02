package padone.common.model.PostBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ETTodayArticleCrawler extends Crawler
{	
	public ETTodayArticleCrawler()
	{
	}
	
	public ArrayList<News> search(String keyWord)
	{
		this.setKeyWord(keyWord);
		clearArticleList();
		String url = new String("https://www.ettoday.net/news_search/doSearch.php?keywords=" + getKeyWord());

		getItem(url);

		return getArticleList();
	}
	
	public void getItem(String url)
	{
		try
        {
            @SuppressWarnings("deprecation")
			Document doc = Jsoup.connect(url)
                                .userAgent("\"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                                .validateTLSCertificates(false)
                                .get();
            Elements list = doc.getElementsByClass("box_2");

            int listLength = list.size();

          	for(int i = 0; i < listLength; i++)
            {
	            setArticle(new News());
	            getArticle().setTitle(list.get(i).select("h2 > a").get(0).text());
	            getArticle().setWebUrl(list.get(i).select("h2 > a").get(0).attr("abs:href"));
	            
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
	
	@SuppressWarnings("deprecation")
	public void getInside(String url)
	{
		try
		{
			Document doc = Jsoup.connect(url)
								.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
								.validateTLSCertificates(false)
								.get();
		
			Elements info = doc.getElementsByClass("story").select("p, div > div, h2 > span > span > strong");
			
			int length = info.size();
			String article = "";
			for(int i = 0; i < length; i++)
			{
				if(info.get(i).text().startsWith("▲") || info.get(i).text().startsWith("▼") || info.get(i).text().startsWith("（圖／") || info.get(i).text().startsWith("►") || info.get(i).text().startsWith("請繼續往下閱讀") || info.get(i).text().startsWith("【"))
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
			if(article.contains("'"))
				article = article.replace("'", " ");
			getArticle().setArticle(article);
			
			Elements date = doc.getElementsByClass("date");
			getArticle().setSendTime(date.get(0).text());
			
			Elements img = doc.getElementsByClass("story").select("p > img");
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