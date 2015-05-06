package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StageQuestion extends ActionBarActivity implements View.OnClickListener{

    EditText usersAnswer;
    TextView stageQuestion;
    String question;
    String answer;
    Button toAnswer;

    int questId;
    int stageLevel;
    int amountStages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_question);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        amountStages = intent.getIntExtra("amountStages", 0);

        toAnswer = (Button) findViewById(R.id.toAnswer);
        toAnswer.setOnClickListener(this);

        stageQuestion = (TextView) findViewById(R.id.stageQuestion);
        stageQuestion.setMovementMethod(new ScrollingMovementMethod());
        //stageQuestion.setText(question);
        usersAnswer = (EditText) findViewById(R.id.usersAnswer);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toAnswer) {
            if (answer.equals(usersAnswer.getText().toString())) {
                stageQuestion.setText(question + "\nВерно!");
                Context context = v.getContext();
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
                stageQuestion.setText(question + "\nНеправильный ответ!");
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
