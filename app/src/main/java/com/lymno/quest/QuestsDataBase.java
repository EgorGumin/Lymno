package com.lymno.quest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Роман on 13.05.2015.
 */
public class QuestsDataBase {
    static final String NAME = "LymnoQuestsDataBase";
    static final String QUEST_NAME = "quest_name";
    static final String ID = "id";
    static final String DESCRIPTION = "description";
    static final String AUTHOR_ID = "author_id";
    static final String START_TIME = "start_time";
    static final String AMOUNT_STAGES = "amount_stages";
    static final String X = "latitude";
    static final String Y = "longitude";
    static final String LENGTH = "length";
    static final String AVERAGE_TIME = "average_time";

    private Context context;
    private DBHelper dbHelper;
    private ContentValues cv;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String[] columns;

    public QuestsDataBase(Context context)
    {
        this.context = context;
    }

    public ArrayList<Quest> getQuests()
    {
        dbHelper = new DBHelper(context, NAME);
        ArrayList<Quest> quests = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        columns = new String[]{QUEST_NAME, ID, DESCRIPTION, AUTHOR_ID, START_TIME, X, Y, AMOUNT_STAGES, LENGTH, AVERAGE_TIME};
        cursor = db.query(NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do
            {
                String questName = cursor.getString(cursor.getColumnIndex(QUEST_NAME));
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                int authorId = cursor.getInt(cursor.getColumnIndex(AUTHOR_ID));
                int startTime = cursor.getInt(cursor.getColumnIndex(START_TIME));
                int amountStages = cursor.getInt(cursor.getColumnIndex(AMOUNT_STAGES));
                double x = cursor.getDouble(cursor.getColumnIndex(X));
                double y = cursor.getDouble(cursor.getColumnIndex(Y));
                double length = cursor.getDouble(cursor.getColumnIndex(LENGTH));
                int averageTime = cursor.getInt(cursor.getColumnIndex(AVERAGE_TIME));

                quests.add(new Quest(id, questName, description, authorId, startTime, amountStages, x, y, length, averageTime));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();
        return quests;
    }

    public void addQuest (Quest quest)
    {
        dbHelper = new DBHelper(context, NAME);
        cv = new ContentValues();
        db = dbHelper.getWritableDatabase();

        cv.put(QUEST_NAME, quest.getName());
        cv.put(ID, quest.getId());
        cv.put(DESCRIPTION, quest.getDescription());
        cv.put(AUTHOR_ID, quest.getAuthorId());
        cv.put(START_TIME, quest.getStartTime());
        cv.put(AMOUNT_STAGES, quest.getAmountStages());
        cv.put(X, quest.getX());
        cv.put(Y, quest.getY());
        cv.put(LENGTH, quest.getLength());
        cv.put(AVERAGE_TIME, quest.getAverageTime());

        db.insert(NAME, null, cv);
        dbHelper.close();
    }

    public void recreateDataBase(Quest[] quests)
    {
        deleteDataBase();
        dbHelper = new DBHelper(context, NAME);
        for (int i = 0; i < quests.length; ++i)
        {
            addQuest(quests[i]);
        }
        dbHelper.close();
    }

    private void deleteDataBase()
    {
        dbHelper = new DBHelper(context, NAME);
        db = dbHelper.getWritableDatabase();
        db.delete(NAME, null, null);
        dbHelper.close();
    }
}
