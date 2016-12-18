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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    static final int REQUEST_IMAGE_CAPTURE_FIRST = 1;
    static final int REQUEST_IMAGE_CAPTURE_SECOND = 2;

    public final static String TEST_MESSAGE = "test";

    private ArrayList<TreeMarker> mTreeMarkers;
    private TreeMarker mTreeMarker;

    private Button cameraButton;
    private Button testButton;
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
        testButton = (Button) findViewById(R.id.testPositionButton);
        testButton.setOnClickListener(this);


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

        mTreeMarkers = new ArrayList<>();
        //mTreeMarker = new TreeMarker(700,"","","",0.0,0.0,"");

        //createExampleTreeMarker();
        //mTreeMarkers.add(mTreeMarker);

        TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
        //dataSource.create(mTreeMarker);

        //ArrayList<TreeMarker> treeMarkers = dataSource.read();

        //mTreeMarkers = getValues();
    }

    private void createExampleTreeMarker() {

        mTreeMarker.setTreeImageName("MI_07122016_1453.jpg");
        mTreeMarker.setLeafImageName("MI_07122016_1453.jpg");
        mTreeMarker.setCreateDate("2016-12-15");
        mTreeMarker.setLatitude(100.000000);
        mTreeMarker.setLongitude(100.000000);
        mTreeMarker.setTreeType("pine");
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
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLocation = location;


        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            findInitialLocation(location);
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

        updateMarkers();


//        Intent data = getIntent();
//        String img1 = data.getStringExtra("img1");
//        String img2 = data.getStringExtra("img2");
//        String tree_type = data.getStringExtra("type");
//        Double lat = data.getDoubleExtra("lat",0);
//        Double lon = data.getDoubleExtra("lon",0);
//
//        if(img1 != null && tree_type != null) {
//            createMarkerLocation(lat, lon);
//        }
    }



        @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
         //   stopLocationUpdates();
        }

    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
    private void setUpMapIfNeeded() {

    }
    private void findInitialLocation (Location location){
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 18));
    }
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        actualPosition.setPosition( new LatLng(currentLatitude, currentLongitude));
    }

    private void createTestLocation(Location location) {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.tree_logo3);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Coordinates")
                .icon(BitmapDescriptorFactory.fromBitmap(icon));
        mMap.addMarker(options);
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 18));


    }

    public void createMarkerLocation(int id, Double lat, Double lon) {
        LatLng latLng = new LatLng(lat, lon);
        //int identifier = id;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.tree_logo3);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(Integer.toString(id))
                .icon(BitmapDescriptorFactory.fromBitmap(icon));

        if(mMap!=null) {
            mMap.addMarker(options);
            mMap.setOnMarkerClickListener(this);
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void updateMarkers (){
        TreeDataSource dataSource = new TreeDataSource(this.getApplicationContext());
        ArrayList<TreeMarker> treeMarkers = dataSource.read();
        for (int i = 0; i < treeMarkers.size(); i++){
            TreeMarker currentTree = treeMarkers.get(i);
            int id = currentTree.getId();
            double lat = currentTree.getLatitude ();
            double lon = currentTree.getLongitude ();
           createMarkerLocation(id ,lat,lon);
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
        //createTestLocation(mLocation);
        updateMarkers ();
    }
//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//
//        if (marker.equals(myMarker))
//        {
//            //handle click here
//        }
//    }

    //find marker by title
    @Override
    public boolean onMarkerClick(Marker marker) {


            Intent intent = new Intent(this, TestClickTree.class);
            String message = "this is a tree, test success!";
            String id = marker.getTitle();
            intent.putExtra(TEST_MESSAGE, id);
            startActivity(intent);


        return true;
    }

    // To take the photo of the tree and leaf, and display in DisplayActivity
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
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
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



//    public ArrayList<TreeMarker> getValues() {
//        ArrayList<TreeMarker> markers = new ArrayList<TreeMarker>();
//
//        TreeDatabaseOpenHelper databaseOpenHelper = new TreeDatabaseOpenHelper(getApplicationContext());
//        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
//
//        Cursor cursor = database.query(TreeDatabaseOpenHelper.TREE_TABLE_NAME, null, null, null, null, null, null );
//        //Cursor cursor = database.rawQuery("SELECT * FROM " + TreeDatabaseOpenHelper.TREE_TABLE_NAME, null);
//
//        cursor.moveToFirst();
//
//        for(int i=0; i<cursor.getCount(); i++){
//            markers.add(new TreeMarker(cursor.getInt(1), cursor.getString(2),
//                    cursor.getString(3), cursor.getString(4),cursor.getDouble(5),
//                    cursor.getDouble(6)));
//        }
//
//        cursor.close();
//        database.close();
//        return markers;
//    }


}