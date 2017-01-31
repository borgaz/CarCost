package com.kuba.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.GregorianCalendar;

/**
 * Created by Kuba_HP on 21.01.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "carcost.db";
    public static final String USER_TABLE = "user";
    public static final String USER_COL_1 = "id";
    public static final String USER_COL_2 = "name";
    public static final String USER_COL_3 = "theme";
    public static final String USER_COL_4 = "distance_unit";
    public static final String USER_COL_5 = "volume_unit";
    public static final String USER_COL_6 = "last_vehicle";
    public static final String VEHICLE_TABLE = "vehicle";
    public static final String VEHICLE_COL_1 = "id";
    public static final String VEHICLE_COL_2 = "user_id";
    public static final String VEHICLE_COL_3 = "name";
    public static final String VEHICLE_COL_4 = "fuel_type_1";
    public static final String VEHICLE_COL_5 = "tank_volume_1";
    public static final String VEHICLE_COL_6 = "fuel_type_2";
    public static final String VEHICLE_COL_7 = "tank_volume_2";
    public static final String COST_TABLE = "cost";
    public static final String COST_COL_1 = "id";
    public static final String COST_COL_2 = "vehicle_id";
    public static final String COST_COL_3 = "expense";
    public static final String COST_COL_4 = "cost_date";
    public static final String COST_COL_5 = "mileage";
    public static final String COST_COL_6 = "category";
    public static final String COST_COL_7 = "description";
    public static final String COST_COL_8 = "fuel_unit_amount";
    public static final String COST_COL_9 = "fuel_unit_price";
    public static final String COST_COL_10 = "fuel_full";
    public static final String COST_COL_11 = "fuel_tank_num";
    public static final String COST_COL_12 = "place";
    public static final String COST_COL_13 = "insurer";
    public static final String COST_COL_14 = "insurance";
    public static final String COST_COL_15 = "tank_missed";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE user (" +
                    "id integer NOT NULL CONSTRAINT user_pk PRIMARY KEY AUTOINCREMENT," +
                    "name varchar(50) NOT NULL," +
                    "theme integer," +
                    "distance_unit integer NOT NULL," +
                    "volume_unit integer NOT NULL," +
                    "last_vehicle integer NOT NULL" +
                    ");");
            db.execSQL("CREATE TABLE vehicle (" +
                    "id integer NOT NULL CONSTRAINT vehicle_pk PRIMARY KEY AUTOINCREMENT," +
                    "user_id integer NOT NULL," +
                    "name varchar(50) NOT NULL," +
                    "fuel_type_1 integer NOT NULL," +
                    "tank_volume_1 double NOT NULL," +
                    "fuel_type_2 integer," +
                    "tank_volume_2 double," +
                    "CONSTRAINT vehicle_user FOREIGN KEY (user_id)" +
                    "REFERENCES user (id)" +
                    ");");
            db.execSQL("CREATE TABLE cost (" +
                    "id integer NOT NULL CONSTRAINT cost_pk PRIMARY KEY AUTOINCREMENT," +
                    "vehicle_id integer NOT NULL," +
                    "expense double NOT NULL," +
                    "cost_date date NOT NULL," +
                    "mileage integer NOT NULL," +
                    "category integer NOT NULL," +
                    "description varchar(255)," +
                    "fuel_unit_amount double," +
                    "fuel_unit_price double," +
                    "fuel_full integer," +
                    "fuel_tank_num integer," +
                    "place varchar(255)," +
                    "insurer varchar(255)," +
                    "insurance integer," +
                    "tank_missed integer," +
                    "CONSTRAINT cost_vehicle FOREIGN KEY (vehicle_id)" +
                    "REFERENCES vehicle (id)" +
                    ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS vehicle");
        db.execSQL("DROP TABLE IF EXISTS cost");

        onCreate(db);
    }

    public boolean insertUserData(String name, int theme, int distance_unit, int volume_unit, int last_vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, name);
        contentValues.put(USER_COL_3, theme);
        contentValues.put(USER_COL_4, distance_unit);
        contentValues.put(USER_COL_5, volume_unit);
        contentValues.put(USER_COL_6, last_vehicle);
        if (db.insert(USER_TABLE, null, contentValues) == -1)
            return false;
        else
            return true;
    }

    public boolean insertVehicleData(int user_id, String name, int fuel_type_1, double tank_volume_1,
                                     int fuel_type_2, double tank_volume_2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEHICLE_COL_2, user_id);
        contentValues.put(VEHICLE_COL_3, name);
        contentValues.put(VEHICLE_COL_4, fuel_type_1);
        contentValues.put(VEHICLE_COL_5, tank_volume_1);
        contentValues.put(VEHICLE_COL_6, fuel_type_2);
        contentValues.put(VEHICLE_COL_7, tank_volume_2);
        if (db.insert(VEHICLE_TABLE, null, contentValues) == -1)
            return false;
        else
            return true;
    }

    public boolean insertCostData(int vehicle_id, double expense, String cost_date, int mileage,
                                  int category, String description, double fuel_unit_amount,
                                  double fuel_unit_price, int fuel_full, int fuel_tank_num,
                                  String place, String insurer, int insurance, int tank_missed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COST_COL_2, vehicle_id);
        contentValues.put(COST_COL_3, expense);
        contentValues.put(COST_COL_4, cost_date);
        contentValues.put(COST_COL_5, mileage);
        contentValues.put(COST_COL_6, category);
        contentValues.put(COST_COL_7, description);
        contentValues.put(COST_COL_8, fuel_unit_amount);
        contentValues.put(COST_COL_9, fuel_unit_price);
        contentValues.put(COST_COL_10, fuel_full);
        contentValues.put(COST_COL_11, fuel_tank_num);
        contentValues.put(COST_COL_12, place);
        contentValues.put(COST_COL_13, insurer);
        contentValues.put(COST_COL_14, insurance);
        contentValues.put(COST_COL_15, tank_missed);
        if (db.insert(COST_TABLE, null, contentValues) == -1)
            return false;
        else
            return true;
    }

    public boolean updateUserData(int id, String name, int theme, int distance_unit, int volume_unit, int last_vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, name);
        contentValues.put(USER_COL_3, theme);
        contentValues.put(USER_COL_4, distance_unit);
        contentValues.put(USER_COL_5, volume_unit);
        contentValues.put(USER_COL_6, last_vehicle);
        if (db.update(USER_TABLE, contentValues, "id = ?", new String[] {Integer.toString(id)}) == -1)
            return false;
        else
            return true;
    }

    public boolean updateVehicleData(int id, int user_id, String name, int fuel_type_1, double tank_volume_1,
                                     int fuel_type_2, double tank_volume_2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VEHICLE_COL_2, user_id);
        contentValues.put(VEHICLE_COL_3, name);
        contentValues.put(VEHICLE_COL_4, fuel_type_1);
        contentValues.put(VEHICLE_COL_5, tank_volume_1);
        contentValues.put(VEHICLE_COL_6, fuel_type_2);
        contentValues.put(VEHICLE_COL_7, tank_volume_2);
        if (db.update(VEHICLE_TABLE, contentValues, "id = ?", new String[] {Integer.toString(id)}) == -1)
            return false;
        else
            return true;
    }

    public boolean updateCostData(int id, int vehicle_id, double expense, String cost_date, int mileage,
                                  int category, String description, double fuel_unit_amount,
                                  double fuel_unit_price, int fuel_full, int fuel_tank_num, String place,
                                  String insurer, int insurance, int tank_missed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COST_COL_2, vehicle_id);
        contentValues.put(COST_COL_3, expense);
        contentValues.put(COST_COL_4, cost_date);
        contentValues.put(COST_COL_5, mileage);
        contentValues.put(COST_COL_6, category);
        contentValues.put(COST_COL_7, description);
        contentValues.put(COST_COL_8, fuel_unit_amount);
        contentValues.put(COST_COL_9, fuel_unit_price);
        contentValues.put(COST_COL_10, fuel_full);
        contentValues.put(COST_COL_11, fuel_tank_num);
        contentValues.put(COST_COL_12, place);
        contentValues.put(COST_COL_13, insurer);
        contentValues.put(COST_COL_14, insurance);
        contentValues.put(COST_COL_15, tank_missed);
        if (db.update(COST_TABLE, contentValues, "id = ?", new String[] {Integer.toString(id)}) == -1)
            return false;
        else
            return true;
    }

    public Cursor getUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + USER_TABLE, null);
        return res;
    }

    public Cursor getVehicle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + VEHICLE_TABLE + " WHERE id=" + id, null);
        return res;
    }

    public Cursor getCostByCategory(String since, String to, int... params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(params[0]);
        for(int i = 1; i < params.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(params[i]);
        }
        String param = stringBuilder.toString();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + "WHERE (category IN (" + param + ")) AND (cost_date BETWEEN \"" + since + "\" AND \"" + to + "\")", null);
        return res;
    }

    public Cursor getCostByDate(String since, String to) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + "WHERE cost_date BETWEEN \"" + since + "\" AND \"" + to + "\"" , null);
        return res;
    }

    public Cursor getLastMonthCost() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + "WHERE cost_date >= date('now', '-1 months')", null);
        return res;
    }


}
