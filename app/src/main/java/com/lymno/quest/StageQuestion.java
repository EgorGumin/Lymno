package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class StageQuestion extends ActionBarActivity implements View.OnClickListener{

    private Stage stage;
    public EditText usersAnswer;
    TextView questionField;
    Button toAnswer;
    int questId;
    int stageLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_question);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);

        toAnswer = (Button) findViewById(R.id.toAnswer);
        toAnswer.setOnClickListener(this);

        new GetStage().execute(Request.serverIP + "api/stages/getby?QuestId=" + questId  +
                "&Level=" + stageLevel);
        questionField = (TextView) findViewById(R.id.stageQuestion);
        usersAnswer = (EditText) findViewById(R.id.usersAnswer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stage_question, menu);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toAnswer) {
            if (stage.getAnswer().equals(usersAnswer.getText().toString())){
                questionField.setText(questionField.getText().toString() + "\nВерно!");
                Context context = v.getContext();
                Intent place_intent = new Intent(context, StagePlace.class);
                place_intent.putExtra("questId", questId);
                place_intent.putExtra("stageLevel", stageLevel);
                place_intent.putExtra("X", stage.getX());
                place_intent.putExtra("Y", stage.getY());
                place_intent.putExtra("stageDescription", stage.getDescription());
                context.startActivity(place_intent);
            }
            else{
                questionField.setText(questionField.getText().toString() + "\nНеправильный ответ!");
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
                questionField.setText("Больше этапов нет! Наверное, вы победили!");
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
                            final double x = stageJSON.getDouble("X");
                            final double y = stageJSON.getDouble("Y");
                            final String question = stageJSON.getString("Question");
                            final String answer = stageJSON.getString("Answer");

                            stage = new Stage(id, level, questId, name, description, x, y,
                                    question, answer);


                            questionField.setText(stage.getQuestion());
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
}
