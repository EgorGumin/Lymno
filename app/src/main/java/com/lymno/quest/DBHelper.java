package com.lymno.quest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Roman Belkov on 13.05.2015.
 */
public class DBHelper extends SQLiteOpenHelper
{
    String name;

    public DBHelper(Context context, String name)
    {
        super(context, name, null, 1);
        this.name = name;
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        switch (name)
        {
            case QuestsDataBase.NAME:
                database.execSQL("create table " + QuestsDataBase.NAME + "("
                        + QuestsDataBase.QUEST_NAME + " text,"
                        + QuestsDataBase.ID + " int,"
                        + QuestsDataBase.DESCRIPTION + " text,"
                        + QuestsDataBase.AUTHOR_ID + " int,"
                        + QuestsDataBase.START_TIME + " long,"
                        + QuestsDataBase.AMOUNT_STAGES + " int,"
                        + QuestsDataBase.X + " double,"
                        + QuestsDataBase.Y + " double,"
                        + QuestsDataBase.LENGTH + " double,"
                        + QuestsDataBase.AVERAGE_TIME + " int" + ");");
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {

    }
}