package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

public class Drink {
    String name;
    int mPrice = 0;
    int lPrice = 0;
    int imageId;

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("mPrice", mPrice);
            jsonObject.put("lPrice", lPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Drink newInstanceWithData(String data) {
        Drink drink = new Drink();
        try {
            JSONObject jsonObject = new JSONObject(data);
            drink.name = jsonObject.getString("name");
            drink.lPrice = jsonObject.getInt("lPrice");
            drink.mPrice = jsonObject.getInt("mPrice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }

}
