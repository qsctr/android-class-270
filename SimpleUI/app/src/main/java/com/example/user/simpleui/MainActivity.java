package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    boolean submitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        textView.setText(R.string.app_name);
    }

    public void submit(View view) {
        if (!submitted) {
            textView.setText(R.string.submitted_message);
            button.setText(R.string.button_unsubmit);
            submitted = true;
        } else {
            textView.setText(R.string.app_name);
            button.setText(R.string.button_submit);
            submitted = false;
        }
    }
}
