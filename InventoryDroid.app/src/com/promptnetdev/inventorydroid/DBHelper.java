package com.promptnetdev.inventorydroid;

import com.promptnetdev.inventorydroid.datamodel.InventoryItem;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {

	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDB;

	public static final String DB_NAME = "inventory.db";
	public static final String TABLE_ITEMS = "items";
	public static final String KEY_ID = "_id";
	public static final String KEY_CODE = "code";
	public static final String KEY_FORMAT = "format";
	public static final String KEY_NAME = "name";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_DESCRIPTION = "description";
	
	// add Manufacturer, item type, Model  Field
	public static final String KEY_ITEMTYPE = "itemtype";
	public static final String KEY_MANUFACTURER = "manufacturer";
	public static final String KEY_MODEL = "model";
	private static final int DB_VERSION = 1;
	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		// add Manufacturer, item type, Model  FieldS to table
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS
					+ " (_id INTEGER primary key autoincrement, "
					+ " code VARCHAR(20), "
					+ " format VARCHAR(20), "
					+ " name VARCHAR(30), "
					+ " itemtype VARCHAR(30),"
					+ " manufacturer VARCHAR(50), "
					+ " model VARCHAR(30), "
					+ " quantity VARCHAR(10), "
					+ " description VARCHAR(200))"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		}

	}

	public DBHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public DBHelper open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDBHelper.close();
		mDBHelper = null;
	}

	/******* Methoden ******/

	// Cursor bekommen (zB für ListViews)
	public Cursor getCursor(String sql) {
		return mDB.rawQuery(sql, null);
	}

	public void deleteDB() {
		mDB.delete(TABLE_ITEMS, null, null);
	}
	
	public long addItem(InventoryItem ii){
		long newId = mDB.insert(TABLE_ITEMS, null, ii.getContentValues());
		ii.setId(newId);
		return newId;
	}
	
	public int updateItem(InventoryItem ii){
		return mDB.update(TABLE_ITEMS, ii.getContentValues(), KEY_ID+"="+ii.getId(), null);
	}
	
	public void deleteItem(long articleId){
		mDB.delete(TABLE_ITEMS, KEY_ID+"="+articleId, null);
	}
	
	public InventoryItem getItem(String sBarcode, String sBarcodeFormat){
		Cursor c = getCursor("SELECT * FROM "+TABLE_ITEMS+" WHERE "+KEY_CODE+
				"='"+sBarcode+"' AND "+KEY_FORMAT+"='"+sBarcodeFormat+"'");
		
		if(!c.moveToFirst()){
			c.close();
			return null;
		}
		
		InventoryItem ii = new InventoryItem(c);
		
		c.close();
		return ii;
	}
	
	public InventoryItem getItem(long id){
		
		Cursor c = getCursor("SELECT * FROM "+TABLE_ITEMS+" WHERE "+KEY_ID+"="+id);
		
		if(!c.moveToFirst()){
			c.close();
			return new InventoryItem();
		}
		
		InventoryItem ii = new InventoryItem(c);
		
		c.close();
		return ii;
	}
	
	public String getProduktCSV(){
		if(mDBHelper == null || mDB==null)
			open();
		
		Cursor c = getCursor("SELECT "
				+KEY_CODE+","	
				+KEY_FORMAT+","			
				+KEY_NAME+","
				+KEY_ITEMTYPE+","
				+KEY_MANUFACTURER+","
				+KEY_MODEL+","
				+KEY_QUANTITY+","
				+KEY_DESCRIPTION
				+" FROM "+TABLE_ITEMS+" ORDER BY "+KEY_NAME);
		
		
		return Tools.getCSVFromCursor(c);
	}
	
}
