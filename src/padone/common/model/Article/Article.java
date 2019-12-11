package padone.common.model.Article;

import java.util.ArrayList;

public class Article {
	private String articleID;
	private String title;
	private String author;
	private String authorID;
	private String department;
	private String postTime;
	private String lastUpdateTime;
	private String description;
	private ArrayList<String> tag;
	private int imageNum = 0;
	private ArrayList<String> imageURL;
	private int great = 0;
	private boolean ifEvaluted = false;
	private boolean ifTracked = false;
	private boolean ifSuggestedByFamily = false;
	private boolean ifSuggestedByDoctor = false;
  
	private String hospital;
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
	public void setImage(int imageNum) {
		this.imageNum=imageNum;
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
	public void setTag(ArrayList<String> tag) {
		this.tag= tag;
	}
	public void setDepartment(String department) {
		this.department=department;
	}
	public void setHospital(String hospital){ this.hospital = hospital; }
	public void setImageURL(ArrayList<String> imageURL){
		this.imageURL = imageURL;
	}
	public void setGreat(int great) {
		this.great = great;
	}
	public void setIfEvaluted(boolean ifEvaluted) {
		this.ifEvaluted = ifEvaluted;
	}
	public void setIfTracked(boolean ifTracked){
		this.ifTracked = ifTracked;
	}
	public void setIfSuggestedByFamily(boolean ifSuggestedByFamily) {
		this.ifSuggestedByFamily = ifSuggestedByFamily;
	}
	public void setIfSuggestedByDoctor(boolean ifSuggestedByDoctor) {
		this.ifSuggestedByDoctor = ifSuggestedByDoctor;
	}

	public String getArticleID() {
		return articleID;
	}

	public int getImageNum() {
		return imageNum;
	}

	public int getGreat() {
		return great;
	}

	public ArrayList<String> getTag(){
		return this.tag;
	}

	public Article() {
		
	}
	public Article(String articleID, String author, String authorID, String department, String postTime, String lastUpdateTime, String description, ArrayList<String> tag, String hospital) {
		setArticleID(articleID);
		setAuthor(author);
		setAuthorID(authorID);
		setPostTime(postTime);
		setLastUpdateTime(lastUpdateTime);
		setDescription(description);
		setTag(tag);
		setDepartment(department);
		setHospital(hospital);
	}
	void tostring() {
		System.out.println("articleID:"+articleID+",author:"+author+", authorID:"+ authorID + ",department:"+department+",postTime:"+postTime+",lastUpdateTime:"+lastUpdateTime+",description:"+description+",tag:"+tag+",imageNum:"+imageNum+",title:"+title);
	}
	
}
