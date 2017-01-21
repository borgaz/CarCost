package com.kuba.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kuba_HP on 21.01.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "carcost.db";
    public static final String USER_NAME = "user";
    public static final String USER_COL_1 = "id";
    public static final String USER_COL_2 = "name";
    public static final String USER_COL_3 = "theme";
    public static final String USER_COL_4 = "distance_unit";
    public static final String USER_COL_5 = "fuel_unit";
    public static final String VEHICLE_NAME = "vehicle";
    public static final String VEHICLE_COL_1 = "id";
    public static final String VEHICLE_COL_2 = "user_id";
    public static final String VEHICLE_COL_3 = "name";
    public static final String VEHICLE_COL_4 = "fuel_type";
    public static final String VEHICLE_COL_5 = "tank_volume_1";
    public static final String VEHICLE_COL_6 = "tank_volume_2";
    public static final String COST_NAME = "cost";
    public static final String COST_COL_1 = "id";
    public static final String COST_COL_2 = "vehicle_id";
    public static final String COST_COL_3 = "expense";
    public static final String COST_COL_4 = "mileage";
    public static final String COST_COL_5 = "category";
    public static final String COST_COL_6 = "description";
    public static final String COST_COL_7 = "fuel_unit_amount";
    public static final String COST_COL_8 = "fuel_unit_prize";
    public static final String COST_COL_9 = "fuel_full";
    public static final String COST_COL_10 = "place";
    public static final String COST_COL_11 = "insurer";
    public static final String COST_COL_12 = "insurance";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USER_NAME + " (" +
                "id integer NOT NULL CONSTRAINT user_pk PRIMARY KEY AUTOINCREMENT," +
                "name varchar(50)," +
                "theme integer NOT NULL DEFAULT 0," +
                "distance_unit integer DEFAULT 0," +
                "fuel_unit integer DEFAULT 0" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public boolean insertUserData(String name, String theme, String distance_unit, String fuel_unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, name);
        contentValues.put(USER_COL_3, theme);
        contentValues.put(USER_COL_4, distance_unit);
        contentValues.put(USER_COL_5, fuel_unit);
        long result = db.insert(USER_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
}
