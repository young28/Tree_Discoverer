package com.example.pinatala.tree_discoverer.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pinatala.tree_discoverer.R;
import com.example.pinatala.tree_discoverer.Utilities;
import com.example.pinatala.tree_discoverer.database.TreeDataSource;
import com.example.pinatala.tree_discoverer.model.TreeMarker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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

import java.util.ArrayList;

/**
 * Created by YouYang and Matteo Pontiggia on 20/11/2016.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener{

    //Create the fields
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    static final int REQUEST_IMAGE_CAPTURE_FIRST = 1;
    static final int REQUEST_IMAGE_CAPTURE_SECOND = 2;

    public final static String MARKER_MESSAGE = "marker";
    private Button cameraButton;
    private Button refreshButton;
    private Bundle imagesBundle;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private Marker actualPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocation = new Location("initial_location");
        mLocation.setLatitude(46.0109729);
        mLocation.setLongitude(8.9575052);


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        cameraButton = (Button) findViewById(R.id.cameraButton);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhotoIntent1.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePhotoIntent1, REQUEST_IMAGE_CAPTURE_FIRST);
                }

            }
        });

        imagesBundle = new Bundle();


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLocation = location;


        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            findCurrentLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        if (mGoogleApiClient.isConnected())LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        //Refresh the map view and get markers again when the app resumes
        if(mMap != null) {
            mMap.clear();
            findCurrentLocation(mLocation);
        }
        updateMarkers();

    }



        @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    private void setUpMapIfNeeded() {

    }

    //Method to get current location
    private void findCurrentLocation(Location location){
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.user_position_logo);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.fromBitmap(icon));
        actualPosition = mMap.addMarker(options);
        actualPosition.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 18));
    }
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        if(actualPosition!=null) {
            actualPosition.setPosition(new LatLng(currentLatitude, currentLongitude));
        }else{
            findCurrentLocation(mLocation);
        }
    }

// Method to create markers for the trees when refreshing.
    public void createMarkerLocation(int id, Double lat, Double lon) {
        LatLng latLng = new LatLng(lat, lon);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.tree_logo3);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(Integer.toString(id))
                .icon(BitmapDescriptorFactory.fromBitmap(icon));

        if(mMap!=null) {
            mMap.addMarker(options).setTag(1);
            mMap.setOnMarkerClickListener(this);
        }
    }

    //Method to retrieve information from database, and create tree markers on map based on that
    public void updateMarkers (){
        TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
        ArrayList<TreeMarker> treeMarkers = dataSource.read();
        for (int i = 0; i < treeMarkers.size(); i++){
            TreeMarker currentTree = treeMarkers.get(i);
            int id = currentTree.getId();
            if(currentTree.getTreeType().equals("deleted_tree")){

            }else{
                double lat = currentTree.getLatitude ();
                double lon = currentTree.getLongitude ();
                createMarkerLocation(id ,lat,lon);
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
        mLocation = location;
        Log.d(TAG, "My Last Location " + mLocation.getLatitude()+ " " + mLocation.getLongitude());
    }


    @Override
    public void onClick(View v) {
        mMap.clear();
        findCurrentLocation(mLocation);
        updateMarkers ();
    }
    // Send the tree ID to the TreeMarkerDisplay Class to display its info
    @Override
    public boolean onMarkerClick(Marker marker) {

            Intent intent = new Intent(this, TreeMarkerDisplay.class);
            String id = marker.getTitle();
            if((Integer) marker.getTag()==1) {
                intent.putExtra(MARKER_MESSAGE, id);
                startActivity(intent);
            }


        return true;
    }

    // To take the photo of the tree and leaf, and display in DisplayActivity
    // We send the image info as ByteArray and send it out by intent to FindNewTreeActivity
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE_FIRST && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                imagesBundle.putByteArray("tree_image", Utilities.bitmapToByteArray(image));

                Intent takePhotoIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhotoIntent2.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePhotoIntent2, REQUEST_IMAGE_CAPTURE_SECOND);
                }
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE_SECOND && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                imagesBundle.putByteArray("leaf_image", Utilities.bitmapToByteArray(image));


                Intent newActivityIntent = new Intent(this, FindNewTreeActivity.class);
                newActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle locBundle = new Bundle();
                locBundle.putDouble("lat", mLocation.getLatitude());
                locBundle.putDouble("lon", mLocation.getLongitude());
                newActivityIntent.putExtra("location", locBundle);
                newActivityIntent.putExtra("extras", imagesBundle);
                startActivity(newActivityIntent);
            }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
    }

}
