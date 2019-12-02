package padone.common.model.PostBot;

import java.util.ArrayList;

public abstract class Crawler
{
	private ArrayList<News> articleList = new ArrayList<News>();
	private News article;
	private String keyWord = null;
	
	public ArrayList<News> search(String keyWord)
	{
		return null;
	}
	
	public void clearArticleList() 
	{
		articleList.clear();
	}
	
	public abstract void getItem(String url);
	
	public abstract void getInside(String url);
	
	public ArrayList<News> getArticleList()
	{
		return articleList;
	}

	public void setNewsList(ArrayList<News> newsList)
	{
		this.articleList = newsList;
	}
	
	public News getArticle() {
		return article;
	}

	public void setArticle(News article)
	{
		this.article = article;
	}

	public String getKeyWord()
	{
		return keyWord;
	}

	public void setKeyWord(String keyWord)
	{
		this.keyWord = keyWord;
	}
}