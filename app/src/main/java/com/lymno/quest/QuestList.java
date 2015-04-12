package com.lymno.quest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Colored on 05.04.2015.
 */
public class QuestList extends ActionBarActivity implements View.OnClickListener{
    //LinearLayout llMain;
    private static ArrayList<String> questData = new ArrayList<>();
    CardsAdapter mAdapter;
    Context context;
    Button btnCreate;
    Button btnClear;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quests_list);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // 3. create an adapter
        mAdapter = new CardsAdapter(questData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                questData.clear();
                mAdapter.notifyDataSetChanged();
                new GetQuests().execute(Request.serverIP + "home/api/gui/quest/all");
                break;

            case R.id.btnClear:
                questData.clear();
                mAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Flushed list of quests", Toast.LENGTH_SHORT).show();
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
                        //Button btnNew = new Button(getBaseContext());
                        String str = (name + "\n(" + description + ").");
                        //llMain.addView(btnNew);
                        questData.add(str);
                        mAdapter.notifyItemInserted(questData.size() - 1);
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
