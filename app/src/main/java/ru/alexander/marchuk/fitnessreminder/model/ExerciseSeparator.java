package ru.alexander.marchuk.fitnessreminder.model;

import ru.alexander.marchuk.fitnessreminder.R;

public class ExerciseSeparator implements Item {

    public static final int TYPE_OVERDUE = R.string.separator_overdue;
    public static final int TYPE_TODAY = R.string.separator_today;
    public static final int TYPE_TOMORROW = R.string.separator_tomorrow;
    public static final int TYPE_FUTURE = R.string.separator_future;

    private int type;

    public ExerciseSeparator(int type){
        this.type = type;
    }

    @Override
    public boolean isExercise() {
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
