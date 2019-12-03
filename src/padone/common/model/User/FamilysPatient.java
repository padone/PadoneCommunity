package padone.common.model.User;

public class FamilysPatient
{
	private String patientID = null;
	private String patientName = null;
	
	public void setPatientID(String patientID)
	{
		this.patientID = patientID;
	}

	public void setPatientName(String patientName)
	{
		this.patientName = patientName;
	}
	
	public String getPatientID()
	{
		return patientID;
	}

	public String getPatientName()
	{
		return patientName;
	}
	
	public String toString()
	{
		String output = "patientID: " + patientID + "\r\n"
				+ "patientName: " + patientName + "\r\n";
		
		return output;
	}
}
