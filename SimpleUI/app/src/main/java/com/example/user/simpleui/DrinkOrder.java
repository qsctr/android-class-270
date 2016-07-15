package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

public class DrinkOrder {

    Drink drink;
    int lNumber;
    int mNumber;
    String sugar = "Normal";
    String ice = "Normal";
    String note = "";

    public DrinkOrder(Drink drink) {
        this.drink = drink;
    }

    public String toData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("drink", drink.getJSON().toString())
                    .put("lNumber", lNumber)
                    .put("mNumber", mNumber)
                    .put("sugar", sugar)
                    .put("ice", ice)
                    .put("note", note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static DrinkOrder newInstanceWithData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            DrinkOrder drinkOrder =
                    new DrinkOrder(Drink.newInstanceWithData(jsonObject.getString("drink")));
            drinkOrder.lNumber = jsonObject.getInt("lNumber");
            drinkOrder.mNumber = jsonObject.getInt("mNumber");
            drinkOrder.sugar = jsonObject.getString("sugar");
            drinkOrder.ice = jsonObject.getString("ice");
            drinkOrder.note = jsonObject.getString("note");
            return drinkOrder;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
