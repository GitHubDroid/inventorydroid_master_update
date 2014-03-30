package com.promptnetdev.inventorydroid;

import android.database.Cursor;

public class Tools {
	
	public static String getCSVFromCursor(Cursor c){
		
		//Check if Cursor is null
		if(c == null)
			return "";
		
		final char itemDivider = ';';
		final String rowDivider = "\r\n";  
		
		StringBuilder sb = new StringBuilder();
		
		String[] columnNames = c.getColumnNames();
		
		//"Header" (names of columns) 
		for(String col : columnNames){
			sb.append(col).append(itemDivider);
		}
		sb.deleteCharAt(sb.length()-1).append(rowDivider); //delete last itemDivider and add rowDivider
		
		//Check if Cursor has 0 rows / is empty
		if(c.getCount()<1){
			
			sb.delete(sb.length()-rowDivider.length(), sb.length()); //delete last row divider
			
			return sb.toString();
		}
		
		//else
		//"Data" (data from cursor)
		c.moveToFirst();
		
		do{
			for (int index = 0; index < columnNames.length; index++) {
				sb.append(c.getString(index)).append(itemDivider);
			}
			sb.deleteCharAt(sb.length()-1).append(rowDivider); //delete last itemDivider and add rowDivider
			
		}while(c.moveToNext());
		
		sb.delete(sb.length()-rowDivider.length(), sb.length()); //delete last row divider
		
		return sb.toString();
	}	
	
	//TODO tool for choosing table rows and so on for csv?
	//("export what you want")
	
}
