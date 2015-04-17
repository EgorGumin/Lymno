package com.lymno.quest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Colored on 05.04.2015.
 */
public class QuestList extends ActionBarActivity implements View.OnClickListener{
    //LinearLayout llMain;
    private static ArrayList<Quest> quests = new ArrayList<>();
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
        mAdapter = new CardsAdapter(quests);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                quests.clear();
                mAdapter.notifyDataSetChanged();
                new GetQuests().execute(Request.serverIP + "home/api/gui/quest/all");
                break;

            case R.id.btnClear:
                quests.clear();
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
                //JSONObject quest;
                try {
                    for (int i = 0; i < questArray.length(); ++i) {
                        JSONObject questJSON = questArray.getJSONObject(i);
                        final int id = questJSON.getInt("Id");
                        final String name = questJSON.getString("Name");
                        final String description = questJSON.getString("Description");
                        final int    authorId = questJSON.getInt("AuthorId");
                        final int    startTime = questJSON.getInt("StartTime");
                        final int    amountStages = questJSON.getInt("AmountStages");
                        final double x = questJSON.getDouble("X");
                        final double y = questJSON.getDouble("Y");
                        final double length = questJSON.getDouble("Length");
                        final int    averageTime = questJSON.getInt("AverageTime");

                        Quest quest = new Quest(id, name, description, authorId, startTime,
                                amountStages, x, y, length, averageTime);
                        quests.add(quest);

                        mAdapter.notifyItemInserted(QuestList.quests.size() - 1);
                        //mAdapter.notifyDataSetChanged();
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
