package padone.common.model.PostBot;

public class PostBot
{
	String botID = null;
	String keyword = null;
	String website = null;
	String frequency = null;
	
	public PostBot()
	{

	}
	
	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
	
	public void setWebsite(String website)
	{
		this.website = website;
	}
	
	public void setFrequency(String frequency)
	{
		this.frequency = frequency;
	}
	
	public void setBotID(String botID)
	{
		this.botID = botID;
	}

	public String getKeyword()
	{
		return keyword;
	}
	
	public String getWebsite()
	{
		return website;
	}

	public String getFrequency()
	{
		return frequency;
	}
	
	public String getBotID()
	{
		return botID;
	}
}
