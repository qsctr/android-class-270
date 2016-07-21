package com.example.user.simpleui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Utils {

    public static void writeFile(Context context, String fileName, String content) {
        try {
            FileOutputStream fileOutputStream =
                    context.openFileOutput(fileName, Context.MODE_APPEND);
            fileOutputStream.write(content.getBytes());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[2048];
            fileInputStream.read(buffer, 0, buffer.length);
            fileInputStream.close();
            return new String(buffer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] urlToBytes(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatLngFromGoogleMapsAPI(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = "http://maps.google.com/maps/api/geocode/json?address=" + address;

        byte[] data = urlToBytes(apiURL);

        if (data == null)
            return null;

        String result = new String(data);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("status").equals("OK")) {
                JSONObject latLng = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                return new double[] {latLng.getDouble("lat"), latLng.getDouble("lng")};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getStaticMap(double[] latLng) {
        String center = String.valueOf(latLng[0]) + "," + String.valueOf(latLng[1]);
        String staticMapURL = "http://maps.google.com/maps/api/staticmap?center="
                + center + "&size=640x480&zoom=11";

        byte[] bytes = urlToBytes(staticMapURL);

        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }

        return null;
    }

}
