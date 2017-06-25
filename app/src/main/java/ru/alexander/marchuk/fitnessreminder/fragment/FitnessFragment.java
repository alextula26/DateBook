package ru.alexander.marchuk.fitnessreminder.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import ru.alexander.marchuk.fitnessreminder.R;
import ru.alexander.marchuk.fitnessreminder.adapter.FitnessAdapter;
import ru.alexander.marchuk.fitnessreminder.database.FitnessBaseHelper;
import ru.alexander.marchuk.fitnessreminder.database.FitnessDbScheme.FitnessTable;
import ru.alexander.marchuk.fitnessreminder.dialog.EditExerciseDialogFragment;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseLab;
import ru.alexander.marchuk.fitnessreminder.model.Item;

public abstract class FitnessFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected FitnessAdapter mAdapter;

    protected abstract FitnessAdapter createAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.exercise_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addExerciseFromDB();
    }

    public abstract void addExercise(Exercise newExercise, boolean saveToDB);

    public void updateExercise(Exercise exercise){
        mAdapter.updateItem(exercise);
    }

    public void removeExercise(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_removing_message);

        Item item = mAdapter.getItem(position);

        if (item.isExercise()) {
            Exercise exercise = (Exercise) item;
            final UUID uuid = exercise.getId();
            final boolean[] isRemoved = {false};

            builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAdapter.removeItem(position);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addExercise(ExerciseLab.get(getActivity()).getExercise(
                                    FitnessBaseHelper.SELECTION_UUID,
                                    new String[]{String.valueOf(uuid)},
                                    FitnessTable.Cols.DATE
                            ), false);
                            isRemoved[0] = false;
                        }
                    });

                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {

                            if (isRemoved[0]) {
                                ExerciseLab.get(getActivity()).removeExercise(String.valueOf(uuid));
                            }

                        }
                    });

                    snackbar.show();

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        builder.show();

    }

    public void showExerciseEditDialog(Exercise exercise){
        DialogFragment editingExerciseDialog = EditExerciseDialogFragment.newInstance(exercise);
        editingExerciseDialog.show(getActivity().getSupportFragmentManager(), "EditingExerciseDialogFragment");
    }

    public abstract void findExercise(String title);

    public abstract void addExerciseFromDB();

    public abstract void moveExercise(Exercise exercise);
}
