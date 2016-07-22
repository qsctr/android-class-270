package com.example.user.simpleui;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity
        extends AppCompatActivity
        implements GeoCodingTask.GeoCodingResponse,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener,
            RoutingListener {

    final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;

    String storeInfo;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    LatLng storeLocation;

    List<Polyline> polylines = new ArrayList<>();

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
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 6);
            // MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(storeInfo);
            // googleMap.animateCamera(cameraUpdate);
            // googleMap.addMarker(markerOptions);

//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    Log.d("Debug", "Marker clicked");
//                    return false;
//                }
//            });

            storeLocation = latlng;

            createGoogleApiClient();
        }
    }

    private void createGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug", "Not granted");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
            return;
        }

        createLocationRequest();

        if (locationRequest != null) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng start = new LatLng(25.0186348, 121.5398379);
        if (location != null) {
            start = new LatLng(location.getLatitude(), location.getLongitude());
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 6));
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.TRANSIT)
                .waypoints(start, storeLocation)
                .withListener(this)
                .build();
        routing.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onConnected(null);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void createLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest()
                    .setInterval(1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 6));

        int index = -1;
        if (polylines.size() > 0) {
            for (Polyline polyline : polylines) {
                List<LatLng> points = polyline.getPoints();
                for (int i = 0; i < points.size(); i++) {
                    if (i != points.size() -1) {
                        LatLng p1 = points.get(i);
                        LatLng p2 = points.get(i + 1);

                        if (currentLatLng.latitude >= Math.min(p1.latitude, p2.latitude)
                                && currentLatLng.latitude <= Math.max(p1.latitude, p2.latitude)
                                && currentLatLng.longitude >= Math.min(p1.longitude, p2.longitude)
                                && currentLatLng.longitude <= Math.max(p1.longitude, p2.longitude)) {
                            index = i;
                            break;
                        }
                    }
                }

                if (index != -1) {
                    for (int i = index - 1; i >= 0; i--) {
                        points.remove(0);
                    }
                    points.set(0, currentLatLng);
                    polyline.setPoints(points);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routes, int i) {
        if (polylines.size() > 0)
        {
            for (Polyline polyline : polylines) {
                polyline.remove();
            }
            polylines.clear();
        }

        for (Route route : routes) {
            polylines.add(
                    googleMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(route.getPoints())
                                    .color(Color.GREEN)
                                    .width(10)
                    )
            );
        }
    }

    @Override
    public void onRoutingCancelled() {

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
