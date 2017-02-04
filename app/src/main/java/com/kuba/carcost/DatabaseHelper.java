package com.kuba.carcost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

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

    public void importDatabase(Context context) throws IOException {
        File direct = new File(Environment.getExternalStorageDirectory() + "/CarCost");
        String cDBPath = "//data//com.kuba.carcost//databases//carcost.db";
        String bDBPath = "/CarCost/carcost.db";
        if(!direct.exists()) {
            if(direct.mkdir()) {}
        }
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        try {
            if (sd.canWrite()) {
                File backupDB = new File(data, cDBPath);
                File currentDB = new File(sd, bDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDatabase(Context context) throws IOException {
        File direct = new File(Environment.getExternalStorageDirectory() + "/CarCost");
        String currentDBPath = "//data//com.kuba.carcost//databases//carcost.db";
        String backupDBPath = "/CarCost/carcost.db";
        if (!direct.exists()) {
            if (direct.mkdir()) {}
        }
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        try {
            if (sd.canWrite()) {
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUserData(String name, int theme, int distance_unit, int volume_unit, int last_vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, name);
        contentValues.put(USER_COL_3, theme);
        contentValues.put(USER_COL_4, distance_unit);
        contentValues.put(USER_COL_5, volume_unit);
        contentValues.put(USER_COL_6, last_vehicle);
        return db.insert(USER_TABLE, null, contentValues) != -1;
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
        return db.insert(VEHICLE_TABLE, null, contentValues) != -1;
    }

    public boolean insertCostData(int vehicle_id, double expense,
                                  String cost_date, int mileage,
                                  int category, String description,
                                  double fuel_unit_amount,
                                  double fuel_unit_price,
                                  int fuel_full, int fuel_tank_num,
                                  String place, String insurer,
                                  int insurance, int tank_missed) {
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
        return db.insert(COST_TABLE, null, contentValues) != -1;
    }

    public boolean updateCostData(int id, int vehicle_id, double expense, String cost_date, int mileage,
                                  int category, String description, double fuel_unit_amount,
                                  double fuel_unit_price, int fuel_full, int fuel_tank_num, String place,
                                  String insurer, int insurance, int tank_missed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COST_COL_1, id);
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
        return db.update(COST_TABLE, contentValues, "id = " + id, null) != -1;
    }

    public boolean deleteCostById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COST_TABLE, "id = " + id, null) != 0;
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

    public double getCostDataForAvgFuel() {
        SQLiteDatabase db = this.getWritableDatabase();
        double sum;
        int distance;
        Cursor res = db.rawQuery("SELECT sum(fuel_unit_amount),min(mileage),max(mileage) FROM " + COST_TABLE, null);
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                sum = res.getDouble(0);
                distance = res.getInt(2) - res.getInt(1);
                if(distance != 0) {
                    sum /= distance;
                    sum *= 100;
                    return sum;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    public double getCostDataForAvgAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        double sum;
        int distance;
        Cursor res = db.rawQuery("SELECT sum(expense),min(mileage),max(mileage) FROM " + COST_TABLE, null);
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                sum = res.getDouble(0);
                distance = res.getInt(2) - res.getInt(1);
                if(distance != 0) {
                    sum /= distance;
                    return sum;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    public double getCostDataForAvg30() {
        SQLiteDatabase db = this.getWritableDatabase();
        double sum;
        int distance;
        Cursor res = db.rawQuery("SELECT sum(expense),min(mileage),max(mileage) FROM " + COST_TABLE + " WHERE cost_date BETWEEN date('now','-1 month') AND date('now')", null);
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                sum = res.getDouble(0);
                distance = res.getInt(2) - res.getInt(1);
                if(distance != 0) {
                    sum /= distance;
                    return sum;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    public ChartEntry getCostDataForChart30() {
        SQLiteDatabase db = this.getWritableDatabase();
        ChartEntry chartEntry = new ChartEntry();
        Cursor res = db.rawQuery("SELECT expense, cost_date FROM " + COST_TABLE + " WHERE cost_date BETWEEN date('now','-1 month') AND date('now') ORDER BY cost_date", null);
        if (res.getCount() != 0) {
            for (int i = 0; res.moveToNext(); i++) {
                chartEntry.addBarEntry(new BarEntry(res.getFloat(0), i));
                chartEntry.addLabel(res.getString(1));
            }
            return chartEntry;
        } else {
            chartEntry.addBarEntry(new BarEntry(0.0f, 0));
            chartEntry.addLabel("BrakDanych");
            return chartEntry;
        }
    }

    public Cursor getCostByCategoryAndDate(String since, String to, ArrayList<Integer> categories) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(categories.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(categories.get(0).intValue());
            if (categories.size() > 1) {
                for (int i = 1; i < categories.size(); i++) {
                    stringBuilder.append(", ");
                    stringBuilder.append(categories.get(i).intValue());
                }
            }
            String param = stringBuilder.toString();
            Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + " WHERE (category IN (" + param + ")) AND (cost_date BETWEEN \"" + since + "\" AND \"" + to + "\") ORDER BY cost_date", null);
            return res;
        } else {
            Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + " WHERE cost_date BETWEEN \"" + since + "\" AND \"" + to + "\" ORDER BY cost_date", null);
            return res;
        }
    }

    public Cursor getCostById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + COST_TABLE + " WHERE id = " + id, null);
        return res;
    }
}
