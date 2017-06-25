package ru.alexander.marchuk.fitnessreminder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;

public class ExerciseCursorWrapper extends CursorWrapper {

    public ExerciseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Exercise getExercise(){
        String uuid = getString(getColumnIndex(FitnessTable.Cols.UUID));
        String title = getString(getColumnIndex(FitnessTable.Cols.TITLE));
        long date = getLong(getColumnIndex(FitnessTable.Cols.DATE));
        int status = getInt(getColumnIndex(FitnessTable.Cols.STATUS));
        long timeStamp = getLong(getColumnIndex(FitnessTable.Cols.TIMESTAMP));

        Exercise exercise = new Exercise(UUID.fromString(uuid));
        exercise.setTitle(title);
        exercise.setDate(new Date(date));
        exercise.setStatus(status);
        exercise.setTimeStamp(timeStamp);

        return exercise;
    }
}
