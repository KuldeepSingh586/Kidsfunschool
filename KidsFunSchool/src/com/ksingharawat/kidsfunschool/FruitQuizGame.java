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

public class FruitQuizGame extends Activity 
{
	 // String used when logging error messages
   private static final String TAG = "FruitQuizGame Activity";  
   private List<String> fileNameList; // flag file names
   private List<String> quizFruitList;
   private Map<String, Boolean> regionsMap; 
   private String correctAnswer;    // correct fruit for the current fruit
   private int totalGuesses; // number of guesses made
   private int correctAnswers; // number of correct guesses
   private int guessRows;   // correct country for the current fruit
   private Random random;   // used to randomize the quiz
   private Handler handler;    // used to delay loading next fruit
   private Animation shakeAnimation;  // animation for incorrect guess
   
   private TextView answerTextView;   // displays Correct! or Incorrect!
   private TextView questionNumberTextView;  // shows current question #
   private ImageView fruitImageView;     // displays a fruit
   private TableLayout buttonTableLayout;    //
   
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState); 
      setContentView(R.layout.main); 

      fileNameList = new ArrayList<String>();
      quizFruitList = new ArrayList<String>(); 
      regionsMap = new HashMap<String, Boolean>(); 
      guessRows = 2; 
      random = new Random(); 
      handler = new Handler(); 
      shakeAnimation = 
         AnimationUtils.loadAnimation(this, R.anim.incorrect_shake); 
      shakeAnimation.setRepeatCount(3); String[] regionNames = 
         getResources().getStringArray(R.array.fruitsList);
      for (String region : regionNames )
         regionsMap.put(region, true);
      questionNumberTextView = 
         (TextView) findViewById(R.id.questionNumberTextView);
      fruitImageView = (ImageView) findViewById(R.id.imageView);
      buttonTableLayout = 
         (TableLayout) findViewById(R.id.buttonTableLayout);
      answerTextView = (TextView) findViewById(R.id.answerTextView);
      questionNumberTextView.setText(
         getResources().getString(R.string.question) + " 1 " + 
         getResources().getString(R.string.of) + " 10");

      resetQuiz();
   } 
   
   // set up and start the next quiz 
   private void resetQuiz() 
   {      
	   // use AssetManager to get image file names for enabled regions
      AssetManager assets = getAssets(); 
      fileNameList.clear();   // empty list of image file names
      
      try 
      {
         Set<String> regions = regionsMap.keySet();

         // loop through each region
         for (String region : regions) 
         {
            if (regionsMap.get(region))
            	 // get a list of all fruit image files in this region
            {               String[] paths = assets.list(region);

               for (String path : paths) 
                  fileNameList.add(path.replace(".png", ""));
            }
         }
      } 
      catch (IOException e) 
      {
         Log.e(TAG, "Error loading image file names", e);
      } 
      
      correctAnswers = 0;    // reset the number of correct answers made
      totalGuesses = 0;    // reset the total number of guesses the user made
      quizFruitList.clear();   // clear prior list of quiz animal
      
      int flagCounter = 1; 
      int numberOfFlags = fileNameList.size();
      // add FRUIT_IN_QUIZ random file names to the quizCountriesList
      while (flagCounter <= 10) 
      {
         int randomIndex = random.nextInt(numberOfFlags);          
         String fileName = fileNameList.get(randomIndex);
      // if the region is enabled and it hasn't already been chosen
         if (!quizFruitList.contains(fileName)) 
         {
        	 quizFruitList.add(fileName); // add the file to the list
            ++flagCounter;
         }}
      loadNextFruit();// start the quiz by loading the first fruit
   } // end method resetQuiz
   
   
   // after the quiz is finished it will take the player to the 1st page
   private void loadNextFruit() 
   {
	   // get file name of the next fruit and remove it from the list
      String nextImageName = quizFruitList.remove(0);
      correctAnswer = nextImageName; // update the correct answer

      answerTextView.setText("");   // update the correct answer
      
   // display current question number
      questionNumberTextView.setText(
         getResources().getString(R.string.question) + " " + 
         (correctAnswers + 1) + " " + 
         getResources().getString(R.string.of) + " 10");
      
   // extract the region from the next image's name
      String region = 
         nextImageName.substring(0, nextImageName.indexOf('-'));
      AssetManager assets = getAssets(); // get app's AssetManager
      InputStream stream;
      try
      {
    	  // get an Stream to the asset representing the next animal
    	  stream = assets.open(region + "/" + nextImageName + ".png");
         
    	  // load the asset as a Drawable and display on the animalImageView
         Drawable flag = Drawable.createFromStream(stream, nextImageName);
         fruitImageView.setImageDrawable(flag);                       
      }
      catch (IOException e)  
      {
         Log.e(TAG, "Error loading " + nextImageName, e);
      } 
      for (int row = 0; row < buttonTableLayout.getChildCount(); ++row)
         ((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();

      Collections.shuffle(fileNameList);  // shuffle file names
      
      // put the correct answer at the end of fileNameList
      int correct = fileNameList.indexOf(correctAnswer);
      fileNameList.add(fileNameList.remove(correct));

      LayoutInflater inflater = (LayoutInflater) getSystemService(
         Context.LAYOUT_INFLATER_SERVICE);

      // add 3, 6, or 9 guess Buttons based on the value of guessRows
      for (int row = 0; row < guessRows; row++) 
      {
         TableRow currentTableRow = getTableRow(row);

      // place Buttons in currentTableRow
         for (int column = 0; column < 3; column++) 
         {
        	 // get reference to Button to configure
            Button newGuessButton = 
               (Button) inflater.inflate(R.layout.guess_button, null);
            
         // get country name and set it as newGuessButton's text
            String fileName = fileNameList.get((row * 3) + column);
            newGuessButton.setText(getCountryName(fileName));
            newGuessButton.setOnClickListener(guessButtonListener);
            currentTableRow.addView(newGuessButton);
         } 
      } 
   // randomly replace one Button with the correct answer
      int row = random.nextInt(guessRows);   // pick random row
      int column = random.nextInt(3); 	 // pick random column
      TableRow randomTableRow = getTableRow(row);   // get the row
      String countryName = getCountryName(correctAnswer);
      ((Button)randomTableRow.getChildAt(column)).setText(countryName);    
   } 
   
   // parses the fruit file name and returns the fruit name
   private TableRow getTableRow(int row)
   {
      return (TableRow) buttonTableLayout.getChildAt(row);
   } 
   private String getCountryName(String name)
   {
      return name.substring(name.indexOf('-') + 1).replace('_', ' ');
   }
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

         disableButtons();
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
                     resetQuiz();                                      
                  }                              
               }
            ); 
            AlertDialog resetDialog = builder.create();
            resetDialog.show();
         } 
         else
         {  handler.postDelayed(
               new Runnable()
               { 
                  @Override
                  public void run()
                  {
                	  loadNextFruit();
                  }
               }, 1000); 
         }
      } 
      else    // guess was incorrect  
      {  fruitImageView.startAnimation(shakeAnimation);
      
      // display "Incorrect!" in red 
         answerTextView.setText(R.string.incorrect_answer);
         answerTextView.setTextColor(
            getResources().getColor(R.color.incorrect_answer));
         guessButton.setEnabled(false);	// disable incorrect answer
      } 
   } 

   // utility method that disables all answer Buttons 
   
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
} // end class AnimalQuizGame