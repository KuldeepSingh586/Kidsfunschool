package com.ksingharawat.kidsfunschool;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	// keys for reading data from SharedPreferences
	   public static final String CHOICES = "pref_numberOfChoices";
	   public static final String REGIONS = "pref_regionsToInclude";

	   private boolean phoneDevice = true; // used to force portrait mode
	   private boolean preferencesChanged = true; // did preferences change?

	ImageButton buttonFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // register listener for SharedPreferences changes
        PreferenceManager.getDefaultSharedPreferences(this).
           registerOnSharedPreferenceChangeListener(
              preferenceChangeListener);

        // determine screen size 
        int screenSize = getResources().getConfiguration().screenLayout &
           Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
           screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE )
           phoneDevice = false; // not a phone-sized device
           
        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice) 
           setRequestedOrientation(
              ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        
    }
    
    private void addListenerOnButton() {
		
    	buttonFlag = (ImageButton) findViewById
        (R.id.flagButton);

    	buttonFlag.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            Toast.makeText(MainActivity.this,"ImageButton Clicked : Flag Button Clicked", 
            Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main_flag);
            Start();
          }
        });		
	}
    
    protected void Start()
    {
       super.onStart();
       
       if (preferencesChanged) 
       {
          // now that the default preferences have been set,  
          // initialize QuizFragment and start the quiz
          QuizFragment quizFragment = (QuizFragment)
             getFragmentManager().findFragmentById(R.id.quizFragment);
          quizFragment.updateGuessRows(
             PreferenceManager.getDefaultSharedPreferences(this));
          quizFragment.updateRegions(
             PreferenceManager.getDefaultSharedPreferences(this));
          quizFragment.resetQuiz();
          preferencesChanged = false; 
       }
    } // end method onStart

    // show menu if app is running on a phone or a portrait-oriented tablet
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       // get the default Display object representing the screen
       Display display = ((WindowManager) 
          getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
       Point screenSize = new Point(); // used to store screen size
       display.getRealSize(screenSize); // store size in screenSize
       
       // display the app's menu only in portrait orientation
       if (screenSize.x < screenSize.y) // x is width, y is height
       {
          getMenuInflater().inflate(R.menu.main, menu); // inflate the menu      
          return true;
       }
       else
          return false;
    } // end method onCreateOptionsMenu

    // displays SettingsActivity when running on a phone
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       Intent preferencesIntent = new Intent(this, SettingsActivity.class);
       startActivity(preferencesIntent); 
       return super.onOptionsItemSelected(item);
    } 

    // listener for changes to the app's SharedPreferences
    private OnSharedPreferenceChangeListener preferenceChangeListener = 
       new OnSharedPreferenceChangeListener()
    {
       // called when the user changes the app's preferences
       @Override
       public void onSharedPreferenceChanged(
          SharedPreferences sharedPreferences, String key)
       {
          preferencesChanged = true; // user changed app settings
          
          QuizFragment quizFragment = (QuizFragment)
             getFragmentManager().findFragmentById(R.id.quizFragment);
          
          if (key.equals(CHOICES)) // # of choices to display changed   
          {   
             quizFragment.updateGuessRows(sharedPreferences);
             quizFragment.resetQuiz(); 
          }
          else if (key.equals(REGIONS)) // regions to include changed   
          {
             Set<String> regions = 
                sharedPreferences.getStringSet(REGIONS, null);
             
             if (regions != null && regions.size() > 0)
             {
                quizFragment.updateRegions(sharedPreferences);
                quizFragment.resetQuiz();
             }
             else // must select one region--set North America as default
             {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                regions.add(
                   getResources().getString(R.string.default_region));
                editor.putStringSet(REGIONS, regions);
                editor.commit();
                Toast.makeText(MainActivity.this, 
                   R.string.default_region_message, 
                   Toast.LENGTH_SHORT).show();
             }
          }

          Toast.makeText(MainActivity.this, 
             R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
       } // end method onSharedPreferenceChanged
    }; // end anonymous inner class    
 } // end class MainActivity
    
    
    
