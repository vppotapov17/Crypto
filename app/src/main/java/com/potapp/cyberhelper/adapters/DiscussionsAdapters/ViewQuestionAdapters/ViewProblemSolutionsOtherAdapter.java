package com.potapp.cyberhelper.adapters.DiscussionsAdapters.ViewQuestionAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.data.models.answers.AdviceAnswer;
import com.potapp.cyberhelper.screens.discussions.addAnswer.AddAdvice;
import com.potapp.cyberhelper.data.models.questions.Question;

import java.util.List;

public class ViewProblemSolutionsOtherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdviceAnswer> answerList;
    private Question current_question;
    private FragmentManager fm;

    public ViewProblemSolutionsOtherAdapter(List<AdviceAnswer> answerList, Question current_question, FragmentManager fm)
    {
        this.answerList = answerList;
        this.current_question = current_question;
        this.fm = fm;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0) return 0;
        if (position == getItemCount() - 1) return 2;
        return 1;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View current_view = null;

        switch (viewType)
        {
            case 0: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_advice_header, parent, false); break;
            case 1: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_answer_advice, parent, false); break;
            case 2: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_footer, parent, false); break;
        }

        return new RecyclerView.ViewHolder(current_view){};
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position){
        if (getItemViewType(position) == 0)
        {
            TextView authorValue = holder.itemView.findViewById(R.id.authorValue);
            TextView dateValue = holder.itemView.findViewById(R.id.dateValue);
            TextView categoryValue = holder.itemView.findViewById(R.id.categoryValue);

            authorValue.setText(current_question.getAuthor());
            dateValue.setText(current_question.getDate().toString());
            categoryValue.setText("Оценка и советы");
        }
        else if (getItemViewType(position) == 1)
        {
            TextView author = holder.itemView.findViewById(R.id.author);
            author.setText("@vppotapov17");
        }
        else if (getItemViewType(position) == 2)
        {
            ImageView addAnswer = holder.itemView.findViewById(R.id.add_answer);
            addAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, AddAdvice.newInstance(current_question.getId()));
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }

            });
        }
    }

    @Override
    public int getItemCount()
    {
        return answerList.size() + 2;
    }
}
