package padone.common.model.PostBot;

import java.util.ArrayList;

public class News
{
	private String webUrl = null;
	private String title = null;
	private String sendTime = null;
	private String article = null;
	private ArrayList<String> photoUrl = new ArrayList<String>();

	public News()
	{

	}

	public void setWebUrl(String webUrl)
	{
		this.webUrl = webUrl;
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

	public String getWebUrl()
	{
		return webUrl;
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
	
	public String[] getArrayPhotoUrl()
	{
		String[] photoUrlArray = this.photoUrl.toArray(new String[this.photoUrl.size()]);
		
		return photoUrlArray;
	}

	public String toString()
	{
		String output = "文章標題: " + title + "\r\n"
				+ "文章網址: " + webUrl + "\r\n"
				+ "發布時間: " + sendTime + "\r\n"
				+ "文章內容: " + article + "\r\n";

		int count = 1;

		for (String s : photoUrl)
		{
			output += "文章圖片(" + count + "): " + s + "\r\n";

			count++;
		}

		return output;
	}
}