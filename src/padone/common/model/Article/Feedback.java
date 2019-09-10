package padone.common.model.Article;

public class Feedback {
	private String articleID;
	private String ID;
	private String author;
	private String authorID;
	private String message;
	private String updateTime;
	public void SetArticleID(String articleID) {
		this.articleID=articleID;
	}
	public void SetID(String id) {
		ID=id;
		
	}
	public void SetAuthor(String author) {
		this.author=author;
	}
	public void SetAuthorID(String authorID) {
		this.authorID=authorID;
	}
	public void SetMessage(String message) {
		this.message=message;
	}
	public void SetUpdateTime(String updatetime) {
		updateTime=updatetime;
	}
	public String GetArticleID() {
		return articleID;
	}
	public String GetID() {
		return ID;
		
	}
	public String GetAuthor() {
		return author;
	}
	public String GetAuthorID() {
		return authorID;
	}
	public String GetMessage() {
		return message;
	}
	public String GetUpdateTime() {
		return updateTime;
	}
	public void feedback(String articleID,String id,String author,String authorID,String message,String updatetime) {
		SetArticleID(articleID);
		SetID(id);
		SetAuthor(author);
		SetAuthorID(authorID);
		SetMessage(message);
		SetUpdateTime(updatetime);
	}
	@Override
	public String toString() {
		return "articleID: "+articleID+"\n"+"ID: "+ID+"\n"+"author: "+author+"\n"+"authorID: "+authorID+"\n"+"message: "+message+"\n"+"update: "+updateTime+"\n";
		
	}
	

}
