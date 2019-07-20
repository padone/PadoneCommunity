package padone.common.model;
public class Article {
	private String articleID;
	private String title;
	private String author;
	private String authorID;
	private String department;
	private String postTime;
	private String lastUpdateTime;
	private String description;
	private String tag;
	private String image;
	public void setArticleID(String articleID) {
		this.articleID=articleID;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public void setAuthor(String author) {
		this.author=author;
	}
	public void setAuthorID(String authorID){ this.authorID = authorID; }
	public void setImage(String image) {
		this.image=image;
	}
	public void setPostTime(String postTime) {
		this.postTime=postTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime=lastUpdateTime;
	}
	public void setDescription(String descriptionD) {
		this.description=descriptionD;
	}
	public void setTag(String tag) {
		this. tag= tag;
	}
	public void setDepartment(String department) {
		this.department=department;
	}
	public Article() {
		
	}
	public Article(String articleID,String author, String authorID,String department,String postTime,String lastUpdateTime,String description,String tag) {
		setArticleID(articleID);
		setAuthor(author);
		setAuthorID(authorID);
		setPostTime(postTime);
		setLastUpdateTime(lastUpdateTime);
		setDescription(description);
		setTag(tag);
		setDepartment(department);
		
	}
	void tostring() {
		System.out.println("articleID:"+articleID+",author:"+author+", authorID:"+ authorID + ",department:"+department+",postTime:"+postTime+",lastUpdateTime:"+lastUpdateTime+",description:"+description+",tag:"+tag+",image:"+image+",title:"+title);
	}
	
}
