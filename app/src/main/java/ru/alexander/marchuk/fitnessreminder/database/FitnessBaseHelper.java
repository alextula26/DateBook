package ru.alexander.marchuk.fitnessreminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;

public class FitnessBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "fitnessBase4.db";

    public static final String SELECTION_STATUS = FitnessTable.Cols.STATUS + " = ?";
    public static final String SELECTION_UUID = FitnessTable.Cols.UUID + " = ?";
    public static final String SELECTION_LIKE_TITLE = FitnessTable.Cols.TITLE + " LIKE ?";

    public FitnessBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FitnessTable.NAME + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FitnessTable.Cols.UUID + " LONG, " +
                FitnessTable.Cols.TITLE + " TEXT NOT NULL, " +
                FitnessTable.Cols.DATE + " DATE, " +
                FitnessTable.Cols.STATUS + " INTEGER, " +
                FitnessTable.Cols.TIMESTAMP + " LONG" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + FitnessTable.NAME);
        onCreate(db);
    }
}
