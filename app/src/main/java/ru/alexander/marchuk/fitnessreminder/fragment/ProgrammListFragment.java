package ru.alexander.marchuk.fitnessreminder.fragment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.fitnessreminder.adapter.FitnessAdapter;
import ru.alexander.marchuk.fitnessreminder.adapter.ProgrammAdapter;
import ru.alexander.marchuk.fitnessreminder.database.FitnessBaseHelper;
import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseLab;

public class ProgrammListFragment extends FitnessFragment {

    public ProgrammListFragment() {
    }

    ExerciseRestoreListener mExerciseRestoreListener;

    public interface ExerciseRestoreListener {
        // Удаление упражнения из программы
        void onExerciseRestore(Exercise exercise);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mExerciseRestoreListener = (ExerciseRestoreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingExerciseInProgrammListenet");
        }
    }

    @Override
    protected FitnessAdapter createAdapter() {
        return new ProgrammAdapter(this);
    }

    @Override
    public void addExerciseFromDB() {
        mAdapter.removeAllItem();
        List<Exercise> exercises = new ArrayList<>();
        exercises.addAll(ExerciseLab.get(getActivity()).getExercises(FitnessBaseHelper.SELECTION_STATUS,
                new String[]{Integer.toString(Exercise.STATUS_PROGRAMM)}, FitnessTable.Cols.DATE));

        for (int i = 0; i < exercises.size(); i++) {
            addExercise(exercises.get(i), false);
        }
    }

    @Override
    public void addExercise(Exercise newExercise, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isExercise()) {
                Exercise exercise = (Exercise) mAdapter.getItem(i);
                if (newExercise.getDate().before(exercise.getDate())) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            mAdapter.addItem(position, newExercise);
        } else {
            mAdapter.addItem(newExercise);
        }

        if (saveToDB) {
            ExerciseLab.get(getActivity()).insertExercise(newExercise);
        }
    }

    @Override
    public void findExercise(String title) {
        mAdapter.removeAllItem();
        List<Exercise> exercises = new ArrayList<>();
        exercises.addAll(ExerciseLab.get(getActivity()).getExercises(
                FitnessBaseHelper.SELECTION_LIKE_TITLE + " AND " + FitnessBaseHelper.SELECTION_STATUS,
                new String[]{
                        "%" + title + "%",
                        Integer.toString(Exercise.STATUS_PROGRAMM)
                },
                FitnessTable.Cols.DATE));

        for (int i = 0; i < exercises.size(); i++) {
            addExercise(exercises.get(i), false);
        }
    }

    @Override
    public void moveExercise(Exercise exercise) {
        mExerciseRestoreListener.onExerciseRestore(exercise);
    }
}
