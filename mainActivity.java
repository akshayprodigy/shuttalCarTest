package com.example.techbla.shuttalcar;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class mainActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    private  LatLng initialLocation, destinationLocation;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker initialMarker,currentMarker, destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      */
    }
    @Override
        protected void onResume() {
            super.onResume();
            if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

                buildGoogleApiClient();
                mGoogleApiClient.connect();

            }

            if (mMap == null) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

                mapFragment.getMapAsync(this);
            }
        }

    @Override
        protected void onPause() {
            super.onPause();
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }

        protected synchronized void buildGoogleApiClient() {
           // Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */
         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             return;
         }            else {
             mMap.setMyLocationEnabled(true);
         }
    }

    @Override
    public void onLocationChanged(Location location) {
         mLastLocation = location;
        if(initialMarker == null) {
                    //marker.remove();
            double dLatitude = mLastLocation.getLatitude();
            double dLongitude = mLastLocation.getLongitude();
            initialLocation = new LatLng(dLatitude, dLongitude);
            initialMarker = mMap.addMarker(new MarkerOptions().position(initialLocation).title("Innitial Location").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(initialLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        if(currentMarker == null) {
            //marker.remove();
            double dLatitude = mLastLocation.getLatitude();
            double dLongitude = mLastLocation.getLongitude();
            LatLng currentLocation = new LatLng(dLatitude, dLongitude);
            initialMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000);
                mLocationRequest.setFastestInterval(1000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                //mLocationRequest.setSmallestDisplacement(0.1F);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(this,"onConnected no permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //Toast.makeText(this,"onConnected have permission", Toast.LENGTH_SHORT).show();
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onMapSearch(View view){
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            if(destinationMarker!=null)
                destinationMarker.remove();
           destinationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        
    }

}
