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

		outerloop :
        while (true)
		{		
			
			String lPrevQuery = cQuery;
			System.out.print("$: ");
			input.reset();
			cQuery = input.nextLine();
					
			if (cQuery.isEmpty()) break;

			switch (cQuery)
			{
				case "s" : {
						save(lPrevQuery);
						break;
					}				
				case "q" : break outerloop;					
				default : {
						if (cQuery.matches("^[0-9]+$"))
						{   String lQuery = "select query from queries where id=" + cQuery;
							Cursor lCursor = execute(lQuery);
							if (!lCursor.moveToNext()) continue;
							
							cQuery = lCursor.getString(0);
						}
						
						showResult(execute(cQuery));
							
						}
			}

		}
		
		if (!cCursor.isClosed())
		{
			cCursor.close();
		}
		
		cDb.close();
	}

	public static Cursor execute(String pRawQuery)
	{   Cursor lCursor = null;
		try
		{
			lCursor = cDb.rawQuery(pRawQuery, null);
			
		}
		catch (Exception e)
		{
			lCursor = null;
			System.out.println(e);
		}
		
		
		return lCursor;
	}
	
	public static void showResult(Cursor pCursor) {
		int lColCount = pCursor.getColumnCount();
		int i=0;
		String lKeyword = cQuery.length() > 6 ? cQuery.substring(0, 6) : "";

		if (pCursor == null) return;
		
		if (lKeyword.equals("select"))
		{ 

			for (i = 0;i < lColCount - 1;i++) 
				System.out.print("'" + padRight(pCursor.getColumnName(i), 10) + "'");	
			System.out.println("'" + padRight(pCursor.getColumnName(i), 10) + "'");
		}
		while (pCursor.moveToNext())
		{			
			for (i = 0;i < lColCount - 1;i++) 
				System.out.print("'" + padRight(pCursor.getString(i), 10) + "'");
			System.out.println("'" + padRight(pCursor.getString(i), 10) + "'");
		}
	}	
	

	public static String padRight(String s, int n)
	{
		return String.format("%1$-" + n + "s", s);  
	}

	public static void save(String pQuery)
	{
		String lDml = "insert into queries (query) values (\"" + pQuery + "\")";
		execute(lDml);
	}


}
