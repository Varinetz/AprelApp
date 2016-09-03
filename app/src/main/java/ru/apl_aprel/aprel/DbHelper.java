package ru.apl_aprel.aprel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Руслан on 03.09.2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "aprel_db";

    public static final String TABLE_NAME = "results";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String OCCUPATION = "occupation";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
            + NAME + " TEXT, "
            + SURNAME + " TEXT, "
            + OCCUPATION + " TEXT, "
            + DAY + " INT, "
            + MONTH + " INT, "
            + YEAR + " INT)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
