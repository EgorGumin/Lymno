package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;



public class StagePlace extends ActionBarActivity implements View.OnClickListener{

    TextView placeDescription;
    TextView tvLocationGPS;
    TextView distance;
    TextView tvImHere;
    Button imHere;

    private LocationManager locationManager;
    Location lastGPS;
    Location lastLocation;
    private Stage stage;
    float[] result;
    float radius = 100;

    int questId;
    int stageLevel;
    int amountStages;

    Boolean isInitialized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_place);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        amountStages = intent.getIntExtra("amountStages", 0);
        result = new float [10];
        result[0] = radius*1000;
        new GetStage().execute(Request.serverIP + "api/stages/getby?QuestId=" + questId  +
                "&Level=" + stageLevel);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        tvLocationGPS =    (TextView) findViewById(R.id.tvLocationGPS);
        distance =         (TextView) findViewById(R.id.distance);
        placeDescription = (TextView) findViewById(R.id.placeDescription);
        tvImHere =         (TextView) findViewById(R.id.tvImHere);

        imHere = (Button) findViewById(R.id.imHere);
        imHere.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Обновляем раз в 10 секунд при изменении положения более чем на 1м.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 1, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 1,
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
                if (location.getProvider().equals(locationManager.GPS_PROVIDER)){
                    lastGPS = location;
                    lastLocation = location;
                    //Location.distanceBetween(location.getLatitude(), location.getLongitude(), X, Y, result);
                    //distance.setText(String.valueOf(result[0]) + "метров?");
                }
                if(location.getProvider().equals(locationManager.NETWORK_PROVIDER) ){
                    if(lastGPS==null){
                        //Location.distanceBetween(location.getLatitude(), location.getLongitude(), X, Y, result);
                        //distance.setText(String.valueOf(result[0]) + "м. GPS is OFF.");
                        lastLocation = location;
                    }
                    else{
                        if(((new Date().getTime()) - lastGPS.getTime())/(1000) > 60){
                            //Location.distanceBetween(location.getLatitude(), location.getLongitude(), X, Y, result);
                            //distance.setText(String.valueOf(result[0]) + "м.\n" + String.valueOf(((new Date().getTime()) - lastGPS.getTime())/1000));
                            lastLocation = location;
                        }
                    }
                }
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imHere) {
            if (lastLocation != null){
                new CheckPosition().execute(Request.serverIP + "api/stages/checkPosition?Level="+stage.getLevel()+
                        "&QuestId="+questId+"&X="+lastLocation.getLatitude()+"&Y="+lastLocation.getLongitude());
            }
       }
    }


    public class GetStage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            if (res.equals("[]")){
                //СРОЧНО ПЕРЕПИСАТЬ
                placeDescription.setText("Больше этапов нет! Наверное, произошла ошибка!");
                imHere.setClickable(false);
            }
            else{
                try {
                    JSONArray stageArray = new JSONArray(res);
                    try {
                        for (int i = 0; i < stageArray.length(); ++i) {
                            JSONObject stageJSON = stageArray.getJSONObject(i);
                            final int id = stageJSON.getInt("Id");
                            final int level = stageJSON.getInt("Level");
                            final int questId = stageJSON.getInt("QuestId");
                            final String name = stageJSON.getString("Name");
                            final String description = stageJSON.getString("Description");
                            final String question = stageJSON.getString("Question");

                            stage = new Stage(id, level, questId, name, description,
                                    question);

                            //X = stage.getX();
                            //Y = stage.getY();
                            placeDescription.setText(stage.getDescription());
                            isInitialized = true;
                            //onResume(); //простигосподи
                        }
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public class CheckPosition extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            if (res.equals("[]")){
                //СРОЧНО ПЕРЕПИСАТЬ
                placeDescription.setText("Наверное, произошла ошибка!");
            }
            else{
                try {
                    JSONObject checkResult = new JSONObject(res);
                    if(checkResult.getString("Result").equals("Success")){
                        tvImHere.setText("Вы на месте!");
                        Context context = StagePlace.this;
                        Intent stageQuestionIntent = new Intent(context, StageQuestion.class);
                        stageQuestionIntent.putExtra("questId", questId);
                        stageQuestionIntent.putExtra("stageLevel", stageLevel);
                        stageQuestionIntent.putExtra("question", stage.getQuestion());
                        stageQuestionIntent.putExtra("amountStages", amountStages);
                        context.startActivity(stageQuestionIntent);
                    }
                    else{
                        tvImHere.setText("До места еще " + String.valueOf(result[0]) + "м.");
                    }

                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
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
