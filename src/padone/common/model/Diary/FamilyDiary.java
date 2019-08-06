package padone.common.model.Diary;

public class FamilyDiary {
	private String familyID;
	private String date;
	private String description;
	private String photo;
	public void SetFamilyID(String ID){
		familyID=ID;
	}
	public void SetDate(String date){
		this.date=date;
	}
	public void SetDescription(String des){
		description=des;
	}
	public void SetPhoto(String Photo){
		this.photo=photo;
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
	public String GetPhoto(){
		return photo;
	}
	
	
}
