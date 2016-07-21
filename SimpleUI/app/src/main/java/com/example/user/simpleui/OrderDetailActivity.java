package com.example.user.simpleui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Stack;

public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse {

    GoogleMap googleMap;
    String storeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        storeInfo = intent.getStringExtra("storeInfo");

        TextView noteTextView = (TextView) findViewById(R.id.noteTextView);
        TextView menuResultsTextView = (TextView) findViewById(R.id.menuResultsTextView);
        TextView storeInfoTextView = (TextView) findViewById(R.id.storeInfoTextView);
        final ImageView googleMapsImageView = (ImageView) findViewById(R.id.googleMapsImageView);

        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);
        String text = "";
        if (menuResultList != null) {
            for (String menuResult : menuResultList) {
                text += menuResult + "\n";
            }
        }
        menuResultsTextView.setText(text);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                (new GeoCodingTask(OrderDetailActivity.this))
                        .execute(storeInfo.substring(storeInfo.indexOf(",") + 1));
            }
        });
    }

    @Override
    public void responseWithGeoCodingResults(LatLng latlng) {
        if (googleMap != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 6);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(storeInfo);
            googleMap.animateCamera(cameraUpdate);
            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d("Debug", "Marker clicked");
                    return false;
                }
            });
        }
    }

//    public static class GeoCodingTask extends AsyncTask<String, Void, Bitmap> {
//
//        WeakReference<ImageView> imageViewWeakReference;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            String address = params[0];
//            double[] latLng = Utils.getLatLngFromGoogleMapsAPI(address);
//            return Utils.getStaticMap(latLng);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            if (imageViewWeakReference.get() != null && bitmap != null) {
//                ImageView imageView = imageViewWeakReference.get();
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//
//        public GeoCodingTask(ImageView imageView) {
//            imageViewWeakReference = new WeakReference<ImageView>(imageView);
//        }
//    }
}
