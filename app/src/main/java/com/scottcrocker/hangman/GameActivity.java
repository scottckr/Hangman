package com.scottcrocker.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public static final String TRIESLEFT_KEY = "TRIESLEFT";
    public static final String WINORLOSS_KEY = "WINORLOSS";
    public static final String WORDWAS_KEY = "WORDWAS";
    private int tries = 10;
    private int[] images = {R.drawable.hang0, R.drawable.hang1, R.drawable.hang2,
            R.drawable.hang3, R.drawable.hang4, R.drawable.hang5, R.drawable.hang6,
            R.drawable.hang7, R.drawable.hang8, R.drawable.hang9, R.drawable.hang10
    };
    private ArrayList<String> wordsList = new ArrayList<>(Arrays.asList("SHEEP", "DREAM"));
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void guessLetter(View view) {
        EditText guessedLetter = (EditText) findViewById(R.id.guess_text);
        TextView lettersGuessed = (TextView) findViewById(R.id.guessed_letters);
        char letter = guessedLetter.getText().toString().toUpperCase().charAt(0);

        if (isLetterAndNotEmpty(letter) && !hasBeenGuessed(letter, lettersGuessed)) {
            tries -= 1;

            ImageView hangmanImage = (ImageView) findViewById(R.id.hangman_image);

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

            hangmanImage.setImageResource(images[tries]);

            guessedLetter.setText("");

            String guessedLetters = lettersGuessed.getText().toString();

            TextView triesLeft = (TextView) findViewById(R.id.tries_left_text);
            String triesRemaining = tries + " försök kvar.";
            triesLeft.setText(triesRemaining);

            String lettersText = guessedLetters + ", " + letter;
            if (tries == 9) {
                String letterAsString = "" + letter;
                lettersGuessed.setText(letterAsString);
            } else {
                lettersGuessed.setText(lettersText);
            }
            if (hasWon || tries == 0) {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra(TRIESLEFT_KEY, tries);
                intent.putExtra(WORDWAS_KEY, secretWord);
                intent.putExtra(WINORLOSS_KEY, hasWon);
                startActivity(intent);
            }
        } else if (!isLetterAndNotEmpty(letter)) {
            //Toast.makeText(this, "Du får endast använda bokstäver!", Toast.LENGTH_SHORT).show();
            guessedLetter.setError("Du får endast använda bokstäver!");
        } else if (hasBeenGuessed(letter, lettersGuessed)) {
            //Toast.makeText(this, "Du har redan gissat på denna bokstav!", Toast.LENGTH_SHORT).show();
            guessedLetter.setError("Du har redan gissat på denna bokstav!");
        }
    }

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

    public boolean isLetterAndNotEmpty(char c) {
        return c != ' ' && ((c >= 65 && c <= 90) || (c >= 97 && c <= 122));
    }

    public boolean hasBeenGuessed(char c, TextView lettersGuessed) {
        for (int i = 0; i < lettersGuessed.getText().toString().length(); i++) {
            if (c == lettersGuessed.getText().toString().charAt(i)) {
                return true;
            }
        }
        return false;
    }
}
