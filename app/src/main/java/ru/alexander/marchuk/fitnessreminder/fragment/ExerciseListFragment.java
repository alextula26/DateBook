package ru.alexander.marchuk.fitnessreminder.fragment;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.alexander.marchuk.fitnessreminder.adapter.ExerciseAdapter;
import ru.alexander.marchuk.fitnessreminder.adapter.FitnessAdapter;
import ru.alexander.marchuk.fitnessreminder.database.FitnessBaseHelper;
import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseLab;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseSeparator;

public class ExerciseListFragment extends FitnessFragment {

    public ExerciseListFragment() {
    }

    AddingExerciseInProgrammListener mAddingExerciseInProgrammListener;

    public interface AddingExerciseInProgrammListener {
        // Добавление упражнения в программу
        void onExerciseAddedInProgramm(Exercise exercise);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAddingExerciseInProgrammListener = (AddingExerciseInProgrammListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingExerciseInProgrammListenet");
        }
    }

    @Override
    protected FitnessAdapter createAdapter() {
        return new ExerciseAdapter(this);
    }

    @Override
    public void addExerciseFromDB() {
        mAdapter.removeAllItem();
        List<Exercise> exercises = new ArrayList<>();
        exercises.addAll(ExerciseLab.get(getActivity()).getExercises(FitnessBaseHelper.SELECTION_STATUS + " OR "
                + FitnessBaseHelper.SELECTION_STATUS, new String[]{Integer.toString(Exercise.STATUS_EXERCISE),
                Integer.toString(Exercise.STATUS_OVERDUE)}, FitnessTable.Cols.DATE));

        for (int i = 0; i < exercises.size(); i++) {
            addExercise(exercises.get(i), false);
        }
    }
    @Override
    public void addExercise(Exercise newExercise, boolean saveToDB) {
        int position = -1;
        ExerciseSeparator separator = null;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (mAdapter.getItem(i).isExercise()) {
                Exercise exercise = (Exercise) mAdapter.getItem(i);
                if (newExercise.getDate().before(exercise.getDate())) {
                    position = i;
                    break;
                }
            }
        }

        if (newExercise.getDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newExercise.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newExercise.setDateStatus(ExerciseSeparator.TYPE_OVERDUE);
                if (!mAdapter.containsSeparatorOverdue) {
                    mAdapter.containsSeparatorOverdue = true;
                    separator = new ExerciseSeparator(ExerciseSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newExercise.setDateStatus(ExerciseSeparator.TYPE_TODAY);
                if (!mAdapter.containsSeparatorToday) {
                    mAdapter.containsSeparatorToday = true;
                    separator = new ExerciseSeparator(ExerciseSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newExercise.setDateStatus(ExerciseSeparator.TYPE_TOMORROW);
                if (!mAdapter.containsSeparatorTomorrow) {
                    mAdapter.containsSeparatorTomorrow = true;
                    separator = new ExerciseSeparator(ExerciseSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newExercise.setDateStatus(ExerciseSeparator.TYPE_TOMORROW);
                if (!mAdapter.containsSeparatorFuture) {
                    mAdapter.containsSeparatorFuture = true;
                    separator = new ExerciseSeparator(ExerciseSeparator.TYPE_FUTURE);
                }
            }
        }

        if (position != -1) {

            if (!mAdapter.getItem(position - 1).isExercise()) {
                if (position - 2 >= 0 && mAdapter.getItem(position - 2).isExercise()) {
                    Exercise exercise = (Exercise) mAdapter.getItem(position - 2);
                    if (exercise.getDateStatus() == newExercise.getDateStatus()) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newExercise.getDate() == null) {
                    position -= 1;
                }
            }

            if (separator != null) {
                mAdapter.addItem(position - 1, separator);
            }

            mAdapter.addItem(position, newExercise);
        } else {
            if (separator != null) {
                mAdapter.addItem(separator);
            }
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
                FitnessBaseHelper.SELECTION_LIKE_TITLE + " AND " +
                        FitnessBaseHelper.SELECTION_STATUS + " OR " +
                        FitnessBaseHelper.SELECTION_STATUS,
                new String[]{
                        "%" + title + "%",
                        Integer.toString(Exercise.STATUS_EXERCISE),
                        Integer.toString(Exercise.STATUS_OVERDUE)
                },
                FitnessTable.Cols.DATE));

        for (int i = 0; i < exercises.size(); i++) {
            addExercise(exercises.get(i), false);
        }
    }

    @Override
    public void moveExercise(Exercise exercise) {
        mAddingExerciseInProgrammListener.onExerciseAddedInProgramm(exercise);
    }
}
