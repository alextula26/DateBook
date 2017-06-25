package ru.alexander.marchuk.fitnessreminder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.alexander.marchuk.fitnessreminder.database.ExerciseCursorWrapper;
import ru.alexander.marchuk.fitnessreminder.database.FitnessBaseHelper;
import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;

public class ExerciseLab {

    private static ExerciseLab sExerciseLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ExerciseLab get(Context context) {
        if (sExerciseLab == null) {
            sExerciseLab = new ExerciseLab(context);
        }
        return sExerciseLab;
    }

    private ExerciseLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FitnessBaseHelper(mContext).getWritableDatabase();
    }

    // Получить полный список упражнений
    public List<Exercise> getExercises(String selection, String[] selectionArgs, String orderBy) {

        List<Exercise> exercises = new ArrayList<>();

        ExerciseCursorWrapper cursor = queryExercise(selection, selectionArgs, orderBy);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                exercises.add(cursor.getExercise());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return exercises;
    }

    public Exercise getExercise(String selection, String[] selectionArgs, String orderBy){
        ExerciseCursorWrapper cursor = queryExercise(selection, selectionArgs, orderBy);

        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getExercise();
        }finally {
            cursor.close();
        }
    }

    private ExerciseCursorWrapper queryExercise(String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                FitnessTable.NAME,
                null, // Colums - null выбирает все столбцы
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                orderBy // orderBy
        );
        return new ExerciseCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(FitnessTable.Cols.UUID, exercise.getId().toString());
        values.put(FitnessTable.Cols.TITLE, exercise.getTitle());
        values.put(FitnessTable.Cols.DATE, exercise.getDate().getTime());
        values.put(FitnessTable.Cols.STATUS, exercise.getStatus());
        values.put(FitnessTable.Cols.TIMESTAMP, exercise.getTimeStamp());

        return values;
    }

    public void insertExercise(Exercise exercise) {
        ContentValues values = getContentValues(exercise);
        mDatabase.insert(FitnessTable.NAME, null, values);
    }

    public void updateExercise(Exercise exercise) {

        String uuid = exercise.getId().toString();
        ContentValues values = getContentValues(exercise);

        mDatabase.update(FitnessTable.NAME, values,
                FitnessBaseHelper.SELECTION_UUID,
                new String[]{uuid});

    }

    public void updateTitle(String uuid, String title) {
        updateExercise(FitnessTable.Cols.TITLE, uuid, title);
    }

    public void updateDate(String uuid, String date) {
        updateExercise(FitnessTable.Cols.DATE, uuid, date);
    }

    public void updateStatus(String uuid, String status) {
        updateExercise(FitnessTable.Cols.STATUS, uuid, status);
    }

    private static ContentValues getColumnContentValues(String column, String value) {
        ContentValues values = new ContentValues();
        values.put(column, value);
        return values;
    }

    private void updateExercise(String column, String key, String value) {

        ContentValues values = getColumnContentValues(column, value);

        mDatabase.update(FitnessTable.NAME, values,
                FitnessBaseHelper.SELECTION_UUID,
                new String[]{key});
    }

    public void removeExercise(String key){
        mDatabase.delete(FitnessTable.NAME, FitnessBaseHelper.SELECTION_UUID, new String[]{key});
    }


}
