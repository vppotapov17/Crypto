package com.potapp.cyberhelper.adapters.DiscussionsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewComponentsSelection;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewOther;
import com.potapp.cyberhelper.models.questions.AdviceQuestion;
import com.potapp.cyberhelper.models.questions.ComponentsSelectionQuestion;
import com.potapp.cyberhelper.models.questions.Question;
import com.potapp.cyberhelper.screens.discussions.ViewQuestion.ViewAdvice.ViewAdviceFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Question> questionList;                                                            // список всех вопросов
    FragmentManager fm;

    private List<Question> myQuestionList;
    private List<Question> otherQuestionList;                                                       // список чужих вопросов


    public QuestionsAdapter(List<Question> myQuestionList, List<Question> otherQuestionList, FragmentManager fm)
    {
        this.fm = fm;
        this.myQuestionList = myQuestionList;
        this.otherQuestionList = otherQuestionList;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (otherQuestionList.size() != 0 && myQuestionList.size() != 0)
        {
            if (position == 0 || position == myQuestionList.size() + 1) return 0;

            return 1;
        }
        else
        {
            if (position == 0) return 0;
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View currentView = null;                                                                    // текущая разметка

        switch (viewType)
        {
            case 0: currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_title_question_list, parent, false); break;
            case 1: currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_question, parent, false); break;
        }

        return new RecyclerView.ViewHolder(currentView) {};
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position)
    {
        int viewType = getItemViewType(position);

        // заголовок
        if (viewType == 0)
        {
            TextView title = holder.itemView.findViewById(R.id.title);
            if (otherQuestionList.size() != 0 && myQuestionList.size() == 0)
            {
                if (position == 0) title.setText("Все вопросы");
            }
            else {
                if (position == 0) title.setText("Мои вопросы");
                else title.setText("Все вопросы");
            }
        }
        else {
            // определение текущего вопроса
            Question current_question;

            // существуют и чужие, и свои вопросы
            if (myQuestionList.size() != 0 && otherQuestionList.size() != 0) {
                // мои вопросы
                if (position < myQuestionList.size() + 1)
                    current_question = myQuestionList.get(position - 1);
                    // чужие вопросы
                else
                    current_question = otherQuestionList.get(position - myQuestionList.size() - 2);
            }
            // существуют только чужие вопросы
            else if (myQuestionList.size() == 0) current_question = otherQuestionList.get(position - 1);
                // существуют только мои вопросы
            else current_question = myQuestionList.get(position - 1);


            TextView authorName = holder.itemView.findViewById(R.id.authorName);                    // имя автора
            TextView authorNick = holder.itemView.findViewById(R.id.authorNick);                    // ник автора
            TextView date = holder.itemView.findViewById(R.id.date);                                // дата публикации
            TextView questionText = holder.itemView.findViewById(R.id.questionText);                // текст
            TextView category = holder.itemView.findViewById(R.id.category);                        // категория
            TextView answersQuantity = holder.itemView.findViewById(R.id.answersQuantity);          // количество ответов

            final Fragment current_fragment;

            String path;                                                                            // путь к вопросу

            // категория
            if (current_question instanceof ComponentsSelectionQuestion)
            {
                path = "Data/Questions/ComponentsSelection/" + current_question.getId();
                current_fragment = ViewComponentsSelection.newInstance((ComponentsSelectionQuestion) current_question);
                category.setText("Подбор комплектующих");
            }
            else if (current_question instanceof AdviceQuestion) {
                path = "Data/Questions/Advice/" + current_question.getId();
               // current_fragment = ViewAdviceFragment.newInstance((AdviceQuestion) current_question);
                category.setText("Оценка и советы");
            }
            else
            {
                path = "Data/Questions/Other/" + current_question.getId();
                current_fragment = ViewOther.newInstance(current_question);
                category.setText("Прочее");
            }


            // обработка нажатия
            holder.itemView.findViewById(R.id.cardview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //loadFragment(current_fragment);
                }
            });

            authorName.setText("Андрей");                                                           // имя автора
            authorNick.setText("@" + current_question.getAuthor());                                 // ник автора
            questionText.setText(current_question.getText());                                       // текст

            // дата публикации
            date.setText(new SimpleDateFormat("dd MMM").format(current_question.getDate()));

            // количество ответов
            FirebaseDatabase.getInstance().getReference(path + "/Answers/").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    answersQuantity.setText(snapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    answersQuantity.setText("0");
                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        int count = 0;
        if (myQuestionList.size() != 0)
        {
            count ++;
            count += myQuestionList.size();
        }
        if (otherQuestionList.size() != 0)
        {
            count ++;
            count += otherQuestionList.size();
        }


        return count;
    }

    void loadFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
