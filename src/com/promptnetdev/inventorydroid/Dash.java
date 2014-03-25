package com.promptnetdev.inventorydroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Dash extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dash);
        Button addButton = (Button) findViewById(R.id.addMenuButton);
        addButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dash.this, AddItem.class));
            }
        });
    }
}
