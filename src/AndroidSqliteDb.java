import java.util.*;
import java.sql.*;
import java.sql.Connection; 
import android.database.sqlite.*;
import android.database.*;


public class AndroidSqliteDb
{
	private static String path = "/storage/emulated/0/Android/data/com.vel.barcodetosheet/files/";	
	private static String dbName = "barcode_to_sheet.db";
	private static String dbFullName = path +  dbName;
	private static SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFullName, null, 0);
	private static String query = "select sqlite_version() AS sqlite_version";
	private static Cursor cursor = null;	
	private static int colCount = 0;
	
	public static void main(String[] args)
	{   
		System.out.println("Database: " + dbName);
		System.out.println("Db type: Sqlite");
		System.out.print("Db version: ");
		
	    cursor = db.rawQuery(query, null);
		while (cursor.moveToNext())
		{
			System.out.println(cursor.getString(0));
		}	
		cursor.close();
		System.out.println("All tables: ");
		query = "SELECT name FROM sqlite_master WHERE type='table'";
		cursor = db.rawQuery(query, null);

	    while (cursor.moveToNext())
		{
			System.out.println("   " + cursor.getString(0));
		}
		cursor.close();

		

		Scanner input = new Scanner(System.in);
        while (true)
		{
			System.out.print("$: ");
			query = input.nextLine();
			
			if (query.isEmpty()) break;
			input.reset();
			
			execute(query);
			
		}
		
		db.close();
	}
	
	public static void execute(String pRawQuery) {
		try {
			cursor = db.rawQuery(pRawQuery, null);
			colCount = cursor.getColumnCount();
			int i=0;
			String keyword = query.substring(0,6);

			if ( keyword.equals("select")) { 

				for (i = 0;i < colCount - 1;i++) 
					System.out.print("'" + padRight(cursor.getColumnName(i), 10) + "'");	
				System.out.println(cursor.getColumnName(i));
			}
			while (cursor.moveToNext())
			{			
				for (i = 0;i < colCount - 1;i++) 
					System.out.print("'" + padRight(cursor.getString(i), 10) + "'");
				System.out.println(cursor.getString(i));
			}
		} catch (Exception e){
			System.out.println(e);
		} finally {
			cursor.close();
		}
	}

	public static String padRight(String s, int n)
	{
		return String.format("%1$-" + n + "s", s);  
	}
}
