package padone.common.model.PostBot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EbcNewsCrawler extends Crawler
{
	public EbcNewsCrawler()
	{
	}
	
	public Article search(String keyWord)
	{
		this.setKeyWord(keyWord);
		String url = new String("https://news.ebc.net.tw/Search/Result?type=keyword&value=" + keyWord);
		
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
            Elements list = doc.getElementsByClass("style1 white-box").select("a > div > div > span");
            Elements web = doc.getElementsByClass("style1 white-box").select("div > a");
            Elements time = doc.getElementsByClass("small-gray-text");

            setArticle(new Article());
            getArticle().setTitle(list.get(0).text());
            getArticle().setWebUrl(web.get(0).attr("abs:href"));
            if (time.size() > 0)
            	getArticle().setSendTime(time.get(0).text());
            else
            	getArticle().setSendTime("");
            getInside(web.get(0).attr("abs:href"));

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
				if(info.get(i).text().startsWith("▲") || info.get(i).text().startsWith("▼") || info.get(i).text().startsWith("★") || info.get(i).text().startsWith("【") || info.get(i).text().startsWith("\n"));
				else
					article += info.get(i).text() + "\n";
			}
			
			String[] articleSplit = article.split("往下看更多新聞");
			getArticle().setArticle(articleSplit[0]);
			
			Elements headImage = doc.getElementsByClass("news-cover").select("img");
			getArticle().setHeadPhotoUrl(headImage.attr("src"));
			getArticle().setHeadPhotoDescription(headImage.attr("alt"));
			
			Elements img = info.select("img");
			for(Element image: img)
			{
				getArticle().setPhotoUrl(image.attr("src"));
				getArticle().setPhotoDescription(image.attr("alt"));
			}
		}
		catch (IOException e)
		{
			System.out.println("Error!! " + e);
			e.printStackTrace();
		}
	}
}