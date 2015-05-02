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


public class QuestInfo extends ActionBarActivity implements View.OnClickListener {

    Button startQuest;
    EditText tst;
    int questId;
    int amountStages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        questId = intent.getIntExtra("questId", 0);
        amountStages = intent.getIntExtra("amountStages", 0);

        setContentView(R.layout.quest_info);

        startQuest = (Button) findViewById(R.id.button_start_quest);
        startQuest.setOnClickListener(this);

        tst = (EditText) findViewById(R.id.editText3);
        tst.setText("Quest ID = " + questId);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start_quest) {
            Context context = view.getContext();
            Intent stagePlaceIntent = new Intent(context, StagePlace.class);
            stagePlaceIntent.putExtra("stageLevel", 1);
            stagePlaceIntent.putExtra("questId", questId);
            stagePlaceIntent.putExtra("amountStages", amountStages);
            context.startActivity(stagePlaceIntent);
        }
    }

    //Автосгенерированный код
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest_info, menu);
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
