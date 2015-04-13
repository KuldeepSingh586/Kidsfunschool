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

public class VegetableQuizGame extends Activity 
{
   private static final String TAG = "VegetableQuizGame Activity";  
   private List<String> fileNameList; // Vegetable file names
   private List<String> quizVegetablesList; // countries in current quiz
   private Map<String, Boolean> vegetablesMap; // Created a Map for Vegetable  in current quiz
   private String correctAnswer; // correct country for the current Vegetable
   private int totalGuesses; // number of guesses made
   private int correctAnswers; // number of correct guesses
   private int guessRows; // number of rows displaying guess Buttons
   private Random random;  // used to randomize the quiz
   private Handler handler;// used to delay loading next Vegetable
   private Animation shakeAnimation;// animation for incorrect guess
   
   private TextView answerTextView; // displays Correct! or Incorrect!
   private TextView questionNumberTextView;// shows current question #
   private ImageView vegImageView; // displays a Vegetable
   private TableLayout buttonTableLayout; // displays button table layout
   
   // configures the VegetableQuizGame when its View is created
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState); 
      setContentView(R.layout.main); // to display Main View When Vegetable button clicked

      fileNameList = new ArrayList<String>();//creating array of name list
      quizVegetablesList = new ArrayList<String>(); //creating array list of Vegetables list
      vegetablesMap = new HashMap<String, Boolean>(); //create Hash Map
      guessRows = 2;  //Number of guess rows
      random = new Random(); //Calling Random function
      handler = new Handler(); 
      
   // load the shake animation that's used for incorrect answers
      shakeAnimation = 
         AnimationUtils.loadAnimation(this, R.anim.incorrect_shake); 
      
   // animation repeats 3 times 
      shakeAnimation.setRepeatCount(3); 
      //Storing the vegetableNames into array
      String[] vegetableNames = 
         getResources().getStringArray(R.array.vegetablesList);
      
   // creating a for loop
      for (String region : vegetableNames )
         vegetablesMap.put(region, true);//putting vegetable into hash map
      
   // get references to GUI components
    //getting Question text id and used further to set the the Question TextView 
      questionNumberTextView = 
         (TextView) findViewById(R.id.questionNumberTextView);
      
    //getting Image id and used further to set the the Image on main.xml 
      vegImageView = (ImageView) findViewById(R.id.imageView);
      buttonTableLayout = 
         (TableLayout) findViewById(R.id.buttonTableLayout);
      answerTextView = (TextView) findViewById(R.id.answerTextView);
      
   // set questionNumberTextView's text
      // getResources() class for accessing an application's resources   
      questionNumberTextView.setText(
         getResources().getString(R.string.question) + " 1 " + 
         getResources().getString(R.string.of) + " 10");

      resetQuiz();//call this method to reset the quiz
   } 
   //resetQuiz method to reset the quiz
   private void resetQuiz() 
   {      
	// asset manager of the application (accessible through getAssets())  
      AssetManager assets = getAssets(); 
      fileNameList.clear();//clear the list
      
      try 
      {
         Set<String> vegetable = vegetablesMap.keySet();//Creating a set  regions map

         for (String region : vegetable) //for loop to find out vegetable
         {
            if (vegetablesMap.get(region))
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
      totalGuesses = 0;  //set the correct answer 0 in the beginning 
      quizVegetablesList.clear(); //clear the country list
      
      
      int flagCounter = 1; //set the counter to 1
      int numberOfFlags = fileNameList.size();//get the number of vegetable
      while (flagCounter <= 10)  //if the number of flag is less than or equal to 10 then while loop work
      {
         int randomIndex = random.nextInt(numberOfFlags); //find the random index of vegetable               
         String fileName = fileNameList.get(randomIndex);//get the file name so that it can be set on button  as a name
         if (!quizVegetablesList.contains(fileName)) //check the condition quizcountry list contain file name or not
         {
            quizVegetablesList.add(fileName); // if not add to quizCountriesList 
            ++flagCounter;// and increment the flag counter with 1
         }}
      loadNextFlag();// load the next flag from the assets folder
   } 
   //loadNextFlag()method declared here
	 //method to load the next flag 
   private void loadNextFlag() 
   {
      String nextImageName = quizVegetablesList.remove(0);//Removes the object at the specified location from this List
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
      InputStream stream;
      try
      {
    	  stream = assets.open(region + "/" + nextImageName + ".png");
         
    	//draw the picture into a Bitmap from which you can persist it as raw or compressed pixels.
         Drawable flag = Drawable.createFromStream(stream, nextImageName);
         
       //Set the flag image on Main.xml file
         vegImageView.setImageDrawable(flag);                       
      }
      catch (IOException e)   // Catch the Exception if Error loading the file name
      {
         Log.e(TAG, "Error loading " + nextImageName, e);
      } 
      for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
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
      for (int row = 0; row < guessRows; row++) // for loop, Get the number of flags exists in that floder
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
    //Number of rows randomly
      int row = random.nextInt(guessRows);
    //number of Columns randomly 
      int column = random.nextInt(3); 
      TableRow randomTableRow = getTableRow(row);
      String countryName = getCountryName(correctAnswer);
      ((Button)randomTableRow.getChildAt(column)).setText(countryName);    
   } 
 //Creating getTableRow method
   private TableRow getTableRow(int row)
   {
      return (TableRow) buttonTableLayout.getChildAt(row);//return the TableRow
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

         disableButtons();//Disable the buttons on the main.xml
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
                     resetQuiz();  // rest the quiz                                        
                  }                              
               }
            ); 
            AlertDialog resetDialog = builder.create();//display a dialog box to reset the Quiz or game
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
      {  vegImageView.startAnimation(shakeAnimation);// play shake
         answerTextView.setText(R.string.incorrect_answer);
      // display "Incorrect!" in red 
         answerTextView.setTextColor(
            getResources().getColor(R.color.incorrect_answer));//set the incorrect answer text
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
   private final int vegetable_MENU_ID = Menu.FIRST + 1;

   @Override
   public boolean onCreateOptionsMenu(Menu menu)             
   {            
      super.onCreateOptionsMenu(menu);                        
                                                              
      menu.add(Menu.NONE, CHOICES_MENU_ID, Menu.NONE, R.string.choices);             
      menu.add(Menu.NONE, vegetable_MENU_ID, Menu.NONE, R.string.regions);             
                                                              
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

         case vegetable_MENU_ID:
            final String[] vegetableNames = 
               vegetablesMap.keySet().toArray(new String[vegetablesMap.size()]);
         
            boolean[] vegetableEnabled = new boolean[vegetablesMap.size()];
            for (int i = 0; i < vegetableEnabled.length; ++i)
               vegetableEnabled[i] = vegetablesMap.get(vegetableNames[i]);
            AlertDialog.Builder vegetableBuilder =
               new AlertDialog.Builder(this);
            vegetableBuilder.setTitle(R.string.regions);
            
            String[] displayNames = new String[vegetableNames.length];
            for (int i = 0; i < vegetableNames.length; ++i)
               displayNames[i] = vegetableNames[i].replace('_', ' ');
         
            vegetableBuilder.setMultiChoiceItems( 
               displayNames, vegetableEnabled,
               new DialogInterface.OnMultiChoiceClickListener() 
               {
                  @Override
                  public void onClick(DialogInterface dialog, int which,
                     boolean isChecked) 
                  {
                   vegetablesMap.put(
                        vegetableNames[which].toString(), isChecked);
                  }
               } 
            ); 
          
            vegetableBuilder.setPositiveButton(R.string.reset_quiz,
               new DialogInterface.OnClickListener()
               {
                  @Override
                  public void onClick(DialogInterface dialog, int button)
                  {
                     resetQuiz(); 
                  } 
               } 
            ); 
            AlertDialog vegetableDialog = vegetableBuilder.create();
            vegetableDialog.show();
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