package com.promptnetdev.inventorydroid;

import com.promptnetdev.inventorydroid.datamodel.InventoryItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.promptnetdev.inventorydroid.R;

//TODO make a setup dialog explaining all the settings
public class ScanActivity extends Activity 
		implements OnClickListener{
	
	public static final String EXTRA_ARTICLE_ID = "com.promptnetdev.inventorydroid_articleId";
	public static final String EXTRA_FINISH_ON_DELETE = "com.promptnetdev.inventorydroid_finishOnDelete";
	public static final String EXTRA_NEW_ON_SAVE = "com.promptnetdev.inventorydroid_newOnSave";
	
	Button bt_scan, bt_plusone, bt_plusten, bt_minusone, bt_minusten, bt_save, bt_delete;
	EditText et_code, et_format, et_name, et_itemtype, et_manufacturer, et_model, et_quantity, et_description;
	AlertDialog mDeleteDialog;
	DBHelper mDBHelper;
	Intent scanIntent;
	long itemId = InventoryItem.INVALID;
	boolean finishOnDelete = false, newOnSave = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        
        mDBHelper = new DBHelper(this);
        mDBHelper.open();
        
        scanIntent = new Intent("com.google.zxing.client.android.SCAN");
        scanIntent.setPackage("com.google.zxing.client.android");
        scanIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
        
        itemId = getIntent().getLongExtra(EXTRA_ARTICLE_ID, -1);
        finishOnDelete = getIntent().getBooleanExtra(EXTRA_FINISH_ON_DELETE, false);
        newOnSave = getIntent().getBooleanExtra(EXTRA_NEW_ON_SAVE, false);
        
        bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_plusone = (Button) findViewById(R.id.bt_plusone);
        bt_plusten = (Button) findViewById(R.id.bt_plusten);
        bt_minusone = (Button) findViewById(R.id.bt_minusone);
        bt_minusten = (Button) findViewById(R.id.bt_minusten);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        
        et_code = (EditText) findViewById(R.id.et_code);
        et_format = (EditText) findViewById(R.id.et_format);
        et_name = (EditText) findViewById(R.id.et_name);
        et_itemtype = (EditText) findViewById(R.id.et_itemtype);
        et_manufacturer = (EditText) findViewById(R.id.et_manufacturer);
        et_model = (EditText) findViewById(R.id.et_model);
        et_quantity = (EditText) findViewById(R.id.et_quantity);
        et_description = (EditText) findViewById(R.id.et_description);
        
        bt_scan.setOnClickListener(this);
        bt_plusone.setOnClickListener(this);
        bt_plusten.setOnClickListener(this);
        bt_minusone.setOnClickListener(this);
        bt_minusten.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        
        if(itemId == InventoryItem.INVALID){
        	startActivityForResult(scanIntent, 0);
        }else{ //populate Data
        	InventoryItem ii = mDBHelper.getItem(itemId);
        	populateItem(ii);
        }
    }


	@Override
	protected void onDestroy() {
		mDBHelper.close();
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		if(v == bt_scan){
			
			startActivityForResult(scanIntent, 0);
			
		}else if(v == bt_plusone){
			addToQty(1);
		}else if(v == bt_plusten){
			addToQty(10);
		}else if(v == bt_minusone){
			addToQty(-1);
		}else if(v == bt_minusten){
			addToQty(-10);
		}else if(v == bt_save){
			
			if(itemId == InventoryItem.INVALID){
				itemId = mDBHelper.addItem(readInventoryItem());
				
				if(newOnSave){
					itemId = -1;
					resetViews();
					startActivityForResult(scanIntent, 0);
				}else{
					bt_delete.setVisibility(View.VISIBLE);
				}
				
			}else{
				mDBHelper.updateItem(readInventoryItem());
			}
			
		}else if(v == bt_delete){
			
			if(itemId<0){
				bt_delete.setVisibility(View.GONE);
				return;
			}
			
			if(mDeleteDialog == null){
				AlertDialog.Builder adb = new Builder(this);
				adb.setTitle(R.string.delete_dialog_title);
				adb.setMessage(R.string.delete_dialog_message);
				adb.setIcon(R.drawable.ic_launcher);
				adb.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDBHelper.deleteItem(itemId);
						itemId = -1;
						
						if(finishOnDelete){
							ScanActivity.this.finish();
						}
						dialog.dismiss();
					}
				});
				adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				mDeleteDialog = adb.create();
			}
			
			mDeleteDialog.show();
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 0 && resultCode == RESULT_OK) {
			
			String code = data.getStringExtra("SCAN_RESULT");
			String format = data.getStringExtra("SCAN_RESULT_FORMAT");
			
			InventoryItem existing = mDBHelper.getItem(code, format);
			
			if(existing != null){
				populateItem(existing);
			}else{
				et_code.setText(code);
				et_format.setText(format);
				
				et_name.requestFocus(); //focus on name
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void addToQty(int quantity){
		
		String current = et_quantity.getText().toString(); 
		
		if(current.equals("")){
			
			et_quantity.setText(String.valueOf(quantity));
			
		}else{
			
			int cur = Integer.valueOf(current);
			int neu = cur + quantity;
			
			if(neu<0){
				et_quantity.setText("0");
			}else{
				et_quantity.setText(String.valueOf(neu));
			}
			
		}
	}
	
	private InventoryItem readInventoryItem(){
		InventoryItem a = new InventoryItem();
		a.setId(itemId);
		a.setBarcode(et_code.getText().toString());
		a.setBarcodeFormat(et_format.getText().toString());
		a.setName(et_name.getText().toString());
		a.setItemType(et_itemtype.getText().toString());
		a.setManufacturer(et_manufacturer.getText().toString());
		a.setModel(et_model.getText().toString());
		a.setQty(et_quantity.getText().toString());
		a.setDescription(et_description.getText().toString());
		return a;
	}
	
	private void populateItem(InventoryItem ii){
		if(ii == null){
			resetViews();
		}
		
		itemId = ii.getId();
    	et_code.setText(ii.getBarcode());
    	et_format.setText(ii.getBarcodeFormat());
		et_name.setText(ii.getName());
		et_itemtype.setText(ii.getItemType());
		et_manufacturer.setText(ii.getManufacturer());
		et_model.setText(ii.getModel());
    	et_quantity.setText(ii.getQty());
    	et_description.setText(ii.getDescription());

    	
    	if(itemId == InventoryItem.INVALID)
    		bt_delete.setVisibility(View.GONE);
    	else
    		bt_delete.setVisibility(View.VISIBLE);
	}
	
	private void resetViews(){
		et_code.setText("");
		et_format.setText("");
		et_name.setText("");
		et_itemtype.setText("");
		et_manufacturer.setText("");
		et_model.setText("");
		et_quantity.setText("1");
		et_description.setText("");
		
	}
	
}
