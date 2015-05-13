package com.lymno.quest;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Colored on 05.04.2015.
 */
public class QuestList extends ActionBarActivity {

    private CardsAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private QuestsDataBase db = new QuestsDataBase(this);
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quests_list);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.quests_list_swipe_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.GREEN);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new updateQuestList().execute("home/api/gui/quest/all");
            }
        });

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.quests_list_recycler_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // 3. create an adapter
        mAdapter = new CardsAdapter(db.getQuests());
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private class updateQuestList extends BasicRequest {
        protected void onPostExecute(String JSONString) {
            try {
                JSONArray questArray = new JSONArray(JSONString);
                db.recreateDataBase(new ArrayList<Quest>());
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
                        db.addQuest(quest);
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            mAdapter.updateItems(db.getQuests());
            refreshLayout.setRefreshing(false);
        }
    }
}
