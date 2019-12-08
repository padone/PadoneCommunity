package padone.common.model.User;

public class PatientsFamily
{
	private String familyID = null;
	private String familyName = null;
	
	public PatientsFamily()
	{

	}
	
	public void setFamilyID(String familyID)
	{
		this.familyID = familyID;
	}

	public void setFamilyName(String familyName)
	{
		this.familyName = familyName;
	}
	
	public String getFamilyID()
	{
		return familyID;
	}

	public String getFamilyName()
	{
		return familyName;
	}
	
	public String toString()
	{
		String output = "familyID: " + familyID + "\r\n"
				+ "familyName: " + familyName + "\r\n";
		
		return output;
	}
}