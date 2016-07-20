package com.example.user.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        TextView noteTextView = (TextView) findViewById(R.id.noteTextView);
        TextView menuResultsTextView = (TextView) findViewById(R.id.menuResultsTextView);
        TextView storeInfoTextView = (TextView) findViewById(R.id.storeInfoTextView);

        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);
        String text = "";
        if (menuResultList != null) {
            for (String menuResult : menuResultList) {
                text += menuResult + "\n";
            }
        }
        menuResultsTextView.setText(text);
    }
}
