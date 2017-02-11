import java.sql.*;

public class JDBCSqlDroidDB
{
	public static void main(String[] args) throws SQLException {
		
		Connection connection;
		String path = "/storage/emulated/0/Android/data/com.vel.barcodetosheet/files/";	
	    String dbName = "barcode_to_sheet.db";
		String dbFullName = path +  dbName;	

		try 
		{ 
		  DriverManager.registerDriver((Driver)Class.forName("org.sqldroid.SQLDroidDriver").newInstance()); 
		} catch ( Exception e) {
		  throw new RuntimeException("Failed to register SQLDroidDriver ");
		}
		System.out.println("Driver registered");
		
		String jdbcUrl = "jdbc:sqldroid:" + dbFullName;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
		
		String query = "SELECT name FROM sqlite_master WHERE type='table'";
		Statement stmt = null;
		try {
		    stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next())
			{
				System.out.println(rs.getString("name"));
			}
			rs.close();
		} catch (SQLException e ) {
		  System.out.println(e);
		} finally {
			if (stmt != null) { stmt.close(); }
		}
		connection.close();
    }
}
