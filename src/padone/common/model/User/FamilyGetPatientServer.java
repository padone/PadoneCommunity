package padone.common.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;

public class FamilyGetPatientServer
{
	synchronized public ArrayList<FamilysPatient> getPatient(DataSource datasource, String familyID)
	{
		ArrayList<FamilysPatient> patientSet = new ArrayList<FamilysPatient>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FamilysPatient user; 

		try
		{
			con = datasource.getConnection();
			//Statement st = con.createStatement();
			//rs = st.executeQuery("select patientID FROM patientrelationship WHERE familyID = '" + userID + "'");
			pstmt = con.prepareStatement("SELECT ps.patientID as id, p.name as Name FROM patientrelationship as ps INNER JOIN patient as p ON p.userID = ps.patientID and ps.familyID = ?");
			pstmt.setString(1, familyID);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				user = new FamilysPatient();
				user.setPatientID(rs.getString("id"));
				user.setPatientName(rs.getString("Name"));

				patientSet.add(user);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
		} catch (SQLException e)
		{
			System.out.println("ProfileSettingServer isExistingUser Exception :" + e.toString());
			e.printStackTrace();
		} finally
		{
			if (con != null)
			{
				try
				{
					con.close();
				} catch (Exception ignore){}
			}
		}
		return patientSet;
	}
}