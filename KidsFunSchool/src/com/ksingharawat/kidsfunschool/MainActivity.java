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
	 ImageButton button1;
	 ImageButton button2;
	 ImageButton button3;
	 ImageButton button4;
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
		 button1 = (ImageButton) findViewById(R.id.animalButton);
		 button2 = (ImageButton) findViewById(R.id.fruitsButton);
		 button3 = (ImageButton) findViewById(R.id.vegetablesButton);
		 button4 = (ImageButton) findViewById(R.id.monumentButton);
		 
		 
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
		 button1.setOnClickListener(new OnClickListener() {
	          @Override
	          public void onClick(View view) {
	            Toast.makeText(MainActivity.this,"ImageButton Clicked : Animal Button Clicked", 
	            Toast.LENGTH_SHORT).show();
	            setContentView(R.layout.main);
	            Intent intent = new Intent(MainActivity.this, AnimalQuizGame.class);
	  		  startActivity(intent);
	           
	          }
	        });	
		 button2.setOnClickListener(new OnClickListener() {
	          @Override
	          public void onClick(View view) {
	            Toast.makeText(MainActivity.this,"ImageButton Clicked : Fruit Button Clicked", 
	            Toast.LENGTH_SHORT).show();
	            setContentView(R.layout.main);
	            Intent intent = new Intent(MainActivity.this, FruitQuizGame.class);
	  		  startActivity(intent);
	           
	          }
	        });	
		 button4.setOnClickListener(new OnClickListener() {
	          @Override
	          public void onClick(View view) {
	            Toast.makeText(MainActivity.this,"ImageButton Clicked : Monument Button Clicked", 
	            Toast.LENGTH_SHORT).show();
	            setContentView(R.layout.main);
	            Intent intent = new Intent(MainActivity.this, MonumentQuizGame.class);
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
    
    
    
