package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_question);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        stageLevel = intent.getIntExtra("stageLevel", 0);
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");

        toAnswer = (Button) findViewById(R.id.toAnswer);
        toAnswer.setOnClickListener(this);

        stageQuestion = (TextView) findViewById(R.id.stageQuestion);
        stageQuestion.setText(question);
        usersAnswer = (EditText) findViewById(R.id.usersAnswer);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toAnswer) {
            if (answer.equals(usersAnswer.getText().toString())){
                stageQuestion.setText(question + "\nВерно!");

                Context context = v.getContext();
                Intent place_intent = new Intent(context, StagePlace.class);
                place_intent.putExtra("questId", questId);
                place_intent.putExtra("stageLevel", stageLevel+1);
                context.startActivity(place_intent);
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
