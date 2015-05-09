package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class StagePlace extends ActionBarActivity implements View.OnClickListener{

    private TextView tvPlaceDescription;
    private Button btnImHere;

    private LocationManager locationManager;
    private Location lastGPS;
    private Location lastLocation;
    private Stage stage;

    private int questId;
    private int stageLevel;
    private int amountStages;
    private int secondsToUpdateGPS = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_place);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        amountStages = intent.getIntExtra("amountStages", 0);

        new GetStage().execute(Request.serverIP + "api/stages/getby?QuestId=" + questId  +
                "&Level=" + stageLevel);

        tvPlaceDescription = (TextView) findViewById(R.id.placeDescription);
        btnImHere =            (Button) findViewById(R.id.btnImHere);
        btnImHere.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isProviderEnabled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Обновляем раз в 10 секунд при изменении положения более чем на 1м.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 10, 1, locationListener);
        isProviderEnabled();
    }

    protected Boolean isProviderEnabled(){
        Boolean enabledGPS     = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean enabledNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!enabledGPS && !enabledNetwork){
            Toast.makeText(getBaseContext(), "GPS отключен. Включите, чтобы продолжить.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                if (location.getProvider().equals(locationManager.GPS_PROVIDER)){
                    lastGPS = location;
                    lastLocation = location;
                }
                if(location.getProvider().equals(locationManager.NETWORK_PROVIDER) ){
                    if(lastGPS == null){
                        lastLocation = location;
                    }
                    else{
                        if(((new Date().getTime()) - lastGPS.getTime())/(1000) > secondsToUpdateGPS){
                            lastLocation = location;
                        }
                    }
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            isProviderEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImHere:
                if (isProviderEnabled() && (lastLocation != null)) {
                    new CheckPosition().execute(Request.serverIP + "api/stages/checkPosition?Level=" + stage.getLevel() +
                            "&QuestId=" + questId + "&X=" + lastLocation.getLatitude() + "&Y=" + lastLocation.getLongitude());
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
                tvPlaceDescription.setText("Больше этапов нет! Наверное, произошла ошибка!");
                btnImHere.setClickable(false);
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

                            stage = new Stage(id, level, questId, name, description, question);
                            tvPlaceDescription.setText(stage.getDescription());
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
                tvPlaceDescription.setText("Наверное, произошла ошибка!");
            }
            else{
                try {
                    JSONObject checkResult = new JSONObject(res);

                    if(checkResult.getString("Result").equals("Success")){
                        Context context = StagePlace.this;
                        Intent stageQuestionIntent = new Intent(context, StageQuestion.class);
                        stageQuestionIntent.putExtra("questId", questId);
                        stageQuestionIntent.putExtra("stageLevel", stageLevel);
                        stageQuestionIntent.putExtra("question", stage.getQuestion());
                        stageQuestionIntent.putExtra("amountStages", amountStages);
                        context.startActivity(stageQuestionIntent);
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Вы не угадали с местом.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException ex) {
                        ex.printStackTrace();
                }
            }
        }
    }
}
