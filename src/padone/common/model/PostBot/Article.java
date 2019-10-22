package padone.common.model.PostBot;

import java.util.ArrayList;

public class Article
{
	private String webUrl = null;
	private String title = null;
	private String sendTime = null;
	private String article = null;
	private String headPhotoUrl = null;
	private String headPhotoDescription = null;
	private ArrayList<String> photoUrl = new ArrayList<String>();
	private ArrayList<String> photoDescription = new ArrayList<String>();
 
	public Article()
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

	public void setHeadPhotoUrl(String headPhotoUrl)
	{
		this.headPhotoUrl = headPhotoUrl;
	}

	public void setHeadPhotoDescription(String headPhotoDescription)
	{
		this.headPhotoDescription = headPhotoDescription;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl.add(photoUrl);
	}

	public void setPhotoDescription(String photoDescription)
	{
		this.photoDescription.add(photoDescription);
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

	public String getHeadPhotoUrl()
	{
		return this.headPhotoUrl;
	}

	public String getHeadPhotoDescription()
	{
		return this.headPhotoDescription;
	}

	public ArrayList<String> getPhotoUrl()
	{
		return this.photoUrl;
	}

	public ArrayList<String> getPhotoDescription()
	{
		return this.photoDescription;
	}

	public String toString()
	{
		String output = "Title: " + title + "\r\n"
				+ "Web Url: " + webUrl + "\r\n"
				+ "Send Time: " + sendTime + "\r\n"
				+ "Article: " + article + "\r\n"
				+ "Head Photo Url: " + headPhotoUrl + "\r\n"
				+ "Head Photo Description: " + headPhotoDescription + "\r\n";

		int count = 1;

		for (String s : photoUrl)
		{
			output += "Photo Url(" + count + "): " + s + "\r\n"
					+ "Photo Description(" + count + "): " + photoDescription.get(count - 1) + "\r\n";

			count++;
		}

		return output;
	}
}