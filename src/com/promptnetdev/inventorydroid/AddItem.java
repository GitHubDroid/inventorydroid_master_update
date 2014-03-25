package com.promptnetdev.inventorydroid;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddItem extends Activity implements OnClickListener {

	   private static final int REQUEST_BARCODE = 0;
	    private static final ProductData mProductData = new ProductData();

	    // private fields omitted

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.add_item);

	        mSerialEdit = (EditText) findViewById(R.id.serialEdit);
	        mFormatEdit = (EditText) findViewById(R.id.codeFormatEdit);
	        mItemEdit = (EditText) findViewById(R.id.itemEdit);
	        mScanButton = (Button) findViewById(R.id.scanButton);
	        mScanButton.setOnClickListener(this);
	        mAddButton = (Button) findViewById(R.id.addButton);
	        mAddButton.setOnClickListener(this);
	        // mProductDb = new ProductDatabase(this); // not yet shown
	    }

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		
			case R.id.addButton:
			    String serial = mSerialEdit.getText().toString();
			    String format = mFormatEdit.getText().toString();
			    String item = mItemEdit.getText().toString();
			    
			    String errors = validateFields(serial, format, item);
			    if (errors.length() > 0) {
			        showInfoDialog(this, "Please fix errors", errors);
			    } else {
			        mProductData.barcode = serial;
			        mProductData.format = format;
			        mProductData.title = item;
			     
			        // mProductDb.insert(mProductData);
			        showInfoDialog(this, "Success", "Product saved successfully");
			        resetForm();
			    }
			    break;
			case R.id.scanButton:
		        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
		        startActivityForResult(intent, REQUEST_BARCODE);
		        break;
		    }
		}

		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		    if (requestCode == REQUEST_BARCODE) {
		        if (resultCode == RESULT_OK) {
		            String serial = intent.getStringExtra("SCAN_RESULT");
		            mSerialEdit.setText(serial);

		            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		            mFormatEdit.setText(format);
		        } else if (resultCode == RESULT_CANCELED) {
		            finish();
		        }
		    }
		}
		
		private static String validateFields(String serial, String format, 
			    String item, String price) {
			    StringBuilder errors = new StringBuilder();

			    if (serial.matches("^\\s*$")) {
			        errors.append("Serial required\n");
			    }

			    if (format.matches("^\\s*$")) {
			        errors.append("Format required\n");
			    }

			    if (item.matches("^\\s*$")) {
			        errors.append("Title required\n");
			    }

			    return errors.toString();
			}
	}