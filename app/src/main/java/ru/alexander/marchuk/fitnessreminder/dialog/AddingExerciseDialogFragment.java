package ru.alexander.marchuk.fitnessreminder.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import ru.alexander.marchuk.fitnessreminder.R;
import ru.alexander.marchuk.fitnessreminder.Utils;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;

public class AddingExerciseDialogFragment extends DialogFragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private TextInputLayout tilTitle;
    private EditText etTitle;
    private TextInputLayout tilDate;
    private EditText etDate;

    private Exercise exercise;

    private String ARG_TITLE = "ru.alexander.marchuk.fitnessreminder.dialog.addingexercisedialogfragment.title";
    private String ARG_DATE = "ru.alexander.marchuk.fitnessreminder.dialog.addingexercisedialogfragment.date";

    private AddingExerciseListener mAddingExerciseListener;

    public interface AddingExerciseListener {
        // Добавление нового упражнения
        void onExerciseAdded(Exercise newExercise);

        // Закрытие диалога добавления упражнения
        void onExerciseAddingCancel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAddingExerciseListener = (AddingExerciseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddingTaskListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercise = new Exercise();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title);

        View container = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exercise, null);

        tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTitle);
        etTitle = tilTitle.getEditText();

        tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogDate);
        etDate = tilDate.getEditText();

        tilTitle.setHint(getResources().getString(R.string.exercise_title));
        tilDate.setHint(getResources().getString(R.string.exercise_date));

        builder.setView(container);

//        if(savedInstanceState != null){
//            etTitle.setText(savedInstanceState.getString(ARG_TITLE));
//            etDate.setText(savedInstanceState.getString(ARG_DATE));
//        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(exercise.getDate());
                datePickerFragment.setTargetFragment(AddingExerciseDialogFragment.this, REQUEST_DATE);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), DIALOG_DATE);
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etTitle.length() != 0) {
                    exercise.setTitle(etTitle.getText().toString());
                }

                if (etDate.length() != 0) {
                    exercise.setDate(Utils.parseDate(etDate.getText().toString()));
                }

                exercise.setStatus(Exercise.STATUS_EXERCISE);
                mAddingExerciseListener.onExerciseAdded(exercise);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAddingExerciseListener.onExerciseAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                }

                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilTitle.setError(getResources().getString(R.string.dialog_error_empty_title));
                        } else {
                            positiveButton.setEnabled(true);
                            tilTitle.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            if (date != null) {
                etDate.setText(Utils.getDate(date));
            }
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putString(ARG_TITLE, etTitle.getText().toString());
//        outState.putString(ARG_DATE, etDate.getText().toString());
//    }
}
