package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class StageQuestion extends ActionBarActivity implements View.OnClickListener{

    EditText usersAnswer;
    TextView stageQuestion;
    String question;
    String storedToken;
    Button toAnswer;

    int questId;
    int stageLevel;
    int amountStages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_question);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        question = intent.getStringExtra("question");
        amountStages = intent.getIntExtra("amountStages", 0);

        SharedPreferences cache = getSharedPreferences("cache", MODE_PRIVATE);
        storedToken = cache.getString("IDToken", "");

        toAnswer = (Button) findViewById(R.id.toAnswer);
        toAnswer.setOnClickListener(this);

        stageQuestion = (TextView) findViewById(R.id.stageQuestion);
        stageQuestion.setMovementMethod(new ScrollingMovementMethod());
        stageQuestion.setText(question);
        usersAnswer = (EditText) findViewById(R.id.usersAnswer);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toAnswer) {
            new CheckAnswer().execute(Request.serverIP + "api/stages/checkAnswer?Level=" + stageLevel +
                    "&QuestId=" + questId  + "&Token=" + storedToken + "&Answer=" + usersAnswer.getText().toString());
        }
    }

    public class CheckAnswer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            if (res.equals("[]")){
                //СРОЧНО ПЕРЕПИСАТЬ
                stageQuestion.setText("Наверное, произошла ошибка!");
            }
            else{
                try {
                    JSONObject checkResult = new JSONObject(res);
                    if(checkResult.getString("Result").equals("Success")){
                        stageQuestion.setText("Вы на месте!");
                        Context context = StageQuestion.this;
                        if (stageLevel == amountStages) {
                            Intent finishedQuestIntent = new Intent(context, QuestFinished.class);
                            context.startActivity(finishedQuestIntent);
                        }
                        else{
                            Intent stagePlaceIntent = new Intent(context, StagePlace.class);
                            stagePlaceIntent.putExtra("questId", questId);
                            stagePlaceIntent.putExtra("stageLevel", stageLevel + 1);
                            stagePlaceIntent.putExtra("amountStages", amountStages);
                            context.startActivity(stagePlaceIntent);
                        }
                    }
                    else{
                        stageQuestion.setText("Неправильно");
                    }

                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    //Автосгенерированный код
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
}
