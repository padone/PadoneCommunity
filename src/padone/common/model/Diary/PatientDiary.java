package padone.common.model.Diary;

import java.util.ArrayList;

public class PatientDiary {
	private String patientID;
	private String familyID;
	private String date;
	private String familyDescription;
	private String patientDescription;
	private ArrayList<String> imageURL;
	
	public void SetFamilyID(String ID){
		familyID=ID;
	}
	public void SetPatientID(String ID){
		patientID=ID;
	}
	
	public void SetDate(String date){
		this.date=date;
	}
	public void SetFamilyDescription(String  Description){
		 familyDescription= Description;
	}
	public void SetPatientDescription(String  Description){
		 patientDescription= Description;
	}
	
	public void setImageURL(ArrayList<String> imageURL){
		this.imageURL = imageURL;
	}
	
	/**********************************************************************************/
	public String GetPatientID(){
		return patientID;
	}
	public String GetFamilyID(){
		return familyID;
	}
	public String GetDate(){
		return date;
	}
	public String GetFamilyDescription(){
		return familyDescription;
	}
	public String GetPatientDescription(){
		return patientDescription;
	}
	public void PatientDiary(String patientID,String familyID,String date, String familyDescription,String patientDescription){
		SetFamilyID(familyID);
		SetPatientID(patientID);
		SetDate(date);
		SetFamilyDescription(familyDescription);
		SetPatientDescription(patientDescription);
	}
	@Override
	public String toString() {
		return "patientID: "+patientID+"\n"+"familyID: "+familyID+"\n"+"date: "+date+"\n"+
	"familyDescription: "+familyDescription+"\n"+"patientDescription: "+patientDescription+"\n";
	}
}
