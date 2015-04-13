// FlagQuizFragment.java
// Contains the Flag Quiz logic

package com.ksingharawat.kidsfunschool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FlagQuizGame extends Activity {
    
	// String used when logging error messages
	  private static final String TAG = "FlagQuizGame Activity";  
	   private List<String> fileNameList; // flag file names
	   private List<String> quizCountriesList; // countries in current quiz
	   private Map<String, Boolean> regionsMap;  // world regions in current quiz
	   private String correctAnswer; // correct country for the current flag
	   private int totalGuesses; // number of guesses made
	   private int correctAnswers; // number of correct guesses
	   private int guessRows; // number of rows displaying guess Buttons
	   private Random random;  // used to randomize the quiz
	   private Handler handler;// used to delay loading next flag
	   private Animation shakeAnimation;// animation for incorrect guess
	   
	   private TextView answerTextView;// displays Correct! or Incorrect!
	   private TextView questionNumberTextView;// shows current question #
	   private ImageView flagImageView; // displays a flag
	   private TableLayout buttonTableLayout; // displays button table layout
	   
	   // configures the FlagQuizFragment when its View is created
	   @Override
	   public void onCreate(Bundle savedInstanceState) 
	   {
	      super.onCreate(savedInstanceState); 
	      setContentView(R.layout.main); 

	      fileNameList = new ArrayList<String>();//creating array of name list
	      quizCountriesList = new ArrayList<String>(); //creating array list of countries list
	      regionsMap = new HashMap<String, Boolean>(); //create Hash Map
	      guessRows = 2; //Number of guess rows
	      random = new Random(); //Calling Random function
	      handler = new Handler(); 
	      
	      // load the shake animation that's used for incorrect answers
	      shakeAnimation = 
	         AnimationUtils.loadAnimation(this, R.anim.incorrect_shake); 
	      shakeAnimation.setRepeatCount(3); // animation repeats 3 times 
	      String[] regionNames = 
	         getResources().getStringArray(R.array.regionsList);
	      for (String region : regionNames )// creating a for loop
	         regionsMap.put(region, true);//putting regions into hash map
	      // get references to GUI components
	      questionNumberTextView = 
	         (TextView) findViewById(R.id.questionNumberTextView);//getting Question text id and used further to set the the Question TextView 
	      flagImageView = (ImageView) findViewById(R.id.imageView);//getting Image id and used further to set the the Image on main.xml 
	      buttonTableLayout = 
	         (TableLayout) findViewById(R.id.buttonTableLayout);
	      answerTextView = (TextView) findViewById(R.id.answerTextView);
	   // set questionNumberTextView's text
	      // getResources() class for accessing an application's resources   
	      questionNumberTextView.setText(
	         getResources().getString(R.string.question) + " 1 " + 
	         getResources().getString(R.string.of) + " 10"); //display 1 to 10 Question on Main.xml at the top

	      resetQuiz(); //cal this method to reset the quiz
	   } 
	   
	   //resetQuiz method to reset the quiz
	   private void resetQuiz() 
	   {      
		  
	      AssetManager assets = getAssets(); // asset manager of the application (accessible through getAssets()) 
	      fileNameList.clear(); //clear the list
	      
	      try 
	      {
	         Set<String> regions = regionsMap.keySet();//Creating a set og regions map

	         for (String region : regions) //for loop to find out regions
	         {
	            if (regionsMap.get(region))
	            {               String[] paths = assets.list(region);//store the list into path 

	               for (String path : paths) 
	                  fileNameList.add(path.replace(".png", ""));//find the .png in the file name and replace it
	            }
	         }
	      } 
	      catch (IOException e) //catch the exception if there is an error loading image file names
	      {
	         Log.e(TAG, "Error loading image file names", e);
	      } 
	      
	      correctAnswers = 0; //set the correct answer 0 in the beginning 
	      totalGuesses = 0; //set the correct answer 0 in the beginning 
	      quizCountriesList.clear(); //clear the country list
	      
	      int flagCounter = 1; //set the counter to 1
	      int numberOfFlags = fileNameList.size();//get the number of flag
	      while (flagCounter <= 10) //if the number of flag is less than or equal to 10 then while loop work
	      {
	         int randomIndex = random.nextInt(numberOfFlags); //find the random index of flag         
	         String fileName = fileNameList.get(randomIndex);//get the file name so that it can be set on button  as a name
	         if (!quizCountriesList.contains(fileName)) //check the condition quizcountry list contain file name or not
	         {
	            quizCountriesList.add(fileName); // if not add to quizCountriesList 
	            ++flagCounter;// and increment the flag counter with 1
	         }}
	      loadNextFlag();// load the next flag from the assets folder
	   } 
	   //loadNextFlag()method declared here
	 //method to load the next flag 
	   private void loadNextFlag() 
	   {
	      String nextImageName = quizCountriesList.remove(0);//Removes the object at the specified location from this List
	      correctAnswer = nextImageName;//correct answer stored into the correctAnswer object

	      answerTextView.setText("");  
	   // set questionNumberTextView's text
	      questionNumberTextView.setText(
	         getResources().getString(R.string.question) + " " + 
	         (correctAnswers + 1) + " " + 
	         getResources().getString(R.string.of) + " 10");//set the Question textView(questionNumberTextView)
	      
	    //getting the name of region, used substring method to find the index of (-) and break it 
	     //store the name of region from substring at index of 0
	      //for example if flag name is Africa-Djibouti then it break it to the Africa and Djibouti
	      //store that value of index 0 to String region
	      String region = 
	         nextImageName.substring(0, nextImageName.indexOf('-'));
	      
	      //Retrieve underlying AssetManager storage for these resources.
	      AssetManager assets = getAssets(); // get app's AssetManager
	      
	      //InputStream will use input streams that read data from the file system
	      InputStream stream;//creating the object of input stream 
	      try
	      {
	    	  stream = assets.open(region + "/" + nextImageName + ".png");//open the assets folder 
	    	  
	         //draw the picture into a Bitmap from which you can persist it as raw or compressed pixels.
	         Drawable flag = Drawable.createFromStream(stream, nextImageName);
	         
	         //Set the flag image on Main.xml file
	         flagImageView.setImageDrawable(flag);                       
	      }
	      catch (IOException e)  // Catch the Exception if Error loading the file name
	      {
	         Log.e(TAG, "Error loading " + nextImageName, e);//Output can be seen at console or logCat
	      } 
	      for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)// for loop, Get the number of flags exists in that floder
	         ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();
	      
          //Moves every element of the list to a random new position 
	      //in the list using the specified random number generator.
	      Collections.shuffle(fileNameList); 
	      
	      //Get the correct Answer index and store the name in correct variable 
	      int correct = fileNameList.indexOf(correctAnswer);
	      //add the file name into file name list
	      fileNameList.add(fileNameList.remove(correct));
	      
	      //Use with getSystemService(String) to retrieve a LayoutInflater for inflating layout resources in this context.
	      LayoutInflater inflater = (LayoutInflater) getSystemService(
	         Context.LAYOUT_INFLATER_SERVICE);

	      //for loop
	      for (int row = 0; row < guessRows; row++) 
	      {
	         TableRow currentTableRow = getTableRow(row);

	         for (int column = 0; column < 3; column++) 
	         {
	            Button newGuessButton = 
	               (Button) inflater.inflate(R.layout.guess_button, null);
	            String fileName = fileNameList.get((row * 3) + column);
	            newGuessButton.setText(getCountryName(fileName));
	            newGuessButton.setOnClickListener(guessButtonListener);
	            currentTableRow.addView(newGuessButton);
	         } 
	      } 
	      int row = random.nextInt(guessRows);//Number of rows randomly
	      int column = random.nextInt(3); //number of Columns randomly 
	      TableRow randomTableRow = getTableRow(row);
	      String countryName = getCountryName(correctAnswer);
	      ((Button)randomTableRow.getChildAt(column)).setText(countryName);    
	   } 
	   
	   //Creating getTableRow method
	   private TableRow getTableRow(int row) 
	   {
	      return (TableRow) buttonTableLayout.getChildAt(row); //return the TableRow
	   } 
	   //Creating Method getCountryName and 
	   //Take one String argument
	   //return the String name
	   private String getCountryName(String name)
	   {
	      return name.substring(name.indexOf('-') + 1).replace('_', ' ');
	   }
	   
	 //comments for submitGuess() Method 
	   /**
	    * submitGuess() Method 
	    * accept one argument of type button
	    * Get the text from guess button
	    * if the guess button equal to answer 
	    *  then Set the color to BLACK
	    *  Otherwise set to RED
	    * @param guessButton
	    */
	   private void submitGuess(Button guessButton) 
	   {
	      String guess = guessButton.getText().toString();
	      String answer = getCountryName(correctAnswer);
	      ++totalGuesses; 
	      if (guess.equals(answer)) 
	      {
	         ++correctAnswers;
	         answerTextView.setText(answer + "!");
	         answerTextView.setTextColor(
	            getResources().getColor(R.color.correct_answer));

	         disableButtons(); //Disable the buttons on the main.xml
	         if (correctAnswers == 10) 
	         {
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);

	            builder.setTitle(R.string.reset_quiz); 
	            
	            builder.setMessage(String.format("%d %s, %.02f%% %s", 
	               totalGuesses, getResources().getString(R.string.guesses), 
	               (1000 / (double) totalGuesses), 
	               getResources().getString(R.string.correct)));

	            builder.setCancelable(false); 
	            builder.setPositiveButton(R.string.reset_quiz,
	               new DialogInterface.OnClickListener()                
	               {                                                       
	                  public void onClick(DialogInterface dialog, int id) 
	                  {
	                     resetQuiz();// rest the quiz                               
	                  }                              
	               }
	            ); 
	            AlertDialog resetDialog = builder.create(); //display a dialog box to reset the Quiz or game
	            resetDialog.show();
	         } 
	         else
	         {  handler.postDelayed(
	               new Runnable()
	               { 
	                  @Override
	                  public void run()
	                  {
	                     loadNextFlag();
	                  }
	               }, 1000); 
	         }
	      } 
	      else  
	      {  flagImageView.startAnimation(shakeAnimation); // play shake
	         answerTextView.setText(R.string.incorrect_answer); //set the incorrect answer text
	         
	      // display "Incorrect!" in red 
	         answerTextView.setTextColor(
	            getResources().getColor(R.color.incorrect_answer));
	         guessButton.setEnabled(false);
	      } 
	   } 
	  
	   
	   /**
	    * utility method that disables all answer Buttons 
	    */
	   private void disableButtons()
	   {
	      for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
	      {
	         TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
	         for (int i = 0; i < tableRow.getChildCount(); ++i)
	            tableRow.getChildAt(i).setEnabled(false);
	      } 
	   } 
	   private final int CHOICES_MENU_ID = Menu.FIRST;
	   private final int REGIONS_MENU_ID = Menu.FIRST + 1;

	   @Override
	   public boolean onCreateOptionsMenu(Menu menu)             
	   {            
	      super.onCreateOptionsMenu(menu);                        
	                                                              
	      menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);             
	      menu.add(Menu.NONE, REGIONS_MENU_ID, Menu.NONE, R.string.regions);             
	                                                              
	      return true; 
	   }
	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) 
	      {
	         case CHOICES_MENU_ID:
	            final String[] possibleChoices = 
	               getResources().getStringArray(R.array.guessesList);

	            AlertDialog.Builder choicesBuilder = 
	               new AlertDialog.Builder(this);         
	            choicesBuilder.setTitle(R.string.choices);
	         
	            choicesBuilder.setItems(R.array.guessesList,                    
	               new DialogInterface.OnClickListener()                    
	               {                                                        
	                  public void onClick(DialogInterface dialog, int item) 
	                  {                                                     
	                     guessRows = Integer.parseInt(                      
	                        possibleChoices[item].toString()) / 3;          
	                     resetQuiz();                      
	                  }                               
	               } 
	            );                               
	            AlertDialog choicesDialog = choicesBuilder.create();
	            choicesDialog.show();          
	            return true; 

	         case REGIONS_MENU_ID:
	            final String[] regionNames = 
	               regionsMap.keySet().toArray(new String[regionsMap.size()]);
	         
	            boolean[] regionsEnabled = new boolean[regionsMap.size()];
	            for (int i = 0; i < regionsEnabled.length; ++i)
	               regionsEnabled[i] = regionsMap.get(regionNames[i]);
	            AlertDialog.Builder regionsBuilder =
	               new AlertDialog.Builder(this);
	            regionsBuilder.setTitle(R.string.regions);
	            
	            String[] displayNames = new String[regionNames.length];
	            for (int i = 0; i < regionNames.length; ++i)
	               displayNames[i] = regionNames[i].replace('_', ' ');
	         
	            regionsBuilder.setMultiChoiceItems( 
	               displayNames, regionsEnabled,
	               new DialogInterface.OnMultiChoiceClickListener() 
	               {
	                  @Override
	                  public void onClick(DialogInterface dialog, int which,
	                     boolean isChecked) 
	                  {
	                   regionsMap.put(
	                        regionNames[which].toString(), isChecked);
	                  }
	               } 
	            ); 
	          
	            regionsBuilder.setPositiveButton(R.string.reset_quiz,
	               new DialogInterface.OnClickListener()
	               {
	                  @Override
	                  public void onClick(DialogInterface dialog, int button)
	                  {
	                     resetQuiz(); 
	                  } 
	               } 
	            ); 
	            AlertDialog regionsDialog = regionsBuilder.create();
	            regionsDialog.show();
	            return true;
	      } 

	      return super.onOptionsItemSelected(item);
	   } 
	   private OnClickListener guessButtonListener = new OnClickListener() 
	   {
	      @Override
	      public void onClick(View v) 
	      {
	         submitGuess((Button) v); 
	      }
	   }; 
} // end class FlagQuiz
