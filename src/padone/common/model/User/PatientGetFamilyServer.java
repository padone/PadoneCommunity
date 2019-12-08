package padone.common.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.tomcat.jdbc.pool.DataSource;

public class PatientGetFamilyServer
{
	synchronized public ArrayList<PatientsFamily> getFamily(DataSource datasource, String familyID)
	{
		ArrayList<PatientsFamily> familySet = new ArrayList<PatientsFamily>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PatientsFamily user; 

		try
		{
			con = datasource.getConnection();
			pstmt = con.prepareStatement("SELECT ps.familyID as id, p.name as Name FROM patientrelationship as ps INNER JOIN patient as p ON p.userID = ps.familyID and ps.patientID = ?");
			pstmt.setString(1, familyID);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				user = new PatientsFamily();
				user.setFamilyID(rs.getString("id"));
				user.setFamilyName(rs.getString("Name"));

				familySet.add(user);
			}
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
		} catch (SQLException e)
		{
			System.out.println("PatientGetFamilyServer getFamily Exception :" + e.toString());
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
		return familySet;
	}
}
