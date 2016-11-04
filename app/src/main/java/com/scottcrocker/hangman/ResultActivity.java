package com.scottcrocker.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.scottcrocker.hangman.GameActivity.TRIESLEFT_KEY;
import static com.scottcrocker.hangman.GameActivity.WINORLOSS_KEY;
import static com.scottcrocker.hangman.GameActivity.WORDWAS_KEY;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView resultText = (TextView)findViewById(R.id.result_text);
        boolean hasWon = getIntent().getBooleanExtra(WINORLOSS_KEY, false);
        if (hasWon) {
            resultText.setText(R.string.result_won_text);
        } else {
            resultText.setText(R.string.result_lost_text);
        }
        String word = getIntent().getStringExtra(WORDWAS_KEY);
        TextView wordWasText = (TextView)findViewById(R.id.result_word_was);
        String wordWas = "Ordet var: " + word;
        wordWasText.setText(wordWas);
        int tries = getIntent().getIntExtra(TRIESLEFT_KEY, 0);
        TextView triesLeftText = (TextView)findViewById(R.id.result_tries_remain);
        String triesLeft = "";
        if (hasWon) {
            triesLeft = "Antal försök kvar: " + tries;
        }
        triesLeftText.setText(triesLeft);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
