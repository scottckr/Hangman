package com.scottcrocker.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;

/**
 * A simple menu for displaying two buttons which leads to either the game activity or an about activity.
 */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_menu_text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a new intent and starts the GameActivity class.
     * @param view The view component that is executed by click handler.
     */
    public void playGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);
    }

    /**
     * Creates a new intent and starts the AboutActivity class.
     * @param view The view component that is executed by click handler.
     */
    public void aboutInfo(View view) {
        Intent intent = new Intent(this, AboutActivity.class);

        startActivity(intent);
    }
}
