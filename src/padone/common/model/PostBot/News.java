package padone.common.model.PostBot;

import java.util.ArrayList;

public class News
{
	private String keyword = null;
	private String webUrl = null;
	private String sourceUrl = null;
	private String title = null;
	private String sendTime = null;
	private String article = null;
	private ArrayList<String> photoUrl = new ArrayList<String>();

	public News()
	{

	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
	
	public void setWebUrl(String webUrl)
	{
		this.webUrl = webUrl;
	}
	
	public void setSourceUrl(String sourceUrl)
	{
		this.sourceUrl = sourceUrl;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}

	public void setArticle(String article)
	{
		this.article = article;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl.add(photoUrl);
	}
	
	public void setAllPhotoUrl(ArrayList<String> photoUrl)
	{
		this.photoUrl = photoUrl;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public String getWebUrl()
	{
		return webUrl;
	}
	
	public String getSourceUrl()
	{
		return sourceUrl;
	}

	public String getTitle()
	{
		return title;
	}

	public String getSendTime()
	{
		return sendTime;
	}

	public String getArticle()
	{
		return article;
	}

	public ArrayList<String> getPhotoUrl()
	{
		return this.photoUrl;
	}
	
	public String getArrayPhotoUrl()
	{
		int count = 0;
		
		String photoUrlList = "[\"";

		for (String s : photoUrl)
		{
			if(count == 0)
				photoUrlList += s + "\"";
			else
				photoUrlList += ", \"" + s + "\"";
			
			count++;
		};
		
		photoUrlList += "]";
		
		return photoUrlList;
	}
	
	public String toString()
	{
		String output = "文章標題: " + title + "\r\n"
				+ "文章網址: " + webUrl + "\r\n"
				+ "搜尋網址:" + sourceUrl + "\r\n"
				+ "發布時間: " + sendTime + "\r\n"
				+ "文章內容: " + article + "\r\n"
				+ "關鍵字" + keyword + "\r\n";

		int count = 1;

		for (String s : photoUrl)
		{
			output += "文章圖片(" + count + "): " + s + "\r\n";

			count++;
		}

		return output;
	}
}