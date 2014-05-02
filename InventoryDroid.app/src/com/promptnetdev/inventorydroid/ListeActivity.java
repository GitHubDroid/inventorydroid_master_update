package com.promptnetdev.inventorydroid;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.promptnetdev.inventorydroid.R;

public class ListeActivity extends ListActivity implements OnItemClickListener, OnItemLongClickListener{
	
	DBHelper mDBHelper;
	SimpleCursorAdapter mAdapter;
	Cursor mCursor;
	final String SQL_QUERY = "SELECT * FROM "+DBHelper.TABLE_ITEMS+" ORDER BY "+DBHelper.KEY_NAME; 
	
	TextView empty;
	
	//OLD ORIGINAL String[] from = new String[]{DBHelper.KEY_AMOUNT,DBHelper.KEY_NAME, DBHelper.KEY_PRICE};
	// OLD ORIGINAL int[] to = new int[]{R.id.tv_amount, R.id.tv_name, R.id.tv_price};
	String[] from = new String[]{DBHelper.KEY_QUANTITY,DBHelper.KEY_NAME};
	int[] to = new int[]{R.id.tv_amount, R.id.tv_name};
	
	AlertDialog mDeleteDialog;
	long lastDeleteId = -1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mDBHelper = new DBHelper(this);
        mDBHelper.open();
        
        mCursor = mDBHelper.getCursor(SQL_QUERY);
        startManagingCursor(mCursor);
        
        mAdapter = new SimpleCursorAdapter(this, R.layout.article_item, mCursor, from, to);
        
        empty = new TextView(this);
        empty.setText(R.string.empty_list_info);
        empty.setId(android.R.id.empty);
        empty.setVisibility(View.GONE);
        
        ((ViewGroup)getListView().getParent()).addView(empty);
        
        getListView().setEmptyView(empty);
        
        setListAdapter(mAdapter);
        
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
        
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		
		Intent intent = new Intent(this, ScanActivity.class);
		intent.putExtra(ScanActivity.EXTRA_ARTICLE_ID, id);
		intent.putExtra(ScanActivity.EXTRA_FINISH_ON_DELETE, true);
		startActivity(intent);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v, int position,
			long id) {
		
		lastDeleteId = id;
		
		if(mDeleteDialog == null){
			AlertDialog.Builder adb = new Builder(this);
			adb.setTitle(R.string.delete_dialog_title);
			adb.setMessage(R.string.delete_dialog_message);
			adb.setIcon(R.drawable.ic_launcher);
			adb.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDBHelper.deleteItem(lastDeleteId);
					mCursor.requery();
					mAdapter.notifyDataSetChanged();
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
		
		return true;
	}
	
	@Override
	protected void onResume() {
		mCursor.requery();
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mDBHelper.close();
		super.onDestroy();
	}
	
}
