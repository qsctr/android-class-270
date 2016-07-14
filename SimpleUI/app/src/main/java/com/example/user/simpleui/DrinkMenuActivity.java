package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class DrinkMenuActivity extends AppCompatActivity {

    TextView totalTextView;
    ListView drinkMenuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("Debug", "DrinkMenuActivity onCreate");

        totalTextView = (TextView) findViewById(R.id.totalTextView);
        drinkMenuListView = (ListView) findViewById(R.id.drinkMenuListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Debug", "DrinkMenuActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "DrinkMenuActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debug", "DrinkMenuActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Debug", "DrinkMenuActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Debug", "DrinkMenuActivity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Debug", "DrinkMenuActivity onRestart");
    }
}
