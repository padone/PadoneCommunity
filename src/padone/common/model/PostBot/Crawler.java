package padone.common.model.PostBot;

public abstract class Crawler
{
	private Article article;
	private String keyWord = null;
	
	public Article search(String keyWord)
	{
		return null;
	}
	
	public abstract void getItem(String url);
	
	public abstract void getInside(String url);

	public String getKeyWord()
	{
		return keyWord;
	}

	public void setKeyWord(String keyWord)
	{
		this.keyWord = keyWord;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}
}