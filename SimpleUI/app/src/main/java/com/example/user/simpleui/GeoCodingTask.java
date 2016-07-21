package com.example.user.simpleui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

public class GeoCodingTask extends AsyncTask<String, Void, LatLng> {

    WeakReference<GeoCodingResponse> geoCodingResponseWeakReference;

    @Override
    protected LatLng doInBackground(String... params) {
        String address = params[0];
        double[] latLng = Utils.getLatLngFromGoogleMapsAPI(address);
        return new LatLng(latLng[0], latLng[1]);
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        super.onPostExecute(latLng);
        if (geoCodingResponseWeakReference.get() != null && latLng != null) {
            GeoCodingResponse geoCodingResponse = geoCodingResponseWeakReference.get();
            geoCodingResponse.responseWithGeoCodingResults(latLng);
        }
    }

    public GeoCodingTask(GeoCodingResponse geoCodingResponse) {
        geoCodingResponseWeakReference = new WeakReference<GeoCodingResponse>(geoCodingResponse);
    }

    interface GeoCodingResponse {
        void responseWithGeoCodingResults(LatLng latlng);
    }
}