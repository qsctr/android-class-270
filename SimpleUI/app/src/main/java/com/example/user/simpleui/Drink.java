package com.example.user.simpleui;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Drink")
public class Drink extends ParseObject {
    int imageId;

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getmPrice() {
        return getInt("mPrice");
    }

    public void setmPrice(int mPrice) {
        put("mPrice", mPrice);
    }

    public int getlPrice() {
        return getInt("lPrice");
    }

    public void setlPrice(int lPrice) {
        put("lPrice", lPrice);
    }

    public JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("mPrice", getmPrice());
            jsonObject.put("lPrice", getlPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Drink newInstanceWithData(String data) {
        Drink drink = new Drink();
        try {
            JSONObject jsonObject = new JSONObject(data);
            drink.setName(jsonObject.getString("name"));
            drink.setlPrice(jsonObject.getInt("lPrice"));
            drink.setmPrice(jsonObject.getInt("mPrice"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }

}
