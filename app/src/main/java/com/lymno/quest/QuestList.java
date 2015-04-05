package com.lymno.quest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Colored on 05.04.2015.
 */
public class QuestList extends ActionBarActivity implements View.OnClickListener{
    LinearLayout llMain;
    Button btnCreate;
    Button btnClear;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quests_list);

        llMain = (LinearLayout) findViewById(R.id.llMain);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        new GetQuests().execute(Request.serverIP + "home/api/gui/quest/all");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                llMain.removeAllViews();
                new GetQuests().execute(Request.serverIP + "home/api/gui/quest/all");
                break;

            case R.id.btnClear:
                llMain.removeAllViews();
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public class GetQuests extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            try {
                JSONArray questArray = new JSONArray(res);
                JSONObject quest;
                try {
                    for (int i = 0; i < questArray.length(); ++i) {
                        quest = questArray.getJSONObject(i);
                        String name = quest.getString("Name");
                        String description = quest.getString("Description");
                        Button btnNew = new Button(getBaseContext());
                        btnNew.setText(name + "\n(" + description + ").");
                        llMain.addView(btnNew);
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
