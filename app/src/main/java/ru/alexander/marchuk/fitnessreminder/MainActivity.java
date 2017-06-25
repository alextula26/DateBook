package ru.alexander.marchuk.fitnessreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import ru.alexander.marchuk.fitnessreminder.adapter.TabAdapter;
import ru.alexander.marchuk.fitnessreminder.dialog.AddingExerciseDialogFragment;
import ru.alexander.marchuk.fitnessreminder.dialog.EditExerciseDialogFragment;
import ru.alexander.marchuk.fitnessreminder.fragment.ExerciseListFragment;
import ru.alexander.marchuk.fitnessreminder.fragment.FitnessFragment;
import ru.alexander.marchuk.fitnessreminder.fragment.ProgrammListFragment;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseLab;

public class MainActivity extends AppCompatActivity implements
        AddingExerciseDialogFragment.AddingExerciseListener,
        ExerciseListFragment.AddingExerciseInProgrammListener,
        ProgrammListFragment.ExerciseRestoreListener,
        EditExerciseDialogFragment.EditingExerciseListener{

    private FragmentManager fragmentManager;
    private TabAdapter mTabAdapter;

    private FitnessFragment mExerciseListFragment;
    private FitnessFragment mProgrammListFragment;

    private SearchView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        setUI();

    }

    private void setUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            setSupportActionBar(toolbar);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_exercises));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_name_programm));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        mTabAdapter = new TabAdapter(fragmentManager, 2);
        viewPager.setAdapter(mTabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mExerciseListFragment = (ExerciseListFragment) mTabAdapter.getItem(TabAdapter.EXERCISE_FRAGMENT_POSITION);
        mProgrammListFragment = (ProgrammListFragment) mTabAdapter.getItem(TabAdapter.PROGRAMM_FRAGMENT_POSITION);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mExerciseListFragment.findExercise(newText);
                mProgrammListFragment.findExercise(newText);
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingExerciseDialogFragment = new AddingExerciseDialogFragment();
                addingExerciseDialogFragment.show(fragmentManager, "AddingExerciseDialogFragment");
            }
        });


    }

    @Override
    public void onExerciseAdded(Exercise newExercise) {
        // Добавляем новое упражнение
        mExerciseListFragment.addExercise(newExercise, true);
    }

    @Override
    public void onExerciseAddingCancel() {
        Toast.makeText(this, "Exercises adding Cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExerciseAddedInProgramm(Exercise exercise) {
        // Добавляем упражнение в программу
        mProgrammListFragment.addExercise(exercise, false);
    }

    @Override
    public void onExerciseRestore(Exercise exercise) {
       // Удаляем упражнение из программы
        mExerciseListFragment.addExercise(exercise, false);
    }

    @Override
    public void onExerciseEdited(Exercise updateExercise) {
        mExerciseListFragment.updateExercise(updateExercise);
        ExerciseLab.get(getApplicationContext()).updateExercise(updateExercise);
    }
}
