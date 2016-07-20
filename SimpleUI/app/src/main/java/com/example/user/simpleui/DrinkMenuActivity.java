package com.example.user.simpleui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity implements DrinkOrderDialog.OnDrinkOrderListener {

    TextView totalTextView;
    ListView drinkMenuListView;

    String[] names = {"Red tea 1", "Red tea 2", "Latte 1", "Latte 2"};
    int[] mPrices = {25, 35, 45, 35};
    int[] lPrices = {35, 45, 55, 45};
    int[] imageId = {R.drawable.drink1, R.drawable.drink2, R.drawable.drink3, R.drawable.drink4};

    List<Drink> drinks = new ArrayList<>();
    List<DrinkOrder> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("Debug", "DrinkMenuActivity onCreate");

        totalTextView = (TextView) findViewById(R.id.totalTextView);
        drinkMenuListView = (ListView) findViewById(R.id.drinkMenuListView);

        setData();
    }

    private void setData() {
//        for (int i = 0; i < names.length; i++) {
//            Drink drink = new Drink();
//            drink.setName(names[i]);
//            drink.setmPrice(mPrices[i]);
//            drink.setlPrice(lPrices[i]);
//            drink.imageId = imageId[i];
//            drinks.add(drink);
//        }

        Drink.syncDrinksFromRemote(new FindCallback<Drink>() {
            @Override
            public void done(List<Drink> objects, ParseException e) {
                drinks = objects;
                setupDrinkMenuListView();
            }
        });
    }

    private void setupDrinkMenuListView() {
        drinkMenuListView.setAdapter(new DrinkAdapter(this, drinks));

        drinkMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDrinkOrderDialog((Drink) parent.getAdapter().getItem(position));
            }
        });
    }

    public void showDrinkOrderDialog(Drink drink) {
        DrinkOrder drinkOrder = new DrinkOrder(drink);
        for (DrinkOrder order : orders) {
            if (order.drink.getName().equals(drink.getName())) {
                drinkOrder = order;
                break;
            }
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("DrinkOrderDialog");
        if (prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);
        DrinkOrderDialog.newInstance(drinkOrder).show(fragmentTransaction, "DrinkOrderDialog");
    }

    public void updateTotal() {
        int total = 0;
        for (DrinkOrder drinkOrder : orders)
            total += drinkOrder.mNumber * drinkOrder.drink.getmPrice()
                    + drinkOrder.lNumber * drinkOrder.drink.getlPrice();
        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view) {
        Intent intent = new Intent();
        JSONArray jsonArray = new JSONArray();
        for (DrinkOrder drinkOrder : orders)
            jsonArray.put(drinkOrder.toData());
        intent.putExtra("results", jsonArray.toString());
        setResult(RESULT_OK, intent);
        finish();
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

    @Override
    public void onDrinkOrderFinished(DrinkOrder drinkOrder) {
        boolean flag = false;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).drink.getName().equals(drinkOrder.drink.getName())) {
                orders.set(i, drinkOrder);
                flag = true;
                break;
            }
        }
        if (!flag)
            orders.add(drinkOrder);
    }
}
