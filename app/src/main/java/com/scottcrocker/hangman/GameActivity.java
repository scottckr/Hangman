package com.scottcrocker.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private int tries = 10;

    int[] images = {R.drawable.hang0, R.drawable.hang1, R.drawable.hang2,
            R.drawable.hang3, R.drawable.hang4, R.drawable.hang5, R.drawable.hang6,
            R.drawable.hang7, R.drawable.hang8, R.drawable.hang9, R.drawable.hang10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void guessLetter(View view) {
        tries -= 1;

        ImageView hangmanImage = (ImageView)findViewById(R.id.hangman_image);

        hangmanImage.setImageResource(images[10]);

        EditText guessedLetter = (EditText)findViewById(R.id.guess_text);
        String letter = guessedLetter.getText().toString();
        guessedLetter.setText("");

        TextView lettersGuessed = (TextView)findViewById(R.id.guessed_letters);
        String guessedLetters = lettersGuessed.getText().toString();

        TextView triesLeft = (TextView)findViewById(R.id.tries_left_text);
        String triesRemaining = tries + " försök kvar.";
        triesLeft.setText(triesRemaining);

        String lettersText = guessedLetters + letter;

        lettersGuessed.setText(lettersText);
    }
}
