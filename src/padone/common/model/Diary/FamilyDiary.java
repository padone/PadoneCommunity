package padone.common.model.Diary;

import java.util.ArrayList;

public class FamilyDiary {
	private String familyID;
	private String date;
	private String description;
	private ArrayList<String> imageURL;
	
	public void SetFamilyID(String ID){
		familyID=ID;
	}
	public void SetDate(String date){
		this.date=date;
	}
	public void SetDescription(String des){
		description=des;
	}
	public void setImageURL(ArrayList<String> imageURL){
		this.imageURL = imageURL;
	}
	/***************************************************************************************************/
	
	public String GetFamilyID(){
		return familyID;
	}
	public String GetDate(){
		return date;
	}
	public String GetDescription(){
		return description;
	}
	public void FamilyDiary(String userID,String date,String description) {
		SetFamilyID(userID);
		SetDescription(description);
		SetDate(date);
	}
}