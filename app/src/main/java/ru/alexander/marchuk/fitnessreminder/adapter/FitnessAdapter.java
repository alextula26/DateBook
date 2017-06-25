package ru.alexander.marchuk.fitnessreminder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.alexander.marchuk.fitnessreminder.fragment.FitnessFragment;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseSeparator;
import ru.alexander.marchuk.fitnessreminder.model.Item;

public abstract class FitnessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> mItems;

    FitnessFragment mFitnessFragment;

    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    public boolean containsSeparatorFuture;

    public FitnessAdapter(FitnessFragment fitnessFragment) {
        mFitnessFragment = fitnessFragment;
        mItems = new ArrayList<>();
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }

    // Добавление пункта в конец списка
    public void addItem(Item item) {
        mItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    // Добавление пункта в определенную позицию списка
    public void addItem(int position, Item item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void updateItem(Exercise newExercise){
        for(int i = 0; i < getItemCount(); i++){
            if(getItem(i).isExercise()){
                Exercise exercise = (Exercise) getItem(i);
                if(newExercise.getId() == exercise.getId()){
                    removeItem(i);
                    getFitnessFragment().addExercise(newExercise, false);
                }
            }
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            mItems.remove(position);
            notifyItemRemoved(position);
            if (position - 1 >= 0 && position <= getItemCount() - 1) {
                if (!getItem(position).isExercise() && !getItem(position - 1).isExercise()) {
                    ExerciseSeparator separator = (ExerciseSeparator) getItem(position - 1);
                    checkSeparators(separator.getType());
                    mItems.remove(position - 1);
                    notifyItemRemoved(position - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isExercise()) {
                ExerciseSeparator separator = (ExerciseSeparator) getItem(getItemCount() - 1);
                checkSeparators(separator.getType());

                int positionTemp = getItemCount() - 1;
                mItems.remove(positionTemp);
                notifyItemRemoved(positionTemp);
            }
        }
    }

    public void checkSeparators(int type) {
        switch (type) {
            case ExerciseSeparator.TYPE_OVERDUE:
                containsSeparatorOverdue = false;
                break;
            case ExerciseSeparator.TYPE_TODAY:
                containsSeparatorToday = false;
                break;
            case ExerciseSeparator.TYPE_TOMORROW:
                containsSeparatorTomorrow = false;
                break;
            case ExerciseSeparator.TYPE_FUTURE:
                containsSeparatorFuture = false;
                break;
        }
    }

    public void removeAllItem() {
        if (getItemCount() != 0) {
            mItems = new ArrayList<>();
            notifyDataSetChanged();
            containsSeparatorOverdue = false;
            containsSeparatorToday = false;
            containsSeparatorTomorrow = false;
            containsSeparatorFuture = false;
        }
    }

    protected class ExerciseViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTitleTextView;
        protected TextView mDateTextView;
        protected LinearLayout mLinearLayoutContainer;
        protected LinearLayout mLinearLayoutDate;

        public ExerciseViewHolder(View itemView, TextView mTitleTextView, TextView mDateTextView, LinearLayout mLinearLayoutContainer, LinearLayout mLinearLayoutDate) {
            super(itemView);
            this.mTitleTextView = mTitleTextView;
            this.mDateTextView = mDateTextView;
            this.mLinearLayoutContainer = mLinearLayoutContainer;
            this.mLinearLayoutDate = mLinearLayoutDate;
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        protected TextView mType;

        public SeparatorViewHolder(View itemView, TextView mType) {
            super(itemView);
            this.mType = mType;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public FitnessFragment getFitnessFragment() {
        return mFitnessFragment;
    }
}
