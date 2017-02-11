import java.util.*;
import java.sql.*;
import java.sql.Connection; 
import android.database.sqlite.*;
import android.database.*;


public class AndroidSqliteDb
{
	private static String cPath = "/storage/emulated/0/Android/data/com.vel.barcodetosheet/files/";	
	private static String cDbName = "barcode_to_sheet.db";
	private static String cDbFullName = cPath +  cDbName;
	private static SQLiteDatabase cDb = SQLiteDatabase.openDatabase(cDbFullName, null, 0);
	private static String cQuery = "select sqlite_version() AS sqlite_version";
	private static Cursor cCursor = null;	
	private static int cColCount = 0;
	
	public static void main(String[] args)
	{   
		System.out.println("Database: " + cDbName);
		System.out.println("Db type: Sqlite");
		System.out.print("Db version: ");
		
	    cCursor = cDb.rawQuery(cQuery, null);
		while (cCursor.moveToNext())
		{
			System.out.println(cCursor.getString(0));
		}	
		cCursor.close();
		System.out.println("All tables: ");
		cQuery = "SELECT name FROM sqlite_master WHERE type='table'";
		cCursor = cDb.rawQuery(cQuery, null);

	    while (cCursor.moveToNext())
		{
			System.out.println("   " + cCursor.getString(0));
		}
		cCursor.close();

		

		Scanner input = new Scanner(System.in);
        while (true)
		{
			String lPrevQuery = cQuery; 
			System.out.print("$: ");
			cQuery = input.nextLine();
			
			if (cQuery.isEmpty()) break;
			input.reset();
			
			switch (cQuery) {
				case "s" : {
				   save(lPrevQuery);
				   break;
				}
				default : execute(cQuery);
			}
						
		}
		
		cDb.close();
	}
	
	public static void execute(String pRawQuery) {
		try {
			cCursor = cDb.rawQuery(pRawQuery, null);
			cColCount = cCursor.getColumnCount();
			int i=0;
			String lKeyword = cQuery.length() > 6 ? cQuery.substring(0,6) : "";

			if ( lKeyword.equals("select")) { 

				for (i = 0;i < cColCount - 1;i++) 
					System.out.print("'" + padRight(cCursor.getColumnName(i), 10) + "'");	
				System.out.println("'" + padRight(cCursor.getColumnName(i), 10) + "'");
			}
			while (cCursor.moveToNext())
			{			
				for (i = 0;i < cColCount - 1;i++) 
					System.out.print("'" + padRight(cCursor.getString(i), 10) + "'");
				System.out.println("'" + padRight(cCursor.getString(i), 10) + "'");
			}
		} catch (Exception e){
			System.out.println(e);
		} finally {
			cCursor.close();
		}
	}

	public static String padRight(String s, int n)
	{
		return String.format("%1$-" + n + "s", s);  
	}
	
	public static void save(String pQuery) {
		String lDml = "insert into queries (query) values (\"" + pQuery + "\")";
		execute(lDml);
	}
}
