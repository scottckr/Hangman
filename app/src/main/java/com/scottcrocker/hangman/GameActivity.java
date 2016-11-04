package com.scottcrocker.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Activity for playing the game, contains lots of variables and methods for checking if the guessed letter is correct.
 * It also changes the ImageView and number of tries left if guess is not correct.
 */
public class GameActivity extends AppCompatActivity {
    /**
     * Key for intent extra "tries left".
     */
    public static final String TRIESLEFT_KEY = "TRIESLEFT";
    /**
     * Key for intent extra "win or loss".
     */
    public static final String WINORLOSS_KEY = "WINORLOSS";
    /**
     * Key for intent extra "word was".
     */
    public static final String WORDWAS_KEY = "WORDWAS";
    private int tries = 10;
    private int[] images = {R.drawable.hang0, R.drawable.hang1, R.drawable.hang2,
            R.drawable.hang3, R.drawable.hang4, R.drawable.hang5, R.drawable.hang6,
            R.drawable.hang7, R.drawable.hang8, R.drawable.hang9, R.drawable.hang10
    };
    private ArrayList<String> wordsList = new ArrayList<>(Arrays.asList("SHEEP", "DREAM", "WORD",
            "BIKE", "FOOTBALL", "MUSIC", "GUITAR", "APPLE", "BANANA", "PLANET", "KEY", "CAR",
            "TRAIN", "BEER", "WHISKEY", "MONEY", "BIRTHDAY", "CHRISTMAS", "SQUIRREL", "SCHOOL",
            "TABLE", "CHAIR", "WINDOW", "HOUSE", "PANCAKE", "CHURCH", "MOTHER", "FATHER", "BROTHER",
            "SISTER", "COUSIN", "CHINA", "EUROPE", "AMERICA", "AFRICA"));
    private String secretWord;
    private char[] hiddenWord;
    private TextView tvSecretWord;
    private String hiddenWordString;
    private boolean hasWon = false;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        chooseRandomWord();
        watcher((EditText)findViewById(R.id.guess_text), (Button)findViewById(R.id.guess_button));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method for checking if EditText field is empty or not, if it's empty, sets the "submit" button to be disabled.
     * @param guessText An xml element(EditText).
     * @param submit An xml element(Button).
     */
    public void watcher(final EditText guessText, final Button submit) {
        guessText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(guessText.length() == 0) {
                    submit.setEnabled(false); // Disable send button if no text entered
                }
                else {
                    submit.setEnabled(true);  // Otherwise enable
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
        });
        if(guessText.length() == 0) {
            submit.setEnabled(false); // Disable at app start
        }
    }

    /**
     * Main game method, gets content of various XML elements and runs methods to check if the input is correct, if it's a letter and such.
     * Adds already guessed letter to a String variable, which is checked on each new guess to make sure you can't guess on the same letter again.
     * Decrements "tries left" variable if a guessed letter is incorrect.
     * If the tries variable reaches 0 or the player wins, they are sent to a result activity, which displays whether or not they have won or lost.
     * @param view The view component that is executed by click handler.
     */
    public void guessLetter(View view) {
        EditText guessedLetter = (EditText) findViewById(R.id.guess_text);
        TextView lettersGuessed = (TextView) findViewById(R.id.guessed_letters);
        char letter = guessedLetter.getText().toString().toUpperCase().charAt(0);
        ImageView hangmanImage = (ImageView) findViewById(R.id.hangman_image);

        if (isLetter(letter) && !hasBeenGuessed(letter, lettersGuessed)) {
            guessedLetter.setText("");

            String guessedLetters = lettersGuessed.getText().toString();

            String lettersText = guessedLetters + ", " + letter;
            if (lettersGuessed.length() == 0) {
                String letterAsString = "" + letter;
                lettersGuessed.setText(letterAsString);
            } else {
                lettersGuessed.setText(lettersText);
            }
            if (!isCorrect(letter)) {
                tries -= 1;
                TextView triesLeft = (TextView) findViewById(R.id.tries_left_text);
                String triesRemaining = tries + " " + getString(R.string.tries_left);
                triesLeft.setText(triesRemaining);
                hangmanImage.setImageResource(images[tries]);
            } else {
                for (int i = 0; i < secretWord.length(); i++) {
                    if (letter == secretWord.charAt(i)) {
                        hiddenWord[i] = secretWord.charAt(i);
                    }
                }

                hiddenWordString = new String(hiddenWord);

                tvSecretWord.setText(hiddenWordString);

                if (hiddenWordString.indexOf('-') == -1) {
                    hasWon = true;
                }
            }
            if (hasWon || tries == 0) {
                finish();
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra(TRIESLEFT_KEY, tries);
                intent.putExtra(WORDWAS_KEY, secretWord);
                intent.putExtra(WINORLOSS_KEY, hasWon);
                startActivity(intent);
            }
        } else if (!isLetter(letter)) {
            guessedLetter.setError(getString(R.string.only_letters_allowed));
        } else if (hasBeenGuessed(letter, lettersGuessed)) {
            guessedLetter.setError(getString(R.string.already_guessed_letter));
        }
    }

    /**
     * Chooses a random word from the wordsList variable.
     * Also replaces all letters of the word with slashes(-).
     */
    public void chooseRandomWord() {
        int randomNum = rand.nextInt(wordsList.size());
        tvSecretWord = (TextView)findViewById(R.id.secret_word);
        secretWord = wordsList.get(randomNum);
        hiddenWord = new char[secretWord.length()];
        hiddenWordString = "";
        for (int i = 0; i < secretWord.length(); i++) {
            hiddenWord[i] = '-';
        }

        hiddenWordString = new String(hiddenWord);

        tvSecretWord.setText(hiddenWordString);
    }

    /**
     * Checks if a character is part of the secret word or not..
     * @param c Inputs a character to be run through the method.
     * @return Returns true if input character is present in the secretWord variable.
     */
    public boolean isCorrect(char c) {
        boolean correct = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (c == secretWord.charAt(i)) {
                correct = true;
            }
        }
        if (correct) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if input character is a letter.
     * @param c Inputs a character to be run through the method.
     * @return Returns true if the input character is a letter.
     */
    public boolean isLetter(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    /**
     * Checks if input character is part of the lettersGuessed variable, which stores already guessed letters.
     * @param c Inputs a character to be run through the method.
     * @param lettersGuessed A TextView that contains already guessed letters.
     * @return Returns true if the character has already been guessed.
     */
    public boolean hasBeenGuessed(char c, TextView lettersGuessed) {
        for (int i = 0; i < lettersGuessed.getText().toString().length(); i++) {
            if (c == lettersGuessed.getText().toString().charAt(i)) {
                return true;
            }
        }
        return false;
    }
}
