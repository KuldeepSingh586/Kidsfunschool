package com.ksingharawat.kidsfunschool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	ImageButton button;
	public static final String CHOICES = null;
	public static final String REGIONS = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_main);
		addListenerOnButton();
	}
	
	 private void addListenerOnButton() {
			
		 button = (ImageButton) findViewById(R.id.flagButton);
		 
		 
		 button.setOnClickListener(new OnClickListener() {
	          @Override
	          public void onClick(View view) {
	            Toast.makeText(MainActivity.this,"ImageButton Clicked : Flag Button Clicked", 
	            Toast.LENGTH_SHORT).show();
	            setContentView(R.layout.main);
	            Intent intent = new Intent(MainActivity.this, FlagQuizGame.class);
	  		  startActivity(intent);
	           
	          }
	        });		
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
 } // end class MainActivity
    
    
    
