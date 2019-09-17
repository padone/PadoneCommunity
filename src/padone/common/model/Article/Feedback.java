package padone.common.model.Article;

public class Feedback {
	private String articleID;
	private String feedbackID;
	private String author;
	private String authorID;
	private String message;
	private String updateTime;

	public void setArticleID(String articleID){
		this.articleID = articleID;
	}

	public void setFeedbackID(String feedbackID){
		this.feedbackID = feedbackID;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public void setAuthorID(String authorID){
		this.authorID = authorID;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public void setUpdateTime(String updateTime){
		this.updateTime = updateTime;
	}

	public void feedback(String articleID,String id, String author,String authorID,String message,String updatetime) {
		setArticleID(articleID);
		setFeedbackID(id);
		setAuthor(author);
		setAuthorID(authorID);
		setMessage(message);
		setUpdateTime(updatetime);
	}
	@Override
	public String toString() {
		return "articleID: "+articleID+"\n"+"ID: "+feedbackID + "\n" + "author: " + author+"\n"+"authorID: "+authorID+"\n"+"message: "+message+"\n"+"update: "+updateTime+"\n";
	}
	

}
