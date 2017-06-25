package ru.alexander.marchuk.fitnessreminder.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.alexander.marchuk.fitnessreminder.fragment.ExerciseListFragment;
import ru.alexander.marchuk.fitnessreminder.fragment.ProgrammListFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private int numberOftabs;

    public static final int EXERCISE_FRAGMENT_POSITION = 0;
    public static final int PROGRAMM_FRAGMENT_POSITION = 1;

    private ExerciseListFragment exerciseListFragment;
    private ProgrammListFragment programmListFragment;

    public TabAdapter(FragmentManager fm, int numberOftabs) {
        super(fm);
        this.numberOftabs = numberOftabs;
        exerciseListFragment = new ExerciseListFragment();
        programmListFragment = new ProgrammListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case EXERCISE_FRAGMENT_POSITION:
                return exerciseListFragment;
            case PROGRAMM_FRAGMENT_POSITION:
                return programmListFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOftabs;
    }
}
