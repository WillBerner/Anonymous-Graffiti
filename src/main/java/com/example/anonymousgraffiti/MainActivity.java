package com.example.anonymousgraffiti;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button makeImage, seeImages;
    public static SQLiteDatabase photosDatabase;
    public static GoogleApiClient c = null;
    public static int currID;
    private static double lat;
    private static double lon;
    private static double offset = 0.0001;

   // private static Cursor cur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the screen
        makeImage = (Button) findViewById(R.id.makeImageButton);
        seeImages = (Button) findViewById(R.id.seeImagesButton);

        // Setting up database - not starting until after A4
        if (photosDatabase == null) {

            photosDatabase = this.openOrCreateDatabase("Photos", Context.MODE_PRIVATE, null);
            photosDatabase.execSQL("drop table if exists Photos;");
            photosDatabase.execSQL("create table Photos (ID int, Photo blob, Latitude double, Longitude double);");
        }
        if (c == null) {
            c = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        currID = 0;
    }

    @Override
    protected void onStart() {
        c.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        c.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected( Bundle bundle) {
        Log.v("MY_TAG", "GPS Connected.");
        try {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(c);
            if (loc != null) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
                Log.v("MY_TAG", "" + loc.getLatitude() + ", " + loc.getLongitude());

            } else {
                Toast.makeText(this,"Make sure location is enabled", Toast.LENGTH_SHORT).show();
            }

        }catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // Make Image Button Logic
    public void handleMakeImage(View v) {
        Intent i = new Intent(this, makeImageActivity.class);
        i.setAction("This is a good way to pass information to the next activity");
        this.startActivity(i);
    }

    // See Images Button Logic
    public void handleSeeImages(View v) {
        Intent i = new Intent(this, seeImagesActivity.class);
        i.setAction("This is a good way to pass information to the next activity");
        this.startActivity(i);
    }

    // called by make image
    public static void addImage(byte[] bit) {

        ContentValues cv = new ContentValues();
        cv.put("ID", currID);
        currID++;
        cv.put("Photo", bit);
        cv.put("Latitude", lat);
        cv.put("Longitude", lon);

        long rv = photosDatabase.insert("Photos", null, cv);

        Log.v("MYTAG", "RV: " + rv);
        printDatabase();

    }

    // Used to test if images were loading
    public static void printDatabase() {

        Log.v("MYTAG", "-------- Begin Printing Photos Database--------");
        Cursor cp = photosDatabase.rawQuery("SELECT ID from Photos", null);
        if (cp.getCount() > 0) {
            cp.moveToFirst();
            do {
                Log.v("MYTAG", "ID: " + cp.getInt(0));
            } while (cp.moveToNext());
        }

        Log.v("MYTAG", "-------- Finished Printing Photos Database--------");

    }

    // called by see images
    public static List<byte[]> getImages() {
        List<byte[]> photos = new ArrayList<byte[]>();
        Cursor cur = photosDatabase.rawQuery("SELECT P.Photo, P.ID from Photos P where P.Latitude < ? and P.Latitude > ? and P.Longitude < ? and P.Latitude > ?", new String[] {lat + offset +"", lat - offset +"", lon + offset +"", lon - offset +""});
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                byte[] ba = cur.getBlob(0);
                photos.add(ba);
            } while (cur.moveToNext());

        } else {
            Log.v("MYTAG", "no image found");
        }
        return photos;
    }

}
