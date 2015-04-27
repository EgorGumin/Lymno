package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;


public class StagePlace extends ActionBarActivity implements View.OnClickListener{

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    TextView placeDescription;
    Button imHere;

    TextView tvLocationGPS;
    TextView distance;
    TextView tvImHere;
    float[] result;

    double X;
    double Y;
    String stageDescription;

    int questId;
    int stageLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_place);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questID", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        stageDescription = intent.getStringExtra("stageDescription");
        X = intent.getDoubleExtra("X", 0.);
        Y = intent.getDoubleExtra("Y", 0.);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        result = new float [10];
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        distance = (TextView) findViewById(R.id.distance);

        placeDescription = (TextView) findViewById(R.id.placeDescription);
        placeDescription.setText(stageDescription);
        tvImHere = (TextView) findViewById(R.id.tvImHere);
        imHere = (Button) findViewById(R.id.imHere);
        imHere.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
            if(location != null){
                location.distanceBetween(location.getLatitude(), location.getLongitude(), X, Y, result);
                distance.setText(String.valueOf(result[0]) + "метров?");
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location) + "\n Данные без GPS.");
        }
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates:\n lat = %1$.4f, lon = %2$.4f",
                location.getLatitude(), location.getLongitude());
    }

;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imHere) {
            if (result[0] < 100.) {
                tvImHere.setText("Вы на месте!");
                Context context = view.getContext();
                Intent place_intent = new Intent(context, StageQuestion.class);
                place_intent.putExtra("questId", questId);
                place_intent.putExtra("stageLevel", (stageLevel+1));
                context.startActivity(place_intent);
            }
            else{
                tvImHere.setText("До места еще " + String.valueOf(result[0]) + "м.");
            }
       }
    }










    //Автосгенерированный код, необязательно для вникания
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stage_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
