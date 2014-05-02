package com.promptnetdev.inventorydroid.datamodel;

import com.promptnetdev.inventorydroid.DBHelper;
import android.content.ContentValues;
import android.database.Cursor;


public class InventoryItem {
	
	public static final long INVALID = -1;
	
	private String sBarcode;
	private String sBarcodeFormat;
	private String sName;
	private String sQuantity;
	private String sDescription;
	private String sItemtype;
	private String sManufacturer;
	private String sModel;
	private long articleId = INVALID;
	
	public InventoryItem() {
		
	}
	
	public InventoryItem(Cursor c){
		try{
			articleId = c.getLong(c.getColumnIndexOrThrow(DBHelper.KEY_ID));
			sBarcode = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_CODE));
			sBarcodeFormat = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_FORMAT));
			sName = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_NAME));
			sItemtype = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_ITEMTYPE));
			sManufacturer = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_MANUFACTURER));
			sModel = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_MODEL));
			sQuantity = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_QUANTITY));
			sDescription = c.getString(c.getColumnIndexOrThrow(DBHelper.KEY_DESCRIPTION));
			
			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ContentValues getContentValues(){
		ContentValues cv = new ContentValues(6);
		
		cv.put(DBHelper.KEY_CODE, sBarcode);
		cv.put(DBHelper.KEY_FORMAT, sBarcodeFormat);
		cv.put(DBHelper.KEY_NAME, sName);
		cv.put(DBHelper.KEY_ITEMTYPE, sItemtype);
		cv.put(DBHelper.KEY_MANUFACTURER, sManufacturer);
		cv.put(DBHelper.KEY_MODEL, sModel);
		cv.put(DBHelper.KEY_QUANTITY, sQuantity);
		cv.put(DBHelper.KEY_DESCRIPTION, sDescription);
		
		return cv;
	}
	
	public long getId(){
		return articleId;
	}
	
	public void setId(long id){
		articleId = id;
	}
	
	public boolean hasId(){
		return articleId != INVALID;
	}

	public String getBarcode() {
		return sBarcode;
	}

	public void setBarcode(String sBarcode) {
		this.sBarcode = sBarcode;
	}

	public String getBarcodeFormat() {
		return sBarcodeFormat;
	}

	public void setBarcodeFormat(String sBarcodeFormat) {
		this.sBarcodeFormat = sBarcodeFormat;
	}

	public String getName() {
		return sName;
	}

	public void setName(String sName) {
		this.sName = sName;
	}
	
	public String getItemType() {
		return sItemtype;
	}
	
	public void setItemType(String sItemtype) {
		this.sItemtype = sItemtype;
	}
	
	public String getManufacturer() {
		return sManufacturer;
	}
	
	public void setManufacturer(String sManufacturer) {
		this.sManufacturer = sManufacturer;
	}
	
	public String getModel() {
		return sModel;
	}
	
	public void setModel(String sModel) {
		this.sModel = sModel;
	}
	
	public String getQty() {
		return sQuantity;
	}

	public void setQty(String sQty) {
		this.sQuantity = sQty;
	}

	public String getDescription() {
		return sDescription;
	}

	public void setDescription(String sDescription) {
		this.sDescription = sDescription;
	}
	
}
