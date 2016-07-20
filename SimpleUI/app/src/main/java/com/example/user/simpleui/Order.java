package com.example.user.simpleui;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Order")
public class Order extends ParseObject {

    public String getNote() {
        return getString("note");
    }

    public void setNote(String note) {
        put("note", note);
    }

    public String getMenuResults() {
        String results = getString("menuResults");
        if (results == null)
            return "";
        return results;
    }

    public void setMenuResults(String menuResults) {
        put("menuResults", menuResults);
    }

    public String getStoreInfo() {
        return getString("storeInfo");
    }

    public void setStoreInfo(String storeInfo) {
        put("storeInfo", storeInfo);
    }

    public int totalNumber() {
        if (getMenuResults() == null || getMenuResults().isEmpty()) {
            return 0;
        }
        try {
            JSONArray jsonArray = new JSONArray(getMenuResults());
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

    public static List<String> getMenuResultList(String menuResults) {
        if (menuResults == null || menuResults == "") {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(menuResults);
            List<String> menuResultList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String data = jsonArray.getString(i);
                DrinkOrder drinkOrder = DrinkOrder.newInstanceWithData(data);
                String menuResult = drinkOrder.drink.getName()
                        + "Medium: " + String.valueOf(drinkOrder.mNumber)
                        + "Large: " + String.valueOf(drinkOrder.lNumber);
                menuResultList.add(menuResult);
            }
            return menuResultList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("note", getNote());
            jsonObject.put("menuResults", getMenuResults());
            jsonObject.put("storeInfo", getStoreInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static Order newInstanceWithData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Order order = new Order();
            order.setNote(jsonObject.getString("note"));
            order.setMenuResults(jsonObject.getString("menuResults"));
            order.setStoreInfo(jsonObject.getString("storeInfo"));
            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getOrdersFromRemote(final FindCallback<Order> callback) {
        getQuery().findInBackground(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {
                    Order.pinAllInBackground("Order", objects);
                }
                callback.done(objects, e);
            }
        });
    }

    public static ParseQuery getQuery() {
        return ParseQuery.getQuery(Order.class);
    }

}
