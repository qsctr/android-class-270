package com.example.user.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;

    TextView textView;
    Button button;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectedTea = "Black tea";

    String menuResults = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        textView.setText(R.string.app_name);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editText.setText(sharedPreferences.getString("editText", ""));
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editor.putString("editText", editText.getText().toString());
                editor.commit();
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedTea = ((RadioButton) findViewById(checkedId)).getText().toString();
            }
        });

//        for (String data : Utils.readFile(this, "history").split("\n")) {
//            Order order = Order.newInstanceWithData(data);
//            if (order != null)
//                orders.add(order);
//        }

        setupListView();

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.storeInfo)));

        spinner.setSelection(sharedPreferences.getInt("spinner", 0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("spinner", spinner.getSelectedItemPosition());
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.d("Debug", "MainActivity onCreate");
    }

    public void setupListView() {

        FindCallback<Order> callback = new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {
                    orders = objects;
                    listView.setAdapter(new OrderAdapter(MainActivity.this, orders));
                }
            }
        };

        NetworkInfo networkInfo =
                ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            Order.getQuery().fromLocalDatastore().findInBackground(callback);
        } else {
            Order.getOrdersFromRemote(callback);
        }
    }

    public void submit(View view) {
        String text = editText.getText().toString();

        textView.setText(text);

        Order order = new Order();
        order.setNote(text);
        order.setMenuResults(menuResults);
        order.setStoreInfo((String) spinner.getSelectedItem());

        order.pinInBackground("Order");
        order.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                setupListView();
            }
        });

        orders.add(order);

        Utils.writeFile(this, "history", order.toData() + "\n");

        editText.setText("");
        menuResults = "";
    }

    public void goToMenu(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY && resultCode == RESULT_OK) {
            Toast.makeText(this, "Order sent", Toast.LENGTH_SHORT).show();
            menuResults = data.getStringExtra("results");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Debug", "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debug", "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Debug", "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Debug", "MainActivity onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Debug", "MainActivity onRestart");
    }
}
