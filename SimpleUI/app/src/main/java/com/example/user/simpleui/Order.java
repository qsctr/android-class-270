package com.example.user.simpleui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Order {

    String note;
    String menuResults;
    String storeInfo;

    public int totalNumber() {
        if (menuResults == null || menuResults.isEmpty()) {
            return 0;
        }
        try {
            JSONArray jsonArray = new JSONArray(menuResults);
            int totalNumber = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                totalNumber += jsonObject.getInt("lNumber") + jsonObject.getInt("mNumber");
            }
            return totalNumber;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String toData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("note", note);
            jsonObject.put("menuResults", menuResults);
            jsonObject.put("storeInfo", storeInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Order newInstanceWithData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Order order = new Order();
            order.note = jsonObject.getString("note");
            order.menuResults = jsonObject.getString("menuResults");
            order.storeInfo = jsonObject.getString("storeInfo");
            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
