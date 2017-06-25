package ru.alexander.marchuk.fitnessreminder.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.alexander.marchuk.fitnessreminder.R;
import ru.alexander.marchuk.fitnessreminder.Utils;
import ru.alexander.marchuk.fitnessreminder.fragment.ProgrammListFragment;
import ru.alexander.marchuk.fitnessreminder.model.Exercise;
import ru.alexander.marchuk.fitnessreminder.model.ExerciseLab;
import ru.alexander.marchuk.fitnessreminder.model.Item;

public class ProgrammAdapter extends FitnessAdapter {

    public ProgrammAdapter(ProgrammListFragment programmListFragment) {
        super(programmListFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_exercise, parent, false);

        TextView title = (TextView) view.findViewById(R.id.txt_title);
        TextView date = (TextView) view.findViewById(R.id.txt_date);
        LinearLayout mLinearLayoutContainer = (LinearLayout) view.findViewById(R.id.l_container);
        LinearLayout mLinearLayoutDate = (LinearLayout) view.findViewById(R.id.l_date);

        return new ExerciseViewHolder(view, title, date, mLinearLayoutContainer, mLinearLayoutDate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = mItems.get(position);

        if (item.isExercise()) {
            holder.itemView.setEnabled(true);
            final Exercise mExercise = (Exercise) item;
            final ExerciseViewHolder exerciseViewHolder = (ExerciseViewHolder) holder;

            final View itemView = exerciseViewHolder.itemView;

            if (position % 2 == 0) {
                exerciseViewHolder.mLinearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mFitnessFragment.getActivity(), R.color.container_even));
                exerciseViewHolder.mLinearLayoutDate.setBackgroundColor(ContextCompat.getColor(mFitnessFragment.getActivity(), R.color.date_even));
            } else {
                exerciseViewHolder.mLinearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mFitnessFragment.getActivity(), R.color.container_uneven));
                exerciseViewHolder.mLinearLayoutDate.setBackgroundColor(ContextCompat.getColor(mFitnessFragment.getActivity(), R.color.date_uneven));
            }

            exerciseViewHolder.mTitleTextView.setText(mExercise.getTitle());
            exerciseViewHolder.mDateTextView.setText(Utils.getFullDate(mExercise.getDate()));

            itemView.setVisibility(View.VISIBLE);
            itemView.setEnabled(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setEnabled(false);
                    mExercise.setStatus(Exercise.STATUS_EXERCISE);
                    ExerciseLab.get(getFitnessFragment().getActivity())
                            .updateStatus(mExercise.getId().toString(), Integer.toString(Exercise.STATUS_EXERCISE));

                    if (mExercise.getStatus() != Exercise.STATUS_PROGRAMM) {

                        ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                "translationX", 0f, -itemView.getWidth());

                        ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                "translationX", -itemView.getWidth(), 0f);

                        AnimatorSet translationSet = new AnimatorSet();
                        translationSet.play(translationX).before(translationXBack);
                        translationSet.start();

                        translationX.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                itemView.setVisibility(View.GONE);
                                // Получаем фрагмент и вызываем метод moveExercise
                                getFitnessFragment().moveExercise(mExercise);
                                // Удаляем упражнение из фрагмента
                                removeItem(exerciseViewHolder.getLayoutPosition());
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getFitnessFragment().removeExercise(exerciseViewHolder.getLayoutPosition());
                        }
                    }, 1000);

                    return true;
                }
            });
        }
    }
}
