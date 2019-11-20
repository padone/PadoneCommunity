package padone.common.model.Diary;

public class FamilyDiary {
	private String familyID;
	private String date;
	private String description;
	private String picture;
	public void SetFamilyID(String ID){
		familyID=ID;
	}
	public void SetDate(String date){
		this.date=date;
	}
	public void SetDescription(String des){
		description=des;
	}
	public void Setpicture(String picture){
		this.picture=picture;
	}
	
	public String GetFamilyID(){
		return familyID;
	}
	public String GetDate(){
		return date;
	}
	public String GetDescription(){
		return description;
	}
	public String Getpicture(){
		return picture;
	}
	
	public void FamilyDiary(String userID,String date,String description,String picture) {
		SetFamilyID(userID);
		SetDescription(description);
		Setpicture(picture);
		SetDate(date);
	}
}
